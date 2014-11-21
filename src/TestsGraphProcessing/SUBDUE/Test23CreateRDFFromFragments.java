/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

import DataStructures.Fragment;
import DataStructures.GraphCollection;
import Factory.Inference.CreateAbstractResource;
import Factory.Inference.CreateHashMapForInference;
import Factory.OPMW.OPMWTemplate2GraphWings;
import IO.Formats.SUBDUE.FragmentCatalogAndResultsToRDFSUBDUE;
import PostProcessing.Formats.SUBDUE.CreateStatisticsFromResultsSUBDUE;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Test to check whether the fragment catalog detected can be translated to RDF
 * successfully or not. 2 tests are performed here. First with the non abstract 
 * results obtained and then with the abstract results. This test embedds 
 * implicitly many others (read fragments, create abstractions, generate RDF, 
 * save results to a File).
 * @author Daniel Garijo
 */
public class Test23CreateRDFFromFragments {
    public static int testNumber = 23;
    public static boolean test(){
        System.out.println("\n\nExecuting test:"+testNumber+" Creating RDF from fragment catalog");
        try{            
           OPMWTemplate2GraphWings test = new OPMWTemplate2GraphWings("http://wind.isi.edu:8890/sparql");    
           test.transformDomainToGraph("TextAnalytics");
           String file = "SUBDUE_TOOL\\results\\Tests\\testResultReduced2";
           String ocFile = "SUBDUE_TOOL\\results\\Tests\\testResultReduced2_occurrences";
           CreateStatisticsFromResultsSUBDUE aux = new CreateStatisticsFromResultsSUBDUE("text analytics",
                    "MDL", true, false,file, ocFile);
           ArrayList<Fragment> obtainedResults = aux.getFilteredMultiStepFragments();
//           HashMap<String,Fragment> obtainedResults = new FragmentReaderSUBDUE(file, ocFile).getFragmentCatalogFromAlgorithmResultFiles();

           //without inference
           FragmentCatalogAndResultsToRDFSUBDUE catalogNoInference = new FragmentCatalogAndResultsToRDFSUBDUE("out29-01-2014.ttl");

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
           aux = new CreateStatisticsFromResultsSUBDUE("text analytics",
                    "MDL", true, false,file, ocFile);
           obtainedResults = aux.getFilteredMultiStepFragments();
//           obtainedResults = new FragmentReaderSUBDUE(file, ocFile).getFragmentCatalogFromAlgorithmResultFiles();

           //without inference
           FragmentCatalogAndResultsToRDFSUBDUE abstractCatalog = new FragmentCatalogAndResultsToRDFSUBDUE("outAbstract29-01-2014.ttl");

           abstractCatalog.transformFragmentCollectionToRDF(obtainedResults);
           abstractCatalog.transformBindingResultsInTemplateCollection(obtainedResults, abstractCollection);
           abstractCatalog.exportToRDFFile("TURTLE");        
            return true;
        }catch(Exception e){
            System.out.println("Error in test PostProcessing. Exception: "+e.getMessage());
            return false;
        }
    }
    public static void main(String[] args){
        if(test())System.out.println("Test "+testNumber+" OK");
        else System.out.println("Test "+testNumber+" FAILED");
    }
}
