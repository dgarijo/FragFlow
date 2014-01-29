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
 * Extension of GraphNode to represent traces in OPMW.
 * @author Daniel Garijo
 */
public class GraphNodeTracesOPMW extends GraphNode{
    
    /**
     * Constructor
     * @param URI
     * @param type
     * @param numberInGraph 
     */
    public GraphNodeTracesOPMW(String URI, String type, int numberInGraph) {      
        super(URI, type, numberInGraph);
    }

    /**
     * Setter of the type
     * @param type 
     */
    @Override
    public void setType(String type) {
        //if the type is different from "Artifact" or process then change it.
        //we avoid the types of prov here.
        if(!type.contains("Activity")&&!type.contains("Entity")){
            if(this.type.contains("Artifact")||this.type.contains("Process")){
                this.type = type;
            }
        }
    }
    
    
    
}
