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
package TestsGraphProcessing.PAFI;
import DataStructures.Fragment;
import Factory.OPMW.OPMWTemplate2Graph;
import IO.Formats.PAFI.FragmentCatalogAndResultsToRDFPAFI;
import PostProcessing.Formats.PAFI.CreateStatisticsFromResultsPAFI;
import PostProcessing.Formats.PAFI.FixDirectionOfFragmentCatalog;
import Static.Configuration;
import Static.GeneralMethods;
import Static.Vocabularies.DCTerms;
import Static.Vocabularies.PPlan;
import Static.Vocabularies.Wffd;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.ArrayList;
/**
 * Test to determine whether the fragments found in PAFI have been properly
 * instantiated in the workflow catalog.
 * @author Daniel Garijo
 */
public class Test30ValidateRDFWFFDFragmentInstances {
    public static int testNumber = 30;
    public static boolean test(){
    try{
            System.out.println("\n\nExecuting test:"+testNumber+" Testing the RDF created from the catalog (PAFI)");
           String fpfile = "PAFI_TOOL\\results\\CollectionInPAFIFormat.fp";
           String pcFile = "PAFI_TOOL\\results\\CollectionInPAFIFormat.pc";
           String tidFile = "PAFI_TOOL\\results\\CollectionInPAFIFormat.tid";
           CreateStatisticsFromResultsPAFI c = new CreateStatisticsFromResultsPAFI("Text analytics", true, false, fpfile, pcFile, tidFile); 
           FragmentCatalogAndResultsToRDFPAFI aux = new FragmentCatalogAndResultsToRDFPAFI("testPafiRDF.ttl");
           ArrayList<Fragment> filteredFixedFragmentCatalog = FixDirectionOfFragmentCatalog.fixDirectionOfCatalogWithOPMWTemplates(Configuration.getPAFIInputPath()+"CollectionInPAFIFormat", c.getFilteredMultiStepFragments(),c.getFragmentsInTransactions(), false);
           aux.transformFragmentCollectionToRDF(filteredFixedFragmentCatalog);
           OPMWTemplate2Graph test = new OPMWTemplate2Graph("http://wind.isi.edu:8890/sparql");    
//           test.transformDomainToGraph("TextAnalytics");
           test.transformToGraph("http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI");
           aux.transformBindingResultsInTemplateCollection(filteredFixedFragmentCatalog, test.getGraphCollection());            

           //instead of querying many instances, in this test we just check that all the fragments 
           //are properly found in one of the most popular one.
           OntModel repo = aux.getRepository();
           //Question 1: 2-1, 2-2 1-8, 5-0, 1-2, 1-3 and 1-6 must be part of the template  
           String queryInstances1 = 
                   "ASK {"
                   + "?frag1 a <"+Wffd.DETECTED_RESULT+">."
                   + "?frag1 <"+DCTerms.TITLE+"> \"2-2\"."
                   + "?frag1 <"+Wffd.FOUND_IN+"> <http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI>."
                   + "?frag2 a <"+Wffd.DETECTED_RESULT+">."
                   + "?frag2 <"+DCTerms.TITLE+"> \"1-8\"."
                   + "?frag2 <"+Wffd.FOUND_IN+"> <http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI>."
                   + "?frag3 a <"+Wffd.DETECTED_RESULT+">."
                   + "?frag3 <"+DCTerms.TITLE+"> \"5-0\"."
                   + "?frag4 a <"+Wffd.DETECTED_RESULT+">."
                   + "?frag4 <"+Wffd.FOUND_IN+"> <http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI>."
                   + "?frag4 <"+DCTerms.TITLE+"> \"1-2\"."
                   + "?frag4 <"+Wffd.FOUND_IN+"> <http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI>."
                   + "?frag5 a <"+Wffd.DETECTED_RESULT+">."
                   + "?frag5 <"+DCTerms.TITLE+"> \"1-3\"."
                   + "?frag5 <"+Wffd.FOUND_IN+"> <http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI>."
                   + "?frag6 a <"+Wffd.DETECTED_RESULT+">."
                   + "?frag6 <"+DCTerms.TITLE+"> \"1-6\"."
                   + "?frag6 <"+Wffd.FOUND_IN+"> <http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI>."
                   + "}";           
           //Question 2: 2-2, 1-8 and 1-6 must have 2 bindings each to the template.
           //it is ok to avoid asking if the bindings are from that template, since we only have loaded 1.
           String queryInstances2 = 
                   "ASK {"
                   + "?frag2 a <"+Wffd.DETECTED_RESULT+">."
                   + "?frag2 <"+DCTerms.TITLE+"> \"2-2\"."
                   + "?frag2 <"+Wffd.FOUND_AS+"> ?binding1."
                   + "?frag2 <"+Wffd.FOUND_AS+"> ?binding2."
                   + "FILTER (?binding1!=?binding2)."
                   + "?frag1 a <"+Wffd.DETECTED_RESULT+">."
                   + "?frag1 <"+DCTerms.TITLE+"> \"1-8\"."
                   + "?frag1 <"+Wffd.FOUND_AS+"> ?binding3."
                   + "?frag1 <"+Wffd.FOUND_AS+"> ?binding4."
                   + "FILTER (?binding3!=?binding4)."
                   + "?frag5 a <"+Wffd.DETECTED_RESULT+">."
                   + "?frag5 <"+DCTerms.TITLE+"> \"1-6\"."
                   + "?frag5 <"+Wffd.FOUND_AS+"> ?binding5."
                   + "?frag5 <"+Wffd.FOUND_AS+"> ?binding6."
                   + "FILTER (?binding5!=?binding6)."
                   + "}";
           
           //Question 3: 5-0, 1-2 and 1-3 must have 1 binding.
           String queryInstances3 = 
                   "ASK {"
                   + "?frag2 a <"+Wffd.DETECTED_RESULT+">."
                   + "?frag2 <"+DCTerms.TITLE+"> \"5-0\"."
                   + "?frag2 <"+Wffd.FOUND_AS+"> ?binding1."
                   + "?frag1 a <"+Wffd.DETECTED_RESULT+">."
                   + "?frag1 <"+DCTerms.TITLE+"> \"1-2\"."
                   + "?frag1 <"+Wffd.FOUND_AS+"> ?binding4."
                   + "?frag5 a <"+Wffd.DETECTED_RESULT+">."
                   + "?frag5 <"+DCTerms.TITLE+"> \"1-3\"."
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
