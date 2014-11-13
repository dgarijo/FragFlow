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
package IO.Formats.SUBDUE;

import DataStructures.Graph;
import DataStructures.GraphCollection;
import IO.GraphCollectionWriter;
import IO.Exception.CollectionWriterException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class that extends the Graph Collection Writter to write graphs in SUBDUE 
 * format.
 * @author Daniel Garijo
 */
public class GraphCollectionWriterSUBDUE extends GraphCollectionWriter{
   
    @Override
    public void writeFullGraphsToFile(GraphCollection gc, String outputFilePath, HashMap replacements)throws CollectionWriterException{
        FileWriter fstream = null; 
        BufferedWriter out = null; 
        Iterator<Graph> it = gc.getGraphs().iterator();
        GraphWriterSUBDUE gw = new GraphWriterSUBDUE();
        int numeration = 0;
        try {
//            if(!outputFilePath.endsWith(".g"))outputFilePath+=".g";
            fstream = new FileWriter(outputFilePath);
            out = new BufferedWriter(fstream);
            while (it.hasNext()){
                Graph currentGraph = it.next();
                // keep the track of the last number of nodes
                if(replacements==null){//i.e., no replacements
                    gw.writeFullGraphToFile(currentGraph, out, numeration);
                }else{
                    gw.writeFullGraphToFile(currentGraph, out, numeration, replacements);
                }
                numeration += currentGraph.getNumberOfNodes();
            }            
        } catch (Exception ex) {
            System.err.println("Exception while writing the graph: . Nodes written: "+numeration+" "+ex.getMessage());
            throw new CollectionWriterException("Exception while writing the graph: . Nodes written: "+numeration+" "+ex.getMessage(), ex);
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
    public void writeFullGraphsToSeparatedFiles(GraphCollection gc, String outputPath, HashMap replacements)throws CollectionWriterException{
    FileWriter fstream = null; 
        BufferedWriter out = null; 
        Iterator<Graph> it = gc.getGraphs().iterator();
        GraphWriterSUBDUE gw = new GraphWriterSUBDUE();
        int i = 0;
        String graphNumber = "graph";
        try {
            
            while (it.hasNext()){
//                if(!outputPath.endsWith(".g"))outputPath+=".g";
                fstream = new FileWriter(outputPath+File.separator+graphNumber+i);
                out = new BufferedWriter(fstream);
                Graph currentGraph = it.next();
                // keep the track of the last number of nodes
                if(replacements==null){//i.e., no replacements
                    gw.writeFullGraphToFile(currentGraph, out, 0);
                }else{
                    gw.writeFullGraphToFile(currentGraph, out, 0, replacements);
                }
                i++;
                out.close();
                fstream.close();                
            }            
        } catch (Exception ex) {
            System.err.println("Exception while writing the graph:  "+i+" "+ex.getMessage());
            throw new CollectionWriterException("Exception while writing the graph:  "+i+" "+ex.getMessage(), ex);
        }finally{
            try {
                if(out!=null)out.close();
                if(fstream!=null)fstream.close();
            } catch (IOException e) {
                System.err.println("Error closing the files: "+e.getMessage());
            }
        } 
    }
    
    
    @Override
    public void writeReducedGraphsToFile(GraphCollection gc, String outputFilePath, HashMap replacements)throws CollectionWriterException{
        FileWriter fstream = null; 
        BufferedWriter out = null;        
        GraphWriterSUBDUE gw = new GraphWriterSUBDUE();
        Iterator<Graph> it = gc.getGraphs().iterator();
        int numeration = 0;
        try {
//            if(!outputFilePath.endsWith(".g"))outputFilePath+=".g";
            fstream = new FileWriter(outputFilePath);
            out = new BufferedWriter(fstream);
            while (it.hasNext()){
                Graph currentGraph = it.next();
                // keep the track of the last number of nodes
                if(replacements==null){//i.e., no replacements
                    gw.writeReducedGraphToFile(currentGraph, out, numeration);                    
                }else{
                    gw.writeReducedGraphToFile(currentGraph, out, numeration, replacements);
                }
                numeration += currentGraph.getNumberOfNodes();
            }
        } catch (Exception ex) {
            System.err.println("Exception while writing the graph: . Nodes written: "+numeration+" "+ex.getMessage());
            throw new CollectionWriterException("Exception while writing the graph: . Nodes written: "+numeration+" "+ex.getMessage(), ex);
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
    public void writeReducedGraphsToSeparatedFiles(GraphCollection gc, String outputPath, HashMap replacements)throws CollectionWriterException{
        FileWriter fstream = null; 
        BufferedWriter out = null; 
        Iterator<Graph> it = gc.getGraphs().iterator();
        GraphWriterSUBDUE gw = new GraphWriterSUBDUE();
        int i = 0;
        String graphNumber = "graph";
        try {
            
            while (it.hasNext()){
//                if(!outputPath.endsWith(".g"))outputPath+=".g";
                fstream = new FileWriter(outputPath+File.separator+graphNumber+i);
                out = new BufferedWriter(fstream);
                Graph currentGraph = it.next();
                // keep the track of the last number of nodes
                if(replacements==null){//i.e., no replacements
                    gw.writeReducedGraphToFile(currentGraph, out, 0);
                }else{
                    gw.writeReducedGraphToFile(currentGraph, out, 0, replacements);
                }
                i++;
                out.close();
                fstream.close();                
            }
        } catch (Exception ex) {
            System.err.println("Exception while writing the graph:  "+i+" "+ex.getMessage());            
            throw new CollectionWriterException("Exception while writing the graph:  "+i+" "+ex.getMessage(), ex);
        }finally{
            try {
                if(out!=null)out.close();
                if(fstream!=null)fstream.close();
            } catch (IOException e) {
                System.err.println("Error closing the files: "+e.getMessage());
            }
        //throw new CollectionWriterException("", ex);
        }  
    }
}
