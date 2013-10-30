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
package Graph;

import Static.GeneralConstants;
import GraphNode.GraphNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A class to store a SUBDUE Graph.
 * @author Daniel Garijo
 */
public class Graph {
    //we have the URIs just for the purpose of making the processing easier (it is easier
    //to processs an arrayList than a hashMap
    private ArrayList<String> URIs;
    private HashMap<String,GraphNode> nodes;    
    //it is an String adjacency Matrix so we know the type of relationship
    //that is connecting 2 nodes. No more than 1 relation connects 2 nodes.
    private String [][] adjacencyMatrix;
    private String name;

    public Graph() {
        URIs = new ArrayList<String>();
        nodes = new HashMap<String, GraphNode>();
    }

    public Graph(ArrayList<String> URIs, HashMap<String, GraphNode> nodes, String[][] adjacencyMatrix, String name) {
        this.URIs = URIs;
        this.nodes = nodes;
        this.adjacencyMatrix = adjacencyMatrix;
        this.name = name;
    }
    
    /**
     * Function that returns the number of nodes in a graph (URIs.size)
     * @return size of the URIs arrayList
     */
    public int getNumberOfNodes(){
        return URIs.size();
    }

    public String[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, GraphNode> getNodes() {
        return nodes;
    }

    public ArrayList<String> getURIs() {
        return URIs;
    }
    
    
    
    
//    public int getNumberOfReducedNodes(){
//        return this.getReducedNodesInAdjacencyMatrix().size();
//    }  
    
    public void setNodes(HashMap<String,GraphNode> newNodes, ArrayList<String> newURIs){
        nodes = newNodes;
        URIs = newURIs;
    }

    public void setAdjacencyMatrix(String[][] adjacencyMatrix) {
        this.adjacencyMatrix = adjacencyMatrix;        
    }
    
    /*
     * if B used Y AND Y wasGeneratedBy A then B informedBy A
     * The reduced list of vertex is included as well.
     */
    public void putReducedNodesInAdjacencyMatrix(){
//        ArrayList<String> reducedVertex = new ArrayList<String>();
        for (int i = 0; i<adjacencyMatrix.length;i++){
            for (int j = 0; j<adjacencyMatrix.length;j++){
                if((adjacencyMatrix[i][j]!=null)&&(adjacencyMatrix[i][j].equals(GeneralConstants.USAGE_DEPENDENCY))){
                    //the adjacency matrix is initialized to null
                    for(int k = 0; k<adjacencyMatrix.length;k++){
                        if((adjacencyMatrix[j][k]!=null) && (adjacencyMatrix[j][k].equals(GeneralConstants.GENERATION_DEPENDENCY))){
                            adjacencyMatrix[i][k] = GeneralConstants.INFORM_DEPENDENCY; 
                        }
                    }
                }
            }
        }
    }

    /**
     * Method used to determine whether one of the types in the adjacency graph is of type URIType
     * @param currentURIType
     * @return true if the type is included as a type of one of the nodes
     */
    public boolean containsURIType(String URIType) {
        Iterator it = URIs.iterator();
        while(it.hasNext()){
            //check if any of the node types are of type URIType
            String currentURI = (String) it.next();
            if(this.nodes.get(currentURI).getType().equals(URIType))return true;
        }
        return false;
    }
    
}
