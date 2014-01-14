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
    public static void main(String[]args){
        //for every test, execute it. Each test should describe itself and 
        //measure wether it has been properly executed or not.
        TestCreateMultidomainOntologyFromWINGSRepository.test();
        TestCreateReplacementHashMap.test();
        TestCreateStatisticsFromSUBDUEResults.test();
        TestReadASUBDUE_Result.test();
        TestTransformAWINGSDomainToGraphCollection.test();
        TestTransformOPMWTemplateToGraphFromRepository.test();
        TestTransformOPMWTraceToGraphFromRepository.test();
        TestSaveCollectionAsFullGraphInFile.test();
        TestSaveCollectionAsReducedGraphInFile.test();
        TestSaveCollectionInSeparatedFilesFullGraph.test();
        TestSaveCollectionInSeparatedFilesReducedGraph.test();
    }
    
}
