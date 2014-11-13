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
package DataStructures;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Wrapper to store the fragment ID, number of occurrences, graph of dependences
 * between processes (a subdue graph), list of structure IDs included in the current
 * one (dependences)
 * @author Daniel Garijo
 */
public class Fragment {
    private String structureID;
    private int numberOfOccurrences;
    private Graph dependencyGraph;
    private ArrayList<Fragment> listOfPointersToIncludedStructures;
    
    private boolean isMultiStepFragment;//a multi step structure is a structure with at least two steps.
    private boolean isFilteredMultiStepFragment;//if a fragment is not filtered multistep, then we may ignore it
    private boolean isFilteredMultiStepIrreducibleFragment;
//    private boolean isMultiStepIrreducibleFragment;
    
    /**
     * Default constructor
     */
    public Fragment(){
        
    }
    
    /**
     * Advanced constructor.
     * @param structureID id of the fragment, like SUB_1
     * @param numberOfOccurrences number of occurrences of the fragment in the 
     * dataset
     * @param dependencyGraph graph associated to the fragment.
     * @param listOfIncludedPointers pointers to other fragments.
     * @param isMultiStep states whether the fragment has more than 2 steps or 
     * not.
     */
    public Fragment(String structureID, int numberOfOccurrences, Graph dependencyGraph, ArrayList<Fragment> listOfIncludedPointers, boolean isMultiStep) {
        this.structureID = structureID;
        this.numberOfOccurrences = numberOfOccurrences;
        this.dependencyGraph = dependencyGraph;
        this.listOfPointersToIncludedStructures = listOfIncludedPointers;
        this.isMultiStepFragment = isMultiStep;
        //the rest are not known normally when creating a fragment. We set them
        //to false by deafult.
        this.isFilteredMultiStepFragment = true;//until filtered, all fragments are considered valuable
        this.isFilteredMultiStepIrreducibleFragment = false;
//        this.isMultiStepIrreducibleFragment = false;
   }

    /**
     * Dependency graph getter
     * @return the dependency graph
     */
    public Graph getDependencyGraph() {
        return dependencyGraph;
    }

    /**
     * Included fragments getter
     * @return the list of included fragments.
     */
    public ArrayList<Fragment> getListOfIncludedIDs() {
        return listOfPointersToIncludedStructures;
    }

    /**
     * Number of occurrences getter
     * @return number of occurrences of this fragment when it was detected.
     */
    public int getNumberOfOccurrences() {
        return numberOfOccurrences;
    }

    /**
     * Structure id getter
     * @return structure id
     */
    public String getStructureID() {
        return structureID;
    }

    /**
     * isMultiStepFragment getter
     * @return whether this fragment is a multi step one or not
     */
    public boolean isMultiStepFragment() {
        return isMultiStepFragment;
    }
    
    /**
     * isFilteredMultiStepFragment getter
     * @return whether this fragment is a filtered multi step fragment or not
     * (i.e., if the fragment is relevant or not).
     */
    public boolean isFilteredMultiStepFragment() {
        return isFilteredMultiStepFragment;
    }

    /**
     * isIrreducibleFragment getter
     * @return if the current fragment is an irreducible fragment or not
     */
    public boolean isFilteredMultiStepIrreducibleFragment() {
        return isFilteredMultiStepIrreducibleFragment;
    }

    /**
     * isMultiStepIrreducibleFragment getter
     * @return if the current fragment is a multi step irreducible fragment or not
     */
//    public boolean isMultiStepIrreducibleFragment() {
//        return isMultiStepIrreducibleFragment;
//    }
    
    /**
     * Dependency graph setter
     * @param dependencyGraph 
     */
    public void setDependencyGraph(Graph dependencyGraph) {
        this.dependencyGraph = dependencyGraph;
    }

    /**
     * List of included ids setter
     * @param listOfIncludedPointers 
     */
    public void setListOfIncludedIDs(ArrayList<Fragment> listOfIncludedPointers) {
        this.listOfPointersToIncludedStructures = listOfIncludedPointers;
    }

    /**
     * number of occurrences setter
     * @param numberOfOccurrences 
     */
    public void setNumberOfOccurrences(int numberOfOccurrences) {
        this.numberOfOccurrences = numberOfOccurrences;
    }

    /**
     * structure ID setter
     * @param structureID 
     */
    public void setStructureID(String structureID) {
        this.structureID = structureID;
    }

    /**
     * isMultiStepStructure setter
     * @param isMeaningfulStructure 
     */
    public void setIsMultiStepStructure(boolean isMeaningfulStructure) {
        this.isMultiStepFragment = isMeaningfulStructure;
    }

    /**
     * isFilteredMultiStepFragment setter
     * @param isFilteredMultiStepFragment 
     */
    public void setIsFilteredMultiStepFragment(boolean isFilteredMultiStepFragment) {
        this.isFilteredMultiStepFragment = isFilteredMultiStepFragment;
    }

    /**
     * isIrreducibleFragment setter
     * @param isIrreducibleFragment 
     */
    public void setIsFilteredMultiStepIrreducibleFragment(boolean isFilteredMultiStepIrreducibleFragment) {
        this.isFilteredMultiStepIrreducibleFragment = isFilteredMultiStepIrreducibleFragment;
    }

    /**
     * isMultiStepIrreducibleFragment setter
     * @param isMultiStepIrreducibleFragment 
     */
//    public void setIsMultiStepIrreducibleFragment(boolean isMultiStepIrreducibleFragment) {
//        this.isMultiStepIrreducibleFragment = isMultiStepIrreducibleFragment;
//    }
    
    
    
    /**
     * Function that return the size of the fragment (regarding the number of 
     * nodes of the fragment plus its included structures).
     * If the structure contains substructures, the size doesn't count the 
     * SUB node, but the nodes contained within that substructure.
     * For example:
     * SUB_1
     * v 1 a
     * v 2 b
     * d 2 1 informedBy
     * 
     * SUB_2
     * v 3 SUB_1
     * v 4 c
     * d 4 3 informedBy
     * 
     * size(SUB_2) would return 3.
     * @return the size of the fragment (nodes)
     */
    public int getSize(){
        int size = this.getDependencyGraph().getNumberOfNodes();
        if(listOfPointersToIncludedStructures!=null && !listOfPointersToIncludedStructures.isEmpty()){
            size = size-listOfPointersToIncludedStructures.size();
            Iterator<Fragment> includedStructuresIterator = this.listOfPointersToIncludedStructures.iterator();
            while(includedStructuresIterator.hasNext()){
                Fragment includedStructure = includedStructuresIterator.next();
                size+= includedStructure.getSize();
            }
        }
        return size;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Fragment))return false;
        Fragment otherFragment = (Fragment)other;
        if (otherFragment.getStructureID().equals(this.structureID)) return true;
        return false;
    } 
   
    
        
}
