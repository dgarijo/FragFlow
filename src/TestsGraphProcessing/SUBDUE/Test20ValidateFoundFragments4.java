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
import Factory.OPMW.OPMWTemplate2Graph;
import IO.Formats.OPMW.Graph2OPMWRDFModel;
import IO.Formats.SUBDUE.FragmentReaderSUBDUE;
import PostProcessing.Formats.SUBDUE.FragmentToSPARQLQueryTemplateSUBDUE;
import Static.GeneralMethods;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
/**
 * Test to check that the fragments that only have connections within fragments
 * work (for example, SUB_3 = SUB_2 was informed by SUB_1).
 * 
 * This test is created from artificial results, as we haven't obtained yet such
 * results with the reduced mode.
 * @author Daniel Garijo
 */
public class Test20ValidateFoundFragments4 {
    public static int testNumber = 20;
     public static boolean test(){
        System.out.println("\n\nExecuting test: "+testNumber+". Validator of a particular set"
                + "of fragment results and templates, by manual inspection. In this case the "
                + "test is designed to cover SUB-SUB fragment compatibility");
        //If any of the tests give results that are not okay, then we have a problem
        //with the detection and binding of fragments.
        try{
            OPMWTemplate2Graph test = new OPMWTemplate2Graph("http://wind.isi.edu:8890/sparql");    
            test.transformToGraph("http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI");
            String file = "SUBDUE_TOOL\\results\\Tests\\testResultReducedFake2";
            String ocFile = "SUBDUE_TOOL\\results\\Tests\\testResultReducedFake2_occurrences";
            HashMap<String,Fragment> obtainedResults = new FragmentReaderSUBDUE(file, ocFile).getFragmentCatalogAsHashMap();
            Iterator<String> fragments = obtainedResults.keySet().iterator();
            //it would be nice to just send the relevant fragments    
            FragmentToSPARQLQueryTemplateSUBDUE qr = new FragmentToSPARQLQueryTemplateSUBDUE();
            OntModel o2 = Graph2OPMWRDFModel.graph2OPMWTemplate(test.getGraphCollection().getGraphs().get(0));   
            while(fragments.hasNext()){
                Fragment f = obtainedResults.get(fragments.next());
                if(f.isMultiStepStructure()){//to ensure it is a meaningful fragment
                    String currentQuery = qr.createQueryFromFragment(f, "http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI");
                    ResultSet rs = GeneralMethods.queryLocalRepository(o2, currentQuery);
//                    System.out.println(currentQuery);
                    int numberOfTimes = 0;
                    while(rs.hasNext()){
                        rs.nextSolution();
                        numberOfTimes++;
                    }
                    //step here to delete duplicated bindings (in some remote cases it could happen)
                    System.out.println("Number of occurrences of fragment "+f.getStructureID()+" in graph: "+numberOfTimes);
                    /* In this template:
                     * SUB_1 must be found 2 times
                     * SUB_2 must be found 2 times
                     * SUB_3 must be found 2 times
                     * SUB_4 must be found 2 time
                     * SUB_5 must be found 2 times
                     * SUB_6 must be found 0 times
                     * If all this is true, then the test is right
                     */
                    if(f.getStructureID().equals("SUB_1")&&numberOfTimes!=2)return false;
                    if(f.getStructureID().equals("SUB_2")&&numberOfTimes!=2)return false;
                    if(f.getStructureID().equals("SUB_3")&&numberOfTimes!=2)return false;
                    if(f.getStructureID().equals("SUB_4")&&numberOfTimes!=2)return false;
                    if(f.getStructureID().equals("SUB_5")&&numberOfTimes!=2)return false;
                    if(f.getStructureID().equals("SUB_6")&&numberOfTimes!=0)return false;
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
