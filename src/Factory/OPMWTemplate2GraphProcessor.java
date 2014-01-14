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
package Factory;

import Static.GeneralConstants;
import Static.Templates.QueriesOPMWTempl;
import DataStructures.Graph;
import DataStructures.GraphNode.GraphNode;
import DataStructures.GraphNode.GraphNodeTemplatesOPMW;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Resource;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class to process OPMW templates into Graphs
 * @author Daniel Garijo
 */
public class OPMWTemplate2GraphProcessor extends GraphCollectionCreator{

    /**
     * Creation method
     * @param repositoryURI 
     */
    public OPMWTemplate2GraphProcessor(String repositoryURI) {
        super(repositoryURI);
    }

//    @Override
//    public void transformRespositoryToSubdueGraph(String outputFileName) {
//        @@TO DO
//        to do: get the rest of the templates from the repository
//        this.transformToSubdueGraph("http://www.opmw.org/export/resource/WorkflowTemplate/ABSTRACTGLOBALWORKFLOW2");
//        this.transformToSubdueGraph("http://www.opmw.org/export/resource/WorkflowTemplate/ABSTRACCTSUBWFFATCAT");       
////        this.collection.writeToFile(outputFileName);
//        
//        to do: concrete tests with all the templates of a domain
//    }
    
    @Override
    public void transformDomainToGraph(String domain){
        ResultSet rs = this.queryRepository(this.repositoryURI,QueriesOPMWTempl.getTemplatesFromSpecificDomain(domain) );        
        while (rs.hasNext()){
            QuerySolution qs = rs.next();
            Resource currentTrace = qs.getResource("?templ");
//            System.out.println(currentTrace.getURI());
            this.transformToGraph(currentTrace.getURI());
        }
    }    

    @Override
    public void transformToGraph(String URI) {
        int countNodes = 1;
        System.out.println("Processing "+ URI);        
        //we store the ordered list in the arrayList. We store the types, etc in the 
        //hashmap. This way we avoid consecutive searches (using the hashmap), and 
        //reordering at the end.
        ArrayList<String> URIs = new ArrayList<String>();
        HashMap<String,GraphNode> nodes = new HashMap<String, GraphNode>();
        //retrieve NODES: variables, parameters (wtArtifacts) and wtProcesses and their types 
        //v NUMBER LABEL(type)
        ResultSet nodesAndTypes = queryRepository(repositoryURI, QueriesOPMWTempl.getWTArtifactsAndTypesOfTemplate(URI));        
        //clean types in nodes. We can only have 1 type on the graph.
        
        while(nodesAndTypes.hasNext()){
            QuerySolution q = nodesAndTypes.next();
            Resource currNode = q.getResource("?node");
            Resource type = q.getResource("?type");
            if(nodes.containsKey(currNode.getURI())){
                nodes.get(currNode.getURI()).setType(type.getURI());
            }else{
                nodes.put(currNode.getURI(), new GraphNodeTemplatesOPMW(currNode.getURI(), type.getURI(), countNodes));
                countNodes++;
                URIs.add(currNode.getURI());
            }
        }        
        try{
            //retrieve EDGES: uses, isGeneratedBy
            //d NUMBER NUMBER
            //creation of adjacency Matrix
            ResultSet usages = queryRepository(repositoryURI, QueriesOPMWTempl.getUsagesOfTemplate(URI));
            
            ResultSet generations = queryRepository(repositoryURI, QueriesOPMWTempl.getGenerationsOfTemplate(URI));
            
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
                       
            Graph graph = new Graph(URIs, nodes, adjacencyMatrix, URI);            
            this.collection.addSubGraph(graph);
        }catch(Exception e){
            System.out.println("Error while writting the results: "+e.getMessage());
        }
        
        
    }
    
}
