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
package TestsGraphProcessing;

import Static.Traces.QueriesOPMWTraces;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import java.util.HashMap;

/**
 *
 * @author Daniel Garijo
 */
public class TestPrintWingsAvailableDomains {
    //this class actually repeats a bit of the code of the others, so it is not a good practice, but it works.
    public static void test(){
        String repositoryURI="http://wind.isi.edu:8890/sparql";
        try{
            HashMap<String, String> domains = new HashMap<String, String>();
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
                            System.out.println("Domain: "+ontologyURI);
                        }
                    }
                }            
        } 
        }catch(Exception e){
            System.out.println("Error in test TestPrintWingsAvailableDomains. Exception: "+e.getMessage());
        }
    }
    
    private static ResultSet queryRepository(String endpointURL, String queryIn){
        Query query = QueryFactory.create(queryIn);
        QueryExecution qe = QueryExecutionFactory.sparqlService(endpointURL, query);
        ResultSet rs = qe.execSelect();        
        return rs;
    }
    
    public static void main(String[] args){
        test();
    }
    
}
