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
import Factory.OPMW.OPMWTrace2Graph;

/**
 * Test that given an OPMW trace URI, transforms it to a Graph.
 * @author Daniel Garijo
 */
public class Test01TransformOPMWTraceToGraphFromRepository {
    public static int testNumber = 1;
    public static boolean test(){
        try{
            System.out.println("\n\nExecuting test:"+testNumber+" Transformation of an OPMW trace to graph from the Wings repository");
            OPMWTrace2Graph tests = new OPMWTrace2Graph("http://wind.isi.edu:8890/sparql");
            tests.transformToGraph("http://www.opmw.org/export/resource/WorkflowExecutionAccount/ACCOUNT1348703551080");
            Graph trace = tests.getGraphCollection().getGraphs().get(0);
            if(trace.getName().equals("http://www.opmw.org/export/resource/WorkflowExecutionAccount/ACCOUNT1348703551080"))
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
