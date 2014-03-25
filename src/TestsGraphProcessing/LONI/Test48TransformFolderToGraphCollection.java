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
package TestsGraphProcessing.LONI;

import DataStructures.Graph;
import Factory.Loni.LoniTemplate2Graph;
import java.io.File;

/**
 * Test to parse a folder of loni files.
 * @author Daniel Garijo
 */
public class Test48TransformFolderToGraphCollection {
    public static int testNumber = 48;
    public static boolean test(){
        try{
            System.out.println("\n\nExecuting test:"+testNumber+" Transofrmation of a LONI template");
            String loniDatasetFolder = "LONI_dataset\\";
            File f = new File(loniDatasetFolder);
            if(f.isDirectory()){
                File[] files = f.listFiles();
                LoniTemplate2Graph test = new LoniTemplate2Graph(loniDatasetFolder);
                for(int i=0;i<files.length;i++){
                    test.transformToGraph(files[i].getName());
//                    System.out.println(files[i].getName());
                }
                if (test.getGraphCollection().getNumberOfSubGraphs()>0)
                    return true;
                return false;
            }
            else {
                System.out.println("The directory is not valid");
                return false;
            }
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
