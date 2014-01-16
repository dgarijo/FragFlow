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

import DataStructures.GraphCollection;
import IO.Exception.CollectionWriterException;
import java.util.HashMap;

/**
 * Abstract class that defines the necessary classes to sreialize collections
 * in any given format. Different formats will extend the methods of this class.
 * @author Daniel Garijo
 */
public class CollectionWritter {
    /**
     * Method to write all the graphs into one file
     * @param gc graph collection to write
     * @param outputFilePath  storing path
     */
    public void writeFullGraphsToFile(GraphCollection gc, String outputFilePath) throws CollectionWriterException{
        this.writeFullGraphsToFile(gc, outputFilePath, null);
    }
    
    /**
     * Method to write all the graphs of the collection into one file, with the 
     * types replaced according to the replacements hashmap
     * @param gc
     * @param outputFilePath
     * @param replacements 
     */
    public void writeFullGraphsToFile(GraphCollection gc, String outputFilePath, HashMap replacements) throws CollectionWriterException{}
    
    public void writeFullGraphsToSeparatedFiles(GraphCollection gc, String outputPath) throws CollectionWriterException{
        this.writeFullGraphsToSeparatedFiles(gc, outputPath, null);
    }
    
    /**
     * Method to write a full colelction to separated files, one per graph, and
     * replace the types according to the replacements hashmap
     */
    public void writeFullGraphsToSeparatedFiles(GraphCollection gc, String outputPath, HashMap replacements) throws CollectionWriterException{}
    
    /**
     * Method to write the reduced graph collection to a file
     * @param gc
     * @param outputFilePath 
     */
    public void writeReducedGraphsToFile(GraphCollection gc, String outputFilePath) throws CollectionWriterException {
        this.writeReducedGraphsToFile(gc, outputFilePath, null);
    }
    
    /**
     * Method to write the reduced graph collection to a file, replacing the types
     * of the collection according to the replacements hashmap
     * @param gc
     * @param outputFilePath
     * @param replacements 
     */
    public void writeReducedGraphsToFile(GraphCollection gc, String outputFilePath, HashMap replacements) throws CollectionWriterException{}
    
    /**
     * Method to write the full reduced collection to separated files, one per 
     * graph in the collection.
     * @param gc
     * @param outputPath 
     */
    public void writeReducedGraphsToSeparatedFiles(GraphCollection gc, String outputPath) throws CollectionWriterException{
        this.writeReducedGraphsToSeparatedFiles(gc, outputPath, null);
    }
    
    /**
     * Method to write the full reduced collection to separated files, one per graph
     * in the collection, replacing the types in each graph with the ones in 
     * the replacements hashmap.
     * @param gc
     * @param outputPath
     * @param replacements 
     */
    public void writeReducedGraphsToSeparatedFiles(GraphCollection gc, String outputPath, HashMap replacements) throws CollectionWriterException{}
}
