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
package PostProcessing.Formats.SUBDUE;

import IO.Formats.SUBDUE.FragmentReaderSUBDUE;
import PostProcessing.CreateStatisticsFromResults;
import DataStructures.Fragment;
import IO.Exception.FragmentReaderException;
import java.util.HashMap;

/**
 * Class that extends CreateStatisticsFromResults to obtain those obtained from 
 * the SUBDUE results.
 * @author Daniel Garijo
 */
public class CreateStatisticsFromResultsSUBDUE extends CreateStatisticsFromResults{
    
    /**
     * Constructos
     * @param domain domain of the templates or traces
     * @param evalType type of evaluation performed
     * @param isTemplate states whether the evaluation was performed on templates
     * or not
     * @param hasInference states whether the evaluation was performed by applying
     * inference or not
     * @param isReducedGraph states if the evaluation has been performed on a 
     * reduced graph.
     */
    public CreateStatisticsFromResultsSUBDUE(String domain, String evalType, boolean isTemplate, boolean hasInference, boolean isReducedGraph){
        this.domain = domain;
        this.evaluationType = evalType;
        this.isTemplate = isTemplate;
        this.hasInference = hasInference;
        this.isReducedGraph = isReducedGraph;
    }
    
    @Override
    public void createStatisticsFromFile(String inputFile, String occurrencesFile) throws FragmentReaderException{
        this.inputFileName = inputFile;
        HashMap<String,Fragment> structureResults = new FragmentReaderSUBDUE().processResultsAndOccurrencesFiles(inputFile, occurrencesFile);
        //Structures found
        structuresFound = structureResults.size();
        countDetectedAndMultiStepStructures(structureResults);        
        //We coulod add the structures as templates/traces found as well.
    }
    
}
