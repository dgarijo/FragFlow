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
package DataStructures.GraphNode;

/**
 * Class to represent the nodes of a Graph. A general node has a URI, a type and
 * the number of the graph in the adjacency matrix.
 * @author Daniel Garijo
 */
public class GraphNode {
    private final String URI;
    protected String type;
    private final int numberInGraph;
    
    /**
     * Advanced constructor.
     * @param URI URI of the node
     * @param type type of the node
     * @param numberInGraph number of the node in adjacency matrix
     */
    public GraphNode(String URI, String type, int numberInGraph) {
        this.URI = URI;
        this.type = type;
        this.numberInGraph = numberInGraph;
    }
   
    /**
     * Type getter
     * @return 
     */
    public String getType() {
        return type;
    }
    
    /**
     * URI getter
     * @return 
     */
    public String getURI() {
        return URI;
    }

    /**
     * Getter of the number of the node in the graph
     * @return 
     */
    public int getNumberInGraph() {
        return numberInGraph;
    }
    
    /**
     * Type setter. In some cases we might need to change the type of the node,
     * when dealing with abstractions for instance.
     * @param type 
     */
    public void setType(String type) {        
        this.type = type;
    }
    
}
