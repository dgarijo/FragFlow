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
package IO.Formats.PAFI;

import DataStructures.Fragment;
import Static.TestConstants;
import java.util.HashMap;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Daniel Garijo
 */
public class FragmentCollectionWriterPAFITest {
    
    public FragmentCollectionWriterPAFITest() {
    }

    /**
     * Test of writeFragmentsToFile method, of class FragmentCollectionWriterPAFI.
     */
    @Test
    public void testWriteFragmentsToFile() throws Exception {
        System.out.println("->Test: Writing fragments to file (PAFI Format)");
        String fpfile = TestConstants.PAFIResultsFolder+"CollectionInPAFIFormat.fp";
        String pcFile = TestConstants.PAFIResultsFolder+"CollectionInPAFIFormat.pc";
        String tidFile = TestConstants.PAFIResultsFolder+"CollectionInPAFIFormat.tid";
        String outputPath = "testPAFIFragments";
        FragmentCollectionWriterPAFI instance = new FragmentCollectionWriterPAFI();
        try{
            HashMap<String,Fragment> fc = new FragmentCollectionReaderPAFI(fpfile,pcFile,tidFile).getFragmentCatalogAsHashMap();
            
            instance.writeFragmentsToFile(fc, outputPath, null);
        }catch(Exception e){
            fail("Test failed: something whent wrong when writing the fragments "+e.getMessage());
        }
    }
}
