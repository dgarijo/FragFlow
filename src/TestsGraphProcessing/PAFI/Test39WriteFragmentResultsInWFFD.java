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
package TestsGraphProcessing.PAFI;

import DataStructures.Fragment;
import Factory.OPMW.OPMWTemplate2Graph;
import IO.Formats.PAFI.FragmentCatalogAndResultsToRDFPAFI;
import PostProcessing.Formats.PAFI.CreateStatisticsFromResultsPAFI;
import PostProcessing.Formats.PAFI.FixDirectionOfFragmentCatalog;
import Static.Configuration;
import java.util.ArrayList;

/**
 * Given a fragment collection in PAFI format, this test checks if the results 
 * have been written in WFFD. This class does not test if the WFFD is right.
 * It just checks the writing.
 * @author Daniel Garijo
 */
public class Test39WriteFragmentResultsInWFFD {
    public static int testNumber = 39;
    public static boolean test(){
        try{
            System.out.println("\n\nExecuting test:"+testNumber+" Write Fragment results in Wffd");
            String fpfile = "PAFI_TOOL\\results\\CollectionInPAFIFormat.fp";
            String pcFile = "PAFI_TOOL\\results\\CollectionInPAFIFormat.pc";
            String tidFile = "PAFI_TOOL\\results\\CollectionInPAFIFormat.tid";
            CreateStatisticsFromResultsPAFI c = new CreateStatisticsFromResultsPAFI("Text analytics", true, false, fpfile, pcFile, tidFile); 
            FragmentCatalogAndResultsToRDFPAFI catalogInRdf = new FragmentCatalogAndResultsToRDFPAFI("PafiRDFCatalogTest.ttl");
            ArrayList<Fragment> filteredFixedFragmentCatalog = FixDirectionOfFragmentCatalog.fixDirectionOfCatalog(Configuration.getPAFIInputPath()+"CollectionInPAFIFormat", c.getFilteredMultiStepFragments(),c.getFragmentsInTransactions());
            //transformation of the fragment catalog to RDF.
            catalogInRdf.transformFragmentCollectionToRDF(filteredFixedFragmentCatalog);
            //now we find where the fragments have been found and we 
            OPMWTemplate2Graph fullCollection = new OPMWTemplate2Graph("http://wind.isi.edu:8890/sparql");    
            fullCollection.transformDomainToGraph("TextAnalytics");

            //pero animal, si sabes en que templates sale cada fragmento, no le pases la coleccion entera...
            //TO FIX
            catalogInRdf.transformBindingResultsInTemplateCollection(filteredFixedFragmentCatalog, fullCollection.getGraphCollection());
            catalogInRdf.exportToRDFFile("TURTLE");
            return true;
         }catch(Exception e){
            System.out.println("Test TestSaveCollectionAsPAFI WFFD File case failed: "+ e.getMessage());
            return false;
        }
    }
    
    public static void main(String[] args){
        if(test())System.out.println("Test "+testNumber+" OK");
        else System.out.println("Test "+testNumber+" FAILED");
    }
    
}
