/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TestsGraphProcessing;

import Factory.Inference.CreateHashMapForInference;
import Graph.GraphCollection;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Test designed to check wether the hashMap for the replacements can be created.
 * Note that we need a multidomain ontology beforehand
 * @author Daniel Garijo
 */
public class TestCreateReplacementHashMap {
    public static void test(){
        try{
        OntModel o = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        InputStream in = FileManager.get().open("C:\\Users\\Monen\\Dropbox\\MotifFinder\\src\\Tests\\test.owl");
        o.read(in, null);        
        HashMap replacements = CreateHashMapForInference.createReplacementHashMap(o);
        System.out.println("OK!");
        }catch(Exception e){
            System.out.println("Error while executing test. Exception: "+e.getMessage());
        }
    }
    
    public static void main(String[] args){
        test();
    }
}
