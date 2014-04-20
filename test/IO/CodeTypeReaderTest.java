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
import java.util.HashMap;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test to check whether the code type reader works fine.
 * @author Daniel Garijo
 */
public class CodeTypeReaderTest {
    
    public CodeTypeReaderTest() {
    }

    /**
     * Test of getTypesForCodes method, of class CodeTypeReader.
     */
    @Test
    public void testGetTypesForCodes() throws Exception {
        System.out.println("-->TEST: Reading a file with codes for types");
        String path = TestConstants.aCodedFileForReadingTests;        
        HashMap result = CodeTypeReader.getTypesForCodes(path);
        assertEquals(true, result.size()>0);
        assertNotNull(result);
    }
}
