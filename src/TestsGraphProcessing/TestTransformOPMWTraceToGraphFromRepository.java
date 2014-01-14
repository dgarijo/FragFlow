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

import Factory.OPMWTrace2GraphProcessor;

/**
 *
 * @author Daniel Garijo
 */
public class TestTransformOPMWTraceToGraphFromRepository {
    public static void test(){
        try{
            System.out.println("Testing: transformation of an OPMW trace to graph from the Wings repository");
            OPMWTrace2GraphProcessor tests = new OPMWTrace2GraphProcessor("http://wind.isi.edu:8890/sparql");
            tests.transformToGraph("http://www.opmw.org/export/resource/WorkflowExecutionAccount/ACCOUNT1348703551080");
        }catch(Exception e){
            System.out.println("Error executing test. Exception: "+e.getMessage());
        }
    }
    public static void main(String[] args){
        test();
    }
}
