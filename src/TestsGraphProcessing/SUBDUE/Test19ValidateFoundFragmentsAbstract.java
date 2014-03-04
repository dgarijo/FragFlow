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
import DataStructures.Graph;
import DataStructures.GraphCollection;
import Factory.Inference.CreateAbstractResource;
import Factory.Inference.CreateHashMapForInference;
import Factory.OPMW.OPMWTemplate2Graph;
import IO.Formats.OPMW.Graph2OPMWRDFModel;
import IO.Formats.SUBDUE.FragmentReaderSUBDUE;
import PostProcessing.Formats.SUBDUE.FragmentToSPARQLQueryTemplateSUBDUE;
import Static.GeneralMethods;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Test designed to check if the abstract fragments are found correctly.
 * We have to make a eparate test to check if the abstraction part indeed works.
 * @author Daniel Garijo
 */
public class Test19ValidateFoundFragmentsAbstract {
    public static int testNumber = 19;
    public static boolean test(){
        System.out.println("\n\nExecuting test: "+testNumber+". Validator of a particualr set"
                + "of fragment results and templates, by manual inspection.");
        //If any of the tests give results that are not okay, then we have a problem
        //with the detection and binding of fragments.
        try{
            //get one graph from the workflow collection
            //tests on a template
            OPMWTemplate2Graph test = new OPMWTemplate2Graph("http://wind.isi.edu:8890/sparql");
    //        test.transformToSubdueGraph("http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_SINGLE_");
            test.transformToGraph("http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI");
    //        test.transformToSubdueGraph("http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLUSTERING");
    //        test.transformToSubdueGraph("http://www.opmw.org/export/resource/WorkflowTemplate/FEATURESELECTION");

            String file = "SUBDUE_TOOL\\results\\Tests\\resultsAbstractCatalog24-10-2013";
            String ocFile = "SUBDUE_TOOL\\results\\Tests\\resultsAbstractCatalog2-24-10-2013_occurrences";
            HashMap<String,Fragment> obtainedResults = new FragmentReaderSUBDUE(file, ocFile).getFragmentCatalogFromAlgorithmResultFiles();
            Iterator<String> fragments = obtainedResults.keySet().iterator();
            
            //with inference
           //first we get the replacement hashmap
           String taxonomyFilePath = "src\\TestFiles\\multiDomainOnto.owl"; //we assume the file has already been created.
           OntModel o = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
           InputStream in = FileManager.get().open(taxonomyFilePath);
           o.read(in, null);        
           HashMap replacements = CreateHashMapForInference.createReplacementHashMap(o);
//           Graph testGraph = test.getGraphCollection().getGraphs().get(0);
           //we create the abstract collection
           
           GraphCollection abstractCollection = CreateAbstractResource.createAbstractCollection(test.getGraphCollection(), replacements);
           Graph abstractG = abstractCollection.getGraphs().get(0);
           OntModel o2 = Graph2OPMWRDFModel.graph2OPMWTemplate(abstractG);   
            FragmentToSPARQLQueryTemplateSUBDUE qr = new FragmentToSPARQLQueryTemplateSUBDUE();
            while(fragments.hasNext()){
                Fragment f = obtainedResults.get(fragments.next());
                if(f.isMultiStepStructure()){//to ensure it is a meaningful fragment

                        String currentQuery = qr.createQueryFromFragment(f, "http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI");
//                        System.out.println(currentQuery);
                        ResultSet rs = GeneralMethods.queryLocalRepository(o2, currentQuery);
                        
                        int numberOfTimes = 0;
                        while(rs.hasNext()){
                            QuerySolution qs = rs.nextSolution();
                            numberOfTimes++;
                        }
                        //step here to delete duplicated bindings (in some remote cases it could happen)
                        System.out.println("Number of occurrences of fragment "+f.getStructureID()+" in graph: "+numberOfTimes);
                    /* In this template:
                     * SUB_1 must be found 2 times
                     * SUB_2 must be found 4 times
                     * SUB_3 must be found 0 times
                     * SUB_4 must be found 0 time
                     * SUB_5 must be found 2 times
                     * SUB_6 must be found 2 times
                     * If all this is true, then the test is right
                     */
                    if(f.getStructureID().equals("SUB_1")&&numberOfTimes!=2)return false;
                    if(f.getStructureID().equals("SUB_2")&&numberOfTimes!=4)return false;
                    if(f.getStructureID().equals("SUB_3")&&numberOfTimes!=0)return false;
                    if(f.getStructureID().equals("SUB_4")&&numberOfTimes!=0)return false;
                    if(f.getStructureID().equals("SUB_5")&&numberOfTimes!=2)return false;
                    if(f.getStructureID().equals("SUB_6")&&numberOfTimes!=2)return false;
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
