/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
package TestsGraphProcessing.OPMWWings;

import DataStructures.Graph;
import DataStructures.GraphCollection;
import Factory.Inference.CreateAbstractResource;
import Factory.Inference.CreateHashMapForInference;
import Factory.OPMW.OPMWTemplate2GraphWings;
import IO.Formats.OPMW.Graph2OPMWRDFModel;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Test designed to transform a Graph to an OPMW Template.
 * This transformation is useful to compare abstract fragments to asbtract 
 * resources.
 * @author Daniel Garijo
 */
public class Test22Graph2OPMWTemplate {
    public static int testNumber = 22;
    public static boolean test(){
        System.out.println("\n\nExecuting test:"+testNumber+" Graph to OPMW transformation");
        try{
            
            OPMWTemplate2GraphWings test = new OPMWTemplate2GraphWings("http://wind.isi.edu:8890/sparql");
            //test.transformToGraph("http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI");
            test.transformToGraph("http://www.opmw.org/export/resource/WorkflowTemplate/FEATUREGENERATION");
            Graph testGraph = test.getGraphCollection().getGraphs().get(0);
            testGraph.putReducedNodesInAdjacencyMatrix();
            String taxonomyFilePath = "src\\TestFiles\\multiDomainOnto.owl"; //we assume the file has already been created.
            OntModel o = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
            InputStream in = FileManager.get().open(taxonomyFilePath);
            o.read(in, null);        
            HashMap replacements = CreateHashMapForInference.createReplacementHashMap(o);

            //we create the abstract collection
            GraphCollection abstractCollection = CreateAbstractResource.createAbstractCollection(test.getGraphCollection(), replacements);
            Graph abstractG = abstractCollection.getGraphs().get(0);
            OntModel o2 = Graph2OPMWRDFModel.graph2OPMWTemplate(abstractG);
            //o2.write(System.out);
            if(o2.isEmpty())return false;
            return true;
        }catch(Exception e){
            System.out.println("Error in test PostProcessing. Exception: "+e.getMessage());
            return false;
        }
    }
    public static void main(String[] args){
        if(test())System.out.println("Test "+testNumber+" OK");
        else System.out.println("Test "+testNumber+" FAILED");
    }
}
