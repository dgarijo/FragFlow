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
package Factory.OPMW;

import Static.GeneralConstants;
import Static.Query.QueriesOPMWTempl;
import DataStructures.Graph;
import DataStructures.GraphNode.GraphNode;
import DataStructures.GraphNode.OPMW.GraphNodeTemplatesOPMWTaverna;
import Factory.GraphCollectionCreator;
import Static.GeneralMethods;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class to process OPMW templates into Graphs.
 * Specialization for the Tavenra Workflows provided by Beatriz Garcia and
 * Mark Wilkinson.
 * @author Daniel Garijo
 */
public class OPMWTemplate2GraphTaverna extends GraphCollectionCreator{

    /**
     * Creation method
     * @param folderName 
     */
    public OPMWTemplate2GraphTaverna(String folderName) {
        super(folderName);
    }    

    @Override
    public void transformToGraph(String fileName) {
        int countNodes = 1;
        System.out.println("Processing "+ fileName);        
        
        //load the rdf file into a repository, locally
        OntModel currentWorkflow = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        try{
            currentWorkflow.read(new FileInputStream(this.repositoryURI+File.separator+fileName),"http://example.org");
        }catch(Exception e){
            System.err.println("Error while reading the file "+fileName+". Mesage: "+e.getMessage());
            return;
        }
        //we store the ordered list in the arrayList. We store the types, etc in the 
        //hashmap. This way we avoid consecutive searches (using the hashmap), and 
        //reordering at the end.
        ArrayList<String> URIs = new ArrayList<String>();
        HashMap<String,GraphNode> nodes = new HashMap<String, GraphNode>();
        //retrieve NODES: variables, parameters (wtArtifacts) and wtProcesses and their types 
        //v NUMBER LABEL(type)
        ResultSet nodesAndTypes = GeneralMethods.queryLocalRepository(currentWorkflow, QueriesOPMWTempl.getAllWTArtifactsAndTypesOfTemplate());        
        //clean types in nodes. We can only have 1 type on the graph.
        
        while(nodesAndTypes.hasNext()){
            QuerySolution q = nodesAndTypes.next();
            Resource currNode = q.getResource("?node");
            Resource type = q.getResource("?type");
            if(nodes.containsKey(currNode.getURI())){
                nodes.get(currNode.getURI()).setType(type.getURI());
            }else{
                nodes.put(currNode.getURI(), new GraphNodeTemplatesOPMWTaverna(currNode.getURI(), type.getURI(), countNodes));
                countNodes++;
                URIs.add(currNode.getURI());
            }
        }        
        try{
            //retrieve EDGES: uses, isGeneratedBy
            //d NUMBER NUMBER
            //creation of adjacency Matrix
            ResultSet usages = GeneralMethods.queryLocalRepository(currentWorkflow, QueriesOPMWTempl.getAllUsagesOfTemplate());
            
            ResultSet generations = GeneralMethods.queryLocalRepository(currentWorkflow, QueriesOPMWTempl.getAllGenerationsOfTemplate());
            
            String[][] adjacencyMatrix = new String[countNodes][countNodes]; //we ignore the [0][0] lines            
        
            while (usages.hasNext()){
                QuerySolution qs = usages.next();
                Resource process = qs.getResource("?process");
                Resource artifact = qs.getResource("?artif");
//                System.out.println("d "+nodes.get(process.getURI()).getNumberInGraph()+" "+ nodes.get(artifact.getURI()).getNumberInGraph()+" uses");
                adjacencyMatrix [nodes.get(process.getURI()).getNumberInGraph()][nodes.get(artifact.getURI()).getNumberInGraph()] = GeneralConstants.USAGE_DEPENDENCY;
//                out.write("d "+nodes.get(process.getURI()).getNumberInGraph()+" "+ nodes.get(artifact.getURI()).getNumberInGraph()+" uses");
//                out.newLine();
            }
            while (generations.hasNext()){
                QuerySolution qs = generations.next();
                Resource process = qs.getResource("?process");
                Resource artifact = qs.getResource("?artif");
//                System.out.println("d "+nodes.get(artifact.getURI()).getNumberInGraph()+" "+ nodes.get(process.getURI()).getNumberInGraph()+" igb");
                adjacencyMatrix [nodes.get(artifact.getURI()).getNumberInGraph()][nodes.get(process.getURI()).getNumberInGraph()] = GeneralConstants.GENERATION_DEPENDENCY;
            }        
            Graph graph = new Graph(URIs, nodes, adjacencyMatrix, fileName);
            graph.putReducedNodesInAdjacencyMatrix();//to ease up things
            
            //if the number of infomr dependencies is 0, discard the workflow.
            if(countNumberOfInformDependencies(graph)>0){
                this.collection.addSubGraph(graph);
            }
        }catch(Exception e){
            System.out.println("Error while writing the results: "+e.getMessage());
        }
        
    }
    
    private int countNumberOfInformDependencies(Graph g){
        String[][] adjMatrix = g.getAdjacencyMatrix();
        int count =0 ;
        for(int i=0; i< adjMatrix.length; i++){
            for(int j=0; j< adjMatrix.length;j++){
                if(adjMatrix[i][j]!=null && adjMatrix[i][j].equals(GeneralConstants.INFORM_DEPENDENCY)){
                    count++;
                }
            }
        }
        return count;
    }
    
}
