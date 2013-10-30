/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TestsGraphProcessing;

import Factory.Inference.CreateHashMapForInference;
import Factory.OPMWTemplate2GraphProcessor;
import Factory.OPMWTrace2GraphProcessor;
import Persistence.Formats.CollectionWriterSUBDUEFormat;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import java.io.InputStream;
import java.util.HashMap;

/**
 *
 * @author Daniel Garijo
 */
public class main {
    //Metod to test the different functionality of templates. It can be 
    //separated in several main methods if necessary
    public static void main(String [] args){
            //templates
//            OPMWTemplateProcessor p = new OPMWTemplateProcessor("http://wind.isi.edu:8890/sparql");
//            p.transformRespositoryToSubdueGraph("c");
//        esta clase se deberia llamar preprocessing
            
            //TESTS FOR THE EVALUATION OF THE TEMPLATES.
            OPMWTemplate2GraphProcessor tp = new OPMWTemplate2GraphProcessor("http://wind.isi.edu:8890/sparql");
            tp.transformDomainToSubdueGraph("TextAnalytics");
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
            t.transformDomainToSubdueGraph("TextAnalytics");
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
       
    }
    
}
