/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TestsGraphProcessing;

import PostProcessing.Formats.CreateStatisticsFromResultsSUBDUE;

/**
 *
 * @author Daniel Garijo
 */
public class PostProcessing {
    public static void test(){
        try{
            CreateStatisticsFromResultsSUBDUE test = new CreateStatisticsFromResultsSUBDUE("text analytics",
                    "MDL", true, false, true);
            String file = "C:\\Users\\Monen\\Dropbox\\NetBeansProjects\\MotifFinder\\SUBDUE_TOOL\\results\\Tests\\testResultReduced2";
            String ocFile = "C:\\Users\\Monen\\Dropbox\\NetBeansProjects\\MotifFinder\\SUBDUE_TOOL\\results\\Tests\\testResultReduced2_occurrences";
            test.createStatisticsFromFile(file, ocFile);
            test.printStatistics("testStatistics");
        }catch(Exception e){
            System.out.println("Error in test PostProcessing. Exception: "+e.getMessage());
        }
    }
    public static void main(String[] args){
        test();
    }
}
