/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistence;

import Graph.Graph;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Abstract class that defines the functions to be extended by a specific
 * serializer of the graph.
 * @author Daniel Garijo
 */
public abstract class GraphWritter {
    public void writeFullGraphToFile(Graph g, BufferedWriter out, int nodeCount)throws IOException{
        writeFullGraphToFile(g,out,nodeCount,null);    
    }
    
    public void writeFullGraphToFile(Graph g, BufferedWriter out, int nodeCount, HashMap replacements)throws IOException{}
    
    
    public void writeReducedGraphToFile(Graph g, BufferedWriter out, int nodeCount)throws IOException{
        writeReducedGraphToFile(g, out, nodeCount, null);
    }
    
    /**
     * Method to write a simiplified version of the dependencies: 
     * if B used Y AND Y wasGeneratedBy A then B informedBy A 
     * @param g
     * @param out
     * @param nodeCount
     * @param replacements
     * @throws IOException 
     */
    public void writeReducedGraphToFile(Graph g, BufferedWriter out, int nodeCount, HashMap replacements)throws IOException{}
}
