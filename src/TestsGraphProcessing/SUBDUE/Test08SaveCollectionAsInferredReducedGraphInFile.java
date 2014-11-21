
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
package TestsGraphProcessing.SUBDUE;

import Factory.Inference.CreateHashMapForInference;
import Factory.OPMW.OPMWTemplate2GraphWings;
import IO.Formats.SUBDUE.GraphCollectionWriterSUBDUE;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Test to get a full collection of graphs from a domain, reduce it and 
 * apply inference to produce the full collection inferred.
 * @author Daniel Garijo
 */
public class Test08SaveCollectionAsInferredReducedGraphInFile {
    public static int testNumber = 8;
    public static boolean test(){        
        try{
            System.out.println("\n\nExecuting test:"+testNumber+" Get a full collection of graphs from a "
                    + "domain, reduce it and apply inference to produce the full collection inferred");
            String endpoint = "http://wind.isi.edu:8890/sparql" ;
            String domain = "TextAnalytics";
            String taxonomyFilePath = "src\\TestFiles\\multiDomainOnto.owl"; //we assume the file has already been created.
            String outputFilePath = "TestAbstractFragmentGraphCollection";
            //process the domain
            OPMWTemplate2GraphWings tp = new OPMWTemplate2GraphWings(endpoint);
            tp.transformDomainToGraph(domain);
            if(tp.getGraphCollection().getNumberOfSubGraphs()>1){
                //create the hashmap for replacements with the taxonomy
                OntModel o = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
                InputStream in = FileManager.get().open(taxonomyFilePath);
                o.read(in, null);        
                HashMap replacements = CreateHashMapForInference.createReplacementHashMap(o);
                if(replacements.isEmpty()) return false;
                //write the full file abstracted.
                GraphCollectionWriterSUBDUE writer = new GraphCollectionWriterSUBDUE();
                writer.writeReducedGraphsToFile(tp.getGraphCollection(),outputFilePath ,replacements);
                //if the writes the collection and that collection is not empty, then the test is OK
                return true;
            }
            return false;
        }catch(Exception e){
            System.out.println("Test TestSaveCollectionAsReducedlGraphInFile case failed: "+ e.getMessage());
            return false;
        }
    }
    
    public static void main(String[] args){
        if(test())System.out.println("Test "+testNumber+" OK");
        else System.out.println("Test "+testNumber+" FAILED");
    }
}
