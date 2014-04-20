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
import DataStructures.GraphCollection;
import Factory.OPMW.OPMWTemplate2Graph;
import IO.GraphCollectionWriter;
import IO.Exception.CollectionWriterException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * PARSEMIS collection writer
 * @author Daniel Garijo
 */
public class GraphCollectionWriterPARSEMIS extends GraphCollectionWriter{

    public GraphCollectionWriterPARSEMIS() {
        super();
    }
    
    

    /**
     * Parsemis collection writer. Very similar to the PAFI Collection writer. 
     * The main difference is when writing the graphs themselves.
     * @param gc graph collection
     * @param outputFilePath output file path
     * @param replacements replacement hashmap (if any)
     * @throws CollectionWriterException  exception when reading the input.
     */
    @Override
    public void writeReducedGraphsToFile(GraphCollection gc, String outputFilePath, HashMap replacements) throws CollectionWriterException {
        FileWriter fstream = null; 
        BufferedWriter out = null;
        GraphWriterPARSEMIS gw = new GraphWriterPARSEMIS();
        Iterator<Graph> it = gc.getGraphs().iterator();        
        try {
            fstream = new FileWriter(outputFilePath);
            out = new BufferedWriter(fstream);
            while (it.hasNext()){
                Graph currentGraph = it.next();
                if(replacements==null){//i.e., no replacements
                    gw.writeReducedGraphToFile(currentGraph, out, -1);                    
                }else{
                    gw.writeReducedGraphToFile(currentGraph, out, -1, replacements);
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
    
//    public static void main(String[] args) throws CollectionWriterException{
//        OPMWTemplate2Graph tp = new OPMWTemplate2Graph("http://wind.isi.edu:8890/sparql");
//            tp.transformDomainToGraph("TextAnalytics");        
////        tp.transformToGraph("http://www.opmw.org/export/resource/WorkflowTemplate/FEATUREGENERATION");
//        CollectionWriterPARSEMIS writer = new CollectionWriterPARSEMIS();
////            writer.writeReducedGraphsToFile(tp.getGraphCollection(), "TestSaveCollectionAsReducedlGraphInFile");
//        writer.writeReducedGraphsToFile(tp.getGraphCollection(), "testParsemis.lg");
//               
//    }
    
}
