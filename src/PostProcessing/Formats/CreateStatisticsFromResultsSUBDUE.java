/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PostProcessing.Formats;

import PostProcessing.CreateStatisticsFromResults;
import PostProcessing.FinalResult;
import java.util.HashMap;

/**
 *
 * @author Daniel Garijo
 */
public class CreateStatisticsFromResultsSUBDUE extends CreateStatisticsFromResults{
    
    public CreateStatisticsFromResultsSUBDUE(String domain, String evalType, boolean isTemplate, boolean hasInference, boolean isReducedGraph){
        this.domain = domain;
        this.evaluationType = evalType;
        this.isTemplate = isTemplate;
        this.hasInference = hasInference;
        this.isReducedGraph = isReducedGraph;
    }
    
    @Override
    public void createStatisticsFromFile(String inputFile, String occurrencesFile){
        this.inputFileName = inputFile;
        HashMap<String,FinalResult> structureResults = new SubdueGraphReader().processResultsAndOccurrencesFiles(inputFile, occurrencesFile);
        //Structures found
        structuresFound = structureResults.size();
        countDetectedAndMultiStepStructures(structureResults);        
        //structures as templates found. Note that this is difficult and may be only possible as templates
        //TO DO (Since this is more complex, it will be left out for the moment)
    }
    
//    public static void main (String[] args){
//        CreateStatisticsFromResultsSUBDUE test = new CreateStatisticsFromResultsSUBDUE("text analytics",
//                "MDL", true, false, true);
//        String file = "C:\\Users\\Monen\\Dropbox\\MotifFinder\\SUBDUE_TOOL\\results\\Tests\\testResultReduced2";
//        String ocFile = "C:\\Users\\Monen\\Dropbox\\MotifFinder\\SUBDUE_TOOL\\results\\Tests\\testResultReduced2_occurrences";
//        test.createStatisticsFromFile(file, ocFile);
//        test.printStatistics("testStatistics");
//    }
}
