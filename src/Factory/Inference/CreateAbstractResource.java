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
package Factory.Inference;

import DataStructures.Graph;
import DataStructures.GraphCollection;
import DataStructures.GraphNode.GraphNode;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class is used to create an abstract graph with a hashmap of replacements.
 * The input is a graph (typically with the reduced format), and the output
 * will be an abstract graph of the current one. Note that the URIs of the graph
 * won't change; just its types.
 * This class is mainly used to determine whether an abstract fragment can be
 * found in a particular instance of a template or a trace in a workflow.
 * @author Daniel Garijo
 */
public class CreateAbstractResource {
    
    /**
     * Function that given a replacement Hashmap and a Graph it returns the same
     * graph with the types of the nodes abstracted according to the taxonomy. If
     * a type is already abstracted, no changes are performed
     * @param g the input graph
     * @param replacements a hasmap of replacements
     * @return the abstract graph
     */
    public static Graph createAbstractGraph(Graph g, HashMap<String, String> replacements){
        HashMap<String,GraphNode> nodes = g.getNodes();
        Iterator<String> nodeKeyIterator =  nodes.keySet().iterator();
        while(nodeKeyIterator.hasNext()){
            String currentKey = nodeKeyIterator.next();
            GraphNode currentNode = nodes.get(currentKey);
            if(replacements.containsKey(currentNode.getType())){
                currentNode.setType(replacements.get(currentNode.getType()));
            }
        }
        return g;
    }
    /**
     * Method designed to abstract a collection.
     * @param gc the collection to abstract.
     * @param replacements the replacement hashmap.
     * @return the collection with abstract types.
     */
    public static GraphCollection createAbstractCollection(GraphCollection gc, HashMap<String, String> replacements){
        Iterator<Graph> graphIterator =  gc.getGraphCollection().iterator();
        while(graphIterator.hasNext()){
            Graph currentGraph = graphIterator.next();
            currentGraph = createAbstractGraph(currentGraph, replacements);
        }
        return gc;
    }
    
}
