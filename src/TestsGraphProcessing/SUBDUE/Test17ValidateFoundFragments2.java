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
import IO.Formats.SUBDUE.FragmentCollectionReaderSUBDUE;
import PostProcessing.Formats.SUBDUE.FragmentToSPARQLQueryTemplateSUBDUE;
import Static.GeneralMethods;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
/**
 * This test is a manual validation of the fragment recognizer 
 * (as the TestValidateFoundFragments1 test). In this case the template chosen
 * overlaps partially with some fragments, in order to check that the detection
 * is made properly.
 * @author Daniel Garijo
 */
public class Test17ValidateFoundFragments2 {    
    public static int testNumber = 17;
    public static boolean test(){
        System.out.println("\n\nExecuting test: "+testNumber+". Validator of a particualr set "
                + "of fragment results and templates, by manual inspection.");
        //If any of the tests give results that are not okay, then we have a problem
        //with the detection and binding of fragments.
        try{
            OPMWTemplate2Graph test = new OPMWTemplate2Graph("http://wind.isi.edu:8890/sparql");    
            test.transformToGraph("http://www.opmw.org/export/resource/WorkflowTemplate/FEATURESELECTION");
    
            String file = "SUBDUE_TOOL\\results\\Tests\\testResultReduced2";
            String ocFile = "SUBDUE_TOOL\\results\\Tests\\testResultReduced2_occurrences";
            HashMap<String,Fragment> obtainedResults = new FragmentCollectionReaderSUBDUE(file, ocFile).getFragmentCatalogAsHashMap();
            Iterator<String> fragments = obtainedResults.keySet().iterator();
            //it would be nice to just send the relevant fragments    
            FragmentToSPARQLQueryTemplateSUBDUE qr = new FragmentToSPARQLQueryTemplateSUBDUE();
            OntModel o2 = Graph2OPMWRDFModel.graph2OPMWTemplate(test.getGraphCollection().getGraphs().get(0));   
            while(fragments.hasNext()){
                Fragment f = obtainedResults.get(fragments.next());
                if(f.isMultiStepFragment()){//to ensure it is a meaningful fragment
                    String currentQuery = qr.createQueryFromFragment(f, "http://www.opmw.org/export/resource/WorkflowTemplate/FEATURESELECTION");
                    ResultSet rs = GeneralMethods.queryLocalRepository(o2, currentQuery);
                    int numberOfTimes = 0;
                    while(rs.hasNext()){
                        rs.nextSolution();
                        numberOfTimes++;
                    }
                    //step here to delete duplicated bindings (in some remote cases it could happen)
                    System.out.println("Number of occurrences of fragment "+f.getStructureID()+" in graph: "+numberOfTimes);
                    /* In this template:
                     * SUB_1 must be found 1 times
                     * SUB_2 must be found 1 times
                     * SUB_3 must be found 0 times
                     * SUB_4 must be found 0 time
                     * SUB_5 must be found 0 times
                     * SUB_6 must be found 0 times
                     * SUB_7 must be found 0 time
                     * SUB_8 must be found 1 times
                     * SUB_9 must be found 1 times
                     * If all this is true, then the test is right
                     */
                    if(f.getStructureID().equals("SUB_1")&&numberOfTimes!=1)return false;
                    if(f.getStructureID().equals("SUB_2")&&numberOfTimes!=1)return false;
                    if(f.getStructureID().equals("SUB_3")&&numberOfTimes!=0)return false;
                    if(f.getStructureID().equals("SUB_4")&&numberOfTimes!=0)return false;
                    if(f.getStructureID().equals("SUB_5")&&numberOfTimes!=0)return false;
                    if(f.getStructureID().equals("SUB_6")&&numberOfTimes!=0)return false;
                    if(f.getStructureID().equals("SUB_7")&&numberOfTimes!=0)return false;
                    if(f.getStructureID().equals("SUB_8")&&numberOfTimes!=1)return false;
                    if(f.getStructureID().equals("SUB_9")&&numberOfTimes!=1)return false;
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
