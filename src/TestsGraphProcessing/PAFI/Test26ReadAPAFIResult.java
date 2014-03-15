/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TestsGraphProcessing.PAFI;

import DataStructures.Fragment;
import IO.Formats.PAFI.FragmentReaderPAFI;
import java.util.HashMap;

/**
 * Test designed to read a result file produced by PAFI.
 * The result consists on 3 files: the catalog, the dependences and the 
 * support (occurrences).
 * @author Dani
 */
public class Test26ReadAPAFIResult {
    public static int testNumber = 26;
    public static boolean test(){
        System.out.println("\n\nExecuting test:"+testNumber+" Checking whether a PAFI result file can be read.");
        try{
            String fpfile = "PAFI_TOOL\\results\\CollectionInPAFIFormat.fp";
            String pcFile = "PAFI_TOOL\\results\\CollectionInPAFIFormat.pc";
            String tidFile = "PAFI_TOOL\\results\\CollectionInPAFIFormat.tid";
            HashMap<String,Fragment> structureResults = new FragmentReaderPAFI(fpfile,pcFile,tidFile).getFragmentCatalogAsHashMap();
            if(structureResults.isEmpty())return false;
            return true;
        }catch(Exception e){
            System.out.println("Error executing test. Exception: "+e.getMessage());
            return false;
        }
    }
    
    public static void main(String[] args){
        if(test())System.out.println("Test "+testNumber+" OK");
        else System.out.println("Test "+testNumber+" FAILED");
    }
}
