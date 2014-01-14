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

import DataStructures.Fragment;
import IO.Formats.SubdueFragmentReader;
import java.util.HashMap;

/**
 * Test to check wether we can read a single SUBDUE Result File. The file and the occurrences
 * of the structures are needed.
 * @author Daniel Garijo
 */
public class TestReadASUBDUE_Result {
    public static void test(){
        try{
            String file = "C:\\Users\\Monen\\Dropbox\\MotifFinder\\SUBDUE_TOOL\\results\\Tests\\testResultReduced2";
            String ocFile = "C:\\Users\\Monen\\Dropbox\\MotifFinder\\SUBDUE_TOOL\\results\\Tests\\testResultReduced2_occurrences";
            HashMap<String,Fragment> structureResults = new SubdueFragmentReader().processResultsAndOccurrencesFiles(file, ocFile);
        }catch(Exception e){
            System.out.println("Error executing test. Exception: "+e.getMessage());
        }
    }
    
    public static void main(String[] args){
        test();
    }
     
}
