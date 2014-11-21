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
package MainGraphProcessingScripts.SUBDUE;

import Factory.Inference.CreateHashMapForInference;
import Factory.OPMW.OPMWTemplate2GraphWings;
import Factory.OPMW.OPMWTrace2Graph;
import IO.Formats.SUBDUE.GraphCollectionWriterSUBDUE;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Script holding the main format steps to execute the clustering algorithms:
 * 1) Query the repository for certain structures.
 * 2) Save them into files accroding to SUBDUE's format.
 * 3) Abstract the collection and save it to a file.
 * @author Daniel Garijo
 */
public class STEP1OPMWTemplates2SUBDUEReduced {
    //Class made to test the subdue method with the new improvements made for filtering
    //thus, we use the reduced graph.
    public static void main(String [] args){
        try{
            //TESTS FOR THE EVALUATION OF THE TEMPLATES.
            OPMWTemplate2GraphWings tp = new OPMWTemplate2GraphWings("http://wind.isi.edu:8890/sparql");
            tp.transformDomainToGraph("TextAnalytics");
            //write down results (no inference, no taxonomy)
//            tp.getGraphCollection().writeFullGraphsToFile("Text_Analytics_Graph_No_Inference_Templates");
            GraphCollectionWriterSUBDUE writer = new GraphCollectionWriterSUBDUE();
            writer.writeFullGraphsToFile(tp.getGraphCollection(), "Templates_No_Inference_Text_Analytics");
            writer.writeReducedGraphsToFile(tp.getGraphCollection(), "Templates_No_Inference_Text_Analytics");
            //por ahora no me interesa guardarlo en ficheros separados.
//            writer.writeFullGraphsToSeparatedFiles(tp.getGraphCollection(),"TESTS_NO_INFERENCE_TEXT_ANALYTICS_TEMPLATES");
            
            //INFERENCE
            //reading the taxonomy (already created)
            OntModel o = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
            InputStream in = FileManager.get().open("src\\TestFiles\\multiDomainOnto.owl");
            // read the RDF/XML file
            o.read(in, null);
            HashMap replacements = CreateHashMapForInference.createReplacementHashMap(o);
            
            writer.writeReducedGraphsToFile(tp.getGraphCollection(),"Templates_Inference_Text_Analytics", replacements);            
//            writer.writeFullGraphsToSeparatedFiles(tp.getGraphCollection(),"TESTS_INFERENCE_TEXT_ANALYTICS_templates", replacements);

        }catch(Exception e){
            System.err.println("Error while executing main method "+e.getMessage());
        }
       
    }
    
}
