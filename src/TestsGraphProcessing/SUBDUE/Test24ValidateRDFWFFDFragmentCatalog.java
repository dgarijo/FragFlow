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
import PostProcessing.Formats.SUBDUE.CreateStatisticsFromResultsSUBDUE;
import Static.GeneralMethods;
import Static.Vocabularies.DCTerms;
import Static.Vocabularies.PPlan;
import Static.Vocabularies.Wffd;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.ArrayList;
/**
 * Test designed to check whether the RDF generated in Wf-fd from a fragment 
 * catalog is correct or not. In order to achieve this, we have to generate 
 * RDF from some concrete templates and then check it out with fixed SPARQL 
 * queries.
 * @author Daniel Garijo
 */
public class Test24ValidateRDFWFFDFragmentCatalog {    
    public static int testNumber = 24;
    public static boolean test(){        
       try{
           System.out.println("\n\nExecuting test:"+testNumber+" Testing the RDF created from the catalog");
//           OPMWTemplate2Graph test = new OPMWTemplate2Graph("http://wind.isi.edu:8890/sparql");    
//           test.transformDomainToGraph("TextAnalytics");
//           test.transformToGraph("http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI");
           String file = "SUBDUE_TOOL\\results\\Tests\\testResultReduced2";
           String ocFile = "SUBDUE_TOOL\\results\\Tests\\testResultReduced2_occurrences";
//           HashMap<String,Fragment> obtainedResults = new FragmentReaderSUBDUE(file, ocFile).getFragmentCatalogFromAlgorithmResultFiles();

           CreateStatisticsFromResultsSUBDUE aux = new CreateStatisticsFromResultsSUBDUE("text analytics",
                    "-", true, false,file, ocFile);
           ArrayList<Fragment> obtainedResults = aux.getFilteredMultiStepFragments();
           //without inference
           FragmentCatalogAndResultsToRDFSUBDUE catalogNoInference = new FragmentCatalogAndResultsToRDFSUBDUE("outTEST.ttl");

           catalogNoInference.transformFragmentCollectionToRDF(obtainedResults);
//           catalogNoInference.transformBindingResultsInTemplateCollection(obtainedResults, test.getGraphCollection());           

           OntModel repo = catalogNoInference.getRepository();
           //Question 1: Fragment 1 must have stopwords followed by smallwords.
           String queryTemplates1 = 
                   "ASK {"
                   + "?frag1 a <"+Wffd.DETECTED_RESULT+">."
                   + "?frag1 <"+DCTerms.TITLE+"> \"SUB_1\"."
                   + "?stop a <http://www.isi.edu/ac/TextAnalytics/library.owl#StopWords>."
                   + "?stop <"+PPlan.IS_STEP_OF_PLAN+"> ?frag1."
                   + "?small a <http://www.isi.edu/ac/TextAnalytics/library.owl#SmallWords>."
                   + "?small <"+PPlan.IS_STEP_OF_PLAN+"> ?frag1."
                   + "?small <"+PPlan.IS_PRECEEDED_BY+"> ?stop."
                   + "}";
           //Fragment 2 must have SUB_1 and then a stemmer.
           //Remember that when a fragment appea
           String queryTemplates2 = 
                   "ASK {"
                   + "?frag2 a <"+Wffd.DETECTED_RESULT+">."
                   + "?frag2 <"+DCTerms.TITLE+"> \"SUB_2\"."
                   + "?node1 <"+PPlan.IS_STEP_OF_PLAN+"> ?frag2."
                   + "?node1 <"+PPlan.IS_DECOMPOSED_AS_PLAN+"> ?sub1."
                   + "?sub1 <"+DCTerms.TITLE+"> \"SUB_1\"."
                   + "?stem a <http://www.isi.edu/ac/TextAnalytics/library.owl#Stemmer>."
                   + "?stem <"+PPlan.IS_STEP_OF_PLAN+"> ?frag2."
                   + "?stem <"+PPlan.IS_PRECEEDED_BY+"> ?node1."
                   + "}";
           //Fragment 5 must have mult2Single preceededBy TF_IDF and then preceededBy SUB_2
           String queryTemplates3 = 
                   "ASK {"
                   + "?frag5 a <"+Wffd.DETECTED_RESULT+">."
                   + "?frag5 <"+DCTerms.TITLE+"> \"SUB_5\"."
                   + "?multi a <http://www.isi.edu/ac/TextAnalytics/library.owl#Multi2Single>."
                   + "?multi <"+PPlan.IS_STEP_OF_PLAN+"> ?frag5."
                   + "?tf a <http://www.isi.edu/ac/TextAnalytics/library.owl#TF_IDF>."
                   + "?tf <"+PPlan.IS_STEP_OF_PLAN+"> ?frag5."
                   + "?multi <"+PPlan.IS_PRECEEDED_BY+"> ?tf."
                   + "?tf <"+PPlan.IS_PRECEEDED_BY+"> ?node1."
                   + "?node1 <"+PPlan.IS_STEP_OF_PLAN+"> ?frag5."
                   + "?node1 <"+PPlan.IS_DECOMPOSED_AS_PLAN+"> ?sub2."
                   + "?sub2 <"+DCTerms.TITLE+"> \"SUB_2\"."
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
