/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

import PostProcessing.Formats.SUBDUE.CreateStatisticsFromResultsSUBDUE;
import java.util.ArrayList;

/**
 * Small script to produce statistics from the SUBDUE results. The statistics 
 * produced are 
 *  - The structures found (number of fragments)
 *  - The number of irreducible fragments (structures that do not contain others)
 *  - The number multi step fragments structures found (i.e., those with at 
 * least two process steps)
 *  - Number of multi step irreducible fragments found.
 *  - Frequency of detected structures.
 *  - Frequency of multi step fragments
 * @author Daniel Garijo
 */
public class STEP3ProduceStatisticsFromOPMWResultsSUBDUE {
    
    public static void main(String[] args){
        try{
            /**
             * Templ (inf)
             */
            //eval 1
            String file = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_Inference_Templates-results1";
            String ocFile = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_Inference_Templates-results1_occurrences";
            CreateStatisticsFromResultsSUBDUE test1 = new CreateStatisticsFromResultsSUBDUE("text analytics","MDL", true, true, file, ocFile);
//            test1.createStatistics();
            test1.printStatistics("STATISTICS-Text_Analytics_Graph_Inference_Templates-results1");
            //eval 2
            file = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_Inference_Templates-results2";
            ocFile = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_Inference_Templates-results2_occurrences";
            CreateStatisticsFromResultsSUBDUE test2 = new CreateStatisticsFromResultsSUBDUE("text analytics","size", true, true,file, ocFile);
//            test2.createStatistics();
            test2.printStatistics("STATISTICS-Text_Analytics_Graph_Inference_Templates-results2");
            /**
             * Templ (no-inf)
             */
            //eval 1
            file = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_No_Inference_Templates-results1";
            ocFile = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_No_Inference_Templates-results1_occurrences";
            CreateStatisticsFromResultsSUBDUE test3 = new CreateStatisticsFromResultsSUBDUE("text analytics","MDL", true, false,file, ocFile);
//            test3.createStatistics();
            test3.printStatistics("STATISTICS-Text_Analytics_Graph_No_Inference_Templates-results1");
            //eval 2
            file = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_No_Inference_Templates-results2";
            ocFile = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_No_Inference_Templates-results2_occurrences";
            CreateStatisticsFromResultsSUBDUE test4 = new CreateStatisticsFromResultsSUBDUE("text analytics","size", true, false,file, ocFile);
//            test4.createStatistics();
            test4.printStatistics("STATISTICS-Text_Analytics_Graph_No_Inference_Templates-results2");
            /**
             * Traces (inf)
             */
            //eval 1
            file = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_Inference-results1";
            ocFile = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_Inference-results1_occurrences";
            CreateStatisticsFromResultsSUBDUE test5 = new CreateStatisticsFromResultsSUBDUE("text analytics","MDL", false, true,file, ocFile);
//            test5.createStatistics();
            test5.printStatistics("STATISTICS-Text_Analytics_Graph_Inference-results1");
            //eval 2
            file = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_Inference-results2";
            ocFile = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_Inference-results2_occurrences";
            CreateStatisticsFromResultsSUBDUE test6 = new CreateStatisticsFromResultsSUBDUE("text analytics","size", false, true,file, ocFile);
//            test6.createStatistics();
            test6.printStatistics("STATISTICS-Text_Analytics_Graph_Inference-results2");
            /**
             * Traces (no-inf)
             */
            //eval 1
            file = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_No_Inference-results1";
            ocFile = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_No_Inference-results1_occurrences";
            CreateStatisticsFromResultsSUBDUE test7 = new CreateStatisticsFromResultsSUBDUE("text analytics","MDL", false, false,file, ocFile);
//            test7.createStatistics();
            test7.printStatistics("STATISTICS-Text_Analytics_Graph_No_Inference-results1");
            //eval 2
            file = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_No_Inference-results2";
            ocFile = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_No_Inference-results2_occurrences";
            CreateStatisticsFromResultsSUBDUE test8 = new CreateStatisticsFromResultsSUBDUE("text analytics","size", false, false,file, ocFile);
//            test8.createStatistics();
            test8.printStatistics("STATISTICS-Text_Analytics_Graph_No_Inference-results2");
        }catch(Exception e){
            System.err.println("Error while executing the script "+e.getMessage());
        }
    }
}
