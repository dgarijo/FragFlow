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
package IO.Formats.PARSEMIS;

import DataStructures.Graph;
import DataStructures.GraphNode.GraphNode;
import IO.GraphWriter;
import Static.GeneralConstants;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Graph Writer for parsemis. We will write it in the ".lg" syntax, which is the
 * one that so far has been understood successfully by the Parsemis framework.
 * That is: 
 * t # nameOfGraph
 * v 1 label1 
 * v 2 label2
 * e 1 2 label1_label2 labelEdge (labelEdge is an int, which will be 1)
 * 
 * @author Daniel Garijo
 */
public class GraphWriterPARSEMIS extends GraphWriter{

    /**
     * PARSEMIS graph writer. Very similar to the PAFI one.
     * @param g graph to write
     * @param out buffered wirter where we will write the graph
     * @param nodeCount the initial count for the vertex
     * @param replacements (optional) replacement hashmap
     * @throws IOException exception when reading the input
     */
    @Override
    public void writeReducedGraphToFile(Graph g, BufferedWriter out, int nodeCount, HashMap replacements) throws IOException {
        out.write("t # "+g.getName());
        out.newLine();
        System.out.println("%Writting: "+g.getName());
        //retrieve the reduced graph
        g.putReducedNodesInAdjacencyMatrix();
        ArrayList<String>uris = g.getURIs();
        Iterator<String> it = uris.iterator();
        HashMap<String,GraphNode> nodes = g.getNodes();
        String[][] adjacencyMatrix = g.getAdjacencyMatrix();
        nodeCount = -1;//PAFI must start in 0 always.
        if(replacements==null){
            while (it.hasNext()){
                    String currentURI = it.next();
                    GraphNode currNode = (GraphNode) nodes.get(currentURI);
//                    System.out.println("v "+(currNode.getNumberInGraph()+nodeCount)+" "+currNode.getType());
                    out.write("v "+(currNode.getNumberInGraph()+nodeCount)+" "+currNode.getType());
                    out.newLine();
            }
        }else{
            while (it.hasNext()){
                String currentURI = it.next();
                GraphNode currNode = (GraphNode) nodes.get(currentURI);
                String nodeType = currNode.getType();
                //the hashMap may NOT have the node for replacement (leave the one it has already)
                if(replacements.containsKey(currNode.getType())){
                    nodeType = (String) replacements.get(nodeType);
                }
//                System.out.println("v "+(currNode.getNumberInGraph()+nodeCount)+" "+nodeType);
                out.write("v "+(currNode.getNumberInGraph()+nodeCount)+" "+nodeType);
                out.newLine();
            }
        }
        //write down the dependencies (adjacency matrix)              
        for (int i = 0; i<adjacencyMatrix.length;i++){
            for(int j = 0; j<adjacencyMatrix.length; j++){
                if(adjacencyMatrix[i][j]!=null && adjacencyMatrix[i][j].equals(GeneralConstants.INFORM_DEPENDENCY)){                    
                    //this is a bug that has to be fixed. the [0][0] lines should NOT be ignored, and therefore
                    //the -1 should not be necessary.
                    //This has to be fixed and reviewed (opmw2Template and opmw2Trace)
                    out.write("e "+(i+nodeCount)+" "+(j+nodeCount)+" "+nodes.get(uris.get(i-1)).getType()+"_"+nodes.get(uris.get(j-1)).getType()+" "+GeneralConstants.INFORM_EGDE);
                    out.newLine();
                }
            }                
        }  
    }
    
}
