/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PostProcessing;

import Graph.Graph;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Wrapper to store the structure ID, number of occurrences, graph of dependences
 * between processes (a subdue graph), list of structure IDs included in the current
 * one (dependences)
 * @author Daniel Garijo
 */
public class FinalResult {
    private String structureID;
    private int numberOfOccurrences;
    private Graph dependencyGraph;
    //private ArrayList<String> listOfIncludedIDs;
    private ArrayList<FinalResult> listOfPointersToIncludedStructures;
    
    private boolean isMultiStepStructure;//a meaningful structure is a structure with at least two steps.
    
    
    public FinalResult(){
        
    }
    
    public FinalResult(String structureID, int numberOfOccurrences, Graph dependencyGraph, ArrayList<FinalResult> listOfIncludedPointers, boolean isMultiStep) {
        this.structureID = structureID;
        this.numberOfOccurrences = numberOfOccurrences;
        this.dependencyGraph = dependencyGraph;
        this.listOfPointersToIncludedStructures = listOfIncludedPointers;
        this.isMultiStepStructure = isMultiStep;
        
       
//        System.out.println("Structure "+structureID+ "has number of occurrences: "+numberOfOccurrences+"and it is meaningful: "+isMeaningfulStructure);
    }

    public Graph getDependencyGraph() {
        return dependencyGraph;
    }

    public ArrayList<FinalResult> getListOfIncludedIDs() {
        return listOfPointersToIncludedStructures;
    }

    public int getNumberOfOccurrences() {
        return numberOfOccurrences;
    }

    public String getStructureID() {
        return structureID;
    }

    public boolean isMultiStepStructure() {
        return isMultiStepStructure;
    }
    

    public void setDependencyGraph(Graph dependencyGraph) {
        this.dependencyGraph = dependencyGraph;
    }

    public void setListOfIncludedIDs(ArrayList<FinalResult> listOfIncludedPointers) {
        this.listOfPointersToIncludedStructures = listOfIncludedPointers;
    }

    public void setNumberOfOccurrences(int numberOfOccurrences) {
        this.numberOfOccurrences = numberOfOccurrences;
    }

    public void setStructureID(String structureID) {
        this.structureID = structureID;
    }

    public void setIsMultiStepStructure(boolean isMeaningfulStructure) {
        this.isMultiStepStructure = isMeaningfulStructure;
    }
    
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
        int size = this.getDependencyGraph().getNumberOfNodes()-listOfPointersToIncludedStructures.size();
        Iterator<FinalResult> includedStructuresIterator = this.listOfPointersToIncludedStructures.iterator();
        while(includedStructuresIterator.hasNext()){
            FinalResult includedStructure = includedStructuresIterator.next();
            size+= includedStructure.getSize();
        }
        return size;
    }
        
//    /**
//     * Tests for comparing if a final result is included in a template
//     * 
//     */
//    
//    public boolean isPartOfGraph(Graph biggerGraph){
//        //Check that the types are the same (or at least they appear in the bigger graph)
//        if(!areAllTheNodesIncludedIn(biggerGraph))return false;
//        //The nodes are included... but are they in the right order? (wasInformedBy)
//        if(!areAllTheStepDependenciesOnRightOrder(biggerGraph)) return false;
//        //if the nodes are included and the dependencies are right as well then the fragment can be found
//        return true;
//    }
//    
//    private boolean areAllTheNodesIncludedIn(Graph biggerGraph){
//        Iterator it = this.listOfPointersToIncludedStructures.iterator();        
//        //if the values of the dependency structures are not there, then we return false.
//        while(it.hasNext()){
//            FinalResult dependency = (FinalResult) it.next();
//            if(dependency.areAllTheNodesIncludedIn(biggerGraph) == false) return false;
//        }
//        //check that the rest of the nodes of the result are there.
//        //take into account that we are comparing the types of each node. The uris in this case are irrelevant.
//        Iterator it2 = this.dependencyGraph.getURIs().iterator();
//        HashMap<String,GraphNode> urisAndNodes = this.dependencyGraph.getNodes();
//        while(it2.hasNext()){
//            String currentURIType = urisAndNodes.get((String) it2.next()).getType();
//            //we skip substructures
//            if(!currentURIType.contains("SUB_")){
//                if(!biggerGraph.containsURIType(currentURIType))return false;
//            }            
//        }
//        return true;
//    }
//    
//    private boolean areAllTheStepDependenciesOnRightOrder(Graph biggerGraph) {        
//        //first get sequences to find in the bigger structure
//        ArrayList sequencesToCheck = getNumberSequencesFromAdjMatrix(dependencyGraph.getAdjacencyMatrix());
//        //we have the sequences of the fragment. Now we measure if we can find them on the bigger graph.
//        //if a SUB structure appears, we have to subdivide and create new sequences.
//        //create a method sequenceToArrayList: given a sequence, get the types.
//        biggerGraph.putReducedNodesInAdjacencyMatrix();
//        //get the sequences from the bigger graph
//        ArrayList<String> biggerNumberedGraphSequences = this.getNumberSequencesFromAdjMatrix(biggerGraph.getAdjacencyMatrix());
//        ArrayList<String> biggerTypedGraphSequences = new ArrayList<String>();
//        //get the bigger graph TypedSequences        
//        Iterator aux = biggerNumberedGraphSequences.iterator();
//        HashMap<String,GraphNode> biggerGraphNodes = biggerGraph.getNodes();
//        ArrayList<String> biggerGraphURIs = biggerGraph.getURIs();
//        while(aux.hasNext()){
//            String current = (String) aux.next();            
//            String aux1 ="";
//            String[] sequence = current.split(";");
//            for(int i=0;i<sequence.length;i++){
//                if(!sequence[i].equals("")){//because of the first comma (small fix)
//                    //we substract 1 because the adjacency matrix starts in 0
//                    String currentType = biggerGraphNodes.get(biggerGraphURIs.get(Integer.parseInt(sequence[i])-1)).getType();
//                    aux1+=currentType;
//                }
//            }
//            biggerTypedGraphSequences.add(aux1);
//            System.out.println(aux1);
//        }
//        
//        Iterator<String> it = sequencesToCheck.iterator();
//        while(it.hasNext()){
//            String current= it.next();
//            System.out.println(current);
////test//            ArrayList a = getNodeTypesFromFinalResultDependencies("SUB_2", listOfPointersToIncludedStructures);
//            //if(!testSequenceInBiggerGraph(getTypedSequenceFromNumberSequence(current,this), biggerTypedGraphSequences))return false;
//            if(!testSequenceInBiggerGraph(getTreeTypedSequenceFromNumberSequence(";4;5;1;3",this), biggerTypedGraphSequences))return false;
//            
//        }        
//        //if all the sequences are found, it means that the fragment is found in the bigger graph.
//        return true;
//    }
//    
//    /**
//     * Given an arraylist with either Strings or an ArrayList of types, this method 
//     * compares the concatenation of the Strings to each of the sequences in the bigger graph.
//     * Note that if you have [a,ArrayList] then you have to test with all the components in the arrayList
//     * If you have [a,ArrayList,b] you have to create 2 tests: [a,ArrayList];[ArrayList,b]
//     * @param typedSequenceFromNumberSequence
//     * @param bigGraph
//     * @return 
//     */
////    private boolean testSequenceInBiggerGraph(ArrayList typedSequenceFromNumberSequence, ArrayList<String> bigGraph) {       
////        ArrayList<String>candidatesToTest = new ArrayList<String>(); //set of strings to test.
////        Iterator it = typedSequenceFromNumberSequence.iterator();
////        ArrayList copyArrayList = (ArrayList) typedSequenceFromNumberSequence.clone();
////        candidatesToTest.add("");//first sequence
////        boolean stop = false;
////        while(it.hasNext() && !stop){
////            Object currentItem = it.next();
////            copyArrayList.remove(currentItem);
////            if (currentItem.getClass().getName().equals("java.lang.String")){
////                for(int i = 0; i< candidatesToTest.size();i++){
////                    candidatesToTest.set(i, candidatesToTest.get(i)+currentItem);
////                }                
////            }else{
////                ArrayList<String> currentArrayList = (ArrayList<String>) currentItem;
////                ArrayList<String> newCandidates = new ArrayList<String>();
////                boolean rest = false;
////                for(int i = 0; i< candidatesToTest.size();i++){
////                    for(int j = 0; j< currentArrayList.size();j++){
////                        newCandidates.add(candidatesToTest.get(i)+currentArrayList.get(j));
////                        copyArrayList.add(0,currentArrayList.get(j));
////                        rest = rest || testSequenceInBiggerGraph(copyArrayList, bigGraph);//this also prevents to recalcultate if true because of lazy evaluation
////                        copyArrayList.remove(0);
////                    }
////                }
////                if(!rest)return false;//at least one of the other sequences MUST be included
////                candidatesToTest = newCandidates;
////                //we stop the loop. The rest will be figured out on the sub calls.
////                stop = true;
////            }            
////        }
//        
////        //now we have all the sequences that are included... we have to check 
////        //each of them individually. If all the candidates to test are included
////        //in the bigger sequence then return true. Otherwise return false.
////        
////        Iterator<String> itCandidates = candidatesToTest.iterator();        
////        while(itCandidates.hasNext()){            
////            String candidateToTest = itCandidates.next();
////            Iterator<String> itBigGraph = bigGraph.iterator();
////            System.out.println("Looking for "+candidateToTest);
////            while(itBigGraph.hasNext()){                
////                if(itBigGraph.next().contains(candidateToTest)){
////                    System.out.println("FOUND");
////                    return true;
////                }
////            }
////        }
////        return false;
////    }
//    
//    /**
//     * Method that returns an arraylist with the types corresponding to the sequence.
//     * For example. Input: 123. Output[type1,type2,type3]     
//     * @param numberSequence
//     * @param g
//     * @return 
//     */
//    private SequenceTree getTreeTypedSequenceFromNumberSequence(String numberSequence,FinalResult g){
////        ArrayList typedSequences = new ArrayList();
////        String[] sequence = numberSequence.split(";");
////        HashMap<String,GraphNode> graphNodes = g.getDependencyGraph().getNodes();
////        ArrayList<String> graphURIs = g.getDependencyGraph().getURIs();
////        for(int i=0;i<sequence.length;i++){
////            if(!sequence[i].equals("")){//because of the first comma (small fix)
////                //we substract 1 because the adjacency matrix starts in 0
////                String currentType = graphNodes.get(graphURIs.get(Integer.parseInt(sequence[i])-1)).getType();
////                if(currentType.contains("SUB_")){
////                    //we save the types of the nodes, which are the ones to test
////                    typedSequences.add(getNodeTypesFromFinalResultDependencies(currentType,g.getListOfIncludedIDs()));
////                }else{
////                    typedSequences.add(currentType);
////                }
////            }
////        }
////        return typedSequences;
//        SequenceTree transformedTreeSequence = new SequenceTree(new SequenceTreeNode(null, "Root", null));
//        String[] sequence = numberSequence.split(";");
//        HashMap<String,GraphNode> graphNodes = g.getDependencyGraph().getNodes();
//        ArrayList<String> graphURIs = g.getDependencyGraph().getURIs();
//        for(int i=0;i<sequence.length;i++){
//            if(!sequence[i].equals("")){//because of the first comma (small fix)
//                //we substract 1 because the adjacency matrix starts in 0
//                String currentType = graphNodes.get(graphURIs.get(Integer.parseInt(sequence[i])-1)).getType();
//                if(currentType.contains("SUB_")){
//                    FinalResult linkToNextSubstructure = null;
//                    Iterator<FinalResult> pointer = g.getListOfIncludedIDs().iterator();
//                    boolean structureFound = false;
//                    while(pointer.hasNext() &&!structureFound){
//                        linkToNextSubstructure = pointer.next();
//                        if(linkToNextSubstructure.getStructureID().equals(currentType)){
//                            structureFound = true;
//                        }
//                    }
//                    //look for the sequences of the substructure
//                    ArrayList<String> numberSequencesFromSubStructure = getNumberSequencesFromAdjMatrix(linkToNextSubstructure.getDependencyGraph().getAdjacencyMatrix());
//                    ArrayList<SequenceTree> sequenceSubTrees = new ArrayList<SequenceTree>();                    
//                    Iterator<String> seqFromSubTreeIterator = numberSequencesFromSubStructure.iterator();
//                    while (seqFromSubTreeIterator.hasNext()){
//                        sequenceSubTrees.add(getTreeTypedSequenceFromNumberSequence(seqFromSubTreeIterator.next(),linkToNextSubstructure));
//                    }
//                    //add the trees. Having into account that a tree like: o->(1)->(2)-> 3 
//                    //having o->4 will generate: 
//                    //    ->1->2->3
//                    //o->4->2->3
//                    //    ->3
//                    
//                    //for all the sequences, generate the appropriate subtree.
//                    Iterator<SequenceTree> sequenceSubTreeIterator = sequenceSubTrees.iterator();
//                    while (sequenceSubTreeIterator.hasNext()){
//                        SequenceTree currentSequence = sequenceSubTreeIterator.next();
//                        ArrayList<SequenceTreeNode> currentChildren = currentSequence.getRoot().getChildren();
//                        //if the number of children is 1, then we must add the appropriate OR among the children
//                        //otherwise, the structure is fine.
//                        if(currentChildren.size()==1){//the SUB is a sequence like a->b->c
//                            SequenceTreeNode branchToExpand = currentChildren.get(0);
//                            //add as children the optional parts of the branch
//                            ArrayList<SequenceTreeNode> aux = branchToExpand.getChildren();
//                            while (aux!=null && aux.size()==1){
//                                currentChildren.add(aux.get(0));
//                                aux = aux.get(0).getChildren();                                
//                                //what this does is: this arrives: o->a->b->c.
//                                //this is what leaves:
//                                //o->a->b->c
//                                // ->b->c
//                                //->c
//                            }
//                        }
//                    }
//                    
//                    //generate an arrayList of nodes with the children
//                    ArrayList<SequenceTreeNode> children = new ArrayList<SequenceTreeNode>();
//                    Iterator<SequenceTree> sequenceSubTreeIterator2 = sequenceSubTrees.iterator();
//                    while (sequenceSubTreeIterator2.hasNext()){
//                        SequenceTree currentSequence = sequenceSubTreeIterator2.next();
//                        ArrayList<SequenceTreeNode> currentChildren = currentSequence.getRoot().getChildren();
//                        for(int e = 0; e< currentChildren.size(); e++){
//                            children.add(currentChildren.get(e));
//                        }
//                    }
//                    
//                    //add all the appropriate subtrees as leafs to the current tree.
//                    transformedTreeSequence.addLeafToAllChildren(children); 
//                }else{
//                    //add to all leaves of the tree this node as children.
//                    transformedTreeSequence.addLeafToAllChildren(currentType);                    
//                }
//            }
//        }
//        return transformedTreeSequence;
//    }
//    
//    /**
//     * Given a structure ID and the arraylist with the dependencies, this method
//     * extracts the structure id from the dependencly array and calculates its node dependencies.
//     * @param structureID
//     * @param dependencies
//     * @return 
//     */
//    /*private ArrayList<String> getOrderedNodeTypesFromFinalResultDependencies(String structureID, ArrayList<FinalResult> dependencies){
//        Iterator it = dependencies.iterator();
//        ArrayList<String> finalNodes = new ArrayList<String>();                
//        boolean found = false;
//        FinalResult structureToGetNodes = null;
//        //get the substructure on which we are going to perform the search
//        while(!found && it.hasNext()){
//            structureToGetNodes = (FinalResult)it.next();
//            if(structureID.equals(structureToGetNodes.structureID)){
//                found = true;
//            }
//        }
//        //now we have the structure. Get the nodes plus the substructures contained recursively
//        Iterator it2 = structureToGetNodes.getDependencyGraph().getURIs().iterator();
//        HashMap<String,GraphNode> urisAndNodes = structureToGetNodes.getDependencyGraph().getNodes();
//        while(it2.hasNext()){
//            String currentURIType = urisAndNodes.get((String) it2.next()).getType();
//            if(currentURIType.contains("SUB_")){
//                ArrayList aux = getOrderedNodeTypesFromFinalResultDependencies(currentURIType, structureToGetNodes.listOfPointersToIncludedStructures);
//                Iterator it3 = aux.iterator();
//                while(it3.hasNext()){
//                    finalNodes.add((String)it3.next());
//                }
//            }else{
//                finalNodes.add(currentURIType);
//            }            
//        }
//        return finalNodes;
//    }*/
//    
//    
//    private ArrayList<String> getNumberSequencesFromAdjMatrix(String [][] adjacencyMatrix){
//        ArrayList sequencesToCheck = new ArrayList<String>();        
//        for (int i=0;i< adjacencyMatrix.length;i++){
//            for(int j=i;j<adjacencyMatrix.length;j++){
//                if(adjacencyMatrix[j][i]!=null && adjacencyMatrix[j][i].equals(GeneralConstants.INFORM_DEPENDENCY)){                    
//                    sequencesToCheck = this.addNewNumberSequences(sequencesToCheck,";"+j,";"+i);                
//                }
//            }
//            for(int j=i;j<adjacencyMatrix.length;j++){
//                if(adjacencyMatrix[i][j]!=null && adjacencyMatrix[i][j].equals(GeneralConstants.INFORM_DEPENDENCY)){                    
//                    sequencesToCheck = this.addNewNumberSequences(sequencesToCheck,";"+i,";"+j);
//                }
//            }
//        }
//        return sequencesToCheck;
//    }
//    /*
//     * Given an array of sequencies and a new one, this method calculates how
//     * to adapt the old ones with the new ones. For example, if you have 
//     * 5-4 and 1-5 arrives, it will create 1-5-4. On the other hand, if you have 
//     * 5-4 and 4-1 arrives, it will create 5-4-1. Finally, if you have
//     * 1-2-3-4 and 1-3 arrives, it will create 1-2-3-4, 1-3-4
//     * 1-2-3-4 and 3-5 arrives, it will create 1-2-3-4, 1-2-3-5
//     */
//    private ArrayList<String> addNewNumberSequences(ArrayList<String> old, String firstPart, String secondPart) {
//        ArrayList<String> candidates = (ArrayList<String>) old.clone();
//        boolean addedInList = false;    
//        for(int c =0; c<old.size();c++){
//            String currentList=  (String) old.get(c);
//            String total = firstPart+secondPart;
//            if(!currentList.contains(total)){
//                if(currentList.startsWith(secondPart)){
//                    currentList=firstPart+currentList;
//                    addedInList = true;
//                    candidates.remove(c);
//                    candidates.add(c,currentList);
//                }else if (currentList.endsWith(firstPart)){
//                    currentList+=secondPart;
//                    addedInList = true;
//                    candidates.remove(c);
//                    candidates.add(c,currentList);
//                }else{
//                    //if j is included, then we need to copy the list.
//                    if(currentList.contains(secondPart)&&!currentList.endsWith(secondPart)){
//                        //copy from i to end.
//                        //create a new list
//                        currentList = currentList.substring(currentList.indexOf(secondPart), currentList.length());
//                        currentList = firstPart+currentList;
//                        candidates.add(currentList);
//                        addedInList = true;
//                    }else if(currentList.contains(firstPart) && !currentList.startsWith(firstPart)){
//                        //this case has the limitation because sometimes it generates a wrong sequence.
//                        //copy from i to beggining. Append j at the end
//                        //create a new list
//                        currentList = currentList.substring(0,currentList.indexOf(firstPart)+1);
//                        currentList += secondPart;                        
//                        candidates.add(currentList);
//                        addedInList = true;
//                    }
//                }                            
//            }  
//        }        
//        //if it hasnt been added to a list, we add it in a new list
//        if(!addedInList){
//            String aux1 = ""+firstPart +secondPart;
//            candidates.add(aux1);
//        }                
//          
//        return candidates;
//    }
}
