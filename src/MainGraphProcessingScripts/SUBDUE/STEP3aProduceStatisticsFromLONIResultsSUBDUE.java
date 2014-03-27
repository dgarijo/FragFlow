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

/**
 * Small script to produce statistics from the LONI results. It also filters the 
 * catalog
 * @author Daniel Garijo
 */
public class STEP3aProduceStatisticsFromLONIResultsSUBDUE {
    
    public static void main(String[] args){
        try{
            
            //eval 1
            String file = "SUBDUE_TOOL\\results\\resultsLoni\\resultsLoniDatasetEval1";
            String ocFile = "SUBDUE_TOOL\\results\\resultsLoni\\resultsLoniDatasetEval1_occurrences";
            CreateStatisticsFromResultsSUBDUE test1 = new CreateStatisticsFromResultsSUBDUE("LONI Dataset","MDL", true, true, file, ocFile);
            test1.printStatistics("STATISTICS-LoniDatasetEval1");
            //eval 2
            file = "SUBDUE_TOOL\\results\\resultsLoni\\resultsLoniDatasetEval2";
            ocFile = "SUBDUE_TOOL\\results\\resultsLoni\\resultsLoniDatasetEval2_occurrences";
            CreateStatisticsFromResultsSUBDUE test2 = new CreateStatisticsFromResultsSUBDUE("LONI Dataset","size", true, true,file, ocFile);
            test2.printStatistics("STATISTICS-LoniDatasetEval2");
            
        }catch(Exception e){
            System.err.println("Error while executing the script "+e.getMessage());
        }
    }
}
