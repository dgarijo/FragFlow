/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PostProcessing;
import Graph.Graph;
import Static.GeneralConstants;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Daniel Garijo
 */
public abstract class GraphReader {
    //will be initialized on the subclasses
    protected HashMap<String,FinalResult> finalResults;
    
    
    public void processResultFile(String graphFile){        
        
    }    
    /**
     * Method that given a file it looks for the occurrences of each substructure.
     * @param occFile 
     */
    public void processOccurrencesFile(String occFile){
        
    }
    
    public HashMap<String,FinalResult> processResultsAndOccurrencesFiles(String resultsFile, String ocFile){
        this.processResultFile(resultsFile);
        this.processOccurrencesFile(ocFile);
        return finalResults;
    }
    
    /**
     * Method to determine whether a structure is multi step or not.
     * 
     * Note: we can not do comparisons with the type directly because we do not
     * know which format will the URIs have (e.g., we can not ask for the number /dc/
     * as in Wings.
     * 
     * @param URIs
     * @param nodes
     * @param adjacencyMatrix
     * @return 
     */
    protected boolean isMultiStepStructure(Graph g, ArrayList includedSubstructures) {
        /*a structure is meaningful IF:
         * 1- It has at least two steps: 
         *      if the adjacency matrix has 2 inform depedencies
         * 2- It has a step but has a dependency on a graph with a step
         * 3- It has no steps but links two structures with at least one step
         */
        //if we only have one URI, we are done: it's not meaningful
        if(g.getNumberOfNodes()<=1)return false;
        //if the graph is not reduced, we reduce it
        g.putReducedNodesInAdjacencyMatrix();
        int numberOfDependencies = getNumberOfInformDependencies(g.getAdjacencyMatrix());
        numberOfDependencies += getNumberOfDependenciesFromIncludedStructures(includedSubstructures);
//        System.out.println("number of dependencies: "+numberOfDependencies);
        return (numberOfDependencies>0);
        
    }
    
    protected int getNumberOfInformDependencies(String[][] matrix){
        int dependencies = 0;
        for(int i = 0; i< matrix[0].length;i++){
            for(int j = 0; j<matrix[0].length;j++){
                if((matrix[i][j]!=null)&&(matrix[i][j].equals(GeneralConstants.INFORM_DEPENDENCY)))
                    dependencies ++;
            }
        }
        return dependencies;
    }
    
    protected int getNumberOfDependenciesFromIncludedStructures(ArrayList<FinalResult> substructureResults){
        int dependencies = 0;
        for(int i=0; i<substructureResults.size();i++){
            //for each dependency, we check if it has other dependencies.
            FinalResult currentR = substructureResults.get(i);
            ArrayList<FinalResult> structureDependencyList = currentR.getListOfIncludedIDs();
            if(!structureDependencyList.isEmpty()){
                dependencies+=getNumberOfDependenciesFromIncludedStructures(structureDependencyList);
            }
            dependencies+=getNumberOfInformDependencies(currentR.getDependencyGraph().getAdjacencyMatrix());
        }
        
        return dependencies;
    }
    
}
