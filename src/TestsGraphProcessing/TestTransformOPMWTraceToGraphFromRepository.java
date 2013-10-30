/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
            tests.transformToSubdueGraph("http://www.opmw.org/export/resource/WorkflowExecutionAccount/ACCOUNT1348703551080");
        }catch(Exception e){
            System.out.println("Error executing test. Exception: "+e.getMessage());
        }
    }
    public static void main(String[] args){
        test();
    }
}
