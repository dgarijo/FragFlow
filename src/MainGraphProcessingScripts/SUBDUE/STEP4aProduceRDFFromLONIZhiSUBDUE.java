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
package MainGraphProcessingScripts.SUBDUE;

import DataStructures.Fragment;
import DataStructures.Graph;
import Factory.Loni.LoniTemplate2Graph;
import IO.Formats.SUBDUE.FragmentCatalogAndResultsToRDFSUBDUE;
import PostProcessing.Formats.SUBDUE.CreateStatisticsFromResultsSUBDUE;
import Static.GeneralConstants;
import Static.GeneralMethods;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Main script to export the LONI Fragment Catalog determined by SUBDUE to RDF.
 * The model will be exported according to the workflow fragment description 
 * ontology (http://purl.org/net/wf-fd)
 * @author Daniel Garijo
 */
public class STEP4aProduceRDFFromLONIZhiSUBDUE {
  public static void main(String[] args)   {
      try{
//           OPMWTemplate2Graph test = new OPMWTemplate2Graph("http://wind.isi.edu:8890/sparql");    
//           test.transformDomainToGraph("TextAnalytics");
           String file = "SUBDUE_TOOL\\results\\resultsLoni\\resultsLoniDatasetZhiEval1";
           String ocFile = "SUBDUE_TOOL\\results\\resultsLoni\\resultsLoniDatasetZhiEval1_occurrences";
           //note that calling this method is faster than declaring the reader and then filtering the catalog.
           CreateStatisticsFromResultsSUBDUE aux = new CreateStatisticsFromResultsSUBDUE("LONI dataset",
                    "MDL", true, false,file, ocFile);
           ArrayList<Fragment> obtainedResults = aux.getMultiStepFragments();
           
//           casi mejor: haz un decode del dataset y llama a lo que ya tenias, tio bestia
           
           /**
            * TO DO
            */
           
           //generalize the fragments types (workaround)
//           Iterator<Fragment> itF = obtainedResults.iterator();
//           while(itF.hasNext()){
//               GeneralMethods.setTypesOfCurrentFragment(itF.next());
//           }
           
//           String loniDatasetFolder = "LONI_dataset\\";
//           File f = new File(loniDatasetFolder);
//           LoniTemplate2Graph fullCollection = new LoniTemplate2Graph(loniDatasetFolder);
//           if(f.isDirectory()){
//               File[] files = f.listFiles();
//               for(int i=0;i<files.length;i++){
//                   fullCollection.transformToGraph(files[i].getName());
//               }
//           }
           //we need to transform some types and names to URIs in order to properly find things
           //(the fragments have been properly treated when fixing the directionality)
//           ArrayList<Graph> collection = fullCollection.getGraphCollection().getGraphs();
//           Iterator<Graph> it = collection.iterator();
//           while (it.hasNext()){
//               Graph currentG = it.next();
//               GeneralMethods.setStringTypesToURIs(currentG);
//               if(!currentG.getName().startsWith("http://")){
//                   currentG.setName(GeneralConstants.PREFIX_FOR_RDF_GENERATION+GeneralMethods.encode(currentG.getName()));
//               }
//           }
           
           FragmentCatalogAndResultsToRDFSUBDUE catalogNoInference = new FragmentCatalogAndResultsToRDFSUBDUE("outLONIZHI.ttl");

           catalogNoInference.transformFragmentCollectionToRDF(obtainedResults);
//           catalogNoInference.transformBindingResultsInTemplateCollection(obtainedResults, fullCollection.getGraphCollection());

           catalogNoInference.exportToRDFFile("TURTLE");

           //with evaluation 2
           //it is the same code
           
        }catch(Exception e){
            System.err.println("Error while saving the catalog to RDF "+e.getMessage());
        }
  }
}
