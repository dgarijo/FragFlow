
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

import Static.Traces.ConstantsOPMWTraces;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Simple test for downloading the types of all the WorkflowTemplateProcesses.
 * The types are filtered to only obtain the ones we are interested in (i.e., 
 * those with the taxonomy type). The test does the query and prints a ttl to 
 * the desired destination.
 * @author Daniel Garijo
 */
public class TestSaveTypesOfProcessesToFile {
    
    public static void test(){
        String queryIn="select distinct ?process ?type where {\n"+
                "?process a <http://www.opmw.org/ontology/WorkflowTemplateProcess>.\n"+
                "?process a ?type.\n"+
                "FILTER regex(?type , \"http://www.isi.edu/ac/\").\n" +
           " }";
        String destination = "types.ttl";
        Query query = QueryFactory.create(queryIn);
        //System.out.println(queryIn);
        QueryExecution qe = QueryExecutionFactory.sparqlService("http://wind.isi.edu:8890/sparql", query);
        ResultSet rs = qe.execSelect();
        
        OntModel o = ModelFactory.createOntologyModel();
        while (rs.hasNext()){
            QuerySolution qs = rs.next();
//            System.out.print("p: "+qs.getResource("?process"));
//            System.out.println("; t: "+qs.getResource("?type"));
            o.createIndividual(qs.getResource("?process").getURI(), qs.getResource("?type"));
        }
        OutputStream out;
        try {
            out = new FileOutputStream(destination);
            o.write(out,"TURTLE");
        } catch (FileNotFoundException ex) {
            System.out.println("Error while writing the model to file "+ex.getMessage());
        }
    }
    

public static void main(String[] args){
    test();
}

}
