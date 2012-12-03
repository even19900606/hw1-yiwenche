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
import java.util.Iterator;
import java.util.Set;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;
import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunker;
import com.aliasi.chunk.Chunking;
import com.aliasi.util.AbstractExternalizable;
import model.GeneName;

/**
 * GeneNameAnnotator that detects gene names using LIngpipe tools.
 * 
 * @author jacky
 * @version 1.0 14 Oct 2012
 */
public class GeneNameAnnotator extends JCasAnnotator_ImplBase {
  
  /**
   * Counting the the number of spaces before the character indexed by sentinel
   * in the text
   * 
   * @param sentinel  specify the character in the text before which the method 
   *                             counts the number of spaces
   * @param text        the string of text the method works on
   * @return               the number of spaces before the character indexed by sentinel
   *                             in the text                            
   */
  public int Indentation(int sentinel, String text){
    text = text.substring(0,  sentinel + 1);
    int index = -1;
    int count = 0;
    while((index = text.indexOf(' ')) != -1){
      count++;
      text = text.substring(index + 1);
    }
    return count;
  }
  
  /**
   * The GeneNameAnnotator use this method to process the CAS objects input from 
   * the Collection Reader
   * 
   * @param aJCas CAS objects got from the Collection Reader of CPE
   * @see JCasAnnotator_ImplBase#process(JCas)
   */
  public void process(JCas aJCas) {
    //Get document text and other initializations
    String docText = aJCas.getDocumentText();
    String[] sentences = docText.split("\n");
    String ID, Name, sent;
    int begin, end, delimiterIndex;
    ID = null;
    Name = null;
    begin = end = delimiterIndex = -1;
    //Call the methods from Lingpipe NER to detects the gene names in the documents.
    //String lingpipeNER = "src/main/java/ne-en-bio-genetag.HmmChunker";
    String lingpipeNER = "ne-en-bio-genetag.HmmChunker";
    File modelFile = new File(lingpipeNER);
    Chunker chunker;
    Chunking chunking;
    Set<Chunk> cs;
    Iterator<Chunk> iter;
    Chunk c;
    int start_indent, end_indent;
    start_indent = end_indent = -1;
    int accumulate_offset = 0;
    try{
      chunker = (Chunker) AbstractExternalizable.readObject(modelFile);
      for(String sentence : sentences){
        sentence = sentence.trim();
        delimiterIndex = sentence.indexOf(' ');
        ID = sentence.substring(0, delimiterIndex);
        //extract the sentence from each line of the documents
        sent = sentence.substring(delimiterIndex + 1);
             
        chunking = chunker.chunk(sent);
        cs = chunking.chunkSet();
        iter = cs.iterator();
        /*
         * Each time lingpipeNER detects a gene name, create a new annotation,
         * set relevant features and add the annotation to the index.
         */
        
        while(iter.hasNext()){ 
          c = iter.next();
          GeneName annotation = new GeneName(aJCas);
          Name = sent.substring(c.start(), c.end());

          start_indent = Indentation(c.start(), sent);
          end_indent = Indentation(c.end() - 1, sent);
          begin = c.start() - start_indent + accumulate_offset;
          end = c.end() - end_indent - 1 + accumulate_offset;
          annotation.setID(ID);
          annotation.setName(Name);
          annotation.setBegin(begin);
          annotation.setEnd(end);
          annotation.setAccumulate(accumulate_offset);
          annotation.addToIndexes();
        }
        accumulate_offset += sent.length();
      }
   }catch(ClassNotFoundException e){
     System.err.println("FileNotFoundException: " + e.getLocalizedMessage());
   }catch(IOException e){
     System.err.println("FileNotFoundException: " + e.getLocalizedMessage());
   }
  }
}
