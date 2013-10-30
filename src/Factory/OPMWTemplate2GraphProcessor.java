/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Factory;

import Static.GeneralConstants;
import Static.Templates.QueriesOPMWTempl;
import Graph.Graph;
import GraphNode.GraphNode;
import GraphNode.GraphNodeTemplatesOPMW;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Resource;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class to process OPMW templates into SUBDUE graphs
 * @author Daniel Garijo
 */
public class OPMWTemplate2GraphProcessor extends GraphCollectionCreator{

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
    public void transformDomainToSubdueGraph(String domain){
        ResultSet rs = this.queryRepository(this.repositoryURI,QueriesOPMWTempl.getTemplatesFromSpecificDomain(domain) );        
        while (rs.hasNext()){
            QuerySolution qs = rs.next();
            Resource currentTrace = qs.getResource("?templ");
//            System.out.println(currentTrace.getURI());
            this.transformToSubdueGraph(currentTrace.getURI());
        }
    }

//    @Override
//    public void transformToSubdueGraph(ArrayList<String> URIs, String outputFileName) {
//        Iterator<String> it = URIs.iterator();
//        while(it.hasNext()){
//            String currentURI = it.next();
//            this.transformToSubdueGraph(currentURI);
//        }
//        this.collection.writeFullGraphsToFile(outputFileName);
//    }    
    

    @Override
    public void transformToSubdueGraph(String URI) {
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
//        Iterator<String> it = URIs.iterator();
        try{
//            FileWriter fstream = new FileWriter(this.outputGraph);
//            BufferedWriter out = new BufferedWriter(fstream);
//            while (it.hasNext()){
//                String currentURI = it.next();
//                SubdueGraphNode_TemplatesOPMW currNode = (SubdueGraphNode_TemplatesOPMW) nodes.get(currentURI);
//                System.out.println("v "+currNode.getNumberInGraph()+" "+currNode.getType());
//                out.write("v "+currNode.getNumberInGraph()+" "+currNode.getType());
//                out.newLine();
//            }
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
//                out.write("d "+nodes.get(artifact.getURI()).getNumberInGraph()+" "+ nodes.get(process.getURI()).getNumberInGraph()+" igb");                
//                out.newLine();
            }
            //test
//            for (int i = 0; i<adjacencyMatrix.length;i++){
//                for(int j = 0; j<adjacencyMatrix.length; j++){
//                    if(adjacencyMatrix[i][j]!=null) System.out.println("["+i+"],["+j+"]: "+adjacencyMatrix[i][j]);
//                }
//            }
//            out.close();            
            Graph graph = new Graph(URIs, nodes, adjacencyMatrix, URI);
            
//            *************test
//            FileWriter fstream = new FileWriter("TEstsSimplifiedStructure");
//            BufferedWriter out = new BufferedWriter(fstream);
//                graph.writeSimplifiedGraphToFile(out, 0);
//            out.close();
//            ************end test
            
            this.collection.addSubGraph(graph);
        }catch(Exception e){
            System.out.println("Error while writting the results: "+e.getMessage());
        }
        
        
    }
    
//    public static void main(String []args){
//        OPMWTemplate2GraphProcessor test = new OPMWTemplate2GraphProcessor("http://wind.isi.edu:8890/sparql");
//        test.transformToSubdueGraph("http://www.opmw.org/export/resource/WorkflowTemplate/MODELTHENCLASSIFIY");
//        //test.collection.
//    }

    
}
