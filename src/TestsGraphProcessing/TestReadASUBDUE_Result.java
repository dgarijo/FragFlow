/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TestsGraphProcessing;

import PostProcessing.FinalResult;
import PostProcessing.Formats.SubdueGraphReader;
import java.util.HashMap;

/**
 * Test to check wether we can read a single SUBDUE Result File. The file and the occurrences
 * of the structures are needed.
 * @author Daniel Garijo
 */
public class TestReadASUBDUE_Result {
    public static void test(){
        try{
            String file = "C:\\Users\\Monen\\Dropbox\\MotifFinder\\SUBDUE_TOOL\\results\\Tests\\testResultReduced2";
            String ocFile = "C:\\Users\\Monen\\Dropbox\\MotifFinder\\SUBDUE_TOOL\\results\\Tests\\testResultReduced2_occurrences";
            HashMap<String,FinalResult> structureResults = new SubdueGraphReader().processResultsAndOccurrencesFiles(file, ocFile);
        }catch(Exception e){
            System.out.println("Error executing test. Exception: "+e.getMessage());
        }
    }
    
    public static void main(String[] args){
        test();
    }
     
}
