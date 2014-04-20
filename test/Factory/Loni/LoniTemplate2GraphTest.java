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

import DataStructures.Graph;
import Static.TestConstants;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Daniel Garijo
 */
public class LoniTemplate2GraphTest {
    
    public LoniTemplate2GraphTest() {
    }


    /**
     * Test of transformToGraph method, of class LoniTemplate2Graph.
     */
    @Test
    public void testTransformToGraph() {
        System.out.println("-->TEST: Check transformation of a LONI template");
        LoniTemplate2Graph test = new LoniTemplate2Graph(TestConstants.aLoniTemplatePathForTests);
        test.transformToGraph("DTI_workflow.pipe.xml");
        Graph trace = test.getGraphCollection().getGraphs().get(0);
        //this template has 3 Modules. We don't care about them. If they have been read properly, the test is ok.
        assertEquals(3, trace.getURIs().size());
    }
}
