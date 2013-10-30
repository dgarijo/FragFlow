/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
