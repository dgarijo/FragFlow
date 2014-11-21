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
package DataStructures.GraphNode.OPMW;

import DataStructures.GraphNode.GraphNode;

/**
 * Extension of the GraphNode for modeling nodes of the templates in OPMW.
 * Extension to represent the types of nodes from the Taverna workflows 
 * annotated in OPMW by Beatriz Garcia and Mark Wilkinson
 * @author Daniel Garijo
 */
public class GraphNodeTemplatesOPMWTaverna extends GraphNode{
    
    /**
     * Constructor
     * @param URI
     * @param type
     * @param numberInGraph 
     */
    public GraphNodeTemplatesOPMWTaverna(String URI, String type, int numberInGraph) {        
        super(URI, type, numberInGraph);
    }

    /**
     * Setter extension
     * @param type new type to replace the old type.
     */
    @Override
    public void setType(String type) {
        //In the workflows provided by Beatriz and Mark we only have one annotation
        //we ignore these types if we have the annotation
        if(!type.contains("DataVariable")&&!type.contains("Process")){
            this.type = type;
        }
        //else we leave it as it is
    }
    
    
    
}
