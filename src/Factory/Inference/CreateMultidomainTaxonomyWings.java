/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Factory.Inference;

import Static.Traces.QueriesOPMWTraces;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
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
        
        //test
//        multiDomainOntology.write(System.out);
    }    
    
    /**
     * Method that return a the multi domain ontology based on a Wings repository
     * @return 
     */
    public OntModel getMultiDomainOntology(){
        return this.multiDomainOntology;
    }
    
    public void saveMultiDomainOntologyToFile(String outFile){
        OutputStream out;
        try {
            out = new FileOutputStream(outFile);
            multiDomainOntology.write(out,"RDF/XML");
        } catch (FileNotFoundException ex) {
            System.out.println("Error while writing the model to file "+ex.getMessage());
        }
    }
    
    private ResultSet queryRepository(String endpointURL, String queryIn){
        Query query = QueryFactory.create(queryIn);
        QueryExecution qe = QueryExecutionFactory.sparqlService(endpointURL, query);
        ResultSet rs = qe.execSelect();        
        return rs;
    }
    
    /**
     * Given a repository, detect how many domain ontologies are 
     * necessary to be downloaded (one per domain) plus their URI
     */
    public void getDomainOntologies(String repositoryURI){
        //retrieve all runs from a the repository
        ResultSet accs = queryRepository(repositoryURI, QueriesOPMWTraces.queryWfExecAccount());
        while(accs.hasNext()){
            QuerySolution qs = accs.next();
            String accURI = qs.getResource("?acc").getURI();
            //for each run, analyze whether the ontology belongs to a new domain or not
            //System.out.println(accURI);            
            ResultSet uri = queryRepository(repositoryURI, QueriesOPMWTraces.getComponentOntologyURI(accURI));
            if(uri.hasNext()){
                QuerySolution qs1 = uri.next();
                String ontologyURI = qs1.getResource("?uri").getNameSpace();
                if(!domains.containsKey(ontologyURI)){
                    //we aim to retrieve the uri for download just if the domain is new
                    ResultSet localOntoURI = queryRepository(repositoryURI, QueriesOPMWTraces.getLocalComponentOntologyURI(accURI));
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
    
    
//    public static void main(String [] args){
//        CreateMultidomainTaxonomyWings c = new CreateMultidomainTaxonomyWings("http://wind.isi.edu:8890/sparql");
////        c.getDomainOntologies("http://wind.isi.edu:8890/sparql");
//    }
}
