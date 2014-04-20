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

import DataStructures.Fragment;
import IO.Formats.PARSEMIS.Gspan.FragmentCollectionReaderPARSEMISGspan;
import java.util.HashMap;

/**
 * Test to read a Parsemis Result
 * @author Daniel Garijo
 */
public class Test33ReadAParsemisGSpanResult {
    public static int testNumber = 33;//not given a number until we complete the previous ones.
    public static boolean test(){
         try{
            System.out.println("\n\nExecuting test:"+testNumber+" Read Parsemis results");
            String file = "PARSEMIS_TOOL\\results\\run11-03-2014.txt";
            HashMap<String,Fragment> structureResults = new FragmentCollectionReaderPARSEMISGspan(file).getFragmentCatalogAsHashMap();
            //this particular set of results has 41.
            if(structureResults.size()==41){
                return true;
            }
            return false;
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
