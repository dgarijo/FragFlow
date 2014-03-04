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
import IO.Formats.SUBDUE.FragmentCatalogAndResultsToRDFSUBDUE;
import IO.Formats.SUBDUE.FragmentReaderSUBDUE;
import Static.GeneralMethods;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.HashMap;
/**
 * Test designed to check whether the RDF generated by the program is correct 
 * or not. In order to achieve this, we must generate auxiliar queries and test
 * that the results are ok.
 * @author Daniel Garijo
 */
public class Test25ValidateRDFWFFDFragmentInstances {
    public static int testNumber = 25;
    public static boolean test(){        
       try{
           System.out.println("\n\nExecuting test:"+testNumber+" Testing the RDF created from the catalog (instances)");
           OPMWTemplate2Graph test = new OPMWTemplate2Graph("http://wind.isi.edu:8890/sparql");    
           //we only test 1 template, it is not necessary to test the full catalog
           test.transformToGraph("http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI");
           String file = "SUBDUE_TOOL\\results\\Tests\\testResultReduced2";
           String ocFile = "SUBDUE_TOOL\\results\\Tests\\testResultReduced2_occurrences";
           HashMap<String,Fragment> obtainedResults = new FragmentReaderSUBDUE(file, ocFile).getFragmentCatalogFromAlgorithmResultFiles();

           //without inference
           FragmentCatalogAndResultsToRDFSUBDUE catalogNoInference = new FragmentCatalogAndResultsToRDFSUBDUE("outTEST.ttl");

           catalogNoInference.transformFragmentCollectionToRDF(obtainedResults);
           catalogNoInference.transformBindingResultsInTemplateCollection(obtainedResults, test.getGraphCollection());           

           OntModel repo = catalogNoInference.getRepository();
           //Question 1: SUB_1, SUB_2 and SUB_5 must be part of the template  
           String queryInstances1 = 
                   "ASK {"
                   + "?frag1 a <http://purl.org/net/wf-fd#DetectedResultWorkflowFragment>."
                   + "?frag1 <http://purl.org/dc/terms/title> \"SUB_1\"."
                   + "?frag1 <http://purl.org/dc/terms/isPartOf> <http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI>."
                   + "?frag2 a <http://purl.org/net/wf-fd#DetectedResultWorkflowFragment>."
                   + "?frag2 <http://purl.org/dc/terms/title> \"SUB_2\"."
                   + "?frag2 <http://purl.org/dc/terms/isPartOf> <http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI>."
                   + "?frag5 a <http://purl.org/net/wf-fd#DetectedResultWorkflowFragment>."
                   + "?frag5 <http://purl.org/dc/terms/title> \"SUB_5\"."
                   + "?frag5 <http://purl.org/dc/terms/isPartOf> <http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI>."
                   + "}";           
           //Question 2: SUB_1, SUB_2 and SUB_5 must have 2 bindings each to the template.
           String queryInstances2 = 
                   "ASK {"
                   + "?frag2 a <http://purl.org/net/wf-fd#DetectedResultWorkflowFragment>."
                   + "?frag2 <http://purl.org/dc/terms/title> \"SUB_2\"."
                   + "?frag2 <http://purl.org/net/wf-fd#foundAs> ?binding1."
                   + "?frag2 <http://purl.org/net/wf-fd#foundAs> ?binding2."
                   + "FILTER (?binding1!=?binding2)."
                   + "?frag1 a <http://purl.org/net/wf-fd#DetectedResultWorkflowFragment>."
                   + "?frag1 <http://purl.org/dc/terms/title> \"SUB_1\"."
                   + "?frag1 <http://purl.org/net/wf-fd#foundAs> ?binding3."
                   + "?frag1 <http://purl.org/net/wf-fd#foundAs> ?binding4."
                   + "FILTER (?binding3!=?binding4)."
                   + "?frag5 a <http://purl.org/net/wf-fd#DetectedResultWorkflowFragment>."
                   + "?frag5 <http://purl.org/dc/terms/title> \"SUB_5\"."
                   + "?frag5 <http://purl.org/net/wf-fd#foundAs> ?binding5."
                   + "?frag5 <http://purl.org/net/wf-fd#foundAs> ?binding6."
                   + "FILTER (?binding5!=?binding6)."
                   + "}";
           
           //more queries to validate? add them here
           if (!GeneralMethods.askLocalRepository(repo, queryInstances1)) return false;
           if (!GeneralMethods.askLocalRepository(repo, queryInstances2)) return false;
           
           return true;
        }catch(Exception e){
           System.out.println("Error in test PostProcessing. Exception: "+e.getMessage());
           return false;
        }
    }
    public static void main(String[] args){
        if(test())System.out.println("Test "+testNumber+" OK");
        else System.out.println("Test "+testNumber+" FAILED");
    }
}
