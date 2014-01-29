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
package TestsGraphProcessing.OPMWWings;

import DataStructures.Graph;
import Factory.OPMW.OPMWTemplate2Graph;

/**
 * Test that given an OPMW template URI, retrieves it and transforms it into 
 * graph mode.
 * @author Daniel Garijo
 */
public class Test02TransformOPMWTemplateToGraphFromRepository {
    public static int testNumber = 2;
    
    public static boolean test(){
        try{
            System.out.println("\n\nExecuting test:"+testNumber+" Transformation of an OPMW template to graph from the Wings repository");
            OPMWTemplate2Graph test = new OPMWTemplate2Graph("http://wind.isi.edu:8890/sparql");
            test.transformToGraph("http://www.opmw.org/export/resource/WorkflowTemplate/FEATUREGENERATION");
            Graph template = test.getGraphCollection().getGraphs().get(0);
            if(template.getName().equals("http://www.opmw.org/export/resource/WorkflowTemplate/FEATUREGENERATION")) return true;
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
