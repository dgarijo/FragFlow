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

import Factory.OPMWTemplate2GraphProcessor;

/**
 *
 * @author Daniel Garijo
 */
public class TestTransformOPMWTemplateToGraphFromRepository {
    
    public static void test(){
        try{
            System.out.println("Testing: transformation of an OPMW template to graph from the Wings repository");
            OPMWTemplate2GraphProcessor test = new OPMWTemplate2GraphProcessor("http://wind.isi.edu:8890/sparql");
            test.transformToGraph("http://www.opmw.org/export/resource/WorkflowTemplate/FEATUREGENERATION");
        }catch(Exception e){
            System.out.println("Error executing test. Exception: "+e.getMessage());
        }
    }
    
    public static void main(String[] args){
        test();
    }
}
