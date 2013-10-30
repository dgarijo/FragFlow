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
package Factory.Inference;

import Factory.OPMWTemplate2GraphProcessor;
import Graph.Graph;
import Graph.GraphCollection;
import GraphNode.GraphNode;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class is used to create an abstract graph with a hashmap of replacements.
 * The input is a graph (typically with the reduced format), and the output
 * will be an abstract graph of the current one. Note that the URIs of the graph
 * won't change; just its types.
 * @author Daniel Garijo
 */
public class CreateAbstractResource {
    
    /**
     * Function that given a replacement Hashmap and a Graph it returns the same
     * graph with the types of the nodes abstracted according to the taxonomy. If
     * a type is already abstracted, no changes are performed
     * @param g
     * @param replacements
     * @return 
     */
    public static Graph createAbstractGraph(Graph g, HashMap<String, String> replacements){
        HashMap<String,GraphNode> nodes = g.getNodes();
        Iterator<String> nodeKeyIterator =  nodes.keySet().iterator();
        while(nodeKeyIterator.hasNext()){
            String currentKey = nodeKeyIterator.next();
            GraphNode currentNode = nodes.get(currentKey);
            if(replacements.containsKey(currentNode.getType())){
                currentNode.setType(replacements.get(currentNode.getType()));
            }
        }
        return g;
    }
    
    public static GraphCollection createAbstractCollection(GraphCollection gc, HashMap<String, String> replacements){
        Iterator<Graph> graphIterator =  gc.getGraphCollection().iterator();
        while(graphIterator.hasNext()){
            Graph currentGraph = graphIterator.next();
            currentGraph = createAbstractGraph(currentGraph, replacements);
        }
        return gc;
    }
    
    
    //static test
//    public static void main(String [] args){
//            String taxonomyFilePath = "src\\TestFiles\\multiDomainOnto.owl"; //we assume the file has already been created.
//            String outputFilePath = "AbstractFragmentGraphCollection";
//            //process the domain
////            OPMWTemplate2GraphProcessor tp = new OPMWTemplate2GraphProcessor(endpoint);
////            tp.transformDomainToSubdueGraph(domain);
//            //create the hashmap for replacements with the taxonomy
//            OntModel o = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
//            InputStream in = FileManager.get().open(taxonomyFilePath);
//            o.read(in, null);        
//            HashMap replacements = CreateHashMapForInference.createReplacementHashMap(o);
//            
//            OPMWTemplate2GraphProcessor test = new OPMWTemplate2GraphProcessor("http://wind.isi.edu:8890/sparql");
//            test.transformToSubdueGraph("http://www.opmw.org/export/resource/WorkflowTemplate/FEATUREGENERATION");
//            
//            Graph a = CreateAbstractGraphFromGraph.createAbstract(test.getGraphCollection().getGraphCollection().get(0),replacements);
//            System.out.println(a.getName()+"number of nodes "+a.getNumberOfNodes());
//    }
    
}
