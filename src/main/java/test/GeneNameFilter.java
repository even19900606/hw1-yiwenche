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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;
import model.GeneName;

/**
 * GeneNameFilter that descards some obvious wrong annotations detected by previous annnotator.
 * 
 * @author jacky
 * @version 1.0 14 Oct 2012
 */
public class GeneNameFilter extends JCasAnnotator_ImplBase {
  /**
   * The GeneNameAnnotator use this method descard the annotations with one of two features:
   * 1. The name of the annotation only contains one lower letter.
   * 2. The name of the annotation only contains numbers.
   * 
   * @param aJCas CAS objects got from the Collection Reader of CPE
   * @see JCasAnnotator_ImplBase#process(JCas)
   */
  public void process(JCas aJCas) {
    Iterator it = aJCas.getAnnotationIndex(GeneName.type).iterator();
    //Use regular matching to filter the annotations detected by the previous annotator
    Pattern filter = Pattern.compile("^[a-z]$|^[0-9]+$");
    ArrayList<GeneName> table = new ArrayList<GeneName>();
    GeneName annot;
    Matcher matcher;
    while(it.hasNext()){
      annot = (GeneName)it.next();
      matcher = filter.matcher(annot.getName());
      //Add wrong annotations first into an ArrayList
      if(matcher.find()){
        table.add(annot);
      }
    }
    //Delete wrong annotations from the index
    for(GeneName gn: table){
      aJCas.removeFsFromIndexes(gn);
    }
  }
}
