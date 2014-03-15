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
package MainGraphProcessingScripts.PAFI;

import DataStructures.Fragment;
import Factory.OPMW.OPMWTemplate2Graph;
import IO.Exception.FragmentReaderException;
import IO.Formats.PAFI.FragmentCatalogAndResultsToRDFPAFI;
import PostProcessing.Formats.PAFI.CreateStatisticsFromResultsPAFI;
import PostProcessing.Formats.PAFI.FixDirectionOfFragmentCatalog;
import Static.Configuration;
import java.util.ArrayList;

/**
 * Main step to produce the RDF from the PAFI results.
 * This includes: creating the fragment collection, filtering it, fix the 
 * directionality of the fragments and publish it as rdf.
 * @author Daniel garijo
 */
public class STEP4ProduceRDFFromResultsPAFI{
    public static void main(String[] args) throws FragmentReaderException{
        String fpfile = "PAFI_TOOL\\results\\CollectionInPAFIFormat.fp";
        String pcFile = "PAFI_TOOL\\results\\CollectionInPAFIFormat.pc";
        String tidFile = "PAFI_TOOL\\results\\CollectionInPAFIFormat.tid";
        CreateStatisticsFromResultsPAFI c = new CreateStatisticsFromResultsPAFI("Text analytics", true, false, fpfile, pcFile, tidFile); 
        FragmentCatalogAndResultsToRDFPAFI catalogInRdf = new FragmentCatalogAndResultsToRDFPAFI("PafiRDFCatalog.ttl");
        ArrayList<Fragment> filteredFixedFragmentCatalog = FixDirectionOfFragmentCatalog.fixDirectionOfCatalog(Configuration.getPAFIInputPath()+"CollectionInPAFIFormat", c.getFilteredMultiStepFragments(),c.getFragmentsInTransactions());
        //transformation of the fragment catalog to RDF.
        catalogInRdf.transformFragmentCollectionToRDF(filteredFixedFragmentCatalog);
        //now we find where the fragments have been found and we 
        OPMWTemplate2Graph fullCollection = new OPMWTemplate2Graph("http://wind.isi.edu:8890/sparql");    
        fullCollection.transformDomainToGraph("TextAnalytics");
        catalogInRdf.transformBindingResultsInTemplateCollection(filteredFixedFragmentCatalog, fullCollection.getGraphCollection());
        catalogInRdf.exportToRDFFile("TURTLE");
    }
    
}
