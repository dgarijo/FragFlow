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
 * This test downloads a whole domain of Wings (specified by the user) and 
 * transforms it into a graph collection. This test covers the the transformation 
 * of an OPMW template/trace into Graph
 * @author Daniel Garijo
 */
public class TestTransformAWINGSDomainToGraphCollection {
    public static void test(){
        try{
            System.out.println("Executing test: Transform a Wings Domain To Graph Collection");
            OPMWTemplate2GraphProcessor tp = new OPMWTemplate2GraphProcessor("http://wind.isi.edu:8890/sparql");
            tp.transformDomainToGraph("TextAnalytics");
//            tp.getGraphCollection();
            System.out.println("Test Ok");
        }catch(Exception e){
            System.out.println("Error while executing test: " +e.getMessage());
        }
    }
    public static void main(String[] args){
        test();
    }
}
