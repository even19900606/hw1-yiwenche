/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.DocumentAnnotation;
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.FileUtils;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

/**
 * A simple collection reader that reads documents from a directory in the filesystem. It can be
 * configured with the following parameters:
 * <ul>
 * <li><code>InputDirectory</code> - path to directory containing files</li>
 * <li><code>Encoding</code> (optional) - character encoding of the input files</li>
 * <li><code>Language</code> (optional) - language of the input documents</li>
 * </ul>
 * 
 * @author jacky
 * @version 1.0 14 Oct 2012
 */
public class FileSystemCollectionReader extends CollectionReader_ImplBase {
  /**
   * Name of configuration parameter that must be set to the path of a directory containing input
   * files.
   */
  public static final String PARAM_INPUTDIR = "InputDirectory";

  /**
   * Name of configuration parameter that contains the character encoding used by the input files.
   * If not specified, the default system encoding will be used.
   */
  public static final String PARAM_ENCODING = "Encoding";

  /**
   * Name of optional configuration parameter that contains the language of the documents in the
   * input directory. If specified this information will be added to the CAS.
   */
  public static final String PARAM_LANGUAGE = "Language";

  /**
   * Name of optional configuration parameter that indicates including
   * the subdirectories (recursively) of the current input directory.
   */
  public static final String PARAM_SUBDIR = "BrowseSubdirectories";
  
  private ArrayList<File> mFiles;

  private String mEncoding;

  private String mLanguage;
  
  private Boolean mRecursive;

  private int mCurrentIndex;

  /**
   * Initialization method called by the framework when the collection reader is first created
   * 
   * @throws ResourceInitializationExceptino  if the resources used in initialization cause some errors
   * @see org.apache.uima.collection.CollectionReader_ImplBase#initialize()
   */
  public void initialize() throws ResourceInitializationException{
    File directory = new File(((String) getConfigParameterValue(PARAM_INPUTDIR)).trim());
    mEncoding  = (String) getConfigParameterValue(PARAM_ENCODING);
    mLanguage  = (String) getConfigParameterValue(PARAM_LANGUAGE);
    mRecursive = (Boolean) getConfigParameterValue(PARAM_SUBDIR);
    if (null == mRecursive) { // could be null if not set, it is optional
      mRecursive = Boolean.FALSE;
    }
    mCurrentIndex = 0;

    // if input directory does not exist or is not a directory, throw exception
    if (!directory.exists() || !directory.isDirectory()) {
      System.out.println(directory.getAbsolutePath());
      throw new ResourceInitializationException(ResourceConfigurationException.DIRECTORY_NOT_FOUND,
              new Object[] { PARAM_INPUTDIR, this.getMetaData().getName(), directory.getPath() });
    }

    // get list of files in the specified directory, and subdirectories if the
    // parameter PARAM_SUBDIR is set to True
    mFiles = new ArrayList<File>();
    addFilesFromDir(directory);
  }
  
  /**
   * This method adds files in the directory passed in as a parameter to mFiles.
   * If mRecursive is true, it will include all files in all
   * subdirectories (recursively), as well. 
   * 
   * @param dir the directory from which the method reads the files
   */
  private void addFilesFromDir(File dir) {
    File[] files = dir.listFiles();
    for (int i = 0; i < files.length; i++) {
      if (!files[i].isDirectory()) {
        if(files[i].getName().equals("hw1.in"))mFiles.add(files[i]);
      } else if (mRecursive) {
        addFilesFromDir(files[i]);
      }
    }
  }

  /**
   * Check if there are still some files unread.
   * 
   * @return    true is there're still some files unread, false otherwise
   * @see org.apache.uima.collection.CollectionReader#hasNext()
   */
  public boolean hasNext() {
    return mCurrentIndex < mFiles.size();
  }

  /**
   * Reads the next document from the collection and populates a CAS.
   * 
   * @param aCAS  the CAS populating the next document
   * @throws IOException  if some input or output errors occur
   * @throws CollectionException  if the getJCas() method fails
   * @see org.apache.uima.collection.CollectionReader#getNext(org.apache.uima.cas.CAS)
   */
  public void getNext(CAS aCAS) throws IOException, CollectionException {
    JCas jcas;
    try {
      jcas = aCAS.getJCas();
    } catch (CASException e) {
      throw new CollectionException(e);
    }

    // open input stream to file
    File file = (File) mFiles.get(mCurrentIndex++);
    String text = FileUtils.file2String(file, mEncoding);
      // put document in CAS
    jcas.setDocumentText(text);

    // set language if it was explicitly specified as a configuration parameter
    if (mLanguage != null) {
      ((DocumentAnnotation) jcas.getDocumentAnnotationFs()).setLanguage(mLanguage);
    }
  }

  /**
   * Close the reading process of the Collection Reader.
   * 
   * @throws IOException  if certain input or output errors occur
   * @see org.apache.uima.collection.base_cpm.BaseCollectionReader#close()
   */
  public void close() throws IOException {
  }

  /**
   * @see org.apache.uima.collection.base_cpm.BaseCollectionReader#getProgress()
   */
  public Progress[] getProgress() {
    return new Progress[] { new ProgressImpl(mCurrentIndex, mFiles.size(), Progress.ENTITIES) };
  }

  /**
   * Gets the total number of documents that will be returned by this collection reader. This is not
   * part of the general collection reader interface.
   * 
   * @return the number of documents in the collection
   */
  public int getNumberOfDocuments() {
    return mFiles.size();
  }

}
