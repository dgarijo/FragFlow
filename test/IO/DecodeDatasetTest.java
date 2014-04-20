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

import IO.Formats.SUBDUE.FragmentCollectionReaderSUBDUE;
import IO.Formats.SUBDUE.FragmentCollectionWriterSUBDUE;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Daniel Garijo
 */
public class DecodeDatasetTest {
    
    public DecodeDatasetTest() {
    }

    /**
     * Test of decodeDataset method, of class DecodeDataset.
     */
    @Test
    public void testDecodeDataset() throws Exception {
        System.out.println("TEST-> Decode a given dataset");
        String inputPathCodes = "SUBDUE_TOOL\\input_graphs\\LoniZhiDataset.g-codes";        
        String inputResults = "SUBDUE_TOOL\\results\\resultsLoni\\";
        String outPath = "testDecode";
        try{
            FragmentCollectionReaderSUBDUE r1 = new FragmentCollectionReaderSUBDUE(inputResults+"resultsLoniDatasetZhiEval1", inputResults+"resultsLoniDatasetZhiEval1_occurrences");
            FragmentCollectionWriter outSubdue = new FragmentCollectionWriterSUBDUE();
            DecodeDataset.decodeDataset(r1, inputPathCodes, outSubdue, outPath);
        }catch(Exception e){
            fail("Error while decoding the datasets!");
        }
    }
}
