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

import Static.Templates.ConstantsOPMWTempl;

/**
 * Extension of the GraphNode for modeling nodes of the templates in OPMW.
 * @author Daniel Garijo
 */
public class GraphNodeTemplatesOPMW extends GraphNode{
    
    /**
     * Constructor
     * @param URI
     * @param type
     * @param numberInGraph 
     */
    public GraphNodeTemplatesOPMW(String URI, String type, int numberInGraph) {        
        super(URI, type, numberInGraph);
    }

    /**
     * Setter extension
     * @param type new type to replace the old type.
     */
    @Override
    public void setType(String type) {
        //if the current type belongs to a catalog ontology, then change it.
        //if it doesn't, then leave either wta or wtp (only if the current type is not from the data catalog.
        if(type.contains(ConstantsOPMWTempl.WINGS_ABS_COMP_CATALOG_URI)||type.contains(ConstantsOPMWTempl.WINGS_DATA_CATALOG_URI)){
            this.type = type;            
        }else 
            if((type.contains("DataVariable")||type.contains("Parameter")||type.contains("Process"))&&(!(this.type.contains(ConstantsOPMWTempl.WINGS_ABS_COMP_CATALOG_URI))&&(!this.type.contains(ConstantsOPMWTempl.WINGS_DATA_CATALOG_URI)))){
                this.type = type;
        }
    }
    
    
    
}
