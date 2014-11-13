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

import DataStructures.Fragment;
import DataStructures.GraphCollection;
import Factory.Loni.LoniTemplate2Graph;
import IO.Formats.SUBDUE.FragmentCatalogAndResultsToRDFSUBDUE;
import IO.DatasetFilter;
import IO.Formats.SUBDUE.FragmentCollectionReaderSUBDUE;
import Static.TestConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The purpose of this script is to transform the fragment catalog and instances to RDF
 * (A REDUCED PORTION) to find out examples of certain fragments
 * @author Daniel Garijo
 */
public class FragmentsToRDFExpansion {
  public static void main(String[] args)   {
      try{
          String file = TestConstants.SUBDUEResultFolder +"eScienceResultsAdditional(Samuel)\\";
          
           //String loniDatasetFolder = "C:\\Users\\Monen\\Desktop\\LONIDatasets\\WorkflowIVO\\all\\";
          String loniDatasetFolder = "C:\\Users\\Monen\\Desktop\\LONIDatasets\\datasetSamuel\\";
           File f = new File(loniDatasetFolder);
           LoniTemplate2Graph fullCollection = new LoniTemplate2Graph(loniDatasetFolder);
           if(f.isDirectory()){
               File[] files = f.listFiles();
              for (File file1 : files) {
                  fullCollection.transformToGraph(file1.getName());
              }
           }
           GraphCollection filteredC = DatasetFilter.removeDuplicates(fullCollection.getGraphCollection());
           
           System.out.println("Filtered templates: "+filteredC.getNumberOfSubGraphs());
           //Corpus 4 (Samuel)
           HashMap<String,Fragment> structureResults = new FragmentCollectionReaderSUBDUE(file+"corpus4MDL", file+"corpus4MDL_occurrences").getFragmentCatalogAsHashMap();
           ArrayList<Fragment> selectedFragments = new ArrayList<Fragment>();
           //MDL
           selectedFragments.add(structureResults.get("SUB_2"));
//           
           //size
//           selectedFragments.add(structureResults.get("SUB_1"));
//           selectedFragments.add(structureResults.get("SUB_5"));
//           selectedFragments.add(structureResults.get("SUB_8"));
//           selectedFragments.add(structureResults.get("SUB_9"));
//           selectedFragments.add(structureResults.get("SUB_12"));
//           selectedFragments.add(structureResults.get("SUB_14"));
//           selectedFragments.add(structureResults.get("SUB_16"));
//           selectedFragments.add(structureResults.get("SUB_17"));
//           selectedFragments.add(structureResults.get("SUB_18"));
           
           FragmentCatalogAndResultsToRDFSUBDUE catalogNoInference = new FragmentCatalogAndResultsToRDFSUBDUE("eScienceCorpus1MDL.ttl");

           catalogNoInference.transformFragmentCollectionToRDF(selectedFragments);
           catalogNoInference.transformResultsAndNoBindingsInTemplateCollection(selectedFragments, filteredC);

           catalogNoInference.exportToRDFFile("TURTLE");

           
        }catch(Exception e){
            System.err.println("Error while saving the catalog to RDF "+e.getMessage());
        }
  }
}
