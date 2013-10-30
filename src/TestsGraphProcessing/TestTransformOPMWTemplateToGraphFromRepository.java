/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
            test.transformToSubdueGraph("http://www.opmw.org/export/resource/WorkflowTemplate/FEATUREGENERATION");
        }catch(Exception e){
            System.out.println("Error executing test. Exception: "+e.getMessage());
        }
    }
    
    public static void main(String[] args){
        test();
    }
}
