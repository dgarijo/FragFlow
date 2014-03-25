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

/**
 * Test to check wether we can transform a Loni Template to the graph notation
 * @author Daniel Garijo
 */
public class Test47TransformLONITemplateToGraph {
    public static int testNumber = 47;
    public static boolean test(){
        try{
            System.out.println("\n\nExecuting test:"+testNumber+" Transofrmation of a LONI template");
            LoniTemplate2Graph test = new LoniTemplate2Graph("LONI_dataset\\");
            test.transformToGraph("DTI_workflow.pipe.xml");
            Graph trace = test.getGraphCollection().getGraphs().get(0);
            //this template has 3 Modules. We don't care about them. If they have been read properly, the test is ok.
            if(trace.getURIs().size()== 3)
                return true;
            else return false;
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
