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
package TestsGraphProcessing.OPMWWings;

import Static.GeneralMethods;
import Static.OPMW.Traces.QueriesOPMWTraces;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import java.util.HashMap;

/**
 * Test designed to retrieve and print the available domains.
 * @author Daniel Garijo
 */
public class Test11PrintWingsAvailableDomains {
    //this class actually repeats a bit of the code of the others, so it is not a good practice, but it works.
    public static int testNumber = 11;
    public static boolean test(){
        String repositoryURI="http://wind.isi.edu:8890/sparql";
        System.out.println("\n\nExecuting test:"+testNumber+" Retrieve and print available domains.");
        try{
            HashMap<String, String> domains = new HashMap<String, String>();
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
                            System.out.println("Domain: "+ontologyURI);
                        }
                    }
                }            
            }
            return true;
        }catch(Exception e){
            System.out.println("Error in test TestPrintWingsAvailableDomains. Exception: "+e.getMessage());
            return false;
        }
    }
    
    
    public static void main(String[] args){
        if(test())System.out.println("Test "+testNumber+" OK");
        else System.out.println("Test "+testNumber+" FAILED");
    }
    
}
