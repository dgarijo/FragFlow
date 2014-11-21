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
package TestsGraphProcessing.PARSEMIS;

import Factory.OPMW.OPMWTemplate2GraphWings;
import IO.Formats.PARSEMIS.FragmentCatalogAndResultsToRDFPARSEMIS;
import PostProcessing.Formats.PARSEMIS.CreateStatisticsFromResultsPARSEMIS;
import Static.GeneralMethods;
import Static.Vocabularies.DCTerms;
import Static.Vocabularies.Wffd;
import com.hp.hpl.jena.ontology.OntModel;

/**
 * Test designed to validate the instances obtained in the WFFD (Parsemis)
 * @author Daniel Garijo
 */
public class Test36ValidateRDFWFFDFragmentInstances {
    public static int testNumber = 36;
    public static boolean test(){
    try{
           System.out.println("\n\nExecuting test:"+testNumber+" Testing the RDF created from the catalog (Parsemis)");
           String resultFile = "PARSEMIS_TOOL\\results\\run11-03-2014.txt";
           CreateStatisticsFromResultsPARSEMIS c = new CreateStatisticsFromResultsPARSEMIS("Text analytics", true, false, resultFile); 
           FragmentCatalogAndResultsToRDFPARSEMIS aux = new FragmentCatalogAndResultsToRDFPARSEMIS("testPafiRDF.ttl");
           aux.transformFragmentCollectionToRDF(c.getFilteredMultiStepFragments());
           OPMWTemplate2GraphWings test = new OPMWTemplate2GraphWings("http://wind.isi.edu:8890/sparql");    
//           test.transformDomainToGraph("TextAnalytics");
           test.transformToGraph("http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI");
           aux.transformBindingResultsInTemplateCollection(c.getFilteredMultiStepFragments(), test.getGraphCollection());            

           //instead of querying many instances, in this test we just check that all the fragments 
           //are properly found in one of the most popular one.
           OntModel repo = aux.getRepository();
           //Question 1: 59, 45 52, 76, 94 and 111 must be part of the template  
           String queryInstances1 = 
                   "ASK {"
                   + "?frag1 a <"+Wffd.DETECTED_RESULT+">."
                   + "?frag1 <"+DCTerms.TITLE+"> \"59\"."
                   + "?frag1 <"+Wffd.FOUND_IN+"> <http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI>."
                   + "?frag2 a <"+Wffd.DETECTED_RESULT+">."
                   + "?frag2 <"+DCTerms.TITLE+"> \"45\"."
                   + "?frag2 <"+Wffd.FOUND_IN+"> <http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI>."
                   + "?frag3 a <"+Wffd.DETECTED_RESULT+">."
                   + "?frag3 <"+DCTerms.TITLE+"> \"52\"."
                   + "?frag4 a <"+Wffd.DETECTED_RESULT+">."
                   + "?frag4 <"+Wffd.FOUND_IN+"> <http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI>."
                   + "?frag4 <"+DCTerms.TITLE+"> \"76\"."
                   + "?frag4 <"+Wffd.FOUND_IN+"> <http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI>."
                   + "?frag5 a <"+Wffd.DETECTED_RESULT+">."
                   + "?frag5 <"+DCTerms.TITLE+"> \"94\"."
                   + "?frag5 <"+Wffd.FOUND_IN+"> <http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI>."
                   + "?frag6 a <"+Wffd.DETECTED_RESULT+">."
                   + "?frag6 <"+DCTerms.TITLE+"> \"111\"."
                   + "?frag6 <"+Wffd.FOUND_IN+"> <http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI>."
                   + "}";           
           //Question 2: 52, 59 and 45 must have 2 bindings each to the template.
           //it is ok to avoid asking if the bindings are from that template, since we only have loaded 1.
           String queryInstances2 = 
                   "ASK {"
                   + "?frag2 a <"+Wffd.DETECTED_RESULT+">."
                   + "?frag2 <"+DCTerms.TITLE+"> \"52\"."
                   + "?frag2 <"+Wffd.FOUND_AS+"> ?binding1."
                   + "?frag2 <"+Wffd.FOUND_AS+"> ?binding2."
                   + "FILTER (?binding1!=?binding2)."
                   + "?frag1 a <"+Wffd.DETECTED_RESULT+">."
                   + "?frag1 <"+DCTerms.TITLE+"> \"59\"."
                   + "?frag1 <"+Wffd.FOUND_AS+"> ?binding3."
                   + "?frag1 <"+Wffd.FOUND_AS+"> ?binding4."
                   + "FILTER (?binding3!=?binding4)."
                   + "?frag5 a <"+Wffd.DETECTED_RESULT+">."
                   + "?frag5 <"+DCTerms.TITLE+"> \"45\"."
                   + "?frag5 <"+Wffd.FOUND_AS+"> ?binding5."
                   + "?frag5 <"+Wffd.FOUND_AS+"> ?binding6."
                   + "FILTER (?binding5!=?binding6)."
                   + "}";
           
           //Question 3: 76, 111 and 94 must have 1 binding.
           String queryInstances3 = 
                   "ASK {"
                   + "?frag2 a <"+Wffd.DETECTED_RESULT+">."
                   + "?frag2 <"+DCTerms.TITLE+"> \"76\"."
                   + "?frag2 <"+Wffd.FOUND_AS+"> ?binding1."
                   + "?frag1 a <"+Wffd.DETECTED_RESULT+">."
                   + "?frag1 <"+DCTerms.TITLE+"> \"111\"."
                   + "?frag1 <"+Wffd.FOUND_AS+"> ?binding4."
                   + "?frag5 a <"+Wffd.DETECTED_RESULT+">."
                   + "?frag5 <"+DCTerms.TITLE+"> \"94\"."
                   + "?frag5 <"+Wffd.FOUND_AS+"> ?binding6."
                   + "}";
           
           //more queries to validate? add them here
           if (!GeneralMethods.askLocalRepository(repo, queryInstances1)) return false;
           if (!GeneralMethods.askLocalRepository(repo, queryInstances2)) return false;
           if (!GeneralMethods.askLocalRepository(repo, queryInstances3)) return false;
           
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
