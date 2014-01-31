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
package MainGraphProcessingScripts.SUBDUE;

import DataStructures.Fragment;
import DataStructures.GraphCollection;
import Factory.Inference.CreateAbstractResource;
import Factory.Inference.CreateHashMapForInference;
import Factory.OPMW.OPMWTemplate2Graph;
import IO.Formats.SUBDUE.FragmentCatalogAndResultsToRDFSUBDUE;
import IO.Formats.SUBDUE.FragmentReaderSUBDUE;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Main script to export the Fragment Catalog determined by SUBDUE to RDF.
 * The model will be exported according to the workflow fragment description 
 * ontology (http://purl.org/net/wf-fd)
 * @author Daniel Garijo
 */
public class STEP4ProduceRDFFromFragmentCatalogAndResultsSUBDUE {
  public static void main(String[] args)   {
      try{
           OPMWTemplate2Graph test = new OPMWTemplate2Graph("http://wind.isi.edu:8890/sparql");    
           test.transformDomainToGraph("TextAnalytics");
           String file = "SUBDUE_TOOL\\results\\Tests\\testResultReduced2";
           String ocFile = "SUBDUE_TOOL\\results\\Tests\\testResultReduced2_occurrences";
           HashMap<String,Fragment> obtainedResults = new FragmentReaderSUBDUE().processResultsAndOccurrencesFiles(file, ocFile);

           //without inference
           FragmentCatalogAndResultsToRDFSUBDUE catalogNoInference = new FragmentCatalogAndResultsToRDFSUBDUE("out30-01-2014.ttl");

           catalogNoInference.transformFragmentCollectionToRDF(obtainedResults);
           catalogNoInference.transformBindingResultsInTemplateCollection(obtainedResults, test.getGraphCollection());
           //test1.transformBindingResultsOfFragmentCollectionInTemplateToRDF(obtainedResults, testGraph);

           catalogNoInference.exportToRDFFile("TURTLE");

           //with inference
           //first we get the replacement hashmap
           String taxonomyFilePath = "src\\TestFiles\\multiDomainOnto.owl"; //we assume the file has already been created.
           OntModel o = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
           InputStream in = FileManager.get().open(taxonomyFilePath);
           o.read(in, null);        
           HashMap replacements = CreateHashMapForInference.createReplacementHashMap(o);

           //we create the abstract collection
           GraphCollection abstractCollection = CreateAbstractResource.createAbstractCollection(test.getGraphCollection(), replacements);

           //we load the new files
           file = "resultsAbstractCatalog24-10-2013";
           ocFile = "resultsAbstractCatalog24-10-2013_occurrences";

           obtainedResults = new FragmentReaderSUBDUE().processResultsAndOccurrencesFiles(file, ocFile);

           //without inference
           FragmentCatalogAndResultsToRDFSUBDUE abstractCatalog = new FragmentCatalogAndResultsToRDFSUBDUE("outAbstract30-01-2014.ttl");

           abstractCatalog.transformFragmentCollectionToRDF(obtainedResults);
           abstractCatalog.transformBindingResultsInTemplateCollection(obtainedResults, abstractCollection);
           abstractCatalog.exportToRDFFile("TURTLE");
        }catch(Exception e){
            System.err.println("Error while saving the catalog to RDF "+e.getMessage());
        }
  }
}
