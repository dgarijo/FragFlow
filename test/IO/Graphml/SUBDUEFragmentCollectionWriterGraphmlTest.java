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
package IO.Graphml;

import IO.Formats.SUBDUE.FragmentCollectionReaderSUBDUE;
import Static.TestConstants;
import DataStructures.Fragment;
import java.util.HashMap;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Daniel Garijo
 */
public class SUBDUEFragmentCollectionWriterGraphmlTest {
    
    public SUBDUEFragmentCollectionWriterGraphmlTest() {
    }

   

    /**
     * Test of writeFragmentsToFile method, of class SUBDUEFragmentCollectionWriterGraphml.
     */
    @Test
    public void testWriteFragmentsToFile() throws Exception {
        String file = TestConstants.SUBDUEResultFolder + "\\resultsLoni\\resultsLoniZhiFilteredSUBDUEEval1";
        String ocFile = TestConstants.SUBDUEResultFolder + "\\resultsLoni\\resultsLoniZhiFilteredSUBDUEEval1_occurrences";
        String outputPath = "testGraphm.graphml";
        SUBDUEFragmentCollectionWriterGraphml instance = new SUBDUEFragmentCollectionWriterGraphml();
        try{
            HashMap<String,Fragment> structureResults = new FragmentCollectionReaderSUBDUE(file, ocFile).getFragmentCatalogAsHashMap();
            instance.writeFragmentsToFile(structureResults, outputPath, null);
        }catch(Exception e){
            fail("Test failed: something whent wrong when writing the fragments");
        }
    }
}
