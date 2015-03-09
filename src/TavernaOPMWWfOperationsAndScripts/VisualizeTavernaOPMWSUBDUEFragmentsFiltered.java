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

package TavernaOPMWWfOperationsAndScripts;

import DataStructures.Fragment;
import IO.Formats.SUBDUE.FragmentCollectionReaderSUBDUE;
import IO.Graphml.SUBDUEFragmentCollectionWriterGraphml;
import Static.TestConstants;
import java.util.HashMap;

/**
 * This class will filter fragments with empty annotations and then visualize them.
 * @author Daniel Garijo
 */
public class VisualizeTavernaOPMWSUBDUEFragmentsFiltered {
    //shall I filter something here as well?
    public static void main(String[] args){
        //String file = TestConstants.SUBDUEResultFolder+"resultsTavernaColab\\step1 (categories)\\";
        //String file = TestConstants.SUBDUEResultFolder+"resultsTavernaColab\\step1-1 (web services)\\";
        String file = TestConstants.SUBDUEResultFolder+"resultsTavernaColab\\step 2 (ontologies)\\filtered(edam)\\";
        //String ocFile = TestConstants.SUBDUEResultFolder +"resultsTavernaColab\\step1 (categories)\\";
        String ocFile = file;
        String outputPath="";//"fragments";
        SUBDUEFragmentCollectionWriterGraphml instance = new SUBDUEFragmentCollectionWriterGraphml();
        try{
            HashMap<String,Fragment> structureResults = new FragmentCollectionReaderSUBDUE(file+"resultsTavStep2-1Ev1-EDAM-Filtered", ocFile+"resultsTavStep2-1Ev1-EDAM-Filtered_occurrences").getFragmentCatalogAsHashMap();
            structureResults = TavernaOPMWFragmentFilter.filter(structureResults);
            instance.writeFragmentsToFile(structureResults, outputPath+"resultsTavStep2-1Ev1-EDAM-Filtered.graphml", null);            
            
            HashMap<String,Fragment> structureResults1 = new FragmentCollectionReaderSUBDUE(file+"resultsTavStep2-1Ev2-EDAM-Filtered", ocFile+"resultsTavStep2-1Ev2-EDAM-Filtered_occurrences").getFragmentCatalogAsHashMap();
            structureResults1 = TavernaOPMWFragmentFilter.filter(structureResults1);
            instance.writeFragmentsToFile(structureResults1, outputPath+"resultsTavStep2-1Ev2-EDAM-Filtered.graphml", null);
           
        }catch(Exception e){
            System.err.println("Error: something whent wrong when writing the fragments");
        }
    }
}
