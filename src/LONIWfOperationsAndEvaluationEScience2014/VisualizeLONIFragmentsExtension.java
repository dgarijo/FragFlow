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
 * Dataset: Samuel
 * @author Daniel Garijo
 */
public class VisualizeLONIFragmentsExtension {
    public static void main(String[] args){
        String file = TestConstants.SUBDUEResultFolder+"eScienceResultsAdditional(Samuel)\\";
        String ocFile = TestConstants.SUBDUEResultFolder +"eScienceResultsAdditional(Samuel)\\";
        String outputPath="fragments";
        SUBDUEFragmentCollectionWriterGraphml instance = new SUBDUEFragmentCollectionWriterGraphml();
        try{
            //Corpus 4 (Samuel)
            HashMap<String,Fragment> structureResults = new FragmentCollectionReaderSUBDUE(file+"corpus4Size", ocFile+"corpus4Size_occurrences").getFragmentCatalogAsHashMap();
            HashMap<String,Fragment> selectedResults = new HashMap<String, Fragment>();
            selectedResults.put("SUB_1", structureResults.get("SUB_1"));
            selectedResults.put("SUB_5", structureResults.get("SUB_5"));
            selectedResults.put("SUB_8", structureResults.get("SUB_8"));
            selectedResults.put("SUB_9", structureResults.get("SUB_9"));
            selectedResults.put("SUB_11", structureResults.get("SUB_11"));
            selectedResults.put("SUB_12", structureResults.get("SUB_12"));
            selectedResults.put("SUB_14", structureResults.get("SUB_14"));
            selectedResults.put("SUB_16", structureResults.get("SUB_16"));
            selectedResults.put("SUB_17", structureResults.get("SUB_17"));
            selectedResults.put("SUB_18", structureResults.get("SUB_18"));
            instance.writeFragmentsToFile(selectedResults, outputPath+"corpus4Size.graphml", null);
            
//            structureResults = new FragmentCollectionReaderSUBDUE(file+"corpus1SIZE", ocFile+"corpus1SIZE_occurrences").getFragmentCatalogAsHashMap();
//            instance.writeFragmentsToFile(structureResults, outputPath+"corpus1SIZE.graphml", null);
            
        }catch(Exception e){
            System.err.println("Error: something whent wrong when writing the fragments");
        }
    }
}
