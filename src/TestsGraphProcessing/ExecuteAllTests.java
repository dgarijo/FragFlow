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
        numberOfSuccessfulTests+=printTestStatus(TestCreateMultidomainOntologyFromWINGSRepository.test());
        numberOfSuccessfulTests+=printTestStatus(TestCreateReplacementHashMap.test());
        numberOfSuccessfulTests+=printTestStatus(TestCreateStatisticsFromSUBDUEResults.test());
        numberOfSuccessfulTests+=printTestStatus(TestPostProcessing.test());
        numberOfSuccessfulTests+=printTestStatus(TestPrintWingsAvailableDomains.test());
        numberOfSuccessfulTests+=printTestStatus(TestReadASUBDUE_Result.test());
        numberOfSuccessfulTests+=printTestStatus(TestSaveCollectionAsFullGraphInFile.test());
        numberOfSuccessfulTests+=printTestStatus(TestSaveCollectionAsInferredReducedGraphInFile.test());
        numberOfSuccessfulTests+=printTestStatus(TestSaveCollectionAsReducedGraphInFile.test());
        numberOfSuccessfulTests+=printTestStatus(TestSaveCollectionInSeparatedFilesFullGraph.test());
        numberOfSuccessfulTests+=printTestStatus(TestSaveCollectionInSeparatedFilesReducedGraph.test());
        numberOfSuccessfulTests+=printTestStatus(TestSaveTypesOfProcessesToFile.test());
        numberOfSuccessfulTests+=printTestStatus(TestTransformAWINGSDomainToGraphCollection.test());
        numberOfSuccessfulTests+=printTestStatus(TestTransformOPMWTemplateToGraphFromRepository.test());
        numberOfSuccessfulTests+=printTestStatus(TestTransformOPMWTraceToGraphFromRepository.test());
                
        System.out.println("DONE\n\n Number of tests passed: "+numberOfSuccessfulTests+" out of "+totalNumberOfTests);
                
    }
    
}
