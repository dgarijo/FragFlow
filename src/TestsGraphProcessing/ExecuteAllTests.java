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

import TestsGraphProcessing.OPMWWings.Test11PrintWingsAvailableDomains;
import TestsGraphProcessing.OPMWWings.Test13CreateReplacementHashMap;
import TestsGraphProcessing.SUBDUE.Test15CreateAndValidateStatisticsForASUBDUEResult;
import TestsGraphProcessing.SUBDUE.Test07SaveCollectionAsReducedGraphInFile;
import TestsGraphProcessing.SUBDUE.Test08SaveCollectionAsInferredReducedGraphInFile;
import TestsGraphProcessing.SUBDUE.Test06SaveCollectionInSeparatedFilesFullGraph;
import TestsGraphProcessing.SUBDUE.Test09SaveCollectionAsFullGraphInFile;
import TestsGraphProcessing.SUBDUE.Test05SaveCollectionInSeparatedFilesReducedGraph;
import TestsGraphProcessing.OPMWWings.Test04SaveTypesOfProcessesToFile;
import TestsGraphProcessing.SUBDUE.Test12CreateStatisticsFromSUBDUEResults;
import TestsGraphProcessing.OPMWWings.Test14CreateMultidomainOntologyFromWINGSRepository;
import TestsGraphProcessing.SUBDUE.Test10ReadASUBDUE_Result;
import TestsGraphProcessing.OPMWWings.Test03TransformAWINGSDomainToGraphCollection;
import TestsGraphProcessing.OPMWWings.Test01TransformOPMWTraceToGraphFromRepository;
import TestsGraphProcessing.OPMWWings.Test02TransformOPMWTemplateToGraphFromRepository;
import TestsGraphProcessing.OPMWWings.Test22Graph2OPMWTemplate;
import TestsGraphProcessing.PAFI.Test26ReadAPAFIResult;
import TestsGraphProcessing.PAFI.Test27CreateAndValidateStatisticsForAPAFIResult;
import TestsGraphProcessing.PAFI.Test28FixDirectionalityOfPAFIFRagments;
import TestsGraphProcessing.PAFI.Test29ValidateRDFWFFDFragmentCatalog;
import TestsGraphProcessing.PAFI.Test30ValidateRDFWFFDFragmentInstances;
import TestsGraphProcessing.PAFI.Test31WriteFullCollectionInPAFIFormat;
import TestsGraphProcessing.SUBDUE.Test16ValidateFoundFragments1;
import TestsGraphProcessing.SUBDUE.Test17ValidateFoundFragments2;
import TestsGraphProcessing.SUBDUE.Test18ValidateFoundFragments3;
import TestsGraphProcessing.SUBDUE.Test19ValidateFoundFragmentsAbstract;
import TestsGraphProcessing.SUBDUE.Test20ValidateFoundFragments4;
import TestsGraphProcessing.SUBDUE.Test21ValidateFoundFragments5;
import TestsGraphProcessing.SUBDUE.Test23CreateRDFFromFragments;
import TestsGraphProcessing.SUBDUE.Test24ValidateRDFWFFDFragmentCatalog;
import TestsGraphProcessing.SUBDUE.Test25ValidateRDFWFFDFragmentInstances;

/**
 * Main class to execute all the tests.
 * This will ensure that the minimum functionality is preserved.
 * @author Daniel Garijo
 */
public class ExecuteAllTests {
    /**
     * Small method to print the status of the tests.
     * @param status boolean saying whether the test was OK or not.
     * @return 1 if test was OK, 0 if not
     */
    public static int printTestStatus(boolean status){
        if(status){
            System.out.println("----TEST OK----");
            return 1;
        }
        System.out.println("----TEST FAILED----");
        return 0;
    }
    public static void main(String[]args){
        int numberOfSuccessfulTests = 0;
        int totalNumberOfTests = 18;
        //for every test, execute it. Each test should describe itself and 
        //measure wether it has been properly executed or not.
        numberOfSuccessfulTests+=printTestStatus(Test31WriteFullCollectionInPAFIFormat.test());
        numberOfSuccessfulTests+=printTestStatus(Test30ValidateRDFWFFDFragmentInstances.test());
        numberOfSuccessfulTests+=printTestStatus(Test29ValidateRDFWFFDFragmentCatalog.test());
        numberOfSuccessfulTests+=printTestStatus(Test28FixDirectionalityOfPAFIFRagments.test());
        numberOfSuccessfulTests+=printTestStatus(Test27CreateAndValidateStatisticsForAPAFIResult.test());
        numberOfSuccessfulTests+=printTestStatus(Test26ReadAPAFIResult.test());
        numberOfSuccessfulTests+=printTestStatus(Test25ValidateRDFWFFDFragmentInstances.test());
        numberOfSuccessfulTests+=printTestStatus(Test24ValidateRDFWFFDFragmentCatalog.test());        
        numberOfSuccessfulTests+=printTestStatus(Test23CreateRDFFromFragments.test());
        numberOfSuccessfulTests+=printTestStatus(Test22Graph2OPMWTemplate.test());
        numberOfSuccessfulTests+=printTestStatus(Test21ValidateFoundFragments5.test());
        numberOfSuccessfulTests+=printTestStatus(Test20ValidateFoundFragments4.test());
        numberOfSuccessfulTests+=printTestStatus(Test19ValidateFoundFragmentsAbstract.test());
        numberOfSuccessfulTests+=printTestStatus(Test18ValidateFoundFragments3.test());
        numberOfSuccessfulTests+=printTestStatus(Test17ValidateFoundFragments2.test());
        numberOfSuccessfulTests+=printTestStatus(Test16ValidateFoundFragments1.test());
        numberOfSuccessfulTests+=printTestStatus(Test15CreateAndValidateStatisticsForASUBDUEResult.test());
        numberOfSuccessfulTests+=printTestStatus(Test14CreateMultidomainOntologyFromWINGSRepository.test());
        numberOfSuccessfulTests+=printTestStatus(Test13CreateReplacementHashMap.test());
        numberOfSuccessfulTests+=printTestStatus(Test12CreateStatisticsFromSUBDUEResults.test());
        numberOfSuccessfulTests+=printTestStatus(Test15CreateAndValidateStatisticsForASUBDUEResult.test());
        numberOfSuccessfulTests+=printTestStatus(Test11PrintWingsAvailableDomains.test());
        numberOfSuccessfulTests+=printTestStatus(Test10ReadASUBDUE_Result.test());
        numberOfSuccessfulTests+=printTestStatus(Test09SaveCollectionAsFullGraphInFile.test());
        numberOfSuccessfulTests+=printTestStatus(Test08SaveCollectionAsInferredReducedGraphInFile.test());
        numberOfSuccessfulTests+=printTestStatus(Test07SaveCollectionAsReducedGraphInFile.test());
        numberOfSuccessfulTests+=printTestStatus(Test06SaveCollectionInSeparatedFilesFullGraph.test());
        numberOfSuccessfulTests+=printTestStatus(Test05SaveCollectionInSeparatedFilesReducedGraph.test());
        numberOfSuccessfulTests+=printTestStatus(Test04SaveTypesOfProcessesToFile.test());
        numberOfSuccessfulTests+=printTestStatus(Test03TransformAWINGSDomainToGraphCollection.test());
        numberOfSuccessfulTests+=printTestStatus(Test02TransformOPMWTemplateToGraphFromRepository.test());
        numberOfSuccessfulTests+=printTestStatus(Test01TransformOPMWTraceToGraphFromRepository.test());        
        
                
        System.out.println("DONE\n\n Number of tests passed: "+numberOfSuccessfulTests+" out of "+totalNumberOfTests);
                
    }
    
}
