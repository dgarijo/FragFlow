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

import IO.Formats.PARSEMIS.FragmentCatalogAndResultsToRDFPARSEMIS;
import PostProcessing.Formats.PARSEMIS.CreateStatisticsFromResultsPARSEMIS;
import Static.GeneralMethods;
import Static.Vocabularies.DCTerms;
import Static.Vocabularies.PPlan;
import Static.Vocabularies.Wffd;
import com.hp.hpl.jena.ontology.OntModel;

/**
 *
 * @author Daniel Garijo
 */
public class Test35ValidateRDFWFFDFragmentCatalog {
    public static int testNumber = 35;
    public static boolean test(){        
       try{
           System.out.println("\n\nExecuting test:"+testNumber+" Testing the RDF created from the catalog (Parsemis)");
           String resultFile = "PARSEMIS_TOOL\\results\\run11-03-2014.txt";
           CreateStatisticsFromResultsPARSEMIS c = new CreateStatisticsFromResultsPARSEMIS("Text analytics", true, false, resultFile); 
           //although we can call the filter and then fix the fragments, doing the statistics is faster.
           FragmentCatalogAndResultsToRDFPARSEMIS aux = new FragmentCatalogAndResultsToRDFPARSEMIS("testParsemisRDF.ttl");           
           aux.transformFragmentCollectionToRDF(c.getFilteredMultiStepFragments());
//           aux.exportToRDFFile("TURTLE");
           OntModel repo = aux.getRepository();
           //Question 1: Fragment 52 must have stopwords followed by smallwords 
           String queryTemplates1 = 
                  "ASK {"
                  + "?frag1 a <"+Wffd.DETECTED_RESULT+">."
                  + "?frag1 <"+DCTerms.TITLE+"> \"52\"."
                  + "?stop a <http://www.isi.edu/ac/TextAnalytics/library.owl#StopWords>."
                  + "?stop <"+PPlan.IS_STEP_OF_PLAN+"> ?frag1."
                  + "?small a <http://www.isi.edu/ac/TextAnalytics/library.owl#SmallWords>."
                  + "?small <"+PPlan.IS_STEP_OF_PLAN+"> ?frag1."
                  + "?small <"+PPlan.IS_PRECEEDED_BY+"> ?stop."
                  + "}";
          //Fragment 52 is part of fragment 44, 45, 50 and 47
          //in Parsemis there are no multisteps, so we don't know which steps are 
          //the ones in the other plan.
          String queryTemplates2 = 
                  "ASK {"
                  + "?frag1 a <"+Wffd.DETECTED_RESULT+">."
                  + "?frag1 <"+DCTerms.TITLE+"> \"52\"."
                  + "?frag1 <"+Wffd.PART_OF_WORKFLOW_FRAGMENT+"> ?frag2."
                  + "?frag2 <"+DCTerms.TITLE+"> \"44\"."
                  + "?frag1 <"+Wffd.PART_OF_WORKFLOW_FRAGMENT+"> ?frag3."
                  + "?frag3 <"+DCTerms.TITLE+"> \"45\"."
                  + "?frag1 <"+Wffd.PART_OF_WORKFLOW_FRAGMENT+"> ?frag4."
                  + "?frag4 <"+DCTerms.TITLE+"> \"50\"."
                  + "?frag1 <"+Wffd.PART_OF_WORKFLOW_FRAGMENT+"> ?frag5."
                  + "?frag5 <"+DCTerms.TITLE+"> \"47\"."
                  + "}";
          //Fragment 76 is one of the largest fragments and it must be composed of:
          //validator informed by formatArff and FeatureSelection
          //featureSelection informed by formatArff and Chi Squared.
          //Chi squared informed by format arff
          String queryTemplates3 = 
                  "ASK {"
                  + "?frag5 a <"+Wffd.DETECTED_RESULT+">."
                  + "?frag5 <"+DCTerms.TITLE+"> \"76\"."
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
