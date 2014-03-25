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
package MainGraphProcessingScripts.PARSEMIS;

import Factory.Inference.CreateHashMapForInference;
import Factory.OPMW.OPMWTemplate2Graph;
import IO.Formats.PARSEMIS.CollectionWriterPARSEMIS;
import Static.Configuration;
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
public class STEP1RetrieveOPMWTemplatesFromServerAndTransformThemToPARSEMISFiles {
    public static void main(String[] args){
        try{            
            OPMWTemplate2Graph tp = new OPMWTemplate2Graph("http://wind.isi.edu:8890/sparql");
            tp.transformDomainToGraph("TextAnalytics");        
            CollectionWriterPARSEMIS writer = new CollectionWriterPARSEMIS();            
            if (tp.getGraphCollection().getNumberOfSubGraphs()>1){
                writer.writeReducedGraphsToFile(tp.getGraphCollection(), Configuration.getPARSEMISInputPath()+"CollectionInParsemisFormat.lg");
                System.out.println("Regular collection retrieved successfully");
                //abstract collection
                OntModel o = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
                InputStream in = FileManager.get().open("src\\TestFiles\\multiDomainOnto.owl");
                // read the RDF/XML file
                o.read(in, null);
                HashMap replacements = CreateHashMapForInference.createReplacementHashMap(o);

                writer.writeReducedGraphsToFile(tp.getGraphCollection(),Configuration.getPARSEMISInputPath()+"CollectionInParsemisFormatABSTRACT.lg", replacements);
                System.out.println("Abstract collection retrieved successfully");
            }
        }catch(Exception e){
            System.out.println("Error: "+ e.getMessage());
            
        }
    }
}
