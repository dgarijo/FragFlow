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
package IO.Formats;

import DataStructures.Graph;
import DataStructures.GraphNode.GraphNode;
import DataStructures.Fragment;
import IO.FragmentReader;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class to Read the substructures found by SUBDUE, along with their occurrences.
 * Note that the graph file and the occurrences file are different files.
 * The output is a list : structure ID, number of occurrences, graph of dependences
 * between processes (a subdue graph), list of structure IDs included in the current
 * one (dependences)
 * @author Daniel Garijo
 */
public class SubdueFragmentReader extends FragmentReader {
    
//    private HashMap<String,FinalResult> finalResults;//substructureID, finalResult

    public SubdueFragmentReader() {
        finalResults = new HashMap<String, Fragment>();
    }    
    
    /**
     * Method that given a file with subdue graphs, processes it and represents
     * it in the form of "final result"
     * @param graphFile 
     */
    @Override
    public void processResultFile(String graphFile){
        try{
            FileInputStream fstream = new FileInputStream(graphFile);            
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            int i = 0;
            ArrayList<String> URIs = null; 
            HashMap<String, GraphNode> nodes = null;
            String[][] adjacencyMatrix = null;
            String [] currLine;
            ArrayList<Fragment> includedSubStructures = null;
            while ((strLine = br.readLine()) != null)   {
                if(strLine.equals("S")){
                    //new structure, we store the previous one
                    if(URIs!=null){
                        i++;
                        Graph auxg = new Graph(URIs, nodes, adjacencyMatrix, "SUB_"+i);
                        Fragment currentStructure = new Fragment("SUB_"+i, 0,auxg,includedSubStructures,isMultiStepStructure(auxg, includedSubStructures));
                        finalResults.put("SUB_"+i, currentStructure) ;                        
                    }
                    //we reinicialize all the structures
                    URIs = new ArrayList<String>();
                    nodes = new HashMap<String, GraphNode>();
                    adjacencyMatrix = null; // we can only create it once we have all the nodes
                    includedSubStructures = new ArrayList<Fragment>();
                }
                else{
                    //record vertex and edges.
                    if (strLine.startsWith("v")){
                        //add vertex to the uris and hashmap. Since we dont know
                        //URIs (we just work with the types), we use ids.
                        //we can't use the types as uris, otherwise the hashmap
                        //would be messed up (more than one node is of the same
                        //type
                        currLine = strLine.split(" ");
                        String id = currLine[1].trim();
                        String type = currLine[2].trim();
                        //System.out.println("v "+id+",type "+type);
                        URIs.add(id);
                        //if it is a substructure, save it
                        if (type.contains("SUB_")&&!includedSubStructures.contains(finalResults.get(type))){
                            includedSubStructures.add(finalResults.get(type));                            
                        }
                        nodes.put(id, new GraphNode(id, type, Integer.parseInt(id)));
                    }else if (strLine.startsWith("d")){
                        //add dependences.
                        if(adjacencyMatrix == null){
                            adjacencyMatrix = new String[URIs.size()+1][URIs.size()+1];//we ignore the [0][0] lines
                        }
                        //retrieve positions: d row column relation
                        currLine = strLine.split(" ");
                        adjacencyMatrix[Integer.parseInt(currLine[1])][Integer.parseInt(currLine[2])] = currLine[3];
                        //System.out.println(nodes.get(currLine[1]).getType()+" "+currLine[3]+" "+nodes.get(currLine[2]).getType());
                                                
                    }
                    
                }

            }
            //the last result must be added as well.
            if(URIs!=null){
                i++;
                Graph auxg = new Graph(URIs, nodes, adjacencyMatrix, "SUB_"+i);
                Fragment currentStructure = new Fragment("SUB_"+i, 0,auxg,includedSubStructures,this.isMultiStepStructure(auxg, includedSubStructures));
                finalResults.put("SUB_"+i, currentStructure) ;                        
            }
            in.close();                
        }catch(Exception e){
            System.err.println("Error while reading the file "+e.getMessage());
        }
        
    }
    
    /**
     * Method that given a file it looks for the occurrences of each substructure.
     * The file identifies substructures like this: SUB_n -> number
     * @param occFile 
     */
    @Override
    public void processOccurrencesFile(String occFile){
        FileInputStream fstream =null;
        DataInputStream in = null;
        try{
            fstream = new FileInputStream(occFile);            
            in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;            
            while ((strLine = br.readLine()) != null)   {
                String[] idAndOccurrence = strLine.split("->");
                String id = idAndOccurrence[0].trim();
                int number = Integer.parseInt(idAndOccurrence[1].trim());
                if(!finalResults.containsKey(id)){                    
                    finalResults.put(id, new Fragment());
                }
                finalResults.get(id).setNumberOfOccurrences(number);
            }
            in.close();
            fstream.close();
        }catch(Exception e){
            System.err.println("Error while processing the file "+e.getMessage());
        }
    }
    
//    public HashMap<String,FinalResult> processResultsAndOccurrencesFiles(String resultsFile, String ocFile){
//        this.processResultFile(resultsFile);
//        this.processOccurrencesFile(ocFile);
//        return finalResults;
//    }
//    
    /**
     * Method to determine whether a structure is meaningful or not.
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
//    private boolean isMeaningfulStructure(Graph g, ArrayList includedSubstructures) {
//        /*a structure is meaningful IF:
//         * 1- It has at least two steps: 
//         *      if the adjacency matrix has 2 inform depedencies
//         * 2- It has a step but has a dependency on a graph with a step
//         * 3- It has no steps but links two structures with at least one step
//         */
//        //if we only have one URI, we are done: it's not meaningful
//        if(g.getNumberOfNodes()<=1)return false;
//        //if the graph is not reduced, we reduce it
//        g.putReducedNodesInAdjacencyMatrix();
//        int numberOfDependencies = getNumberOfInformDependencies(g.getAdjacencyMatrix());
//        numberOfDependencies += getNumberOfDependenciesFromIncludedStructures(includedSubstructures);
//        System.out.println("number of dependencies: "+numberOfDependencies);
//        return (numberOfDependencies>0);
//        
//    }
//    
//    private int getNumberOfInformDependencies(String[][] matrix){
//        int dependencies = 0;
//        for(int i = 0; i< matrix[0].length;i++){
//            for(int j = 0; j<matrix[0].length;j++){
//                if((matrix[i][j]!=null)&&(matrix[i][j].equals(GeneralConstants.INFORM_DEPENDENCY)))
//                    dependencies ++;
//            }
//        }
//        return dependencies;
//    }
//    
//    private int getNumberOfDependenciesFromIncludedStructures(ArrayList<String> substructureNames){
//        int dependencies = 0;
//        for(int i=0; i<substructureNames.size();i++){
//            //for each dependency, we check if it has other dependencies.
//            FinalResult currentR = this.finalResults.get(substructureNames.get(i));
//            ArrayList<String> structureDependencyList = currentR.getListOfIncludedIDs();
//            if(!structureDependencyList.isEmpty()){
//                dependencies+=getNumberOfDependenciesFromIncludedStructures(structureDependencyList);
//            }
//            dependencies+=getNumberOfInformDependencies(currentR.getDependencyGraph().getAdjacencyMatrix());
//        }
//        
//        return dependencies;
//    }
    
    //local tests
//    public static void main(String[] args){
//        SubdueGraphReader s = new SubdueGraphReader();
//        //s.processOccurrencesFile("C:\\Users\\Monen\\Documents\\NetBeansProjects\\MotifFinder\\SUBDUE_TOOL\\results\\TEST0803_occurrences");
//        s.processResultFile("C:\\Users\\Monen\\Documents\\NetBeansProjects\\MotifFinder\\SUBDUE_TOOL\\results\\Tests\\Text_Analytics_Graph_Inference_Templates-results1");
//        try{
//            FileWriter fstream = new FileWriter("TEstsSimplifiedStructure");
//            BufferedWriter out = new BufferedWriter(fstream);
//            s.finalResults.get("SUB_1").getDependencyGraph().writeSimplifiedGraphToFile(out, 0);
//            System.out.println(s.finalResults.size());
//            out.close();
//            Object[]keys = s.finalResults.keySet().toArray();
//            for(int i = 0;i<keys.length;i++){
//                FinalResult currentResult = s.finalResults.get((String)keys[i]);
//                currentResult.isIsMeaningfulStructure();
//            }
//        }catch (Exception e){
//            System.err.println("Error "+e.getMessage());
//        }
//    }
}
