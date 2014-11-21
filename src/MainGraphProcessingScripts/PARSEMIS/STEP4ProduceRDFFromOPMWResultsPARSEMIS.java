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
package MainGraphProcessingScripts.PARSEMIS;

import DataStructures.GraphCollection;
import Factory.Inference.CreateAbstractResource;
import Factory.Inference.CreateHashMapForInference;
import Factory.OPMW.OPMWTemplate2GraphWings;
import IO.Exception.FragmentReaderException;
import IO.Formats.PARSEMIS.FragmentCatalogAndResultsToRDFPARSEMIS;
import PostProcessing.Formats.PARSEMIS.CreateStatisticsFromResultsPARSEMIS;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Main step to produce the RDF from the PAFI results.
 * This includes: creating the fragment collection, filtering it, fix the 
 * directionality of the fragments and publish it as rdf.
 * @author Daniel Garijo
 */
public class STEP4ProduceRDFFromOPMWResultsPARSEMIS {
    public static void main(String[] args) throws FragmentReaderException{
        String inputFile = "PARSEMIS_TOOL\\results\\resultsCollectionInParsemisFormat.lg";
        CreateStatisticsFromResultsPARSEMIS c = new CreateStatisticsFromResultsPARSEMIS("Text analytics", true, false, inputFile); 
        FragmentCatalogAndResultsToRDFPARSEMIS catalogToRDF = new FragmentCatalogAndResultsToRDFPARSEMIS("ParsemisRDFFragmentCatalog.ttl");           
        //transformation of the fragment catalog to RDF.
        catalogToRDF.transformFragmentCollectionToRDF(c.getFilteredMultiStepFragments());

        OPMWTemplate2GraphWings fullCollection = new OPMWTemplate2GraphWings("http://wind.isi.edu:8890/sparql");    
        fullCollection.transformDomainToGraph("TextAnalytics");
        catalogToRDF.transformBindingResultsInTemplateCollection(c.getFilteredMultiStepFragments(), fullCollection.getGraphCollection());            
        
        //pero animal, si sabes en que templates sale cada fragmento, no le pases la coleccion entera...
        //TO FIX
        catalogToRDF.exportToRDFFile("TURTLE");
        
        //Abstract collection
        String taxonomyFilePath = "src\\TestFiles\\multiDomainOnto.owl"; //we assume the file has already been created.
        OntModel o = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        InputStream in = FileManager.get().open(taxonomyFilePath);
        o.read(in, null);        
        HashMap replacements = CreateHashMapForInference.createReplacementHashMap(o);
        GraphCollection abstractCollection = CreateAbstractResource.createAbstractCollection(fullCollection.getGraphCollection(), replacements);
        
        inputFile = "PARSEMIS_TOOL\\results\\resultsCollectionInParsemisFormatABSTRACT.lg";
        
        c = new CreateStatisticsFromResultsPARSEMIS("Text analytics", true, false, inputFile); 
        catalogToRDF = new FragmentCatalogAndResultsToRDFPARSEMIS("ParsemisRDFCatalogAbstract.ttl");
        //transformation of the fragment catalog to RDF.
        catalogToRDF.transformFragmentCollectionToRDF(c.getFilteredMultiStepFragments());
        catalogToRDF.transformBindingResultsInTemplateCollection(c.getFilteredMultiStepFragments(), abstractCollection);
        catalogToRDF.exportToRDFFile("TURTLE");
    }
}
