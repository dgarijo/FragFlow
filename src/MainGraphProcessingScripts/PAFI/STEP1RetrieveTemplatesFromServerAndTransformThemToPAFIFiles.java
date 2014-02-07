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
package MainGraphProcessingScripts.PAFI;

import Factory.OPMW.OPMWTemplate2Graph;
import IO.Formats.PAFI.CollectionWriterPAFI;

/**
 * Script that retrieves the templates from the server and transforms it to PAFI 
 * format.
 * @author Daniel Garijo
 */
public class STEP1RetrieveTemplatesFromServerAndTransformThemToPAFIFiles {
    public static void main(String[] args){
        try{            
            OPMWTemplate2Graph tp = new OPMWTemplate2Graph("http://wind.isi.edu:8890/sparql");
            tp.transformDomainToGraph("TextAnalytics");        
            CollectionWriterPAFI writer = new CollectionWriterPAFI();            
            if (tp.getGraphCollection().getNumberOfSubGraphs()>1){
                writer.writeReducedGraphsToFile(tp.getGraphCollection(), "CollectionInPAFIFormat");
                System.out.println("Regular collection retrieved successfully");
            }
            //abstract collection
            OntModel o = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
            InputStream in = FileManager.get().open("src\\Tests\\multiDomainOnto.owl");
            // read the RDF/XML file
            o.read(in, null);
            HashMap replacements = CreateHashMapForInference.createReplacementHashMap(o);
            
            writer.writeFullGraphsToFile(tp.getGraphCollection(),"Text_Analytics_Graph_Inference_Templates", replacements);
        }catch(Exception e){
            System.out.println("Error: "+ e.getMessage());
            
        }
    }
}
