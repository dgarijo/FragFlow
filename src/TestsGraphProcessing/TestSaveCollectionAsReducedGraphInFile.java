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
public class TestSaveCollectionAsReducedGraphInFile {
    public static void test(){
        try{
            OPMWTemplate2GraphProcessor tp = new OPMWTemplate2GraphProcessor("http://wind.isi.edu:8890/sparql");
            tp.transformDomainToSubdueGraph("TextAnalytics");        
            CollectionWriterSUBDUEFormat writer = new CollectionWriterSUBDUEFormat();
            writer.writeReducedGraphsToFile(tp.getGraphCollection(), "TestSaveCollectionAsReducedlGraphInFile");
        }catch(Exception e){
            System.out.println("Test TestSaveCollectionAsReducedlGraphInFile case failed: "+ e.getMessage());
        }
    }
    
    public static void main(String[] args){
        test();
    }
}
