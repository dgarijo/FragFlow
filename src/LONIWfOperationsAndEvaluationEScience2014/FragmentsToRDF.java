/*
 * Copyright 2012-2013 Ontology Engineering Group, Universidad Politécnica de Madrid, Spain
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package LONIWfOperationsAndEvaluationEScience2014;

import MainGraphProcessingScripts.SUBDUE.*;
import DataStructures.Fragment;
import DataStructures.Graph;
import DataStructures.GraphCollection;
import Factory.Loni.LoniTemplate2Graph;
import IO.Formats.SUBDUE.FragmentCatalogAndResultsToRDFSUBDUE;
import IO.DatasetFilter;
import IO.Formats.SUBDUE.FragmentCollectionReaderSUBDUE;
import PostProcessing.Formats.SUBDUE.CreateStatisticsFromResultsSUBDUE;
import Static.GeneralConstants;
import Static.GeneralMethods;
import Static.TestConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * The purpose of this script is to transform the fragment catalog and instances to RDF
 * (A REDUCED PORTION) to find out examples of certain fragments
 * @author Daniel Garijo
 */
public class FragmentsToRDF {
  public static void main(String[] args)   {
      try{
          String file = TestConstants.SUBDUEResultFolder +"eScienceResults(LONI)\\";
          
           //String loniDatasetFolder = "C:\\Users\\Monen\\Desktop\\LONIDatasets\\WorkflowIVO\\all\\";
          String loniDatasetFolder = "C:\\Users\\Monen\\Desktop\\LONIDatasets\\WorkflowBoris\\Pipeline123\\";
           File f = new File(loniDatasetFolder);
           LoniTemplate2Graph fullCollection = new LoniTemplate2Graph(loniDatasetFolder);
           if(f.isDirectory()){
               File[] files = f.listFiles();
               for(int i=0;i<files.length;i++){
                   fullCollection.transformToGraph(files[i].getName());
               }
           }
           GraphCollection filteredC = DatasetFilter.removeDuplicates(fullCollection.getGraphCollection());
           
           System.out.println("Filtered templates: "+filteredC.getNumberOfSubGraphs());
           //Corpus 1 (Boris)
           HashMap<String,Fragment> structureResults = new FragmentCollectionReaderSUBDUE(file+"corpus1SIZE", file+"corpus1SIZE_occurrences").getFragmentCatalogAsHashMap();
           ArrayList<Fragment> selectedFragments = new ArrayList<Fragment>();
           //MDL
//           selectedFragments.add(structureResults.get("SUB_2"));
//           selectedFragments.add(structureResults.get("SUB_8"));
//           selectedFragments.add(structureResults.get("SUB_29"));
//           selectedFragments.add(structureResults.get("SUB_28"));
//           selectedFragments.add(structureResults.get("SUB_43"));
//           selectedFragments.add(structureResults.get("SUB_38"));
//           selectedFragments.add(structureResults.get("SUB_12"));
//           selectedFragments.add(structureResults.get("SUB_18"));
//           selectedFragments.add(structureResults.get("SUB_60"));
           //size
           selectedFragments.add(structureResults.get("SUB_7"));
           selectedFragments.add(structureResults.get("SUB_13"));
           selectedFragments.add(structureResults.get("SUB_20"));
           selectedFragments.add(structureResults.get("SUB_27"));
           selectedFragments.add(structureResults.get("SUB_30"));
           selectedFragments.add(structureResults.get("SUB_22"));
           selectedFragments.add(structureResults.get("SUB_23"));
           selectedFragments.add(structureResults.get("SUB_11"));
           selectedFragments.add(structureResults.get("SUB_17"));
           //Corpus 2 (IVO
//            HashMap<String,Fragment> structureResults = new FragmentCollectionReaderSUBDUE(file+"corpus2MDL", file+"corpus2MDL_occurrences").getFragmentCatalogAsHashMap();
//            HashMap<String,Fragment> structureResults = new FragmentCollectionReaderSUBDUE(file+"corpus2Size", file+"corpus2Size_occurrences").getFragmentCatalogAsHashMap();
//            ArrayList<Fragment> selectedFragments = new ArrayList<Fragment>();
////            selectedFragments.add(structureResults.get("SUB_2"));
////            selectedFragments.add(structureResults.get("SUB_3"));
////            selectedFragments.add(structureResults.get("SUB_18"));
////            selectedFragments.add(structureResults.get("SUB_6"));
////            selectedFragments.add(structureResults.get("SUB_9"));
////            selectedFragments.add(structureResults.get("SUB_20"));
////            selectedFragments.add(structureResults.get("SUB_5"));
////            selectedFragments.add(structureResults.get("SUB_15"));
////            selectedFragments.add(structureResults.get("SUB_10"));
////            selectedFragments.add(structureResults.get("SUB_4"));
//            
//            selectedFragments.add(structureResults.get("SUB_13"));
//            selectedFragments.add(structureResults.get("SUB_8"));
//            selectedFragments.add(structureResults.get("SUB_14"));
//            selectedFragments.add(structureResults.get("SUB_23"));
//            selectedFragments.add(structureResults.get("SUB_29"));
//            selectedFragments.add(structureResults.get("SUB_5"));
//            selectedFragments.add(structureResults.get("SUB_7"));
           
           FragmentCatalogAndResultsToRDFSUBDUE catalogNoInference = new FragmentCatalogAndResultsToRDFSUBDUE("eScienceCorpus1MDL.ttl");

           catalogNoInference.transformFragmentCollectionToRDF(selectedFragments);
           catalogNoInference.transformResultsAndNoBindingsInTemplateCollection(selectedFragments, filteredC);

           catalogNoInference.exportToRDFFile("TURTLE");

           
        }catch(Exception e){
            System.err.println("Error while saving the catalog to RDF "+e.getMessage());
        }
  }
}
