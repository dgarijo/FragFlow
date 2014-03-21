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

import Factory.OPMW.OPMWTemplate2Graph;
import IO.Exception.FragmentReaderException;
import IO.Formats.PARSEMIS.FragmentCatalogAndResultsToRDFPARSEMIS;
import PostProcessing.Formats.PARSEMIS.CreateStatisticsFromResultsPARSEMIS;

/**
 * Main step to produce the RDF from the PAFI results.
 * This includes: creating the fragment collection, filtering it, fix the 
 * directionality of the fragments and publish it as rdf.
 * @author Daniel Garijo
 */
public class STEP4ProduceRDFFromResultsPARSEMIS {
    public static void main(String[] args) throws FragmentReaderException{
        String inputFile = "PARSEMIS_TOOL\\results\\resultsCollectionInParsemisFormat.lg";
        CreateStatisticsFromResultsPARSEMIS c = new CreateStatisticsFromResultsPARSEMIS("Text analytics", true, false, inputFile); 
        FragmentCatalogAndResultsToRDFPARSEMIS aux = new FragmentCatalogAndResultsToRDFPARSEMIS("ParsemisRDFFragmentCatalog.ttl");           
        //transformation of the fragment catalog to RDF.
        aux.transformFragmentCollectionToRDF(c.getFilteredMultiStepFragments());

        OPMWTemplate2Graph fullCollection = new OPMWTemplate2Graph("http://wind.isi.edu:8890/sparql");    
        fullCollection.transformDomainToGraph("TextAnalytics");
        aux.transformBindingResultsInTemplateCollection(c.getFilteredMultiStepFragments(), fullCollection.getGraphCollection());            
        
        //pero animal, si sabes en que templates sale cada fragmento, no le pases la coleccion entera...
        //TO FIX
        aux.exportToRDFFile("TURTLE");
        
        //what about the abstract collection? (missing completely)
        //TO DO
    }
}
