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
package MainGraphProcessingScripts;

import Factory.Inference.CreateHashMapForInference;
import Factory.OPMWTemplate2GraphProcessor;
import Factory.OPMWTrace2GraphProcessor;
import IO.Formats.CollectionWriterSUBDUEFormat;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Script holding the main functionality of the project.
 * @author Daniel Garijo
 */
public class RetrieveTemplatesFromServerAndTransformThemToSUBDUEFiles {
    //Metod to test the different functionality of templates. It can be 
    //separated in several main methods if necessary
    public static void main(String [] args){
        try{
            //templates
//            OPMWTemplateProcessor p = new OPMWTemplateProcessor("http://wind.isi.edu:8890/sparql");
//            p.transformRespositoryToSubdueGraph("c");
//        esta clase se deberia llamar preprocessing
            
            //TESTS FOR THE EVALUATION OF THE TEMPLATES.
            OPMWTemplate2GraphProcessor tp = new OPMWTemplate2GraphProcessor("http://wind.isi.edu:8890/sparql");
            tp.transformDomainToGraph("TextAnalytics");
            //write down results (no inference, no taxonomy)
//            tp.getGraphCollection().writeFullGraphsToFile("Text_Analytics_Graph_No_Inference_Templates");
            CollectionWriterSUBDUEFormat writer = new CollectionWriterSUBDUEFormat();
            writer.writeFullGraphsToFile(tp.getGraphCollection(), "Text_Analytics_Graph_No_Inference_Templates");
//            tp.getGraphCollection().writeReducedGraphsToFile("Text_Analytics_Graph_No_Inference_Templates_Reduced");
            //write down in separate graphs for internal macro evaluation
//            tp.getGraphCollection().writeFullGraphsToSeparatedFiles("TESTS_NO_INFERENCE_TEXT_ANALYTICS_TEMPLATES");
            writer.writeFullGraphsToSeparatedFiles(tp.getGraphCollection(),"TESTS_NO_INFERENCE_TEXT_ANALYTICS_TEMPLATES");
            
            //INFERENCE
            //reading the taxonomy (already created)
            OntModel o = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
            InputStream in = FileManager.get().open("C:\\Users\\Monen\\Dropbox\\NetBeansProjects\\MotifFinder\\src\\Tests\\multiDomainOnto.owl");
            // read the RDF/XML file
            o.read(in, null);
            HashMap replacements = CreateHashMapForInference.createReplacementHashMap(o);
            
            writer.writeFullGraphsToFile(tp.getGraphCollection(),"Text_Analytics_Graph_Inference_Templates", replacements);            
            writer.writeFullGraphsToSeparatedFiles(tp.getGraphCollection(),"TESTS_INFERENCE_TEXT_ANALYTICS_templates", replacements);
        
            //TESTS FOR EVALUATION OF THE TRACES
            //creation of the graph collection
            OPMWTrace2GraphProcessor t = new OPMWTrace2GraphProcessor("http://wind.isi.edu:8890/sparql");
            t.transformDomainToGraph("TextAnalytics");
            //write down results (no inference, no taxonomy)
//            t.getGraphCollection().writeFullGraphsToFile("Text_Analytics_Graph_No_Inference");
            writer.writeFullGraphsToFile(t.getGraphCollection(),"Text_Analytics_Graph_No_Inference");
            //write down in separate graphs for internal macro evaluation
//            t.getGraphCollection().writeFullGraphsToSeparatedFiles("TESTS_NO_INFERENCE_TEXT_ANALYTICS");
            writer.writeFullGraphsToSeparatedFiles(t.getGraphCollection(), "TESTS_NO_INFERENCE_TEXT_ANALYTICS");
            
            
            //INFERENCE
            //reading the taxonomy (already created)
//            OntModel o = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
//            InputStream in = FileManager.get().open("C:\\Users\\Monen\\Documents\\NetBeansProjects\\MotifFinder\\src\\Tests\\multiDomainOnto.owl");
//            // read the RDF/XML file
//            o.read(in, null);
//            HashMap replacements = CreateHashMapForInference.createReplacementHashMap(o);
            
            //write down the whole inferred graph
//            t.getGraphCollection().writeFullGraphsToFile("Text_Analytics_Graph_Inference", replacements);
            writer.writeFullGraphsToFile(t.getGraphCollection(),"Text_Analytics_Graph_Inference", replacements);
//            t.getGraphCollection().writeFullGraphsToSeparatedFiles("TESTS_INFERENCE_TEXT_ANALYTICS", replacements);
            writer.writeFullGraphsToSeparatedFiles(t.getGraphCollection(),"TESTS_INFERENCE_TEXT_ANALYTICS", replacements);
        }catch(Exception e){
            System.err.println("Error while executing main method "+e.getMessage());
        }
       
    }
    
}
