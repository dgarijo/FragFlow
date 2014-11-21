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

import DataStructures.GraphCollection;
import Factory.Loni.LoniTemplate2Graph;
import Factory.OPMW.OPMWTemplate2GraphTaverna;
import IO.DatasetFilter;
import IO.Exception.CollectionWriterException;
import IO.Formats.SUBDUE.GraphCollectionWriterSUBDUE;
import java.io.File;

/**
 * Script to transform the Taverna OPMW Templates created by Beatriz Garcia and
 * Mark Wilkinson to SUBDUE
 * @author Daniel Garijo
 */
public class TavernaOPMWTemplates2SUBDUE {
    private static void processFolder(String folderPath, String outName) throws CollectionWriterException{
        //first process all templates 
        File f = new File(folderPath);
        if(f.isDirectory()){
            OPMWTemplate2GraphTaverna test = new OPMWTemplate2GraphTaverna(folderPath);
            File[] files = f.listFiles();
            for(int i=0; i< files.length;i++){
                test.transformToGraph(files[i].getName());
            }
//            test.transformToGraph("wf_myExperiment_999_annotations_datalinks_oneTerm_EDAM.rdf");
            System.out.println("Initial number of workflows: "+files.length);
            System.out.println("Initial number of processed templates: "+test.getGraphCollection().getNumberOfSubGraphs());
            GraphCollection filteredC = DatasetFilter.removeDuplicates(test.getGraphCollection());
            System.out.println("Number of templates removing duplicates: "+filteredC.getNumberOfSubGraphs());
            GraphCollectionWriterSUBDUE writer = new GraphCollectionWriterSUBDUE();
            writer.writeReducedGraphsToFile(filteredC,outName, null);
        }
    }
    
    //test method
    public static void main(String[] args){
       String step1 = "C:\\Users\\Monen\\Desktop\\colabBeatriz&Mark\\Category Analysis (Step 1)\\inputs\\WfsTypeIsCategory\\";//"C:\\Users\\Monen\\Desktop\\colabBeatriz&Mark\\annotPerOntJustOneTerm_2014.11.13\\AnnotPerOntJustOneTerm\\EDAM\\";
       String step11 = "C:\\Users\\Monen\\Desktop\\colabBeatriz&Mark\\Service URI Analysis (Step 1.1)\\inputs\\WfsTypeIsURIserv\\";
       //note: step 2 has to be done for the 12 ontologies.
       String step2 = "C:\\Users\\Monen\\Desktop\\colabBeatriz&Mark\\Annotation Based Analysis (Step 2 and 3)\\inputs\\AnnotPerOntJustOneTerm\\EDAM\\";
       String step3 = step2;//same as step 2
       String step4 = "";
       String step5 = "";
       try{
            processFolder(step1, "corpusTavStep1.g");
            processFolder(step11, "corpusTavStep1-1.g");
            processFolder(step2, "corpusTavStep2-EDAM.g");
       }catch(Exception e){
           System.err.println("Error while writing the results");
       }
        
    }

}
