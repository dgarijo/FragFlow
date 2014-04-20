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
package IO.Formats.PARSEMIS;

import Static.TestConstants;
import DataStructures.Fragment;
import IO.Formats.PARSEMIS.Gspan.FragmentCollectionReaderPARSEMISGspan;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Daniel Garijo
 */
public class FragmentCollectionWriterPARSEMISTest {
    
    public FragmentCollectionWriterPARSEMISTest() {
    }

    /**
     * Test of writeFragmentsToFile method, of class FragmentCollectionWriterPARSEMIS.
     */
    @Test
    public void testWriteFragmentsToFile_3args(){
        System.out.println("->Test: Writing fragments to file (PARSEMIS Format 3 args)");
        String file = TestConstants.PARSEMISResultFolder + "resultsCollectionInParsemisFormat.lg";
        String outputPath = "testFragmentsParsemis";        
        HashMap<String, String> replacements = null;
        try{
            HashMap<String, Fragment> fc = new FragmentCollectionReaderPARSEMISGspan(file).getFragmentCatalogAsHashMap();
            FragmentCollectionWriterPARSEMIS instance = new FragmentCollectionWriterPARSEMIS();
            instance.writeFragmentsToFile(fc, outputPath, replacements);
        }catch(Exception e){
            fail("There was a problem when writing the fragment files in Parsemis");
        }
    }

    /**
     * Test of writeFragmentsToFile method, of class FragmentCollectionWriterPARSEMIS.
     */
    @Test
    public void testWriteFragmentsToFile_4args() throws Exception {
        System.out.println("->Test: Writing fragments to file (PARSEMIS Format 4 args)");
        String file = TestConstants.PARSEMISResultFolder + "resultsCollectionInParsemisFormat.lg";
        String outputPath = "testFragmentsParsemis";        
        HashMap<String, String> replacements = null;
        try{
            FragmentCollectionReaderPARSEMISGspan fcr = new FragmentCollectionReaderPARSEMISGspan(file);
            HashMap<String, Fragment> fc = fcr.getFragmentCatalogAsHashMap() ;
            FragmentCollectionWriterPARSEMIS instance = new FragmentCollectionWriterPARSEMIS();
            instance.writeFragmentsToFile(fc, outputPath, replacements,fcr.getOccurrencesOfFragmentInTransaction());
        }catch(Exception e){
            fail("There was a problem when writing the fragment files in Parsemis");
        }
    }
}
