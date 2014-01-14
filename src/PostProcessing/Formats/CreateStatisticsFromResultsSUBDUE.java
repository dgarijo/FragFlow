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
package PostProcessing.Formats;

import IO.Formats.SubdueFragmentReader;
import PostProcessing.CreateStatisticsFromResults;
import DataStructures.Fragment;
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
        HashMap<String,Fragment> structureResults = new SubdueFragmentReader().processResultsAndOccurrencesFiles(inputFile, occurrencesFile);
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
