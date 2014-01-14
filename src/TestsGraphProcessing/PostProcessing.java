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
 *
 * @author Daniel Garijo
 */
public class PostProcessing {
    public static void test(){
        try{
            CreateStatisticsFromResultsSUBDUE test = new CreateStatisticsFromResultsSUBDUE("text analytics",
                    "MDL", true, false, true);
            String file = "C:\\Users\\Monen\\Dropbox\\NetBeansProjects\\MotifFinder\\SUBDUE_TOOL\\results\\Tests\\testResultReduced2";
            String ocFile = "C:\\Users\\Monen\\Dropbox\\NetBeansProjects\\MotifFinder\\SUBDUE_TOOL\\results\\Tests\\testResultReduced2_occurrences";
            test.createStatisticsFromFile(file, ocFile);
            test.printStatistics("testStatistics");
        }catch(Exception e){
            System.out.println("Error in test PostProcessing. Exception: "+e.getMessage());
        }
    }
    public static void main(String[] args){
        test();
    }
}
