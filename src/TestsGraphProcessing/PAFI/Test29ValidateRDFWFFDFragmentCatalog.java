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
 * Test to validate if the results obtained by the fragment catalog are right.
 * I have picked some fragments and checked manually if they are right.
 * If those happen to be fine, the test is passed. I have not gone over all the 
 * fragments due to their amount (and the way they are calculated is the same)
 * @author Daniel Garijo
 */
public class Test29ValidateRDFWFFDFragmentCatalog {
    
    public static int testNumber = 29;
    public static boolean test(){        
       try{
           System.out.println("\n\nExecuting test:"+testNumber+" Testing the RDF created from the catalog (PAFI)");
           String fpfile = "PAFI_TOOL\\results\\CollectionInPAFIFormat.fp";
           String pcFile = "PAFI_TOOL\\results\\CollectionInPAFIFormat.pc";
           String tidFile = "PAFI_TOOL\\results\\CollectionInPAFIFormat.tid";
           CreateStatisticsFromResultsPAFI c = new CreateStatisticsFromResultsPAFI("Text analytics", true, false, fpfile, pcFile, tidFile); 
           //although we can call the filter and then fix the fragments, doing the statistics is faster.
           FragmentCatalogAndResultsToRDFPAFI aux = new FragmentCatalogAndResultsToRDFPAFI("testPafiRDF.ttl");
           ArrayList<Fragment> filteredFixedFragmentCatalog = FixDirectionOfFragmentCatalog.fixDirectionOfCatalog(Configuration.getPAFIInputPath()+"CollectionInPAFIFormat", c.getFilteredMultiStepFragments(),c.getFragmentsInTransactions(), false);
           aux.transformFragmentCollectionToRDF(filteredFixedFragmentCatalog);
           //no need to parse bindings. We are just querying the fragment catalog
//           OPMWTemplate2Graph test = new OPMWTemplate2Graph("http://wind.isi.edu:8890/sparql");    
//           test.transformDomainToGraph("TextAnalytics");
//           aux.transformBindingResultsInTemplateCollection(filteredFixedFragmentCatalog, test.getGraphCollection());        
           OntModel repo = aux.getRepository();
           //Question 1: Fragment 1-6 must have stopwords followed by smallwords 
           String queryTemplates1 = 
                  "ASK {"
                  + "?frag1 a <"+Wffd.DETECTED_RESULT+">."
                  + "?frag1 <"+DCTerms.TITLE+"> \"1-6\"."
                  + "?stop a <http://www.isi.edu/ac/TextAnalytics/library.owl#StopWords>."
                  + "?stop <"+PPlan.IS_STEP_OF_PLAN+"> ?frag1."
                  + "?small a <http://www.isi.edu/ac/TextAnalytics/library.owl#SmallWords>."
                  + "?small <"+PPlan.IS_STEP_OF_PLAN+"> ?frag1."
                  + "?small <"+PPlan.IS_PRECEEDED_BY+"> ?stop."
                  + "}";
          //Fragment 1-6 is part of fragment 3-1, 3-2, 2-2 and 3-7
          //in PAFI there are no multisteps, so we don't know which steps are 
          //the ones in the other plan.
          String queryTemplates2 = 
                  "ASK {"
                  + "?frag1 a <"+Wffd.DETECTED_RESULT+">."
                  + "?frag1 <"+DCTerms.TITLE+"> \"1-6\"."
                  + "?frag1 <"+Wffd.PART_OF_WORKFLOW_FRAGMENT+"> ?frag2."
                  + "?frag2 <"+DCTerms.TITLE+"> \"3-1\"."
                  + "?frag1 <"+Wffd.PART_OF_WORKFLOW_FRAGMENT+"> ?frag3."
                  + "?frag3 <"+DCTerms.TITLE+"> \"3-2\"."
                  + "?frag1 <"+Wffd.PART_OF_WORKFLOW_FRAGMENT+"> ?frag4."
                  + "?frag4 <"+DCTerms.TITLE+"> \"2-2\"."
                  + "?frag1 <"+Wffd.PART_OF_WORKFLOW_FRAGMENT+"> ?frag5."
                  + "?frag5 <"+DCTerms.TITLE+"> \"3-7\"."
                  + "}";
          //Fragment 5 is the largest fragment and it must be composed of:
          //validator informed by formatArff and FeatureSelection
          //featureSelection informed by formatArff and Chi Squared.
          //Chi squared informed by format arff
          String queryTemplates3 = 
                  "ASK {"
                  + "?frag5 a <"+Wffd.DETECTED_RESULT+">."
                  + "?frag5 <"+DCTerms.TITLE+"> \"5-0\"."
                  + "?val a <http://www.isi.edu/ac/TextAnalytics/library.owl#Validator>."
                  + "?val <"+PPlan.IS_STEP_OF_PLAN+"> ?frag5."
                  + "?fs a <http://www.isi.edu/ac/TextAnalytics/library.owl#FeatureSelection>."
                  + "?fs <"+PPlan.IS_STEP_OF_PLAN+"> ?frag5."
                  + "?fa a <http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff>."
                  + "?fa <"+PPlan.IS_STEP_OF_PLAN+"> ?frag5."
                  + "?fa2 a <http://www.isi.edu/ac/TextAnalytics/library.owl#FormatArff>."
                  + "?fa2 <"+PPlan.IS_STEP_OF_PLAN+"> ?frag5."
                  + "?chi a <http://www.isi.edu/ac/TextAnalytics/library.owl#ChiSquared>."
                  + "?chi <"+PPlan.IS_STEP_OF_PLAN+"> ?frag5."                  
                  + "?val <"+PPlan.IS_PRECEEDED_BY+"> ?fa."
                  + "?val <"+PPlan.IS_PRECEEDED_BY+"> ?fs."
                  + "?fs <"+PPlan.IS_PRECEEDED_BY+"> ?fa2."
                  + "?fs <"+PPlan.IS_PRECEEDED_BY+"> ?chi."
                  + "?chi <"+PPlan.IS_PRECEEDED_BY+"> ?fa2."
                  + "}";
          
           if (!GeneralMethods.askLocalRepository(repo, queryTemplates1)) return false;
           if (!GeneralMethods.askLocalRepository(repo, queryTemplates2)) return false;
           if (!GeneralMethods.askLocalRepository(repo, queryTemplates3)) return false;
         
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
