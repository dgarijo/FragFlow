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
package Factory.Loni;

import java.io.File;
import DataStructures.Graph;
import Static.TestConstants;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Daniel Garijo
 */
public class LoniTemplate2GraphFolderTest {
    
    public LoniTemplate2GraphFolderTest() {
    }


    /**
     * Test of transformToGraph method, of class LoniTemplate2Graph.
     */
    @Test
    public void testTransformToGraph() {
        System.out.println("-->TEST: Check transformation of a LONI folder");
        String loniDatasetFolder = TestConstants.aLoniTemplatePathForTests;
        File f = new File(loniDatasetFolder);
        if(f.isDirectory()){
            File[] files = f.listFiles();
            LoniTemplate2Graph test = new LoniTemplate2Graph(loniDatasetFolder);
            for(int i=0;i<files.length;i++){
                test.transformToGraph(files[i].getName());
            }
            assertEquals(true, test.getGraphCollection().getNumberOfSubGraphs()>0);
        }else{
            fail("Directory not found");
        }
    }
}
