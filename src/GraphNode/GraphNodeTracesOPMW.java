/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GraphNode;

/**
 *
 * @author Daniel Garijo
 */
public class GraphNodeTracesOPMW extends GraphNode{
    

    public GraphNodeTracesOPMW(String URI, String type, int numberInGraph) {      
        super(URI, type, numberInGraph);
    }


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
