/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GraphNode;

/**
 *
 * @author Daniel Garijo
 */
public class GraphNode {
    private final String URI;
    protected String type;
    private final int numberInGraph;

    public GraphNode(String URI, String type, int numberInGraph) {
        this.URI = URI;
        this.type = type;
        this.numberInGraph = numberInGraph;
    }
   
    
    public String getType() {
        return type;
    }

    public String getURI() {
        return URI;
    }

    public int getNumberInGraph() {
        return numberInGraph;
    }

    public void setType(String type) {        
        
    }
    
}
