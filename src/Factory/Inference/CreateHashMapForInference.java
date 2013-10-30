/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Factory.Inference;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import java.util.HashMap;

/**
 * Class that takes a taxonomy, and produces a replacement hashMap for a collection
 * with the closest superclass to owl:Thing.
 * That is, it "generalizes" the graph to the most abstract method.
 * @author Daniel Garijo
 */
public class CreateHashMapForInference {
    //key: URI of the class, URI of the closest superclass to owl:Thing
    private HashMap <String,String> replacements; 
    private OntModel taxonomy;
    
    
    /**
     * This constructor takes as input a a taxonomy. The
     * method uses the taxonomy to generalize the collection, and make the appropriate
     * replacements in each sub-graph of the collection.
     * @param o: taxonomy of components 
     */
    private CreateHashMapForInference(OntModel o) {
        taxonomy = o;        
        replacements = new HashMap<String, String>();
        taxonomy.setStrictMode(false);
        //from the taxonomy, we create the replacement keys in the hashMap
        //(for testing, take the "test" multi domain ontology).
        ExtendedIterator<OntClass> it = taxonomy.listClasses();
        while (it.hasNext()){
            OntClass currentClass = it.next();
            //get the most generic class that is not owl:Thing
            OntClass generic = retrieveMostGenericSuperclass(currentClass);
            System.out.println("replace "+currentClass.getLocalName()+" with "+ generic.getLocalName());
            replacements.put(currentClass.getURI(),generic.getURI().replace("Class", ""));
            //for the sake of simplifying things (some of them end up with class,
            //some of them don't, I add a duplicate key without "class"
            if(currentClass.getURI().endsWith("Class")){
                replacements.put(currentClass.getURI().replace("Class", ""),generic.getURI().replace("Class", ""));
            }
        }      
    }
    
    /**
     * Each component of the taxonomy has a unique father.
     * @param o
     * @return 
     */
    private OntClass retrieveMostGenericSuperclass(OntClass o){
        if (o.hasSuperClass()){
            if(o.getSuperClass().getURI().contains("Thing")){
                return o;
            }else{
                return retrieveMostGenericSuperclass(o.getSuperClass());
            }
        }
        return o;
    }
    
    private HashMap getReplacementHashMap (){
        return this.replacements;
    }
    
    public static HashMap createReplacementHashMap(OntModel o){
        return new CreateHashMapForInference(o).getReplacementHashMap();
    }    
    
}
