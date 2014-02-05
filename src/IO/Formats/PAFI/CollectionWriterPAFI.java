/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
package IO.Formats.PAFI;

import DataStructures.Graph;
import DataStructures.GraphCollection;
import IO.CollectionWritter;
import IO.Exception.CollectionWriterException;
import IO.Formats.SUBDUE.GraphWriterSUBDUEFormat;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class that extends the Graph Collection Writter to write graphs in PAFI 
 * format. In PAFI we don't need to save the collection in separated files, 
 * as PAFI associates each occurrence of a pattern with the transaction 
 * (substructure). For example, if a pattern appears twice in a graph, it will 
 * only count as one.
 * 
 * The full graphToFile method is not extended because it will be deprecated.
 * @author Daniel Garijo
 */
public class CollectionWriterPAFI extends CollectionWritter{

    @Override
    public void writeReducedGraphsToFile(GraphCollection gc, String outputFilePath, HashMap replacements) throws CollectionWriterException {
        FileWriter fstream = null; 
        BufferedWriter out = null;
        GraphWriterPAFIFormat gw = new GraphWriterPAFIFormat();
        Iterator<Graph> it = gc.getGraphs().iterator();        
        try {
            fstream = new FileWriter(outputFilePath);
            out = new BufferedWriter(fstream);
            while (it.hasNext()){
                Graph currentGraph = it.next();
                // keep the track of the last number of nodes
                if(replacements==null){//i.e., no replacements
                    gw.writeReducedGraphToFile(currentGraph, out, 0);                    
                }else{
                    gw.writeReducedGraphToFile(currentGraph, out, 0, replacements);
                }
            }            
        } catch (Exception ex) {
            System.err.println("Exception while writing the graph: "+ex.getMessage());
            throw new CollectionWriterException("Exception while writing the graph: "+ex.getMessage(), ex);
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
