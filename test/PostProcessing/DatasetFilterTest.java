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
package PostProcessing;

import Static.Configuration;
import IO.Formats.SUBDUE.GraphCollectionWriterSUBDUE;
import Factory.Loni.LoniTemplate2Graph;
import java.io.File;
import DataStructures.GraphCollection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Daniel Garijo
 */
public class DatasetFilterTest {
    
    public DatasetFilterTest() {
    }

    /**
     * Test of removeDuplicates method, of class DatasetFilter.
     */
    @Test
    public void testRemoveDuplicates() {
        System.out.println("TEST-> Remove duplicates in a dataset");
        String loniDatasetFolder = "LONI_dataset\\datasetZhiZhong\\";
        File f = new File(loniDatasetFolder);
        LoniTemplate2Graph test = new LoniTemplate2Graph(loniDatasetFolder);
        int originalTemplates,filteredTemplates;
        try{
        if(f.isDirectory()){
            File[] files = f.listFiles();
            for(int i=0;i<files.length;i++){
                test.transformToGraph(files[i].getName());
            }
//            test.transformToGraph("00eba273-4b1c-4102-869c-1880e39bfde.pipe");
//            test.transformToGraph("0a2da7d0-e550-44ac-9915-1f3664ce6e3.pipe");
            originalTemplates = test.getGraphCollection().getNumberOfSubGraphs();
            GraphCollection filteredC = DatasetFilter.removeDuplicates(test.getGraphCollection());
            filteredTemplates = filteredC.getNumberOfSubGraphs();
            System.out.println(originalTemplates+"    "+filteredTemplates);
            assertTrue(originalTemplates>filteredTemplates);            
            GraphCollectionWriterSUBDUE writer = new GraphCollectionWriterSUBDUE();
            writer.writeReducedGraphsToFile(test.getGraphCollection(),"OriginalDataset", null);
            writer.writeReducedGraphsToFile(filteredC,"filteredTemplates", null);
        }
        }catch(Exception e){
            fail("Test failed due to some exception");
        }
    }
}
