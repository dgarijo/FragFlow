/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Factory;

import Graph.GraphCollection;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class to declare the generic operations to be extended by the specific 
 * processors
 * @author Daniel Garijo
 */
public abstract class GraphCollectionCreator {
    protected GraphCollection collection;
    protected final String repositoryURI;

    public GraphCollectionCreator(String repositoryURI) {
        collection = new GraphCollection();     
        this.repositoryURI = repositoryURI;
    }
    
    //to be implemented by the extension of the class
    /**
     * Method that given a URI of template/trace in a repository, processes it
     * and creates the apropriate SUBDUE Graph
     * @param URI : ID of the template /run in the repository
     */
    public void transformToSubdueGraph(String URI){
        
    }
    
    public GraphCollection getGraphCollection(){
        return collection;
    }
    
    //to be implemented by the different extensions of the class
    /**
     * This method queries the repository for all the subgraphs and processes
     * them through the transformToSubdueGraph method
     */
//    public void transformRespositoryToSubdueGraph(String outputFileName)throws IOException{
//        
//    }
    
    /**
     * Given a domain, we extract the traces or templates form it (reduces the
     * search space)
     * @param domain domain form which we want to perform the extraction
     */
    public void transformDomainToSubdueGraph(String domain){
        
    }
    
    /**
     * Method to transform just a set of subgraphs (templates/traces) as a subdue graph
     * @param URIs 
     */
//    public void transformToSubdueGraph(ArrayList<String> URIs, String outputFileName){
//        
//    }
    
    
    /**
     * Method to perform a query to a repository
     * @param endpointURL URL of the endpoint
     * @param queryIn SPARQL query to do to the endpoint
     * @return 
     */
    protected ResultSet queryRepository(String endpointURL, String queryIn){
        Query query = QueryFactory.create(queryIn);
        //System.out.println(queryIn);
        QueryExecution qe = QueryExecutionFactory.sparqlService(endpointURL, query);
        ResultSet rs = qe.execSelect();        
        return rs;
    }
}
