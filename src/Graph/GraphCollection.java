/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class is used to s troe all the subdue subgraphs.
 * Each subgraph has an adjacency matrix and continues the numeration
 * of the vertex of the previous subgraph.
 * 
 * @author Daniel Garijo
 */

public class GraphCollection {
  private ArrayList<Graph> graphCollection;

    public GraphCollection() {
        graphCollection = new ArrayList<Graph>();
    }
    
    public GraphCollection(ArrayList<Graph> collection){
        graphCollection = collection;
    }
    
    public int getNumberOfSubGraphs(){
        return graphCollection.size();
    }
    
    public void addSubGraph(Graph g){
        graphCollection.add(g);
    }
    
    @Override
    public GraphCollection clone(){
        return new GraphCollection((ArrayList<Graph>)this.graphCollection.clone());
    }

    public ArrayList<Graph> getGraphCollection() {
        return graphCollection;
    }
    
    
    
//    /**
//     * Method to save the collection of graphs to a File.
//     * @param outputFilePath: path of the output file 
//     */
//    public void writeFullGraphsToFile(String outputFilePath) {
//        this.writeFullGraphsToFile(outputFilePath, null);
//    }
//    
//    public void writeFullGraphsToFile(String outputFilePath, HashMap replacements){
//        FileWriter fstream = null; 
//        BufferedWriter out = null; 
//        Iterator<Graph> it = graphCollection.iterator();
//        int numeration = 0;
//        try {
//            fstream = new FileWriter(outputFilePath);
//            out = new BufferedWriter(fstream);
//            while (it.hasNext()){
//                Graph currentGraph = it.next();
//                // keep the track of the last number of nodes
//                if(replacements==null){//i.e., no replacements
//                    currentGraph.writeFullGraphToFile(out, numeration);
//                }else{
//                    currentGraph.writeFullGraphToFile(out, numeration, replacements);
//                }
//                numeration += currentGraph.getNumberOfNodes();
//            }            
//        } catch (IOException ex) {
//            System.err.println("Exception while writing the graph: . Nodes written: "+numeration+" "+ex.getMessage());
//        } finally {
//            try {
//                if(out!=null)out.close();
//                if(fstream!=null)fstream.close();
//            } catch (IOException ex) {
//                System.err.println("Error closing the files: "+ex.getMessage());
//            }
//        }
//    }
//    
//    /**
//     * Method that writes the graph collection in the specified path
//     * A file is created for each subgraph
//     * @param outputPath: path of the output folder
//     */
//    public void writeFullGraphsToSeparatedFiles(String outputPath){
//        this.writeFullGraphsToSeparatedFiles(outputPath, null);
//    }
//    
//    /**
//     * Method that writes the graph collection in the specified path.
//     * A file is created for each subgraph
//     * @param outputPath path of the output folder
//     * @param replacements Hashmap with the replacements for the nodes of the
//     * graph
//     */
//    public void writeFullGraphsToSeparatedFiles(String outputPath, HashMap replacements){
//        FileWriter fstream = null; 
//        BufferedWriter out = null; 
//        Iterator<Graph> it = graphCollection.iterator();
//        int i = 0;
//        String graphNumber = "graph";
//        try {
//            
//            while (it.hasNext()){
//                fstream = new FileWriter(outputPath+File.separator+graphNumber+i);
//                out = new BufferedWriter(fstream);
//                Graph currentGraph = it.next();
//                // keep the track of the last number of nodes
//                if(replacements==null){//i.e., no replacements
//                    currentGraph.writeFullGraphToFile(out, 0);
//                }else{
//                    currentGraph.writeFullGraphToFile(out, 0, replacements);
//                }
//                i++;
//                out.close();
//                fstream.close();                
//            }            
//        } catch (IOException ex) {
//            System.err.println("Exception while writing the graph:  "+i+" "+ex.getMessage());
//            try {
//                if(out!=null)out.close();
//                if(fstream!=null)fstream.close();
//            } catch (IOException e) {
//                System.err.println("Error closing the files: "+e.getMessage());
//            }
//        }       
//    }
//    
//    /**
//     * Method to save the collection of graphs to a File.
//     * @param outputFilePath: path of the output file 
//     */
//    public void writeReducedGraphsToFile(String outputFilePath) {
//        this.writeReducedGraphsToFile(outputFilePath, null);
//    }
//    
//    public void writeReducedGraphsToFile(String outputFilePath, HashMap replacements){
//        FileWriter fstream = null; 
//        BufferedWriter out = null; 
//        Iterator<Graph> it = graphCollection.iterator();
//        int numeration = 0;
//        try {
//            fstream = new FileWriter(outputFilePath);
//            out = new BufferedWriter(fstream);
//            while (it.hasNext()){
//                Graph currentGraph = it.next();
//                // keep the track of the last number of nodes
//                if(replacements==null){//i.e., no replacements
//                    currentGraph.writeSimplifiedGraphToFile(out, numeration);
//                }else{
//                    currentGraph.writeSimplifiedGraphToFile(out, numeration, replacements);
//                }
//                numeration += currentGraph.getNumberOfNodes();
//            }            
//        } catch (IOException ex) {
//            System.err.println("Exception while writing the graph: . Nodes written: "+numeration+" "+ex.getMessage());
//        } finally {
//            try {
//                if(out!=null)out.close();
//                if(fstream!=null)fstream.close();
//            } catch (IOException ex) {
//                System.err.println("Error closing the files: "+ex.getMessage());
//            }
//        }
//    }
//    
//    
//    public void writeReducedGraphsToSeparatedFiles(String outputPath){
//        this.writeReducedGraphsToSeparatedFiles(outputPath, null);
//    }
//    
//    public void writeReducedGraphsToSeparatedFiles(String outputPath, HashMap replacements){
//        FileWriter fstream = null; 
//        BufferedWriter out = null; 
//        Iterator<Graph> it = graphCollection.iterator();
//        int i = 0;
//        String graphNumber = "graph";
//        try {
//            
//            while (it.hasNext()){
//                fstream = new FileWriter(outputPath+File.separator+graphNumber+i);
//                out = new BufferedWriter(fstream);
//                Graph currentGraph = it.next();
//                // keep the track of the last number of nodes
//                if(replacements==null){//i.e., no replacements
//                    currentGraph.writeSimplifiedGraphToFile(out, 0);
//                }else{
//                    currentGraph.writeSimplifiedGraphToFile(out, 0, replacements);
//                }
//                i++;
//                out.close();
//                fstream.close();                
//            }            
//        } catch (IOException ex) {
//            System.err.println("Exception while writing the graph:  "+i+" "+ex.getMessage());
//            try {
//                if(out!=null)out.close();
//                if(fstream!=null)fstream.close();
//            } catch (IOException e) {
//                System.err.println("Error closing the files: "+e.getMessage());
//            }
//        }       
//    }
}
