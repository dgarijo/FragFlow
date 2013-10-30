/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
            tp.transformDomainToSubdueGraph("TextAnalytics");
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
