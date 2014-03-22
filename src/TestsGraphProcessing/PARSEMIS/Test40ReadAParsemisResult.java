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
import IO.Formats.PARSEMIS.Gspan.FragmentReaderPARSEMISGspan;
import java.util.HashMap;

/**
 *
 * @author Daniel Garijo
 */
public class Test40ReadAParsemisResult {
    public static int testNumber = 40;
    public static boolean test(){
        System.out.println("\n\nExecuting test:"+testNumber+" Reading a PARSEMIS result.");
        try{
            String fpfile = "PARSEMIS_TOOL\\results\\resultsCollectionInParsemisFormat.lg";
            HashMap<String,Fragment> structureResults = new FragmentReaderPARSEMISGspan(fpfile).getFragmentCatalogAsHashMap();
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
