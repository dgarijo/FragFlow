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
package IO;

import DataStructures.Graph;
import DataStructures.GraphCollection;
import DataStructures.GraphNode.GraphNode;
import IO.Exception.CollectionWriterException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Class that given a dataset input it gets the codes for it and writes it down
 * @author Daniel Garijo
 */
public class EncodeDataset {
    
//    public static void encodeGraphCollection(GraphReader readerForInput, GraphCollectionWriter out, String encodedDatasetPath){
//        
//        //1: read the graph collection. 
//        //call encodeGraphCollection
//        
//        //for the moment I don't have a reader for the written datasets.. 
//        //TO DO
//        
//    }
    
    public static void encodeAndWriteGraphCollection(GraphCollection g, GraphCollectionWriter out, String outPath) throws CollectionWriterException, IOException{
        //get the codes for the graphCollection
        HashMap<String,String> codes  = getCodesForNodesInGraphCollection(g);
        out.writeReducedGraphsToFile(g, outPath, codes);
        writeCodeFile(outPath,codes);
    }
    
    
    private static void getCodesForNodesInGraph(HashMap<String,String> codes, Graph g){
        Iterator <Entry<String,GraphNode>> it = g.getNodes().entrySet().iterator();
        String codeForNode = "";
        while(it.hasNext()){
            GraphNode currentNode = it.next().getValue();
            String nodeType = currentNode.getType();
            if(!codes.containsKey(nodeType)){
                codeForNode = "a"+codes.size();
                codes.put(nodeType, codeForNode);
            }
        }
    }
    
    private static HashMap<String,String> getCodesForNodesInGraphCollection(GraphCollection c){
        HashMap<String,String> codes = new HashMap<String,String>();
        Iterator<Graph> it = c.getGraphs().iterator();
        while(it.hasNext()){
            getCodesForNodesInGraph(codes, it.next());
        }
        return codes;
    }
    
    /**
     * Generic method for writting the codes of the types written in the files to their respectives URIs
     * @param outputFilePath 
     */
    protected static void writeCodeFile(String outputFilePath, HashMap<String,String> codesForTypes) throws CollectionWriterException, IOException{
        FileWriter fstream = null; 
        BufferedWriter out = null;
        outputFilePath+="-codes";
        try {
            fstream = new FileWriter(outputFilePath);
            out = new BufferedWriter(fstream);
            Iterator<Entry<String,String>> it = codesForTypes.entrySet().iterator();
            while(it.hasNext()){
                Entry currentEntry = it.next();
                out.write(currentEntry.getKey()+"->"+currentEntry.getValue()+"\n");
            }           
        }catch(Exception e){
            System.err.println("Exception while writing the graph codes: "+e.getMessage());
            throw new CollectionWriterException("Exception while writing the graph: "+e.getMessage(), e);
        } finally {
            try {
                if(out!=null)out.close();
                if(fstream!=null)fstream.close();
            } catch (IOException ex) {
                System.err.println("Error closing the files: "+ex.getMessage());
            }
        }
    }
    
}
