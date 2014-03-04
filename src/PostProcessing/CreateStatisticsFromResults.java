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
import IO.Formats.PAFI.FragmentReaderPAFI;
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
    //protected int structuresFound, multiStepFragment, filteredMultiStepFragment,
    //        numberOfIrreducibleStructures, numberOfMultiStepIrreducibleStructures,
    //        occurrencesOfDetectedStructures, occurrencesOfMultiStepStructures, occurrencesOfFilteredMultiStepStructures;
    protected HashMap<String,Fragment> originalFragmentCatalog;
    protected ArrayList<Fragment>  multiStepFragments, filteredMultiStepFragments,
            irreducibleFragments, multiStepIrreducibleFragments;//relevant fragments are the filteredMultiStep ones
    protected int occurrencesOfDetectedStructures, occurrencesOfMultiStepStructures, occurrencesOfFilteredMultiStepStructures;
    //multi step: those with 2 or more steps.
    //filtered multi step: those structures that meaningful and that
    //if they include another structure, then it has to occur at a different frequency than
    //the actual structure. This is done to avoid slicing structures in pieces when those 
    //pieces occur the same number of times as the bigger piece
    protected String domain, evaluationType, evaluationAlgorithm;//Example: text analystics with MDL evaluation.
    protected boolean isTemplate, hasInference; //Example: template without inference.   
    

    /**
     * Default constructor. IsreducedGraph has been ommited because it will be deprecreated
     * @param domain domain of the templates (e.g., text analytics)
     * @param evaluationTypetype of evaluation of the algorithm (if any)
     * @param evaluationAlgorithm Name of the evaluation algorithm (SUBDUE, PAFI, etc.)
     * @param isTemplate is the resource a template?
     * @param hasInference has the resource been generalized?
     * @param inputFile  input file path
     */
    public CreateStatisticsFromResults(String domain, String evaluationType, String evaluationAlgorithm, 
            boolean isTemplate, boolean hasInference, HashMap<String,Fragment> originalFragmentCatalog) {
        this.domain = domain;
        this.evaluationType = evaluationType;        
        this.evaluationAlgorithm = evaluationAlgorithm;
        this.isTemplate = isTemplate;
        this.hasInference = hasInference;
        this.originalFragmentCatalog = originalFragmentCatalog;
        //this.isReducedGraph = isReducedGraph;
        //here each method will invoke the fragment reader, etc. to initialize 
        //the fragment catalogue        
    }

    /**
     * Default getter to retrieve the filtered multi step fragments
     * @return 
     */
    public ArrayList<Fragment> getFilteredMultiStepFragments() {
        return filteredMultiStepFragments;
    }

    /**
     * Default getter to retrieve the  irreducible fragments
     * @return 
     */
    public ArrayList<Fragment> getIrreducibleFragments() {
        return irreducibleFragments;
    }

    /**
     * Default getter to retrieve the multi step fragments
     * @return 
     */
    public ArrayList<Fragment> getMultiStepFragments() {
        return multiStepFragments;
    }

    /**
     * Default getter to retrieve the multi step irreducible fragments
     * @return 
     */
    public ArrayList<Fragment> getMultiStepIrreducibleFragments() {
        return multiStepIrreducibleFragments;
    }
    
    
    /**
     * Returns the number of original found fragments
     * @return 
     */
    public int getNumberOfOriginalFoundFragments(){
        return this.originalFragmentCatalog.size();
    }
    
    /**
     * Returns the number of the multi step fragments
     * @return 
     */
    public int getNumberOfMultiStepFragments(){
        return this.multiStepFragments.size();
    }
    
    /**
     * Returns the number of the multi step filtered fragments
     * @return 
     */
    public int getNumberOfFilteredMultiStepFragments(){
        return this.filteredMultiStepFragments.size();
    }
    
    /**
     * Returns the number of irreducible fragments
     * @return 
     */
    public int getNumberOfIrreducibleFragments(){
        return this.irreducibleFragments.size();
    }
    
    /**
     * Returns the number of filtered irreducible fragments
     * @return 
     */
//    public int getNumberOfFilteredIrreducibleFragments(){
//        return this.filter.size();
//    }
    
    /**
     * Returns the number of multi step irreducible fragments
     * @return 
     */
    public int getNumberOfMultiStepIrreducibleFragments(){
        return this.multiStepIrreducibleFragments.size();
    }
    
    /**
     * Method that returns all the dependencies of a fragment. That is, not only
     * those dependencies included directly, but also those dependencies found
     * recursively.
     * @param f the fragment from which we want to find the dependencies.
     * @return All the fragments that depend directly or indirectly on f
     */
    private ArrayList<Fragment> getFullDependenciesOfFragment(Fragment f){
        ArrayList<Fragment> result = new ArrayList<Fragment>();
        addDependencies(result, f);
        return result;
    }
    /**
     * Method to add the fragment dependencies recursively to a given array.
     * @param results the full dependencies
     * @param f fragment to be explored.
     */
    private void addDependencies(ArrayList<Fragment> results, Fragment f){
        ArrayList<Fragment> aux = f.getListOfIncludedIDs();
        if(aux == null || aux.isEmpty())return;
        Iterator<Fragment> it = aux.iterator();
        while(it.hasNext()){
            Fragment currentDependency = it.next();
            results.add(currentDependency);
            addDependencies(results, currentDependency);
        }
    }
    
    /**
     * Generic constructor to be implemented by each of the statistic creators
     * It depends on the algorithm and the output it provides.
     */
    protected void initializeStatistics(){
        if(originalFragmentCatalog==null||originalFragmentCatalog.isEmpty())return;
        multiStepFragments = new ArrayList<Fragment>();
        filteredMultiStepFragments = new ArrayList<Fragment>();
        irreducibleFragments = new ArrayList<Fragment>(); 
        multiStepIrreducibleFragments = new ArrayList<Fragment>();
        Iterator<String> it = originalFragmentCatalog.keySet().iterator();
        while (it.hasNext()){
            String currentString = it.next();
            Fragment currentResult = originalFragmentCatalog.get(currentString);
            this.occurrencesOfDetectedStructures+= currentResult.getNumberOfOccurrences();
            if(currentResult.isMultiStepStructure()){
                multiStepFragments.add(currentResult);
                //for the moment we add all of them. We will filter them later.
                //we will also remove the duplicated ones
                filteredMultiStepFragments.add(currentResult);                    
                this.occurrencesOfMultiStepStructures+=currentResult.getNumberOfOccurrences();
                ArrayList<Fragment> includedIds = currentResult.getListOfIncludedIDs();
                if(includedIds==null || includedIds.isEmpty()){                    
                    multiStepIrreducibleFragments.add(currentResult);                    
//                    this.occurrencesOfFilteredMultiStepStructures+=currentResult.getNumberOfOccurrences();
                }                              
            }            
            if(currentResult.getListOfIncludedIDs()==null || currentResult.getListOfIncludedIDs().isEmpty()){
                irreducibleFragments.add(currentResult);
            }
        }
        this.filterMultiStepFragments();
    }
    
    /**
     * Method that takes the class attribute filteredMultiStepFragments, (initialized
     * with all the multistep fragments) and filters those fragments that are not 
     * relevant. The rationale followed here is that if a fragment includes other 
     * fragments that occur the same number of times, those included fragments 
     * ought to be removed.
     * For example: Fragment 1 occurs 3 times, Fragment 2 occurs 4 times and 
     * Fragment 4 occurs 3 times). Fragment 2 and 1 are included in Fragment 4.
     * Since Fragment 1 occurs the same number of times as Fragment 4, we don't
     * consider significant (4 is the same but bigger). However Fragment 2 is not
     * removed, as has occurred more times (it means that it has been found outside 
     * fragment 4).
     */
    private void filterMultiStepFragments(){
        Iterator<Fragment> it = filteredMultiStepFragments.iterator();
        ArrayList<Fragment> elementsToRemove = new ArrayList<Fragment>();
        while(it.hasNext()){
            Fragment currentFragment = it.next();
            ArrayList<Fragment> includedFragments = this.getFullDependenciesOfFragment(currentFragment);
            //if any of the included structures has the same occurrences as the actual one,
            //we count the actual as the relevant structure and remove the others from the filtered 
            //multi step fragments
            Iterator<Fragment>includedFragmentsIt = includedFragments.iterator();
            while(includedFragmentsIt.hasNext()){
                Fragment currentIncludedFragment = includedFragmentsIt.next();                
                if(filteredMultiStepFragments.contains(currentIncludedFragment) && currentIncludedFragment.getNumberOfOccurrences() == currentFragment.getNumberOfOccurrences()){
                    if(!elementsToRemove.contains(currentIncludedFragment))
                        elementsToRemove.add(currentIncludedFragment);
                }
            }
        }
        //now that we know the elements to remove, we remove them
        Iterator<Fragment> itRemove = elementsToRemove.iterator();
        occurrencesOfFilteredMultiStepStructures = occurrencesOfMultiStepStructures;
        while(itRemove.hasNext()){
            Fragment fragmentToRemove = itRemove.next();
            //while we remove, calculate the occurrences of filteredmultistep fragments here.
            occurrencesOfFilteredMultiStepStructures-=fragmentToRemove.getNumberOfOccurrences();
            filteredMultiStepFragments.remove(fragmentToRemove);            
        }
        
    }
    
    /**
     * Method that given a fragment, it answers whether it is included in the 
     * filteredMultiStep Fragments list or not. Necessary to filter the list
     * @param f fragment to be found
     * @return 
     */
//    private boolean isIncludedInFilteredMultiStepFragmentList(Fragment f){
//        Iterator<Fragment> it = filteredMultiStepFragments.iterator();
//        while(it.hasNext()){
//            if(f.getStructureID().equals(it.next().getStructureID()))
//                return true;
//        }
//        return false;
//    }
    
    /**
     * Method to remove a fragment for the list. This is for filtering purposes.
     * @param f  fragment to be removed
     */
//    private void removeFragmentFromMultiStepFragmentList (Fragment f){
//        Iterator<Fragment> it = filteredMultiStepFragments.iterator();
//        while(it.hasNext()){
//            Fragment currFragment = it.next();
//            if(f.getStructureID().equals(currFragment.getStructureID())){
//                filteredMultiStepFragments.re
//            }
//                
//        }
//    }
    
    //protected abstract void createStatistics()throws FragmentReaderException;
    /**
     * Function to count the number of detected substructures.
     * Standard for all the types of graphs
     * @param results results obtained when parsing the detected fragments.
     */    
//    protected void countDetectedAndMultiStepStructures(HashMap<String,Fragment> results){
//        if(results==null)return;
//        Iterator<String> it = results.keySet().iterator();
//        this.occurrencesOfDetectedStructures = 0;
//        this.multiStepFragment = 0;
//        this.occurrencesOfMultiStepStructures = 0;
//        this.numberOfIrreducibleStructures = 0;
//        this.numberOfMultiStepIrreducibleStructures = 0;
//        this.filteredMultiStepFragment = 0;
//        this.occurrencesOfFilteredMultiStepStructures = 0;
//        //we assume that the number of occurrences is initialzied
//        while (it.hasNext()){
//            String currentString = it.next();
//            Fragment currentResult = results.get(currentString);
//            this.occurrencesOfDetectedStructures+= currentResult.getNumberOfOccurrences();
//            if(currentResult.isMultiStepStructure()){
//                this.multiStepFragment++;
//                this.occurrencesOfMultiStepStructures+=currentResult.getNumberOfOccurrences();
//                ArrayList<Fragment> includedIds = currentResult.getListOfIncludedIDs();
//                if(includedIds==null || includedIds.isEmpty()){
//                    this.numberOfMultiStepIrreducibleStructures++;
//                    this.filteredMultiStepFragment++;
//                    this.occurrencesOfFilteredMultiStepStructures+=currentResult.getNumberOfOccurrences();
//                }
//                else{
//                //if any of the included structures has more or less occurrences than the actual one,
//                //we count the actual as a new structure.
//                    no se esta teniendo en cuenta el que la estructura de mas afuera
//                            (la que incluye), sea la que se anade... Es decir, que se calcula bien pero no me dice cual es.
//                    if(includedIds!=null){
//                        Iterator<Fragment> idsIterator = includedIds.iterator();
//                        boolean includeStructure = false;
//                        while (idsIterator.hasNext()&&!includeStructure){
//    //                        String currentIncludedId = idsIterator.next();
//                            Fragment includedStructure = idsIterator.next();
//                            if(includedStructure.getNumberOfOccurrences()!= currentResult.getNumberOfOccurrences()){
//                                includeStructure=true;//we have found that it should be included. 
//                                this.filteredMultiStepFragment++;
//                                System.out.println(currentResult.getStructureID());
//                                this.occurrencesOfFilteredMultiStepStructures+=currentResult.getNumberOfOccurrences();
//                            }
//                        }
//                    }
//                }             
//            }
//            if(currentResult.getListOfIncludedIDs()==null || currentResult.getListOfIncludedIDs().isEmpty()){
//                numberOfIrreducibleStructures++;
//            }
//        }
//    }
    
    
    
    /**
     * Method that will print the statistics on the console plus to the 
     * specified file
     * @param outFilePath save path file
     */
    public void printStatistics(String outFilePath){
        try{
            FileWriter fstream = new FileWriter(outFilePath);
            BufferedWriter out = new BufferedWriter(fstream);
            String output = "Creating statistics ...\n"
                    + "Algorithm used: "+evaluationAlgorithm+"\n";
            output+="- Domain: "+domain+", evaluation type: "+evaluationType+"\n";
            if(isTemplate)output+="- Executed on the templates\n";
            else output+="- Executed on the traces\n";
            if(hasInference)output +="- Inference applied\n";
            else output +="- No inference applied\n";
            //if(isReducedGraph)output +="- Graph on reduced format (informBy edges instead of used + wasGenBy)\n";
            //else output +="- Non reduced graph\n\n";
            output+="Number of structures found: "+this.getNumberOfOriginalFoundFragments()+"\n";
            output+="Number of irreducible fragments found (i.e., those structures which do not include parts of others): "+getNumberOfIrreducibleFragments()+"\n";
            output+="Number of multi step fragments structures found (i.e., those with at least two process steps): "+getNumberOfMultiStepFragments()+"("+getNumberOfFilteredMultiStepFragments()+")\n";
            output+="Number of multi step irreducible fragments found: "+getNumberOfMultiStepIrreducibleFragments()+"\n";
            output+="Frequency of detected structures: "+occurrencesOfDetectedStructures+"\n";
            output+="Frequency of multi step fragments: "+occurrencesOfMultiStepStructures+"("+occurrencesOfFilteredMultiStepStructures+")\n";
            output+="FilteredMultiStepFragments:\n";
            Iterator <Fragment> it = filteredMultiStepFragments.iterator();
            while (it.hasNext()){
                output+=it.next().getStructureID()+"; ";
            }
            output+="\nMultiStepFragments:\n";
            Iterator <Fragment> it2 = multiStepFragments.iterator();
            while (it2.hasNext()){
                output+=it2.next().getStructureID()+"; ";
            }
            output+="\nIrreducibleMultiStepFragments:\n";
            Iterator <Fragment> it3 = multiStepIrreducibleFragments.iterator();
            while (it3.hasNext()){
                output+=it3.next().getStructureID()+"; ";
            }
            System.out.println(output);
            out.write(output);
            out.close();
        }catch (Exception e){
            System.err.println("Error "+e.getMessage());
        }
    }    
    
}
