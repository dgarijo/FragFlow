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

import Static.GeneralMethods;
import Static.Query.QueriesOPMWTraces;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Class designed to create a multidomain taxonomy of the Wings component catalogue
 * Since in Wings every run is published with a domain ontology with different URI,
 * we "merge" the domains into a multidomain ontology. This is necessary to perform
 * any inferences, since the same component URIs differ in the run ontologies. Only
 * the ontology name has the domain "correct" URI.
 * Example: http://www.isi.edu/ac/TextAnalytics/library.owl
 * 
 * Note: we are only dealing with taxonomies (subclassing)
 * @author Daniel Garijo
 */
public class CreateMultidomainTaxonomyWings {
    /**
     * Hashmap containing the URI of the domain ontology and the correspondant 
     * URI where we can download it. 
     * Example: http://www.isi.edu/ac/TextAnalytics/library.owl, http://www.opmw.org/datasets/resource/2/247/acdom_library.owl
     * 
     * This class is valid for both templates and runs
     */
    private HashMap<String, String> domains;
    
    //an ontmodel with the classes and subclasses of every domain ontology
    private OntModel multiDomainOntology;

    /**
     * Class creation method
     * @param repositoryURI 
     */
    public CreateMultidomainTaxonomyWings(String repositoryURI) {
        domains = new HashMap<String, String>();        
        this.getDomainOntologies(repositoryURI);
        
        //for each domain, load the ontology in the model, adding the appropriate changes to the URIS
        multiDomainOntology =  ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );        
        
        Iterator<Entry<String,String>> it =  domains.entrySet().iterator();
        while (it.hasNext()){
            Entry<String,String> currentEntry = it.next();
            modifyOntology(currentEntry.getValue(),currentEntry.getKey());//"http://www.opmw.org/datasets/resource/2/199/acdom_library.owl#", "http://www.isi.edu/ac/TextAnalytics/library.owl#");
        }
        
    }    
    
    /**
     * Method that returns a multi domain ontology based on a Wings repository
     * @return the model with the mutlidomain taxonomy.
     */
    public OntModel getMultiDomainOntology(){
        return this.multiDomainOntology;
    }
    /**
     * Method that saves the mutlidomain ontology to a file.
     * @param outFile the path of the file to save.
     */
    public void saveMultiDomainOntologyToFile(String outFile){
        OutputStream out;
        try {
            out = new FileOutputStream(outFile);
            multiDomainOntology.write(out,"RDF/XML");
        } catch (FileNotFoundException ex) {
            System.out.println("Error while writing the model to file "+ex.getMessage());
        }
    }
    
    /**
     * Given a repository, detect how many domain ontologies are 
     * necessary to be downloaded (one per domain) plus their URI
     * @param repositoryURI the repository URI
     */
    private void getDomainOntologies(String repositoryURI){
        //retrieve all runs from a the repository
        ResultSet accs = GeneralMethods.queryOnlineRepository(repositoryURI, QueriesOPMWTraces.queryWfExecAccount());
        while(accs.hasNext()){
            QuerySolution qs = accs.next();
            String accURI = qs.getResource("?acc").getURI();
            //for each run, analyze whether the ontology belongs to a new domain or not
            //System.out.println(accURI);            
            ResultSet uri = GeneralMethods.queryOnlineRepository(repositoryURI, QueriesOPMWTraces.getComponentOntologyURI(accURI));
            if(uri.hasNext()){
                QuerySolution qs1 = uri.next();
                String ontologyURI = qs1.getResource("?uri").getNameSpace();
                if(!domains.containsKey(ontologyURI)){
                    //we aim to retrieve the uri for download just if the domain is new
                    ResultSet localOntoURI = GeneralMethods.queryOnlineRepository(repositoryURI, QueriesOPMWTraces.getLocalComponentOntologyURI(accURI));
                    //if the domain does not exist in the hashmap, add the URI of the ontology.
                    if(localOntoURI.hasNext()){
                        QuerySolution qsOnto = localOntoURI.next();
                        domains.put(ontologyURI, qsOnto.getResource("?localOntoURI").getNameSpace());
                        System.out.println("Local onto "+ qsOnto.getResource("?localOntoURI").getNameSpace()+ " added. Domain: "+ontologyURI);
                    }
                }
            }            
        }        
    }
    
    
    /**
     * Given an ontology URI, load the ontology in a model and change the URIs
     * appropriately with the ontology name
     */
    private void modifyOntology(String ontologyURI, String domainURI){
        //basically, the URI of the ontology should be the name (to do the replacements)
        
        //load the ontology in an auxiliary model (we are just interested in the classes)
        System.out.println("Loading "+ontologyURI);
        OntModel aux = this.readOntology(ontologyURI); 
        //set strict mode to false to avoid checking some conversions of OntClasses.
        aux.setStrictMode(false);
        
        //add just what we are interested. Taxonomy
        ExtendedIterator<OntClass> classIterator = aux.listClasses();
        while (classIterator.hasNext()){
            OntClass currentClass = classIterator.next();
            //add the class plus its dependences (taxonomy)
            OntClass parent = currentClass.getSuperClass();
             //the artifact names still have their respective types
            if(currentClass.getNameSpace().contains("/dc/")){
                //this could be even be ignored. Inference is working at a process level.
                multiDomainOntology.createClass(currentClass.getURI());
            }else{       
                OntClass currentClassInModel = multiDomainOntology.createClass(domainURI+currentClass.getLocalName());
                //we filter "Component" superclasses (to remove non-meaningful classes)
                if(parent!=null&&!parent.getLocalName().equals("Component")){
                    //add as subclass
                    currentClassInModel.addSuperClass(multiDomainOntology.createClass(domainURI+parent.getLocalName()));                
                }
            }
            
//            Filtrados adicionales: quitar "Component" de algunas ontos. No sirve de nada.
//            Filtrar también los ficheros conservando su URI bien (aunque no los vayamos a necesitar)
        }
    }
    
    private OntModel readOntology(String ontologyURI){
        OntModel loadedOnto = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
        try{            
            loadedOnto.read(ontologyURI);
            return loadedOnto;
        }catch(Exception e){
            System.out.println("Error while loading ontology "+ontologyURI+". Trying to read n3...");
            loadedOnto.read(ontologyURI, "N3");
            return loadedOnto;
        }
        
    }
    
}
