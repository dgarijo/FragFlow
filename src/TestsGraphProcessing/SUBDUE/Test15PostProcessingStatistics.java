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
package TestsGraphProcessing.SUBDUE;

import PostProcessing.Formats.SUBDUE.CreateStatisticsFromResultsSUBDUE;

/**
 * Test to check whether the statistics are created properly or not.
 * @author Daniel Garijo
 */
public class Test15PostProcessingStatistics {
    public static int testNumber = 15;
    public static boolean test(){
        System.out.println("\n\nExecuting test:"+testNumber+" Statistics creation");
        try{
            CreateStatisticsFromResultsSUBDUE test = new CreateStatisticsFromResultsSUBDUE("text analytics",
                    "MDL", true, false, true);
            String file = "SUBDUE_TOOL\\results\\Tests\\testResultReduced2";
            String ocFile = "SUBDUE_TOOL\\results\\Tests\\testResultReduced2_occurrences";
            test.createStatisticsFromFile(file, ocFile);
            test.printStatistics("testStatistics");
            //if the file is found, the statistics are created
            return true;
        }catch(Exception e){
            System.out.println("Error in test PostProcessing. Exception: "+e.getMessage());
            return false;
        }
    }
    public static void main(String[] args){
        if(test())System.out.println("Test "+testNumber+" OK");
        else System.out.println("Test "+testNumber+" FAILED");
    }
}
