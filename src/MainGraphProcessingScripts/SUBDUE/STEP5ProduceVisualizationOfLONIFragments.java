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
import IO.Formats.SUBDUE.FragmentCollectionReaderSUBDUE;
import IO.Graphml.SUBDUEFragmentCollectionWriterGraphml;
import Static.TestConstants;
import java.util.HashMap;

/**
 * Small script to visualize the fragments from the different LONI datasets.
 * @author Daniel Garijo
 */
public class STEP5ProduceVisualizationOfLONIFragments {
    public static void main(String[] args){
        String file = TestConstants.SUBDUEResultFolder + "\\resultsLoni\\";
        String ocFile = TestConstants.SUBDUEResultFolder + "\\resultsLoni\\";
        String outputPath="fragments";
        SUBDUEFragmentCollectionWriterGraphml instance = new SUBDUEFragmentCollectionWriterGraphml();
        try{
            //public dataset (ev1,2)
            HashMap<String,Fragment> structureResults = new FragmentCollectionReaderSUBDUE(file+"resultsFullLONISUBDUEEval1", ocFile+"resultsFullLONISUBDUEEval1_occurrences").getFragmentCatalogAsHashMap();
            instance.writeFragmentsToFile(structureResults, outputPath+"PublicDatasetEV1.graphml", null);
            structureResults = new FragmentCollectionReaderSUBDUE(file+"resultsFullLONISUBDUEEval2", ocFile+"resultsFullLONISUBDUEEval2_occurrences").getFragmentCatalogAsHashMap();
            instance.writeFragmentsToFile(structureResults, outputPath+"PublicDatasetEV2.graphml", null);
            
            //zhizhong's dataset (ev1,2)
            structureResults = new FragmentCollectionReaderSUBDUE(file+"resultsLoniZhiFilteredSUBDUEEval1", ocFile+"resultsLoniZhiFilteredSUBDUEEval1_occurrences").getFragmentCatalogAsHashMap();
            instance.writeFragmentsToFile(structureResults, outputPath+"ZhiZhongDatasetEV1.graphml", null);
            structureResults = new FragmentCollectionReaderSUBDUE(file+"resultsLoniZhiFilteredSUBDUEEval2", ocFile+"resultsLoniZhiFilteredSUBDUEEval2_occurrences").getFragmentCatalogAsHashMap();
            instance.writeFragmentsToFile(structureResults, outputPath+"ZhiZhongDatasetEV2.graphml", null);
            
            //boris' dataset (ev1,2)
            structureResults = new FragmentCollectionReaderSUBDUE(file+"resultsLoniBorisFilteredSUBDUEval1", ocFile+"resultsLoniBorisFilteredSUBDUEval1_occurrences").getFragmentCatalogAsHashMap();
            instance.writeFragmentsToFile(structureResults, outputPath+"BorisDatasetEV1.graphml", null);
            structureResults = new FragmentCollectionReaderSUBDUE(file+"resultsLoniBorisFilteredSUBDUEval2", ocFile+"resultsLoniBorisFilteredSUBDUEval2_occurrences").getFragmentCatalogAsHashMap();
            instance.writeFragmentsToFile(structureResults, outputPath+"BorisDatasetEV2.graphml", null);
            
            //neda's dataset (ev1,2)
            structureResults = new FragmentCollectionReaderSUBDUE(file+"resultsLoniNedaFilteredSUBDUEEval1", ocFile+"resultsLoniNedaFilteredSUBDUEEval1_occurrences").getFragmentCatalogAsHashMap();
            instance.writeFragmentsToFile(structureResults, outputPath+"NedaDatasetEV1.graphml", null);
            structureResults = new FragmentCollectionReaderSUBDUE(file+"resultsLoniNedaFilteredSUBDUEval2", ocFile+"resultsLoniNedaFilteredSUBDUEval2_occurrences").getFragmentCatalogAsHashMap();
            instance.writeFragmentsToFile(structureResults, outputPath+"NedaDatasetEV2.graphml", null);
        }catch(Exception e){
            System.err.println("Error: something whent wrong when writing the fragments");
        }
    }
}
