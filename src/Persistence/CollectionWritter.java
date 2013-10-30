/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistence;

import Graph.GraphCollection;
import java.util.HashMap;

/**
 * Abstract class that defines the necessary classes to sreialize collections
 * in any given format. Different formats will extend the methods of this class.
 * @author Daniel Garijo
 */
public class CollectionWritter {
    public void writeFullGraphsToFile(GraphCollection gc, String outputFilePath) {
        this.writeFullGraphsToFile(gc, outputFilePath, null);
    }
    
    public void writeFullGraphsToFile(GraphCollection gc, String outputFilePath, HashMap replacements){}
    
    public void writeFullGraphsToSeparatedFiles(GraphCollection gc, String outputPath){
        this.writeFullGraphsToSeparatedFiles(gc, outputPath, null);
    }
    
    public void writeFullGraphsToSeparatedFiles(GraphCollection gc, String outputPath, HashMap replacements){}
    
    public void writeReducedGraphsToFile(GraphCollection gc, String outputFilePath) {
        this.writeReducedGraphsToFile(gc, outputFilePath, null);
    }
    
    public void writeReducedGraphsToFile(GraphCollection gc, String outputFilePath, HashMap replacements){}
    
    public void writeReducedGraphsToSeparatedFiles(GraphCollection gc, String outputPath){
        this.writeReducedGraphsToSeparatedFiles(gc, outputPath, null);
    }
    
    public void writeReducedGraphsToSeparatedFiles(GraphCollection gc, String outputPath, HashMap replacements){}
}
