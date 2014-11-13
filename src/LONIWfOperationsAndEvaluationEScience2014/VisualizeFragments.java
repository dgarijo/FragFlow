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
import IO.Formats.SUBDUE.FragmentCollectionReaderSUBDUE;
import IO.Graphml.SUBDUEFragmentCollectionWriterGraphml;
import Static.TestConstants;
import java.util.HashMap;

/**
 * Small script to visualize the fragments from the different LONI datasets.
 * Datasets: Boris, Ivo
 * @author Daniel Garijo
 */
public class VisualizeFragments {
    public static void main(String[] args){
        String file = TestConstants.SUBDUEResultFolder +"eScienceResults(LONI)\\";
        String ocFile = TestConstants.SUBDUEResultFolder +"eScienceResults(LONI)\\";
        String outputPath="fragments";
        SUBDUEFragmentCollectionWriterGraphml instance = new SUBDUEFragmentCollectionWriterGraphml();
        try{
            //Boris (corpus 1)
//            HashMap<String,Fragment> structureResults = new FragmentCollectionReaderSUBDUE(file+"corpus1SIZE", ocFile+"corpus1SIZE_occurrences").getFragmentCatalogAsHashMap();
//            HashMap<String,Fragment> selectedResults = new HashMap<String, Fragment>();
//            //MDL fragments (corpus 1) selected for evaluation
////            selectedResults.put("SUB_2", structureResults.get("SUB_2")); 
////            selectedResults.put("SUB_8", structureResults.get("SUB_8"));
////            selectedResults.put("SUB_29", structureResults.get("SUB_29")); 
////            selectedResults.put("SUB_28", structureResults.get("SUB_28"));
////            selectedResults.put("SUB_43", structureResults.get("SUB_43")); 
////            selectedResults.put("SUB_5", structureResults.get("SUB_5"));
////            selectedResults.put("SUB_38", structureResults.get("SUB_38")); 
////            selectedResults.put("SUB_12", structureResults.get("SUB_12"));            
////            selectedResults.put("SUB_18", structureResults.get("SUB_18")); 
////            selectedResults.put("SUB_37", structureResults.get("SUB_37"));
////            selectedResults.put("SUB_4", structureResults.get("SUB_4"));
//            selectedResults.put("SUB_7", structureResults.get("SUB_7"));
//            selectedResults.put("SUB_13", structureResults.get("SUB_13"));
//            selectedResults.put("SUB_20", structureResults.get("SUB_20"));
//            selectedResults.put("SUB_27", structureResults.get("SUB_27"));
//            selectedResults.put("SUB_30", structureResults.get("SUB_30"));
//            selectedResults.put("SUB_22", structureResults.get("SUB_22"));
//            selectedResults.put("SUB_23", structureResults.get("SUB_23"));
//            selectedResults.put("SUB_11", structureResults.get("SUB_11"));
//            instance.writeFragmentsToFile(selectedResults, outputPath+"corpus1SIZE.graphml", null);
            
            //Corpus 2 (IVO
            HashMap<String,Fragment> structureResults = new FragmentCollectionReaderSUBDUE(file+"corpus2MDL", ocFile+"corpus2MDL_occurrences").getFragmentCatalogAsHashMap();
            HashMap<String,Fragment> selectedResults = new HashMap<String, Fragment>();
            selectedResults.put("SUB_2", structureResults.get("SUB_2"));
            selectedResults.put("SUB_3", structureResults.get("SUB_3"));
            selectedResults.put("SUB_18", structureResults.get("SUB_18"));
            selectedResults.put("SUB_6", structureResults.get("SUB_6"));
            selectedResults.put("SUB_9", structureResults.get("SUB_9"));
            selectedResults.put("SUB_20", structureResults.get("SUB_20"));
            selectedResults.put("SUB_5", structureResults.get("SUB_5"));
            selectedResults.put("SUB_15", structureResults.get("SUB_15"));
            selectedResults.put("SUB_10", structureResults.get("SUB_10"));
            selectedResults.put("SUB_4", structureResults.get("SUB_4"));
//            selectedResults.put("SUB_13", structureResults.get("SUB_13"));
//            selectedResults.put("SUB_8", structureResults.get("SUB_8"));
//            selectedResults.put("SUB_14", structureResults.get("SUB_14"));
//            selectedResults.put("SUB_23", structureResults.get("SUB_23"));
//            selectedResults.put("SUB_29", structureResults.get("SUB_29"));
//            selectedResults.put("SUB_5", structureResults.get("SUB_5"));
//            selectedResults.put("SUB_7", structureResults.get("SUB_7"));
//            selectedResults.put("SUB_1", structureResults.get("SUB_1"));
            instance.writeFragmentsToFile(selectedResults, outputPath+"corpus2MDL.graphml", null);
            
//            structureResults = new FragmentCollectionReaderSUBDUE(file+"corpus1SIZE", ocFile+"corpus1SIZE_occurrences").getFragmentCatalogAsHashMap();
//            instance.writeFragmentsToFile(structureResults, outputPath+"corpus1SIZE.graphml", null);
            
        }catch(Exception e){
            System.err.println("Error: something whent wrong when writing the fragments");
        }
    }
}
