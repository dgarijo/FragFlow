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
package TestsGraphProcessing.PARSEMIS;

import IO.Exception.FragmentReaderException;
import PostProcessing.Formats.PARSEMIS.CreateStatisticsFromResultsPARSEMIS;

/**
 * Creation and validation of Statistics for parsemis
 * @author Daniel Garijo
 */
public class Test34CreateAndValidateStatisticsForAPARSEMISResult {
    public static int testNumber = 34;
    public static boolean test(){
         try {
            System.out.println("\n\nExecuting test:"+testNumber+" Producing sample Parsemis statistics based on true results."); 
            CreateStatisticsFromResultsPARSEMIS c = new CreateStatisticsFromResultsPARSEMIS("TextAnalytics", true, false, "PARSEMIS_TOOL\\results\\run11-03-2014.txt");
            c.printStatistics("testStatisticsParsemis");
            //this has been checked manually. The number of filtered multistep fragments must be 11 in this case.
            //curiously enough, the number of found fragments is the same as PAFI
            if(c.getNumberOfFilteredMultiStepFragments()!=11) return false;
            if(c.getOccurrencesOfFilteredMultiStepStructures()!=33) return false;
            return true;
        } catch (FragmentReaderException ex) {
            System.out.println("Error while executing test "+ex.getMessage());
            return false;
        }
    }
    
    public static void main(String[] args){
        if(test())System.out.println("Test "+testNumber+" OK");
        else System.out.println("Test "+testNumber+" FAILED");
    }
}
