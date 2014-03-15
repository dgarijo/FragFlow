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
package IO.Formats.OPMW;

import DataStructures.Graph;
import DataStructures.GraphNode.GraphNode;
import Static.GeneralConstants;
import Static.GeneralMethods;
import Static.Vocabularies.OPMWTemplate;
import Static.Vocabularies.OPMWTrace;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Fragment validation needs to issue queries against templates. These queries
 * can either be issued to a SPARQL endpoint or to a local repository, by 
 * reconverting the graphs to SPARQL. This happens specifically when dealing 
 * with abstractions: in order to find the abstract fragment in a template we 
 * need to create an abstraction of the template. We then transform the 
 * abstraction to RDF and perform the query against it.
 * 
 * @author Daniel Garijo
 */
public class Graph2OPMWRDFModel {
    
    /**
     * Given a graph, this method produces a representation of the graph using
     * the OPMW for templates model (that is, using opmw:uses and 
     * opmw:isGeneratedBy)
     * @param template graph to convert in RDF
     * @return the Jena model with the graph loaded.
     */
    public static OntModel graph2OPMWTemplate(Graph template){
        return generateModel(OPMWTemplate.USES, OPMWTemplate.IS_GEN_BY, template, true);
    }
    
    /**
     * Given a graph, this method produces a representation of the graph using
     * the OPMW for traces model (that is, using opmv:used and 
     * opmv:wasGeneratedBy)
     * @param template graph to convert in RDF
     * @return the Jena model with the graph loaded.
     */
    public static OntModel graph2OPMWTrace(Graph trace){
        return generateModel(OPMWTrace.USED, OPMWTrace.WAS_GEN_BY, trace, false);
    }
    
    /**
     * Given a structure, this method generates an RDF graph from its adjacency 
     * matrix. For every given usage dependency, it will add the "usageDep" 
     * property. For every generation dependency, it will add a "generationDep"
     * dependency. 
     * @param usageDep usage dependency uri
     * @param generationDep generation dependency uri
     * @param structure structure to convert.
     * @param isTemplate boolean indicating whether the structure sent is a template or not.
     * @return resultant model.
     */
    private static OntModel generateModel(String usageDep, String generationDep, Graph structure, boolean isTemplate){
        //here there are no substructures; all is well defined
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        String[][] matrix = structure.getAdjacencyMatrix();
        ArrayList<String> structureURIs = structure.getURIs();
        HashMap<String,GraphNode> nodes = structure.getNodes();
        for(int i=1; i<matrix.length;i++){
            //add the i URIs and types. The j ones are not needed, as they are the i's as well.
            String currentURI = structureURIs.get(i-1);
            GeneralMethods.addIndividual(m, currentURI,nodes.get(currentURI).getType(), null);
            if(isTemplate){
                //all the nodes in a fragment are steps (if it is reduced)
                GeneralMethods.addProperty(m, currentURI, structure.getName(), OPMWTemplate.IS_STEP_OF_TEMPL);
            }else{
                GeneralMethods.addProperty(m, currentURI, structure.getName(), OPMWTrace.WORKFLOW_OPMO_ACCOUNT);
            }
            for(int j=1; j<matrix.length;j++){
                if(matrix[i][j]!=null && matrix[i][j].equals(GeneralConstants.GENERATION_DEPENDENCY)){ 
                    GeneralMethods.addProperty(m, currentURI, structureURIs.get(j-1), generationDep);
                }else if(matrix[i][j]!=null && matrix[i][j].equals(GeneralConstants.USAGE_DEPENDENCY)){ 
                    GeneralMethods.addProperty(m, currentURI, structureURIs.get(j-1), usageDep);
                }
            }
        }
        return m;
    }
    
}
