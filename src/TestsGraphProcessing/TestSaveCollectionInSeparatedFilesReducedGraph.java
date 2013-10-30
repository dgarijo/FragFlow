/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TestsGraphProcessing;

import Factory.OPMWTemplate2GraphProcessor;
import Persistence.Formats.CollectionWriterSUBDUEFormat;

/**
 *
 * @author Daniel Garijo
 */
public class TestSaveCollectionInSeparatedFilesReducedGraph {
    public static void test(){
        try {
            OPMWTemplate2GraphProcessor tp = new OPMWTemplate2GraphProcessor("http://wind.isi.edu:8890/sparql");
            tp.transformDomainToSubdueGraph("TextAnalytics");        
            CollectionWriterSUBDUEFormat writer = new CollectionWriterSUBDUEFormat();
            writer.writeReducedGraphsToSeparatedFiles(tp.getGraphCollection(), "TestSaveCollectionInSeparatedFilesReducedGraph");
        }catch(Exception e){
            System.out.println("Test TestSaveCollectionInSeparatedFilesReducedGraph case failed: "+ e.getMessage());
        }
    }
    
    public static void main(String[] args){
        test();
    }
}
