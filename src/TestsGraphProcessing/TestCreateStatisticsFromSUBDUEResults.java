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
package TestsGraphProcessing;

import PostProcessing.Formats.CreateStatisticsFromResultsSUBDUE;

/**
 * Test designed to create statistics from a result file. 
 * The file can be set up within the test itself
 * @author Daniel Garijo
 */
public class TestCreateStatisticsFromSUBDUEResults {
    public static int testNumber = 12;
    public static boolean test(){
        System.out.println("\n\nExecuting test:"+testNumber+"  Create statistics from SUBDUE Result file");
        
        try{
            /**
             * Templ (inf)
             */
            //eval 1
            CreateStatisticsFromResultsSUBDUE test1 = new CreateStatisticsFromResultsSUBDUE("text analytics","MDL", true, true, false);
            String file = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_Inference_Templates-results1";
            String ocFile = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_Inference_Templates-results1_occurrences";
            test1.createStatisticsFromFile(file, ocFile);
            test1.printStatistics("STATISTICS-Text_Analytics_Graph_Inference_Templates-results1");
            //eval 2
            CreateStatisticsFromResultsSUBDUE test2 = new CreateStatisticsFromResultsSUBDUE("text analytics","size", true, true, false);
            file = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_Inference_Templates-results2";
            ocFile = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_Inference_Templates-results2_occurrences";
            test2.createStatisticsFromFile(file, ocFile);
            test2.printStatistics("STATISTICS-Text_Analytics_Graph_Inference_Templates-results2");
            /**
             * Templ (no-inf)
             */
            //eval 1
            CreateStatisticsFromResultsSUBDUE test3 = new CreateStatisticsFromResultsSUBDUE("text analytics","MDL", true, false, false);
            file = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_No_Inference_Templates-results1";
            ocFile = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_No_Inference_Templates-results1_occurrences";
            test3.createStatisticsFromFile(file, ocFile);
            test3.printStatistics("STATISTICS-Text_Analytics_Graph_No_Inference_Templates-results1");
            //eval 2
            CreateStatisticsFromResultsSUBDUE test4 = new CreateStatisticsFromResultsSUBDUE("text analytics","size", true, false, false);
            file = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_No_Inference_Templates-results2";
            ocFile = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_No_Inference_Templates-results2_occurrences";
            test4.createStatisticsFromFile(file, ocFile);
            test4.printStatistics("STATISTICS-Text_Analytics_Graph_No_Inference_Templates-results2");
            /**
             * Traces (inf)
             */
            //eval 1
            CreateStatisticsFromResultsSUBDUE test5 = new CreateStatisticsFromResultsSUBDUE("text analytics","MDL", false, true, false);
            file = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_Inference-results1";
            ocFile = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_Inference-results1_occurrences";
            test5.createStatisticsFromFile(file, ocFile);
            test5.printStatistics("STATISTICS-Text_Analytics_Graph_Inference-results1");
            //eval 2
            CreateStatisticsFromResultsSUBDUE test6 = new CreateStatisticsFromResultsSUBDUE("text analytics","size", false, true, false);
            file = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_Inference-results2";
            ocFile = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_Inference-results2_occurrences";
            test6.createStatisticsFromFile(file, ocFile);
            test6.printStatistics("STATISTICS-Text_Analytics_Graph_Inference-results2");
            /**
             * Traces (no-inf)
             */
            //eval 1
            CreateStatisticsFromResultsSUBDUE test7 = new CreateStatisticsFromResultsSUBDUE("text analytics","MDL", false, false, false);
            file = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_No_Inference-results1";
            ocFile = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_No_Inference-results1_occurrences";
            test7.createStatisticsFromFile(file, ocFile);
            test7.printStatistics("STATISTICS-Text_Analytics_Graph_No_Inference-results1");
            //eval 2
            CreateStatisticsFromResultsSUBDUE test8 = new CreateStatisticsFromResultsSUBDUE("text analytics","size", false, false, false);
            file = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_No_Inference-results2";
            ocFile = "SUBDUE_TOOL\\results\\resultsSun_Apr_21_19_36_05_CEST_2013\\Text_Analytics_Graph_No_Inference-results2_occurrences";
            test8.createStatisticsFromFile(file, ocFile);
            test8.printStatistics("STATISTICS-Text_Analytics_Graph_No_Inference-results2");

            return true;
        }catch(Exception e){
            System.out.println("Error while executing test. Exception: "+e.getMessage());
            return false;
        }
    }
    
    public static void main(String[] args){
        if(test())System.out.println("Test "+testNumber+" OK");
        else System.out.println("Test "+testNumber+" FAILED");
    }
    
}
