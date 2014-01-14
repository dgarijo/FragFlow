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
package Factory.Inference;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import java.util.HashMap;

/**
 * Class that takes a taxonomy, and produces a replacement hashMap for a collection
 * with the closest superclass to owl:Thing.
 * That is, it "generalizes" each type of the HashMap to the most abstract method.
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
    
    /**
     * Replacement Hashmap getter
     * @return the replacement hashmap
     */
    private HashMap getReplacementHashMap (){
        return this.replacements;
    }
    
    /**
     * Method that creates an instance of the hashmap with the replacements.
     * @param o an Ontology model with al the taxonomy (Catalog of components) 
     * modeled as an ontology
     * @return replacement hashmap
     */
    public static HashMap createReplacementHashMap(OntModel o){
        return new CreateHashMapForInference(o).getReplacementHashMap();
    }    
    
}
