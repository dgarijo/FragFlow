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
package TestsGraphProcessing.PARSEMIS;

import Factory.OPMW.OPMWTemplate2GraphWings;
import IO.Formats.PARSEMIS.FragmentCatalogAndResultsToRDFPARSEMIS;
import PostProcessing.Formats.PARSEMIS.CreateStatisticsFromResultsPARSEMIS;

/**
 * Test designed to check if the writer in WFFD works correctly.
 * @author Daniel Garijo
 */
public class Test41WriteFragmentResultsInWFFD {
    public static int testNumber = 41;
    public static boolean test(){
        try{
            System.out.println("\n\nExecuting test:"+testNumber+" Write Fragment results in Wffd (from Parsemis result file)");
            String inputFile = "PARSEMIS_TOOL\\results\\resultsCollectionInParsemisFormat.lg";
            CreateStatisticsFromResultsPARSEMIS c = new CreateStatisticsFromResultsPARSEMIS("Text analytics", true, false, inputFile); 
            FragmentCatalogAndResultsToRDFPARSEMIS catalogInRdf = new FragmentCatalogAndResultsToRDFPARSEMIS("ParsemisRDFCatalogTest.ttl");
            //transformation of the fragment catalog to RDF.
            catalogInRdf.transformFragmentCollectionToRDF(c.getFilteredMultiStepFragments());
            //now we find where the fragments have been found and we 
            OPMWTemplate2GraphWings fullCollection = new OPMWTemplate2GraphWings("http://wind.isi.edu:8890/sparql");    
            fullCollection.transformDomainToGraph("TextAnalytics");

            //pero animal, si sabes en que templates sale cada fragmento, no le pases la coleccion entera...
            //TO FIX
            catalogInRdf.transformBindingResultsInTemplateCollection(c.getFilteredMultiStepFragments(), fullCollection.getGraphCollection());
            catalogInRdf.exportToRDFFile("TURTLE");
            return true;
         }catch(Exception e){
            System.out.println("Test TestSaveCollectionAsPARSEMIS WFFD File case failed: "+ e.getMessage());
            return false;
        }
    }
    
    public static void main(String[] args){
        if(test())System.out.println("Test "+testNumber+" OK");
        else System.out.println("Test "+testNumber+" FAILED");
    }
}
