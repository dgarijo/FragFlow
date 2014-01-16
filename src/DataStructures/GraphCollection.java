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
package DataStructures;

import java.util.ArrayList;

/**
 * This class is used to store all the graphs.
 * Each graph has an adjacency matrix and continues the numeration
 * of the vertex of the previous subgraph.
 * 
 * @author Daniel Garijo
 */

public class GraphCollection {
  private ArrayList<Graph> graphCollection;

    /**
    * Default constructor
    */  
    public GraphCollection() {
        graphCollection = new ArrayList<Graph>();
    }
    
    /**
     * Advanced constructor, in case you have created the collection already.
     * @param collection 
     */
    public GraphCollection(ArrayList<Graph> collection){
        graphCollection = collection;
    }
    
    /**
     * Returns the number of graphs in the collection.
     * @return number of graphs in the collection
     */
    public int getNumberOfSubGraphs(){
        return graphCollection.size();
    }
    
    /**
     * Method to add a graph to the collection
     * @param g the graph to add
     */
    public void addSubGraph(Graph g){
        graphCollection.add(g);
    }
    
    @Override
    public GraphCollection clone(){
        return new GraphCollection((ArrayList<Graph>)this.graphCollection.clone());
    }
    
    /**
     * Getter of the collection
     * @return returns the whole collection.
     */
    public ArrayList<Graph> getGraphs() {
        return graphCollection;
    }
    
}
