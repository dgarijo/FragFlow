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
package LONIWfOperationsAndEvaluationEScience2014;

import MainGraphProcessingScripts.PARSEMIS.*;
import DataStructures.Fragment;
import DataStructures.Graph;
import DataStructures.GraphCollection;
import Factory.Inference.CreateAbstractResource;
import Factory.Inference.CreateHashMapForInference;
import Factory.Loni.LoniTemplate2Graph;
import Factory.Loni.LoniTemplateAndGroupings2Graph;
import Factory.OPMW.OPMWTemplate2GraphWings;
import IO.DatasetFilter;
import IO.Exception.FragmentReaderException;
import IO.Formats.PARSEMIS.FragmentCatalogAndResultsToRDFPARSEMIS;
import IO.Formats.PARSEMIS.FragmentCollectionWriterPARSEMIS;
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
import java.util.Map.Entry;

/**
 * Main step to produce the RDF from the PAFI results.
 * This includes: creating the fragment collection, filtering it, fix the 
 * directionality of the fragments and publish it as rdf.
 * @author Daniel Garijo
 */
//esta es la clase para calcular lo que me queda de Ivo y Monthly
public class GSpanFragmentsFilterAndOverlap {
    public static void main(String[] args) throws FragmentReaderException{
//        String repositoryFolder = "C:\\Users\\Monen\\Desktop\\LONIDatasets\\WorkflowBoris\\Pipeline123\\";
        //String repositoryFolder = "C:\\Users\\Monen\\Desktop\\LONIDatasets\\WorkflowIVO\\all\\";
        String repositoryFolder = "C:\\Users\\Monen\\Desktop\\LONIDatasets\\dataset3months(Zhizhong)\\2014-01\\";
        LoniTemplateAndGroupings2Graph test = new LoniTemplateAndGroupings2Graph(repositoryFolder);
        File f = new File(repositoryFolder);
        if(f.isDirectory()){
            File[] files = f.listFiles();
            for(int i=0;i<files.length;i++){
                test.transformToGraph(files[i].getName());
            }
        }
        GraphCollection endCollection = DatasetFilter.removeDuplicates(test.getGraphCollection());
        String inputFile10 = "PARSEMIS_TOOL\\results\\eScience\\corpus3Monthly-10.lg";
        //String inputFile20 = "PARSEMIS_TOOL\\results\\eScience\\corpus3Monthly-20.lg";
        CreateStatisticsFromResultsPARSEMIS c15 = new CreateStatisticsFromResultsPARSEMIS("LONI", true, false, inputFile10); 
//        CreateStatisticsFromResultsPARSEMIS c20 = new CreateStatisticsFromResultsPARSEMIS("LONI", true, false, inputFile20); 
        System.out.println("Number of fragments found: " +c15.getMultiStepFragments().size());        
        System.out.println("Number of filtered multistep fragments found: " +c15.getFilteredMultiStepFragments().size());        
        FragmentAndGroupingOverlapLONI fa15 = new FragmentAndGroupingOverlapLONI();
        fa15.printStatisticsOnFragmentOverlap(c15.getFilteredMultiStepFragments(), endCollection);
        
//        System.out.println("Number of fragments found: " +c20.getMultiStepFragments().size());        
//        System.out.println("Number of filtered multistep fragments found: " +c20.getFilteredMultiStepFragments().size());        
//        FragmentAndGroupingOverlapLONI fa20 = new FragmentAndGroupingOverlapLONI();
//        fa20.printStatisticsOnFragmentOverlap(c20.getFilteredMultiStepFragments(), endCollection);
//        ArrayList<Fragment> aux = c.getFilteredMultiStepFragments();
        //Iterator<Fragment> it = aux.iterator();
        //this is a test to see which are the filtered fragments
//        System.out.println("Filtered fragments");
//        while(it.hasNext()){
//            System.out.println(it.next().getStructureID());
//        }
    }
}
