/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistence.Formats;

import Graph.Graph;
import Graph.GraphCollection;
import Persistence.CollectionWritter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class that extends the Graph Collection Writter to 
 * @author Daniel Garijo
 */
public class CollectionWriterSUBDUEFormat extends CollectionWritter{
//     to do   
    @Override
    public void writeFullGraphsToFile(GraphCollection gc, String outputFilePath, HashMap replacements){
        FileWriter fstream = null; 
        BufferedWriter out = null; 
        Iterator<Graph> it = gc.getGraphCollection().iterator();
        GraphWriterSUBDUEFormat gw = new GraphWriterSUBDUEFormat();
        int numeration = 0;
        try {
            fstream = new FileWriter(outputFilePath);
            out = new BufferedWriter(fstream);
            while (it.hasNext()){
                Graph currentGraph = it.next();
                // keep the track of the last number of nodes
                if(replacements==null){//i.e., no replacements
                    //currentGraph.writeFullGraphToFile(out, numeration);
                    gw.writeFullGraphToFile(currentGraph, out, numeration);
                }else{
//                    currentGraph.writeFullGraphToFile(out, numeration, replacements);
                    gw.writeFullGraphToFile(currentGraph, out, numeration, replacements);
                }
                numeration += currentGraph.getNumberOfNodes();
            }            
        } catch (IOException ex) {
            System.err.println("Exception while writing the graph: . Nodes written: "+numeration+" "+ex.getMessage());
        } finally {
            try {
                if(out!=null)out.close();
                if(fstream!=null)fstream.close();
            } catch (IOException ex) {
                System.err.println("Error closing the files: "+ex.getMessage());
            }
        }
    }
    
        
    @Override
    public void writeFullGraphsToSeparatedFiles(GraphCollection gc, String outputPath, HashMap replacements){
    FileWriter fstream = null; 
        BufferedWriter out = null; 
        Iterator<Graph> it = gc.getGraphCollection().iterator();
        GraphWriterSUBDUEFormat gw = new GraphWriterSUBDUEFormat();
        int i = 0;
        String graphNumber = "graph";
        try {
            
            while (it.hasNext()){
                fstream = new FileWriter(outputPath+File.separator+graphNumber+i);
                out = new BufferedWriter(fstream);
                Graph currentGraph = it.next();
                // keep the track of the last number of nodes
                if(replacements==null){//i.e., no replacements
//                    currentGraph.writeFullGraphToFile(out, 0);
                    gw.writeFullGraphToFile(currentGraph, out, 0);
                }else{
//                    currentGraph.writeFullGraphToFile(out, 0, replacements);
                    gw.writeFullGraphToFile(currentGraph, out, 0, replacements);
                }
                i++;
                out.close();
                fstream.close();                
            }            
        } catch (IOException ex) {
            System.err.println("Exception while writing the graph:  "+i+" "+ex.getMessage());
            try {
                if(out!=null)out.close();
                if(fstream!=null)fstream.close();
            } catch (IOException e) {
                System.err.println("Error closing the files: "+e.getMessage());
            }
        } 
    }
    
    
    @Override
    public void writeReducedGraphsToFile(GraphCollection gc, String outputFilePath, HashMap replacements){
        FileWriter fstream = null; 
        BufferedWriter out = null;
        GraphWriterSUBDUEFormat gw = new GraphWriterSUBDUEFormat();
        Iterator<Graph> it = gc.getGraphCollection().iterator();
        int numeration = 0;
        try {
            fstream = new FileWriter(outputFilePath);
            out = new BufferedWriter(fstream);
            while (it.hasNext()){
                Graph currentGraph = it.next();
                // keep the track of the last number of nodes
                if(replacements==null){//i.e., no replacements
                    //currentGraph.writeSimplifiedGraphToFile(out, numeration);
                    gw.writeReducedGraphToFile(currentGraph, out, numeration);                    
                }else{
                    //currentGraph.writeSimplifiedGraphToFile(out, numeration, replacements);
                    gw.writeReducedGraphToFile(currentGraph, out, numeration, replacements);
                }
                numeration += currentGraph.getNumberOfNodes();
            }            
        } catch (IOException ex) {
            System.err.println("Exception while writing the graph: . Nodes written: "+numeration+" "+ex.getMessage());
        } finally {
            try {
                if(out!=null)out.close();
                if(fstream!=null)fstream.close();
            } catch (IOException ex) {
                System.err.println("Error closing the files: "+ex.getMessage());
            }
        }
    }

    
    @Override
    public void writeReducedGraphsToSeparatedFiles(GraphCollection gc, String outputPath, HashMap replacements){
        FileWriter fstream = null; 
        BufferedWriter out = null; 
        Iterator<Graph> it = gc.getGraphCollection().iterator();
        GraphWriterSUBDUEFormat gw = new GraphWriterSUBDUEFormat();
        int i = 0;
        String graphNumber = "graph";
        try {
            
            while (it.hasNext()){
                fstream = new FileWriter(outputPath+File.separator+graphNumber+i);
                out = new BufferedWriter(fstream);
                Graph currentGraph = it.next();
                // keep the track of the last number of nodes
                if(replacements==null){//i.e., no replacements
//                    currentGraph.writeSimplifiedGraphToFile(out, 0);
                    gw.writeReducedGraphToFile(currentGraph, out, 0);
                }else{
//                    currentGraph.writeSimplifiedGraphToFile(out, 0, replacements);
                    gw.writeReducedGraphToFile(currentGraph, out, 0, replacements);
                }
                i++;
                out.close();
                fstream.close();                
            }            
        } catch (IOException ex) {
            System.err.println("Exception while writing the graph:  "+i+" "+ex.getMessage());
            try {
                if(out!=null)out.close();
                if(fstream!=null)fstream.close();
            } catch (IOException e) {
                System.err.println("Error closing the files: "+e.getMessage());
            }
        }  
    }
}
