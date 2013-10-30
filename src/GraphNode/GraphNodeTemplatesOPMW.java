/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GraphNode;

import Static.Templates.ConstantsOPMWTempl;

/**
 *
 * @author Daniel Garijo
 */
public class GraphNodeTemplatesOPMW extends GraphNode{
    

    public GraphNodeTemplatesOPMW(String URI, String type, int numberInGraph) {        
        super(URI, type, numberInGraph);
    }


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
