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

import DataStructures.Fragment;
import IO.Formats.SUBDUE.FragmentReaderSUBDUE;
import java.util.HashMap;

/**
 * Test to check wether we can read a single SUBDUE Result File. The file and the occurrences
 * of the structures are needed.
 * @author Daniel Garijo
 */
public class Test10ReadASUBDUE_Result {
    public static int testNumber = 10;
    public static boolean test(){
        System.out.println("\n\nExecuting test:"+testNumber+" Checking whether a SUBDUE result file can be read.");
        try{
            String file = "SUBDUE_TOOL\\results\\Tests\\testResultReduced2";
            String ocFile = "SUBDUE_TOOL\\results\\Tests\\testResultReduced2_occurrences";
            HashMap<String,Fragment> structureResults = new FragmentReaderSUBDUE(file, ocFile).getFragmentCatalogFromAlgorithmResultFiles();
            if(structureResults.isEmpty())return false;
            return true;
        }catch(Exception e){
            System.out.println("Error executing test. Exception: "+e.getMessage());
            return false;
        }
    }
    
    public static void main(String[] args){
        if(test())System.out.println("Test "+testNumber+" OK");
        else System.out.println("Test "+testNumber+" FAILED");
    }
     
}
