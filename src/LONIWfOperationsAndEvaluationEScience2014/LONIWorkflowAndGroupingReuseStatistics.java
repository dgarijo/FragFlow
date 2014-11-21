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

package LONIWfOperationsAndEvaluationEScience2014;

import DataStructures.Graph;
import DataStructures.GraphCollection;
import DataStructures.GraphNode.GraphNode;
import Factory.Loni.LoniTemplate2Graph;
import Factory.Loni.LoniTemplateAndGroupings2Graph;
import IO.DatasetFilter;
import IO.Formats.OPMW.Graph2OPMWRDFModel;
import Static.GeneralConstants;
import Static.GeneralMethods;
import Static.Vocabularies.OPMWTemplate;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This script counts the number of LONI Pipeline workflows, the number of 
 * groupings in those workflows and prints statistics regarding how each of 
 * them has been used.
 * 
 * Input: a folder of LONI pipeline Workflows
 * Output: Statistics on how the workflows and their groupings are reused
 * 
 * We need this class to assess whether the workflows and groupings are being
 * reused or not.
 * @author Daniel Garijo
 */
public class LONIWorkflowAndGroupingReuseStatistics {
    
    //Each wf or grouping should have a counter on the number of times found
    HashMap<String,Integer> workflowOccurrences;
    HashMap<String,Integer> groupingOccurrences;

    public LONIWorkflowAndGroupingReuseStatistics() {
        workflowOccurrences = new HashMap<String, Integer>();
        groupingOccurrences = new HashMap<String, Integer>();
    }
    
    
    
    //code copied from the fragment to sparql pafi. It is almost the same
    private String createQueryFromGraph(Graph g){//, String structure){
        String query = "SELECT ";
        String[][] depMatrix = g.getAdjacencyMatrix();
        ArrayList<String> graphURIs = g.getURIs();
        HashMap<String,GraphNode> graphNodes = g.getNodes();
        String steps = "";
        //i+1 pq la matriz tiene la primera fila a nulls... hay que arreglar eso
        for(int i=0; i<graphURIs.size();i++){
            query+=" ?node"+(i+1);
//            steps+="?node"+(i+1)+" a <"+graphNodes.get(graphURIs.get(i)).getType()+">.\n";
       //     steps+="?node"+(i+1)+" <"+OPMWTemplate.IS_STEP_OF_TEMPL+"> <"+structure+">.\n"; (not needed)
        }        
        query+=" WHERE{\n";
        query+=steps;
        //we just have to look to the adjacency matrix for performing the right query
        for(int i=0; i < depMatrix.length;i++){
            for(int j=0; j < depMatrix.length; j++){
                if(depMatrix[i][j]!=null && 
                    (depMatrix[i][j].equals(GeneralConstants.INFORM_DEPENDENCY))){
                    query+="{"
                            + "SELECT ?node"+i+" ?node"+j+" WHERE{\n"
                            + "?node"+(i)+" a <"+graphNodes.get(graphURIs.get(i-1)).getType()+">.\n"
                            + "?node"+(j)+" a <"+graphNodes.get(graphURIs.get(j-1)).getType()+">.\n";
                    query+="?node"+i+" <"+OPMWTemplate.USES+"> / <"+OPMWTemplate.IS_GEN_BY+"> ?node"+j+".}\n"
                            + "}\n";
                }
            }
        }
        query+="}";
        return query;
    }
    
    
    private String getWorkflowReuse(String folderPath) {
        //read the dataset of workflows
        File f = new File(folderPath);
        LoniTemplate2Graph test = new LoniTemplate2Graph(folderPath);
        if(f.isDirectory()){
            File[] files = f.listFiles();
            for (File file : files) {
                test.transformToGraph(file.getName());
            }
        }
//        test.transformToGraph("cranium.loni.usc.edu_8001-13c5a718-dabe-48bb-8b67-4eda8e22ee21.pipe");
        GraphCollection filteredCollection = DatasetFilter.removeDuplicates(test.getGraphCollection());
        Iterator<Graph> it = filteredCollection.getGraphs().iterator();
        String problematicWorkflows = "";
        while(it.hasNext()){
            //check the number of times each grah is found in the collection.
            Graph queryGraph = it.next();
            System.out.print("Dealing with "+queryGraph.getName());
            Iterator<Graph> it2 = filteredCollection.getGraphs().iterator();
            String currentQuery = createQueryFromGraph(queryGraph);//, currentGraph.getName());
            while(it2.hasNext()){
                Graph currentGraph = it2.next();
//                System.out.println("Comparing "+ queryGraph.getName()+" and "+currentGraph.getName());
                if(couldGraphBeIncluded(queryGraph, currentGraph)){
                    if(!queryGraph.getName().equals(currentGraph.getName())){//if they are the same don't do the query
                        OntModel o2 = Graph2OPMWRDFModel.graph2OPMWTemplate(currentGraph);
                        System.out.print(".");
//                        ResultSet rs = GeneralMethods.queryLocalRepository(o2, currentQuery);
                        Query query = QueryFactory.create(currentQuery);
                        QueryExecution qe = QueryExecutionFactory.create(query, o2);
                        qe.setTimeout(60000);//we have to set a limit because of the size of some of the graphs. A minute (60k millisec)
                        ResultSet rs = qe.execSelect(); 
                        try{
                            if(rs.hasNext()){
                                System.out.println("\n"+queryGraph.getName()+" found in "+currentGraph.getName());
                                addWorkflowOccurrence(queryGraph.getName());
                            }
                        }catch(Exception e){
                            System.err.println("\n Query between "+queryGraph.getName()+" and "+currentGraph.getName()+" failed ");
                            //here do another query slicing the main query into smaller ones...
                            problematicWorkflows+= queryGraph.getName()+" with "+ currentGraph.getName()+"\n";
                        }
                        qe.close();
                        o2.close();
                    }else{
                        addWorkflowOccurrence(queryGraph.getName());
                    }
                }
            }
            System.out.println();
        }
        String stats = "Size of the collection: "+filteredCollection.getGraphs().size()+"\n";
        stats+="Workflows reused at least 1 time in another workflow:\n";
        stats+= getStats(workflowOccurrences, 2);
        stats+= "Problematic workflows: ";
        stats+= problematicWorkflows;
//        stats+="Workflows reused at least 4 times:\n";
//        stats+= getStats(workflowOccurrences, 4);
        return stats;
    }
    
    private String getGroupingReuse(String folderPath){
        //step 1: read the workflows.
        File f = new File(folderPath);        
        //step 1: read workflows AND grouping
        LoniTemplateAndGroupings2Graph wfAndGroupings = new LoniTemplateAndGroupings2Graph(folderPath);
        if(f.isDirectory()){
            File[] files = f.listFiles();
            for (File file : files) {
                wfAndGroupings.transformToGraph(file.getName());
            }
        }
        GraphCollection filteredWfAndGroupingCollection = DatasetFilter.removeDuplicates(wfAndGroupings.getGraphCollection());
        
        String stats = "Size of the collection: "+filteredWfAndGroupingCollection.getNumberOfSubGraphs()+"\n";
        ArrayList <Graph> groupings = filteredWfAndGroupingCollection.getGraphs();
        ArrayList <Graph> workflows = new ArrayList<Graph>();//instead of rereading, we separate them
        //step 2: separate workflows from grouping collection (we don't want them mixed here now)
        int i=0;
        while(i<groupings.size()){
            Graph wfToRemove = groupings.get(i);
            if(wfToRemove.getName().endsWith(".PIPE")||wfToRemove.getName().endsWith(".XML")){
                groupings.remove(wfToRemove);
                workflows.add(wfToRemove);
            }
            else{
                i++;
            }
        }
        //step 4: Search groupings in workflows
        Iterator<Graph> itGroupings = groupings.iterator();
        String problematicGroupings = "";
        while(itGroupings.hasNext()){
            //check the number of times each grah is found in the collection.
            Graph queryGroupingGraph = itGroupings.next();
            System.out.print("Dealing with "+queryGroupingGraph.getName());
            Iterator<Graph> itWorkflows = workflows.iterator();
            String currentQuery = createQueryFromGraph(queryGroupingGraph);//, currentGraph.getName());
            while(itWorkflows.hasNext()){
                Graph currentWorkflow = itWorkflows.next();
//                System.out.println("Comparing "+ queryGraph.getName()+" and "+currentGraph.getName());
                if(couldGraphBeIncluded(queryGroupingGraph, currentWorkflow)){
                    if(!queryGroupingGraph.getName().equals(currentWorkflow.getName())){//if they are the same don't do the query
                        OntModel o2 = Graph2OPMWRDFModel.graph2OPMWTemplate(currentWorkflow);
                        System.out.print(".");
                        Query query = QueryFactory.create(currentQuery);
                        QueryExecution qe = QueryExecutionFactory.create(query, o2);
                        qe.setTimeout(60000);
                        ResultSet rs = qe.execSelect();                        
                        try{
                            if(rs.hasNext()){
                                System.out.println("\n"+queryGroupingGraph.getName()+" found in "+currentWorkflow.getName());
                                addGroupingOccurrence(queryGroupingGraph.getName());
                            }
                        }catch(Exception e){
                            System.err.println("\n Query between "+queryGroupingGraph.getName()+" and "+currentWorkflow.getName()+" failed\n");
                            problematicGroupings+=queryGroupingGraph.getName()+" with workflow "+currentWorkflow.getName()+"\n";
                        }
                        qe.close();
                        o2.close();
                    }else{
                        addGroupingOccurrence(queryGroupingGraph.getName());
                    }
                }
            }
            System.out.println();
        }
        
        stats+= "Groupings: "+groupings.size()+"\n";
        stats+= "Workflows: "+workflows.size()+"\n";
        stats+="Groupings reused at least 2 in two different workflows:\n";
        stats+= getStats(groupingOccurrences, 2);
        stats+= "Problematic groupings: ";
        stats+= problematicGroupings;
//        stats+="Groupings reused at least 4 times:\n";
//        stats+= getStats(groupingOccurrences, 4);
        return stats;
    }
    
    private String getStats(HashMap <String,Integer> occurrences, int minFreq){
        Iterator<String> keys = occurrences.keySet().iterator();
        String toReturn = "";
        int numberOfUsedTimes = 0;
        int mostReusedStructure = -1;
        int totalReusedStructures = 0;
        while(keys.hasNext()){
            String currentKey = keys.next();
            int freq = occurrences.get(currentKey);
            if(freq>=minFreq){
                toReturn+=currentKey+" appeared "+freq+" times\n";
                totalReusedStructures++;
                numberOfUsedTimes+=freq;
                if(mostReusedStructure<freq){
                    mostReusedStructure = freq;
                }
            }
        }
        toReturn="Total reused structures: "+totalReusedStructures +"\n" +
                "Number of times the structures have been used in the corpus: "+numberOfUsedTimes+"\n"+
                "Structure most reused was reused "+mostReusedStructure+" times\n"+toReturn;
        return toReturn;
    }
    
    private void addWorkflowOccurrence(String uri){
        if(workflowOccurrences.containsKey(uri)){
            workflowOccurrences.put(uri, workflowOccurrences.get(uri)+1);
        }else{
            workflowOccurrences.put(uri, 1);
        }
    }
    
    private void addGroupingOccurrence(String uri){
        if(groupingOccurrences.containsKey(uri)){
            groupingOccurrences.put(uri, groupingOccurrences.get(uri)+1);
        }else{
            groupingOccurrences.put(uri, 1);
        }
    }
    /**
     * true if the input graph could be included in the output graph
     * @return 
     */
    private boolean couldGraphBeIncluded(Graph inputToBeFound, Graph generalGraph){
        if(inputToBeFound.getNumberOfNodes()<=generalGraph.getNumberOfNodes()){
            //other checkings to do here, like check the dependencies type.
            return(inputToBeFound.getNumberOfNodes()>1 && hasConnection(inputToBeFound));//at least one grouping and one connection
        }else{
            return false;
        }
    }
    
    /**
     * Method to determine wether the current graph has a connection.
     * We need this because some groupings are really strange (e.g., separated 
     * components with no connections)
     * @param input
     * @return 
     */
    private boolean hasConnection(Graph input){
        String[][] adjMatrix = input.getAdjacencyMatrix();
        for(int i=0; i< adjMatrix.length; i++){
            for(int j = 0; j< adjMatrix.length; j++){
                if(adjMatrix [i][j]!=null){
                    return true;
                }
            }
        }
        return false;
    }
    
    public static void main(String [] args){
        String stats;
        //String escienceSam = "C:\\Users\\Monen\\Desktop\\LONIDatasets\\datasetSamuel\\";
        String escienceSam = "LONI_dataset\\datasetSamuel\\";
        String escienceBoris = "C:\\Users\\Monen\\Desktop\\LONIDatasets\\WorkflowBoris\\Pipeline123\\";//"C:\\Users\\Dani\\Desktop\\WC1\\Pipeline123\\";//
        String escienceIVO = "C:\\Users\\Monen\\Desktop\\LONIDatasets\\WorkflowIVO\\all\\";
        String escienceMonthly = "C:\\Users\\Monen\\Desktop\\LONIDatasets\\dataset3months(Zhizhong)\\2014-01\\";//"C:\\Users\\Dani\\Desktop\\WC3\\2014-01\\";
        
        
        stats = "";
        
        //boris' stats
//        stats += new LONIWorkflowAndGroupingReuseStatistics().getWorkflowReuse(escienceBoris);
//        stats += new LONIWorkflowAndGroupingReuseStatistics().getGroupingReuse(escienceBoris);
        
        //Ivo's stats
//        stats += new LONIWorkflowAndGroupingReuseStatistics().getWorkflowReuse(escienceIVO);
//        stats += new LONIWorkflowAndGroupingReuseStatistics().getGroupingReuse(escienceIVO);
        
        //monthly stats
        stats += new LONIWorkflowAndGroupingReuseStatistics().getWorkflowReuse(escienceMonthly);
        stats += new LONIWorkflowAndGroupingReuseStatistics().getGroupingReuse(escienceMonthly);
        
        //stats = "Samuel's dataset\n";
//        stats += new LONIWorkflowAndGroupingReuseStatistics().getWorkflowReuse(escienceSam);
//        stats += new LONIWorkflowAndGroupingReuseStatistics().getGroupingReuse(escienceSam);
       
        
        System.out.println(stats);
    }
}
