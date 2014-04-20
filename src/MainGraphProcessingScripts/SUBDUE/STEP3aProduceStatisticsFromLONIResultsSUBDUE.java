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
            
            //Full dataset (public)
            //eval 1
            String file = "SUBDUE_TOOL\\results\\resultsLoni\\resultsFullLONISUBDUEEval1";
            String ocFile = "SUBDUE_TOOL\\results\\resultsLoni\\resultsFullLONISUBDUEEval1_occurrences";
            CreateStatisticsFromResultsSUBDUE test1 = new CreateStatisticsFromResultsSUBDUE("LONI Full Dataset","MDL", true, true, file, ocFile);
            test1.printStatistics("Stats-LoniFullEv1");
            //eval 2
            file = "SUBDUE_TOOL\\results\\resultsLoni\\resultsFullLONISUBDUEEval2";
            ocFile = "SUBDUE_TOOL\\results\\resultsLoni\\resultsFullLONISUBDUEEval2_occurrences";
            CreateStatisticsFromResultsSUBDUE test2 = new CreateStatisticsFromResultsSUBDUE("LONI Full Dataset","size", true, true,file, ocFile);
            test2.printStatistics("Stats-LoniFullEv2");
            
            //Neda
            file = "SUBDUE_TOOL\\results\\resultsLoni\\resultsLoniNedaFilteredSUBDUEEval1";
            ocFile = "SUBDUE_TOOL\\results\\resultsLoni\\resultsLoniNedaFilteredSUBDUEEval1_occurrences";
            test2 = new CreateStatisticsFromResultsSUBDUE("LONI Neda Dataset","MDL", true, true,file, ocFile);
            test2.printStatistics("Stats-LoniNedaEv1");
            
            file = "SUBDUE_TOOL\\results\\resultsLoni\\resultsLoniNedaFilteredSUBDUEval2";
            ocFile = "SUBDUE_TOOL\\results\\resultsLoni\\resultsLoniNedaFilteredSUBDUEval2_occurrences";
            test2 = new CreateStatisticsFromResultsSUBDUE("LONI Neda Dataset","size", true, true,file, ocFile);
            test2.printStatistics("Stats-LoniNedaEv2");
            
            //Boris
            file = "SUBDUE_TOOL\\results\\resultsLoni\\resultsLoniBorisFilteredSUBDUEval1";
            ocFile = "SUBDUE_TOOL\\results\\resultsLoni\\resultsLoniBorisFilteredSUBDUEval1_occurrences";
            test2 = new CreateStatisticsFromResultsSUBDUE("LONI Boris Dataset","MDL", true, true,file, ocFile);
            test2.printStatistics("Stats-LoniBorisEv1");
            
            file = "SUBDUE_TOOL\\results\\resultsLoni\\resultsLoniBorisFilteredSUBDUEval2";
            ocFile = "SUBDUE_TOOL\\results\\resultsLoni\\resultsLoniBorisFilteredSUBDUEval2_occurrences";
            test2 = new CreateStatisticsFromResultsSUBDUE("LONI Boris Dataset","size", true, true,file, ocFile);
            test2.printStatistics("Stats-LoniBorisEv2");
            
            //ZhiZhong
            file = "SUBDUE_TOOL\\results\\resultsLoni\\resultsLoniZhiFilteredSUBDUEEval1";
            ocFile = "SUBDUE_TOOL\\results\\resultsLoni\\resultsLoniZhiFilteredSUBDUEEval1_occurrences";
            test2 = new CreateStatisticsFromResultsSUBDUE("LONI ZhiZhong Dataset","MDL", true, true,file, ocFile);
            test2.printStatistics("Stats-LoniZhiEv1");
            
            file = "SUBDUE_TOOL\\results\\resultsLoni\\resultsLoniZhiFilteredSUBDUEEval2";
            ocFile = "SUBDUE_TOOL\\results\\resultsLoni\\resultsLoniZhiFilteredSUBDUEEval2_occurrences";
            test2 = new CreateStatisticsFromResultsSUBDUE("LONI Zhizhong Dataset","size", true, true,file, ocFile);
            test2.printStatistics("Stats-LoniZhiEv2");
            
        }catch(Exception e){
            System.err.println("Error while executing the script "+e.getMessage());
        }
    }
}
