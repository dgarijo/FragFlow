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
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Abstract class that defines the functions to be extended by a specific
 * serializer of the graph.
 * @author Daniel Garijo
 */
public abstract class GraphWriter {
    /**
     * Method to write the full graph to a file
     * @param g
     * @param out
     * @param nodeCount
     * @throws IOException 
     */
    public void writeFullGraphToFile(Graph g, BufferedWriter out, int nodeCount)throws IOException{
        writeFullGraphToFile(g,out,nodeCount,null);    
    }
    
    /**
     * Method to write the full graph to a file, replacing the types in the graph
     * according to the replacements hashmap.
     * @param g
     * @param out
     * @param nodeCount
     * @param replacements
     * @throws IOException 
     */
    public void writeFullGraphToFile(Graph g, BufferedWriter out, int nodeCount, HashMap replacements)throws IOException{}
    
    /**
     * Method to write a reduced graph to a file.
     * @param g
     * @param out
     * @param nodeCount
     * @throws IOException 
     */
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
    public abstract void writeReducedGraphToFile(Graph g, BufferedWriter out, int nodeCount, HashMap replacements)throws IOException;
}
