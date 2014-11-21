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

import DataStructures.Fragment;
import DataStructures.Graph;
import DataStructures.GraphCollection;
import Factory.Inference.CreateAbstractResource;
import Factory.Inference.CreateHashMapForInference;
import Factory.Loni.LoniTemplate2Graph;
import Factory.OPMW.OPMWTemplate2GraphWings;
import IO.Exception.FragmentReaderException;
import IO.Formats.PARSEMIS.FragmentCatalogAndResultsToRDFPARSEMIS;
import PostProcessing.Formats.PARSEMIS.CreateStatisticsFromResultsPARSEMIS;
import Static.GeneralConstants;
import Static.GeneralMethods;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Main step to produce the RDF from the PAFI results.
 * This includes: creating the fragment collection, filtering it, fix the 
 * directionality of the fragments and publish it as rdf.
 * @author Daniel Garijo
 */
public class STEP4aProduceRDFFromLONIResultsPARSEMIS {
    public static void main(String[] args) throws FragmentReaderException{
        String inputFile = "PARSEMIS_TOOL\\results\\resultsLoniFullDatasetFiltered.lg";
        CreateStatisticsFromResultsPARSEMIS c = new CreateStatisticsFromResultsPARSEMIS("Text analytics", true, false, inputFile); 
        FragmentCatalogAndResultsToRDFPARSEMIS catalogToRDF = new FragmentCatalogAndResultsToRDFPARSEMIS("ParsemisLONIRDFFragmentCatalog.ttl");           
        //generalize the fragments types (workaround)
        ArrayList<Fragment> obtainedResults = c.getFilteredMultiStepFragments();
        Iterator<Fragment> itF = obtainedResults.iterator();
        while(itF.hasNext()){
            GeneralMethods.setTypesOfCurrentFragment(itF.next());
        }

        String loniDatasetFolder = "LONI_dataset\\";
        File f = new File(loniDatasetFolder);
        LoniTemplate2Graph fullCollection = new LoniTemplate2Graph(loniDatasetFolder);
        if(f.isDirectory()){
            File[] files = f.listFiles();
            for(int i=0;i<files.length;i++){
                fullCollection.transformToGraph(files[i].getName());
            }
        }
        //we need to transform some types and names to URIs in order to properly find things
           //(the fragments have been properly treated when fixing the directionality)
           ArrayList<Graph> collection = fullCollection.getGraphCollection().getGraphs();
           Iterator<Graph> it = collection.iterator();
           while (it.hasNext()){
               Graph currentG = it.next();
               GeneralMethods.setStringTypesToURIs(currentG);
               if(!currentG.getName().startsWith("http://")){
                   currentG.setName(GeneralConstants.PREFIX_FOR_RDF_GENERATION+GeneralMethods.encode(currentG.getName()));
               }
           }
        //transformation of the fragment catalog to RDF.
        catalogToRDF.transformFragmentCollectionToRDF(obtainedResults);
        
        //pero animal, si sabes en que templates sale cada fragmento, no le pases la coleccion entera...
        //TO FIX
        catalogToRDF.transformBindingResultsInTemplateCollection(obtainedResults, fullCollection.getGraphCollection());            
        catalogToRDF.exportToRDFFile("TURTLE");
        
        
    }
}
