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
import Static.OPMW.Traces.QueriesOPMWTraces;
import DataStructures.Graph;
import DataStructures.GraphNode.GraphNode;
import DataStructures.GraphNode.OPMW.GraphNodeTracesOPMW;
import Factory.GraphCollectionCreator;
import Static.GeneralMethods;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Resource;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class designed to represent graphs for the traces of an OPMW workflow
 * Since we support PROV, the relationships of the graph are used
 * and wasGeneratedBy. Other relations are not meaningful
 * @author Daniel Garijo
 */
public class OPMWTrace2Graph extends GraphCollectionCreator{

    /**
     * Constructor method
     * @param repositoryURI 
     */
    public OPMWTrace2Graph(String repositoryURI) {
        super(repositoryURI);
    }

    
    /**
     * Method that given a domain, retrieves and parses the runs belonging to it.
     * @param domain Domain from which we aim to retrieve the traces. Example:
     * TextAnalytics.
     */
    @Override
    public void transformDomainToGraph(String domain){
        ResultSet rs = GeneralMethods.queryOnlineRepository(this.repositoryURI, QueriesOPMWTraces.getTracesFromSpecificDomain(domain));
//        System.out.println(QueriesOPMWTraces.getTracesFromSpecificDomain(domain));
//        System.out.println("Found "+rs.getRowNumber()+" results for domain "+domain);
        
        while (rs.hasNext()){
            QuerySolution qs = rs.next();
            Resource currentTrace = qs.getResource("?acc");
            this.transformToGraph(currentTrace.getURI());
        }
    }    

    /**
     * Sometimes we need to know the domain of a component, in order to associate it
     * with the right types and perform the right inferences. Thus this is a 
     * necessary workaround.
     * @return String with the URI of the domain catalog
     */
    private String getComponentDomainOntologyURI(String URI){
        ResultSet sample = GeneralMethods.queryOnlineRepository(repositoryURI, QueriesOPMWTraces.getComponentOntologyURI(URI));
//        System.out.println(QueriesOPMWTraces.getComponentOntologyURI(URI));        
        if(sample.hasNext()){
            QuerySolution qs = sample.next();
            return qs.getResource("?uri").getNameSpace();                   
        }else{
            return "http://globalNamespace#";
        }
    }
    
    /**
     * We need a method 
     * @return String with the URI of the domain catalog
     */
    private String getDataDomainOntologyURI(String URI){
        ResultSet sample = GeneralMethods.queryOnlineRepository(repositoryURI, QueriesOPMWTraces.getDataOntologyURI(URI));
//        System.out.println(QueriesOPMWTraces.getComponentOntologyURI(URI));
        //we just need one
        if(sample.hasNext()){
            QuerySolution qs = sample.next();
            return qs.getResource("?uri").getNameSpace();                   
        }else{
            return "http://globalNamespace#";
        }
    }
    
    /**
     * Method to trasform the URI of an OPMW workflow Execution account to a subdue graph.
     * Just the used/wasGeneratedBy edges are kept (meaningful for the structure of the workflow)
     * @param URI 
     */
    @Override
    public void transformToGraph(String URI) {        
        int countNodes = 1;
        System.out.println("Processing trace "+ URI);        
        //we store the ordered list in the arrayList. We store the types, etc in the 
        //hashmap. This way we avoid consecutive searches (using the hashmap), and 
        //reordering at the end.
        ArrayList<String> URIs = new ArrayList<String>();
        HashMap<String,GraphNode> nodes = new HashMap<String, GraphNode>();
        //retrieve NODES: variables, parameters (wtArtifacts) and wtProcesses and their types 
        
        ResultSet nodesAndTypes = GeneralMethods.queryOnlineRepository(repositoryURI, QueriesOPMWTraces.getWfExecNodes(URI));        
        
        //In Wings every run component belongs to a different domain ontology, because of
        //version control. Thus we take the type of the domain ontology
        //instead of the type of the run.
        String componentCatalogURI = getComponentDomainOntologyURI(URI);
        String dcdomCatalogURI = getDataDomainOntologyURI(URI);
        
        //clean types in nodes. We can only have 1 type on the graph.        
        while(nodesAndTypes.hasNext()){
            QuerySolution q = nodesAndTypes.next();
            Resource currNode = q.getResource("?node");
            Resource type = q.getResource("?type");
            String ty = type.getURI();
            
            //we change both the data and component URIs
            if(ty.contains("acdom"))
                ty = type.getURI().replace(type.getNameSpace(), componentCatalogURI);
            else if(ty.contains("dcdom")){
                ty = type.getURI().replace(type.getNameSpace(), dcdomCatalogURI);
            }
            
            //we filter the agents, as they are not relevant for the usage/generation
            if(!ty.contains("#Agent")){
                if(nodes.containsKey(currNode.getURI())){
                    nodes.get(currNode.getURI()).setType(ty);
                }else{                
                    nodes.put(currNode.getURI(), new GraphNodeTracesOPMW(currNode.getURI(), ty, countNodes));
                    countNodes++;
                    URIs.add(currNode.getURI());
                }
            }
        }
        
        //update the types of the processes
        ResultSet processTypes = GeneralMethods.queryOnlineRepository(repositoryURI, QueriesOPMWTraces.getTypesOfWfExecProcesses(URI));        
        while(processTypes.hasNext()){
            QuerySolution q = processTypes.next();
            Resource currNode = q.getResource("?process");
            Resource type = q.getResource("?type");
            String ty = type.getURI().replace(type.getNameSpace(), componentCatalogURI)+"Class";
            nodes.get(currNode.getURI()).setType(ty);            
        }
        
               
//        Iterator<String> it = URIs.iterator();
//        while (it.hasNext()){
//            String currentURI = it.next();
//            SubdueGraphNode currNode = (SubdueGraphNode) nodes.get(currentURI);
//            System.out.println("v "+currNode.getNumberInGraph()+" "+currNode.getType());           
//        }
        try{
            //retrieve EDGES: uses, isGeneratedBy
            //d NUMBER NUMBER
            //creation of adjacency Matrix
            ResultSet usages = GeneralMethods.queryOnlineRepository(repositoryURI, QueriesOPMWTraces.getUsagesOfTrace(URI));
            
            ResultSet generations = GeneralMethods.queryOnlineRepository(repositoryURI, QueriesOPMWTraces.getGenerationsOfTrace(URI));
            
            String[][] adjacencyMatrix = new String[countNodes][countNodes]; //we ignore the [0][0] lines            
            
            //usage dependencies
            while (usages.hasNext()){
                QuerySolution qs = usages.next();
                Resource process = qs.getResource("?process");
                Resource artifact = qs.getResource("?artif");
                adjacencyMatrix [nodes.get(process.getURI()).getNumberInGraph()][nodes.get(artifact.getURI()).getNumberInGraph()] = GeneralConstants.USAGE_DEPENDENCY;
            }
            
            //generation dependencies
            while (generations.hasNext()){
                QuerySolution qs = generations.next();
                Resource process = qs.getResource("?process");
                Resource artifact = qs.getResource("?artif");
                adjacencyMatrix [nodes.get(artifact.getURI()).getNumberInGraph()][nodes.get(process.getURI()).getNumberInGraph()] = GeneralConstants.GENERATION_DEPENDENCY;

            }           
            Graph graph = new Graph(URIs, nodes, adjacencyMatrix, URI);
            this.collection.addSubGraph(graph);            
        }catch(Exception e){
            System.out.println("Error while writting the results: "+e.getMessage());
        }             
        
    }
}
