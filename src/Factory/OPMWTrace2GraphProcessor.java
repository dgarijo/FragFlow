/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Factory;

import Static.GeneralConstants;
import Static.Traces.ConstantsOPMWTraces;
import Static.Traces.QueriesOPMWTraces;
import Graph.Graph;
import GraphNode.GraphNode;
import GraphNode.GraphNodeTracesOPMW;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Resource;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class designed to represent graphs for the traces of a workflow
 * Since we support PROV, the relationships of the graph are used
 * and wasGeneratedBy. Other relations are not meaningful
 * @author Daniel Garijo
 */
public class OPMWTrace2GraphProcessor extends GraphCollectionCreator{

    public OPMWTrace2GraphProcessor(String repositoryURI) {
        super(repositoryURI);
    }

//    @Override
//    public void transformRespositoryToSubdueGraph(String outputFileName) {
//        /**
//         * @@TO DO
//         */
//        //super.transformRespositoryToSubdueGraph();
//        this.transformToSubdueGraph("http://www.opmw.org/export/resource/WorkflowExecutionAccount/ACCOUNT1348703551080");
//        this.transformToSubdueGraph("http://www.opmw.org/export/resource/WorkflowExecutionAccount/ACCOUNT1348621567824");
//        this.transformToSubdueGraph("http://www.opmw.org/export/resource/WorkflowExecutionAccount/ACCOUNT1348621579471");
////        this.collection.write(outputFileName);
//        
//    }
    
    /**
     * Method that given a domain, retrieves and parses the runs belonging to it.
     * @param domain Domain from which we aim to retrieve the traces. Example:
     * TextAnalytics.
     */
    @Override
    public void transformDomainToSubdueGraph(String domain){
        ResultSet rs = this.queryRepository(this.repositoryURI, QueriesOPMWTraces.getTracesFromSpecificDomain(domain));
//        System.out.println(QueriesOPMWTraces.getTracesFromSpecificDomain(domain));
//        System.out.println("Found "+rs.getRowNumber()+" results for domain "+domain);
        
        while (rs.hasNext()){
            QuerySolution qs = rs.next();
            Resource currentTrace = qs.getResource("?acc");
            this.transformToSubdueGraph(currentTrace.getURI());
        }
    }

//    @Override
//    public void transformToSubdueGraph(ArrayList<String> URIs, String outputFileName) {
//        /**
//         * @@TO DO
//         */
//    }
    

    /**
     * 
     * @return String with the URI of the domain catalog
     */
    private String getComponentDomainOntologyURI(String URI){
        ResultSet sample = this.queryRepository(repositoryURI, QueriesOPMWTraces.getComponentOntologyURI(URI));
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
     * We need a method 
     * @return String with the URI of the domain catalog
     */
    private String getDataDomainOntologyURI(String URI){
        ResultSet sample = this.queryRepository(repositoryURI, QueriesOPMWTraces.getDataOntologyURI(URI));
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
    public void transformToSubdueGraph(String URI) {        
        int countNodes = 1;
        System.out.println("Processing trace "+ URI);        
        //we store the ordered list in the arrayList. We store the types, etc in the 
        //hashmap. This way we avoid consecutive searches (using the hashmap), and 
        //reordering at the end.
        ArrayList<String> URIs = new ArrayList<String>();
        HashMap<String,GraphNode> nodes = new HashMap<String, GraphNode>();
        //retrieve NODES: variables, parameters (wtArtifacts) and wtProcesses and their types 
        
        ResultSet nodesAndTypes = queryRepository(repositoryURI, QueriesOPMWTraces.getWfExecNodes(URI));        
        
        //In Wings every run component belong to a different domain ontology, because of
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
        ResultSet processTypes = queryRepository(repositoryURI, QueriesOPMWTraces.getTypesOfWfExecProcesses(URI));        
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
            ResultSet usages = queryRepository(repositoryURI, QueriesOPMWTraces.getUsagesOfTrace(URI));
            
            ResultSet generations = queryRepository(repositoryURI, QueriesOPMWTraces.getGenerationsOfTrace(URI));
            
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
            //TESTS
//             FileWriter fstream = new FileWriter("TEstsSimplifiedStructure_traces");
//            BufferedWriter out = new BufferedWriter(fstream);
//            graph.writeFullGraphToFile(out, countNodes);
//            graph.writeSimplifiedGraphToFile(out, countNodes);
//            out.close();
            //END TESTS
        }catch(Exception e){
            System.out.println("Error while writting the results: "+e.getMessage());
        }             
        
    }
    
    /**
     * Various tests
     * @param args 
     */
//    public static void main(String[] args){
//        OPMWTrace2GraphProcessor tests = new OPMWTrace2GraphProcessor("http://wind.isi.edu:8890/sparql");
//        tests.transformToSubdueGraph("http://www.opmw.org/export/resource/WorkflowExecutionAccount/ACCOUNT1348703551080");
//    }
}
