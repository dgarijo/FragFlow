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
package IO;

import Static.TestConstants;
import Factory.Loni.LoniTemplate2Graph;
import IO.Formats.PAFI.GraphCollectionWriterPAFI;
import IO.Formats.PARSEMIS.GraphCollectionWriterPARSEMIS;
import IO.Formats.SUBDUE.GraphCollectionWriterSUBDUE;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Daniel Garijo
 */
public class EncodeDatasetTest {
    
    public EncodeDatasetTest() {
    }

    /**
     * Test of encodeGraphCollection method, of class EncodeDataset.
     */
    @Test
    public void testEncodeGraphCollection() {
        System.out.println("->TEST: Encoding and writing of a dataset with the 3 collection writers.");
        LoniTemplate2Graph test = new LoniTemplate2Graph(TestConstants.aLoniTemplatePathForTests);
        test.transformToGraph("DTI_workflow.pipe.xml");
        test.transformToGraph("AutoROIExtraction_LONI.pipe.xml");
        GraphCollectionWriter outPafi = new GraphCollectionWriterPAFI();
        GraphCollectionWriter outParsemis = new GraphCollectionWriterPARSEMIS();
        GraphCollectionWriter outSubdue = new GraphCollectionWriterSUBDUE();
        String outPath = "test";
        try{
            EncodeDataset.encodeAndWriteGraphCollection(test.getGraphCollection(), outPafi, outPath+"pafi");
            EncodeDataset.encodeAndWriteGraphCollection(test.getGraphCollection(), outParsemis, outPath+"parsemis");
            EncodeDataset.encodeAndWriteGraphCollection(test.getGraphCollection(), outSubdue, outPath+"subdue");
//            out.writeReducedGraphsToFile(test.getGraphCollection(), outPath+"original");
        }catch(Exception e){
            fail("Something went wrong when processing the input file");
        }
    }
}
