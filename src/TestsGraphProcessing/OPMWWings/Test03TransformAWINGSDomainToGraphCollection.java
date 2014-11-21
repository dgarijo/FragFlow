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

import Factory.OPMW.OPMWTemplate2GraphWings;

/**
 * This test downloads a whole domain of Wings (specified by the user) and 
 * transforms it into a graph collection. This test covers the the transformation 
 * of an OPMW template/trace into Graph
 * @author Daniel Garijo
 */
public class Test03TransformAWINGSDomainToGraphCollection {
    public static int testNumber = 3;
    public static boolean test(){
        try{
            System.out.println("\n\nExecuting test:"+testNumber+" Transform a Wings Domain To Graph Collection");
            OPMWTemplate2GraphWings tp = new OPMWTemplate2GraphWings("http://wind.isi.edu:8890/sparql");
            tp.transformDomainToGraph("TextAnalytics");
            tp.getGraphCollection().getGraphs().size();
            //as a fact, we know there are 22 templates in this particular domain.
            if(tp.getGraphCollection().getNumberOfSubGraphs()>1) return true;
            else return false;
        }catch(Exception e){
            System.out.println("Error while executing test: " +e.getMessage());
            return false;
        }
    }
    public static void main(String[] args){
        if(test())System.out.println("Test "+testNumber+" OK");
        else System.out.println("Test "+testNumber+" FAILED");
    }
}
