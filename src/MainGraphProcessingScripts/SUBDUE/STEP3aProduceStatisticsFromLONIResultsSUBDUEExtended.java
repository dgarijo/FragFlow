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
public class STEP3aProduceStatisticsFromLONIResultsSUBDUEExtended {
    
    
    
    public static void main(String[] args){
        try{
            
            //WC1 (Boris)
            //eval 1
            String file = "C:\\Users\\dgarijo\\Dropbox\\Tesis\\Graph Matching\\Evaluation-graph matching\\LONI\\results\\fragments-subdue\\eScienceRevision\\WC1-NewMDL";
            String ocFile = "C:\\Users\\dgarijo\\Dropbox\\Tesis\\Graph Matching\\Evaluation-graph matching\\LONI\\results\\fragments-subdue\\eScienceRevision\\WC1-NewMDL_ocurrences";
//            String file = "I:\\Dropbox\\Tesis\\Graph Matching\\Evaluation-graph matching\\LONI\\results\\fragments-subdue\\eScienceRevision\\WC1-NewMDL";
//            String ocFile = "I:\\Dropbox\\Tesis\\Graph Matching\\Evaluation-graph matching\\LONI\\results\\fragments-subdue\\eScienceRevision\\WC1-NewMDL_ocurrences";
            CreateStatisticsFromResultsSUBDUE test1 = new CreateStatisticsFromResultsSUBDUE("WC1","MDL", true, false, file, ocFile,441);
//            test1.printStatistics("Stats-WC1Ev1");
            //eval 2
            file = "C:\\Users\\dgarijo\\Dropbox\\Tesis\\Graph Matching\\Evaluation-graph matching\\LONI\\results\\fragments-subdue\\eScienceRevision\\WC1-NewSize";
            ocFile = "C:\\Users\\dgarijo\\Dropbox\\Tesis\\Graph Matching\\Evaluation-graph matching\\LONI\\results\\fragments-subdue\\eScienceRevision\\WC1-NewSize_ocurrences";
            CreateStatisticsFromResultsSUBDUE test2 = new CreateStatisticsFromResultsSUBDUE("WC1","size", true, false,file, ocFile,441);
//            test2.printStatistics("Stats-WC1Ev2");
            
            //WC2 (public dataset)
            //eval1
            file = "C:\\Users\\dgarijo\\Dropbox\\Tesis\\Graph Matching\\Evaluation-graph matching\\LONI\\results\\fragments-subdue\\eScienceRevision\\WC2-NewMDL";
            ocFile = "C:\\Users\\dgarijo\\Dropbox\\Tesis\\Graph Matching\\Evaluation-graph matching\\LONI\\results\\fragments-subdue\\eScienceRevision\\WC2-NewMDL_ocurrences";
            CreateStatisticsFromResultsSUBDUE test3 = new CreateStatisticsFromResultsSUBDUE("WC2","MDL", true, false, file, ocFile,94);
//            test3.printStatistics("Stats-WC2Ev1");

            //eval2
            file = "C:\\Users\\dgarijo\\Dropbox\\Tesis\\Graph Matching\\Evaluation-graph matching\\LONI\\results\\fragments-subdue\\eScienceRevision\\WC2-NewSize";
            ocFile = "C:\\Users\\dgarijo\\Dropbox\\Tesis\\Graph Matching\\Evaluation-graph matching\\LONI\\results\\fragments-subdue\\eScienceRevision\\WC2-NewSize_ocurrences";
            CreateStatisticsFromResultsSUBDUE test4 = new CreateStatisticsFromResultsSUBDUE("WC2","size", true, false, file, ocFile,94);
//            test4.printStatistics("Stats-WC2Ev2");
            
            //WC3 (monthly dataset)
            //eval1
            file = "C:\\Users\\dgarijo\\Dropbox\\Tesis\\Graph Matching\\Evaluation-graph matching\\LONI\\results\\fragments-subdue\\eScienceRevision\\WC3New-MDL";
            ocFile = "C:\\Users\\dgarijo\\Dropbox\\Tesis\\Graph Matching\\Evaluation-graph matching\\LONI\\results\\fragments-subdue\\eScienceRevision\\WC3New-MDL_ocurrences";
            CreateStatisticsFromResultsSUBDUE test5 = new CreateStatisticsFromResultsSUBDUE("WC3","MDL", true, false, file, ocFile,269);
//            test5.printStatistics("Stats-WC3Ev1");
            
            //eval2
            file = "C:\\Users\\dgarijo\\Dropbox\\Tesis\\Graph Matching\\Evaluation-graph matching\\LONI\\results\\fragments-subdue\\eScienceRevision\\WC3-NewSize";
            ocFile = "C:\\Users\\dgarijo\\Dropbox\\Tesis\\Graph Matching\\Evaluation-graph matching\\LONI\\results\\fragments-subdue\\eScienceRevision\\WC3-NewSize_ocurrences";
            CreateStatisticsFromResultsSUBDUE test6 = new CreateStatisticsFromResultsSUBDUE("WC3","size", true, false, file, ocFile,269);
//            test6.printStatistics("Stats-WC3Ev2");
            
            //WC4 (samuel)
            //eval1
            file = "C:\\Users\\dgarijo\\Dropbox\\Tesis\\Graph Matching\\Evaluation-graph matching\\LONI\\results\\fragments-subdue\\eScienceRevision\\WC4New-MDL";
            ocFile = "C:\\Users\\dgarijo\\Dropbox\\Tesis\\Graph Matching\\Evaluation-graph matching\\LONI\\results\\fragments-subdue\\eScienceRevision\\WC4-NewMDL_ocurrences";
            CreateStatisticsFromResultsSUBDUE test7 = new CreateStatisticsFromResultsSUBDUE("WC4","MDL", true, false, file, ocFile,50);
//            test7.printStatistics("Stats-WC4Ev1");
            
            //eval2
            file = "C:\\Users\\dgarijo\\Dropbox\\Tesis\\Graph Matching\\Evaluation-graph matching\\LONI\\results\\fragments-subdue\\eScienceRevision\\WC4-NewSize";
            ocFile = "C:\\Users\\dgarijo\\Dropbox\\Tesis\\Graph Matching\\Evaluation-graph matching\\LONI\\results\\fragments-subdue\\eScienceRevision\\WC4-NewSize_ocurrences";
            CreateStatisticsFromResultsSUBDUE test8 = new CreateStatisticsFromResultsSUBDUE("WC4","size", true, false, file, ocFile,50);
            test8.printStatistics("Stats-WC4Ev2");
        }catch(Exception e){
            System.err.println("Error while executing the script "+e.getMessage());
        }
    }
}
