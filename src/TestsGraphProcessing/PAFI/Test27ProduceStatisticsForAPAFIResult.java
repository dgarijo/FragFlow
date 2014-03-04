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
package TestsGraphProcessing.PAFI;

import IO.Exception.FragmentReaderException;
import PostProcessing.Formats.PAFI.CreateStatisticsFromResultsPAFI;

/**
 * Test to check whether the statistics from a PAFI result can be obtained.
 * @author Daniel Garijo
 */
public class Test27ProduceStatisticsForAPAFIResult {
    public static int testNumber = 27;
    public static boolean test(){
         try {
            System.out.println("\n\nExecuting test:"+testNumber+" Producing sample PAFI statistics based on true results."); 
            String fpfile = "PAFI_TOOL\\results\\CollectionInPAFIFormat.fp";
            String pcFile = "PAFI_TOOL\\results\\CollectionInPAFIFormat.pc";
            String tidFile = "PAFI_TOOL\\results\\CollectionInPAFIFormat.tid";
            CreateStatisticsFromResultsPAFI c = new CreateStatisticsFromResultsPAFI("Text analytics", true, false, fpfile, pcFile, tidFile);            
            c.printStatistics("testStatisticsPafi");
            //this has been checked manually. The number of fragments must be 11 in this case.
            if(c.getNumberOfFilteredMultiStepFragments()==11) return true;
            return false;
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
