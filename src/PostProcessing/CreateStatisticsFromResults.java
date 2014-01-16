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
package PostProcessing;

import DataStructures.Fragment;
import IO.Exception.FragmentReaderException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class is for creating statistics from the structures:
 * Structures found,
 * Independent structures,
 * Meaningful structures,
 * Occurrences of detcted structures,
 * Occurrences of meaningful structures.
 * 
 * Additional: how many of these structures correspond exactly to templates / traces?
 * Which templates or traces do these structures correspond to?
 * How do substructures continue each other (if one includes the other).
 * 
 * This class also relates the structures to the templates and traces, and
 * shows the specific relation between the structures.
 * 
 * @author Daniel Garijo
 */
public abstract class CreateStatisticsFromResults {
    protected int structuresFound, multiStepFragment, filteredMultiStepFragment,
            numberOfIrreducibleStructures, numberOfMultiStepIrreducibleStructures,
            occurrencesOfDetectedStructures, occurrencesOfMultiStepStructures, occurrencesOfFilteredMultiStepStructures;
    //multi step: those with 2 or more steps.
    //filtered multi step: those structures that meaningful and that
    //if they include another structure, then it has to occur at a different frequency than
    //the actual structure. This is done to avoid slicing structures in pieces when those 
    //pieces occur the same number of times as the bigger piece
    protected String domain, evaluationType, inputFileName;//Example: text analystics with MDL evaluation.
    protected boolean isTemplate, hasInference, isReducedGraph; //Example: template without inference.
    
    
    //A file may contain more than one substructure. For each we use the graph reader.
    public void createStatisticsFromFile(String inputFile, String occurrencesFile)throws FragmentReaderException{
        //to be overriden by the different file formats.
    }    
    
    
    /**
     * Function to count the number of detected substructures.
     * Standard for all the types of graphs
     * @param results results obtained when parsing the detected fragments.
     */    
    protected void countDetectedAndMultiStepStructures(HashMap<String,Fragment> results){
        if(results==null)return;
        Iterator<String> it = results.keySet().iterator();
        this.occurrencesOfDetectedStructures = 0;
        this.multiStepFragment = 0;
        this.occurrencesOfMultiStepStructures = 0;
        this.numberOfIrreducibleStructures = 0;
        this.numberOfMultiStepIrreducibleStructures = 0;
        this.filteredMultiStepFragment = 0;
        this.occurrencesOfFilteredMultiStepStructures = 0;
        //we assume that the number of occurrences is initialzied
        while (it.hasNext()){
            String currentString = it.next();
            Fragment currentResult = results.get(currentString);
            this.occurrencesOfDetectedStructures+= currentResult.getNumberOfOccurrences();
            if(currentResult.isMultiStepStructure()){
                this.multiStepFragment++;
                this.occurrencesOfMultiStepStructures+=currentResult.getNumberOfOccurrences();
                ArrayList<Fragment> includedIds = currentResult.getListOfIncludedIDs();
                if(includedIds.isEmpty()){
                    this.numberOfMultiStepIrreducibleStructures++;
                    this.filteredMultiStepFragment++;
                    this.occurrencesOfFilteredMultiStepStructures+=currentResult.getNumberOfOccurrences();
                }
                else{
                //if any of the included structures has more or less occurrences than the actual one,
                //we count the actual as a new structure.
                    Iterator<Fragment> idsIterator = includedIds.iterator();
                    boolean includeStructure = false;
                    while (idsIterator.hasNext()&&!includeStructure){
//                        String currentIncludedId = idsIterator.next();
                        Fragment includedStructure = idsIterator.next();
                        if(includedStructure.getNumberOfOccurrences()!= currentResult.getNumberOfOccurrences()){
                            includeStructure=true;//we have found that it should be included. 
                            this.filteredMultiStepFragment++;
                            this.occurrencesOfFilteredMultiStepStructures+=currentResult.getNumberOfOccurrences();
                        }
                    }
                }             
            }
            if(currentResult.getListOfIncludedIDs().isEmpty()){
                numberOfIrreducibleStructures++;
            }
        }
//        must add a new statistic to summarize the numbers in brackets.
//                esto se hace viendo si las estructuras de las que depende una 
//                estructura ocurren un numero de veces distinto al de la propia.
//                Si uno sí, entonces es una nueva estructura :)
    }
    
    
    
    /**
     * Method that will print the statistics on the console plus to the 
     * specified file
     * @param outFilePath save path file
     */
    public void printStatistics(String outFilePath){
        try{
            FileWriter fstream = new FileWriter(outFilePath);
            BufferedWriter out = new BufferedWriter(fstream);
            String output = "Creating statistics for: "+inputFileName+"\n";
            output+="- Domain: "+domain+", evaluation type: "+evaluationType+"\n";
            if(isTemplate)output+="- Executed on the templates\n";
            else output+="- Executed on the traces\n";
            if(hasInference)output +="- Inference applied\n";
            else output +="- No inference applied\n";
            if(isReducedGraph)output +="- Graph on reduced format (informBy edges instead of used + wasGenBy)\n";
            else output +="- Non reduced graph\n\n";
            output+="Number of structures found: "+structuresFound+"\n";
            output+="Number of irreducible fragments found (i.e., those structures which do not include parts of others): "+numberOfIrreducibleStructures+"\n";
            output+="Number of multi step fragments structures found (i.e., those with at least two process steps): "+multiStepFragment+"("+filteredMultiStepFragment+")\n";
            output+="Number of multi step irreducible fragments found: "+numberOfMultiStepIrreducibleStructures+"\n";
            output+="Frequency of detected structures: "+occurrencesOfDetectedStructures+"\n";
            output+="Frequency of multi step fragments: "+occurrencesOfMultiStepStructures+"("+occurrencesOfFilteredMultiStepStructures+")\n";
            System.out.println(output);
            out.write(output);
            out.close();
        }catch (Exception e){
            System.err.println("Error "+e.getMessage());
        }
    }
    
    
}
