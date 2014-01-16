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
package TestsGraphProcessing;

import DataStructures.Fragment;
import DataStructures.Graph;
import DataStructures.GraphCollection;
import Factory.Inference.CreateAbstractResource;
import Factory.Inference.CreateHashMapForInference;
import Factory.OPMWTemplate2GraphProcessor;
import IO.Formats.SubdueFragmentReader;
import PostProcessing.SUBDUEFragmentRecongnizer;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Daniel Garijo
 */
public class TestValidateFoundFragmentsAbstract {
    to do
    public static int testNumber = 17;
    public static boolean test(){
        System.out.println("\n\nExecuting test: "+testNumber+". Validator of a particualr set"
                + "of fragment results and templates, by manual inspection.");
        //If any of the tests give results that are not okay, then we have a problem
        //with the detection and binding of fragments.
        try{
            //get one graph from the workflow collection
            //tests on a template
            OPMWTemplate2GraphProcessor test = new OPMWTemplate2GraphProcessor("http://wind.isi.edu:8890/sparql");
    //        test.transformToSubdueGraph("http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_SINGLE_");
            test.transformToGraph("http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI");
    //        test.transformToSubdueGraph("http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLUSTERING");
    //        test.transformToSubdueGraph("http://www.opmw.org/export/resource/WorkflowTemplate/FEATURESELECTION");

            Graph testGraph = test.getGraphCollection().getGraphs().get(0);
            testGraph.putReducedNodesInAdjacencyMatrix();
            //get one final result (or more)
    //        String file = "SUBDUE_TOOL\\results\\Tests\\testResultReducedFake";
    //        String file = "SUBDUE_TOOL\\results\\Tests\\testResultReduced2";
    //        String ocFile = "SUBDUE_TOOL\\results\\Tests\\testResultReduced2_occurrences";
            String file = "resultsAbstractCatalog24-10-2013";
            String ocFile = "resultsAbstractCatalog24-10-2013_occurrences";
            HashMap<String,Fragment> obtainedResults = new SubdueFragmentReader().processResultsAndOccurrencesFiles(file, ocFile);

            //with inference
           //first we get the replacement hashmap
           String taxonomyFilePath = "src\\TestFiles\\multiDomainOnto.owl"; //we assume the file has already been created.
           OntModel o = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
           InputStream in = FileManager.get().open(taxonomyFilePath);
           o.read(in, null);        
           HashMap replacements = CreateHashMapForInference.createReplacementHashMap(o);

           //we create the abstract collection
           GraphCollection abstractCollection = CreateAbstractResource.createAbstractCollection(test.getGraphCollection(), replacements);
            //check whether the fragment is found properly or not.
            //we ask for the first one (which is included)
    //        System.out.println("This test should be true: .... "+obtainedResults.get("SUB_1").isPartOfGraph(testGraph));
            //then we try with another one that is composed and also included
    //        System.out.println("This test should be true: .... "+obtainedResults.get("SUB_2").isPartOfGraph(testGraph));
            //then we try with another one which is even more complex to see wheter it's included
    //        System.out.println("This test should be true: .... "+obtainedResults.get("SUB_8").isPartOfGraph(testGraph));
            //then we get one that overlaps partly but one of the subparts is not included.
    //        System.out.println("This test should be false: .... "+obtainedResults.get("SUB_3").isPartOfGraph(testGraph));
    //        //then we try the same with one where the core parts are not even included
    //        System.out.println("This test should be false: .... "+obtainedResults.get("SUB_4").isPartOfGraph(testGraph));

    //        System.out.println("This test should be false: .... "+obtainedResults.get("SUB_6").isPartOfGraph(testGraph));
            //should do a test where the fragment is included but not in the same sequence.

            //tests on a sequence.
    //        FinalResult f = obtainedResults.get("SUB_7");//SUB_3
    ////        FinalResult f = obtainedResults.get("SUB_7");
    //        ArrayList<HashMap<String,String>> b = f.generateBindingsFromFragmentInGraph(f, testGraph);
    //        b = f.validateBindings(b, testGraph);
    //        System.out.println("Bindings size (number of occurrences of fragment in graph): "+b.size());

            Iterator<String> fragments = obtainedResults.keySet().iterator();
            //it would be nice to just send the relevant fragments    
            SUBDUEFragmentRecongnizer fr = new SUBDUEFragmentRecongnizer();
            while(fragments.hasNext()){
                Fragment f = obtainedResults.get(fragments.next());
                System.out.println("Size of fragment "+f.getStructureID()+" is "+ f.getSize());
                if(f.isMultiStepStructure()){
                    ArrayList<HashMap<String,String>> b = fr.generateBindingsFromFragmentInGraph(f, testGraph);
                    b = fr.validateBindings(b, testGraph, f);
                    //step here to delete duplicated bindings (in some remote cases it could happen)
                    System.out.println("Number of occurrences of fragment "+f.getStructureID()+" in graph: "+b.size());
                }
            }        
            return true;
        }catch(Exception e){
            System.out.println("Error executing test. Exception: "+e.getMessage());
            return false;
        }
    }
    public static void main(String[] args){
        if(test())System.out.println("Test "+testNumber+" OK");
        else System.out.println("Test "+testNumber+" FAILED");        
    }
}
