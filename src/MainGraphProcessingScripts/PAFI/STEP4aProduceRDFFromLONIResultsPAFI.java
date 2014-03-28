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
import DataStructures.GraphCollection;
import Factory.Inference.CreateAbstractResource;
import Factory.Inference.CreateHashMapForInference;
import Factory.Loni.LoniTemplate2Graph;
import Factory.OPMW.OPMWTemplate2Graph;
import IO.Exception.FragmentReaderException;
import IO.Formats.PAFI.FragmentCatalogAndResultsToRDFPAFI;
import PostProcessing.Formats.PAFI.CreateStatisticsFromResultsPAFI;
import PostProcessing.Formats.PAFI.FixDirectionOfFragmentCatalog;
import Static.Configuration;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Main step to produce the RDF from the PAFI results.
 * This includes: creating the fragment collection, filtering it, fix the 
 * directionality of the fragments and publish it as rdf.
 * @author Daniel garijo
 */
public class STEP4aProduceRDFFromLONIResultsPAFI{
    public static void main(String[] args) throws FragmentReaderException{
        String fpfile = "PAFI_TOOL\\results\\LoniDatasetFiltered.fp";
        String pcFile = "PAFI_TOOL\\results\\LoniDatasetFiltered.pc";
        String tidFile = "PAFI_TOOL\\results\\LoniDatasetFiltered.tid";
        CreateStatisticsFromResultsPAFI c = new CreateStatisticsFromResultsPAFI("Text analytics", true, false, fpfile, pcFile, tidFile); 
        FragmentCatalogAndResultsToRDFPAFI catalogInRdf = new FragmentCatalogAndResultsToRDFPAFI("PafiLONIRDFCatalog.ttl");
        ArrayList<Fragment> filteredFixedFragmentCatalog = FixDirectionOfFragmentCatalog.fixDirectionOfCatalogWithOPMWTemplates(Configuration.getPAFIInputPath()+"LONIInPAFIFormat", c.getFilteredMultiStepFragments(),c.getFragmentsInTransactions(), false);
        //transformation of the fragment catalog to RDF.
        catalogInRdf.transformFragmentCollectionToRDF(filteredFixedFragmentCatalog);
        test if the transformation is ok. Change the direction so as to be able to fix it (ahora coge opmw, eso esta mal)
        //now the instances
        String loniDatasetFolder = "LONI_dataset\\";
        File f = new File(loniDatasetFolder);
        LoniTemplate2Graph fullCollection = new LoniTemplate2Graph(loniDatasetFolder);
        if(f.isDirectory()){
            File[] files = f.listFiles();
            for(int i=0;i<files.length;i++){
                fullCollection.transformToGraph(files[i].getName());
            }
            
        }
        //pero animal, si sabes en que templates sale cada fragmento, no le pases la coleccion entera...
        //TO FIX
        me parece que algo aqui no mola nathin
        catalogInRdf.transformBindingResultsInTemplateCollection(filteredFixedFragmentCatalog, fullCollection.getGraphCollection());
        catalogInRdf.exportToRDFFile("TURTLE");
    }
    
}
