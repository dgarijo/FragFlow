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
import com.hp.hpl.jena.query.QuerySolution;
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
    //this method should be generic.
    /**
     * Method that given a graph, creates a query. The start and end are the bits
     * of the graph we want to transform, e.g., start=0, end=depMatrix.length  
     * will convert the full graph. The whole graph is passed so the variables
     * keep their indexes.
     * @param g graph to convert
     * @param start index from which start the conversion
     * @param end index until the conversion should be done.
     * @return 
     */
    private String createQueryFromGraph(Graph g, int start, int end){//, String structure){
        String query = "SELECT distinct ";
        String[][] depMatrix = g.getAdjacencyMatrix();
        ArrayList<String> graphURIs = g.getURIs();
        HashMap<String,GraphNode> graphNodes = g.getNodes();    
        ArrayList<String> variables= new ArrayList<String>();
        String dependencies ="";
        //we just have to look to the adjacency matrix for performing the right query
        for(int i=start; i <end;i++){
            //we have to do all the columns.
            for(int j=0; j < depMatrix.length; j++){
                if(depMatrix[i][j]!=null && 
                    (depMatrix[i][j].equals(GeneralConstants.INFORM_DEPENDENCY))){
                    dependencies+="{"
                            + "SELECT distinct ?node"+i+" ?node"+j+" WHERE{\n"
                            + "?node"+(i)+" a <"+graphNodes.get(graphURIs.get(i-1)).getType()+">.\n"
                            + "?node"+(j)+" a <"+graphNodes.get(graphURIs.get(j-1)).getType()+">.\n";
                    dependencies+="?node"+i+" <"+OPMWTemplate.USES+"> / <"+OPMWTemplate.IS_GEN_BY+"> ?node"+j+".}\n"
//                    query+="?node"+i+" <http://www.opmw.org/ontology/informBy> ?node"+j+".}\n"
                            + "}\n";
                    if(!variables.contains("?node"+i)){
                        variables.add("?node"+i);
                    }
                    if(!variables.contains("?node"+j)){
                        variables.add("?node"+j);
                    }
                }
            }
        }
        if(variables.isEmpty()){
            return "";
        }
        for(String var:variables){
            query+=" "+var;
        }
        query+=" WHERE{\n"+dependencies;
        query+="}";
//        System.out.println(query);
        return query;
    }
    
    /**
     * Connected vars is an arraylist of integers which stores the connected vars for the query.
     * We have to iterate through it to see which are the connected vars.
     * @param g
     * @param connectedVars
     * @return 
     */
    private String createQueryFromGraph(Graph g,ArrayList<Integer> connectedVars, HashMap<String,String> previousValues){
        String query = "SELECT distinct ";
        String[][] depMatrix = g.getAdjacencyMatrix();
        ArrayList<String> graphURIs = g.getURIs();
        HashMap<String,GraphNode> graphNodes = g.getNodes();    
        ArrayList<String> variables= new ArrayList<String>();
        String dependencies ="";
        Iterator<Integer> it1 = connectedVars.iterator();
        while(it1.hasNext()){
            int i = it1.next();
            Iterator<Integer> it2 = connectedVars.iterator();
            while(it2.hasNext()){
                int j = it2.next();
                if(depMatrix[i][j]!=null && 
                    (depMatrix[i][j].equals(GeneralConstants.INFORM_DEPENDENCY))){
                    dependencies+="{"
                            + "SELECT distinct ?node"+i+" ?node"+j+" WHERE{\n"
                            + "?node"+(i)+" a <"+graphNodes.get(graphURIs.get(i-1)).getType()+">.\n"
                            + "?node"+(j)+" a <"+graphNodes.get(graphURIs.get(j-1)).getType()+">.\n";
                    dependencies+="?node"+i+" <"+OPMWTemplate.USES+"> / <"+OPMWTemplate.IS_GEN_BY+"> ?node"+j+".}\n"
//                    dependencies+="?node"+i+" <http://www.opmw.org/ontology/informBy> ?node"+j+".}\n"
                            + "}\n";
                    if(!variables.contains("?node"+i)){
                        variables.add("?node"+i);
                    }
                    if(!variables.contains("?node"+j)){
                        variables.add("?node"+j);
                    }
                }
            }
        }
        if(variables.isEmpty()){
            return "";
        }
        String valuesVar = "VALUES(";
        String prevValues = "";
        for(String var:variables){
            query+=" "+var;
            if(previousValues!=null && previousValues.containsKey(var)){
                valuesVar+=" "+var;
                prevValues+=" <"+previousValues.get(var)+"> ";
            }
        }
        query+=" WHERE{\n"+dependencies;
        //add vars here
        valuesVar +="){\n"+prevValues+"}\n";
        if(valuesVar.contains("?node")){
            query+=valuesVar;
        }
        query+="}";
        return query;
    }
    
    /**
     * Auxiliar method to do ask queries
     * @param g
     * @param connectedVars
     * @return 
     */
    private String createAskQueryFromGraph(Graph g,ArrayList<Integer> connectedVars){
        String query = "ASK ";
        String[][] depMatrix = g.getAdjacencyMatrix();
        ArrayList<String> graphURIs = g.getURIs();
        HashMap<String,GraphNode> graphNodes = g.getNodes();    
        ArrayList<String> variables= new ArrayList<String>();
        String dependencies ="";
        Iterator<Integer> it1 = connectedVars.iterator();
        while(it1.hasNext()){
            int i = it1.next();
            Iterator<Integer> it2 = connectedVars.iterator();
            while(it2.hasNext()){
                int j = it2.next();
                if(depMatrix[i][j]!=null && 
                    (depMatrix[i][j].equals(GeneralConstants.INFORM_DEPENDENCY))){
                    dependencies+="{"
                            + "SELECT distinct ?node"+i+" ?node"+j+" WHERE{\n"
                            + "?node"+(i)+" a <"+graphNodes.get(graphURIs.get(i-1)).getType()+">.\n"
                            + "?node"+(j)+" a <"+graphNodes.get(graphURIs.get(j-1)).getType()+">.\n";
                    dependencies+="?node"+i+" <"+OPMWTemplate.USES+"> / <"+OPMWTemplate.IS_GEN_BY+"> ?node"+j+".}\n"
//                    dependencies+="?node"+i+" <http://www.opmw.org/ontology/informBy> ?node"+j+".}\n"
                            + "}\n";
                    if(!variables.contains("?node"+i)){
                        variables.add("?node"+i);
                    }
                    if(!variables.contains("?node"+j)){
                        variables.add("?node"+j);
                    }
                }
            }
        }
        query+=" {\n"+dependencies;
        query+="}";//LIMIT 20";//it is very unlickely to have more than 20 instances of a workflow in another.
        return query;
    }
    
    
    private String getWorkflowReuse(String folderPath) {
        //to do: check if the folder is indeed correct
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
//            String currentQuery = createQueryFromGraph(queryGraph);//, currentGraph.getName());
            while(it2.hasNext()){
                Graph currentGraph = it2.next();
//                System.out.println("Comparing "+ queryGraph.getName()+" and "+currentGraph.getName());
                  if(isGraphIncludedIn(queryGraph, currentGraph)){
                      addWorkflowOccurrence(queryGraph.getName());
                  }
            }
            System.out.println();
        }
        String stats = "Size of the unfiltered collection: "+test.getGraphCollection().getGraphs().size()+"\n";
        stats+= "Size of the collection (filtered): "+filteredCollection.getGraphs().size()+"\n";
        stats+="Workflows reused at least 1 time in another workflow...\n";
        stats+= getStats(workflowOccurrences, 2);
        stats+= "Problematic workflows: ";
        stats+= problematicWorkflows;
//        stats+="Workflows reused at least 4 times:\n";
//        stats+= getStats(workflowOccurrences, 4);
        return stats;
    }
    
    /**
     * Method to check whether a graph is included in another or not.
     * @param source
     * @param biggerGraph
     * @return 
     */
    private boolean isGraphIncludedIn(Graph source, Graph biggerGraph){
        if(couldGraphBeIncluded(source, biggerGraph)){
            if(!source.getName().equals(biggerGraph.getName())){//if they are the same don't do the query
                System.out.print(".");
                ArrayList<Integer> variablesForQuery = new ArrayList<Integer>();
                for(int i=1; i<source.getAdjacencyMatrix().length;i++){
                    variablesForQuery.add(i);
                }
                ArrayList rs = getAsSubGraphOf(source, Graph2OPMWRDFModel.graph2OPMWTemplate(biggerGraph), biggerGraph, variablesForQuery, null);
                if(rs != null && !rs.isEmpty()){
                    System.out.println("\n"+source.getName()+" found in "+biggerGraph.getName());
                    return true;
                }
            }else{
                return true;//a workflow is included in itself
            }
        }
        return false;
    }
    /**
     * Method that given a graph, finds where it has been found (bindings) on abiggerGraphModelbigger graph.
     * @param source
     * @param biggerGraph
     * @return an arraylist with a set of hashmaps with all possible results. The
     * hashmap is <varName, varValue>.
     */
    private ArrayList<HashMap<String,String>> getAsSubGraphOf(Graph source, OntModel biggerGraphModel, Graph biggerGraph,  ArrayList<Integer> varsToQuery, HashMap<String,String> previousValues){
//        OntModel o2 = Graph2OPMWRDFModel.graph2OPMWTemplate(biggerGraph);
        String q = createQueryFromGraph(source, varsToQuery, previousValues);
        
        if(q.equals("")){
            //if the query is empty then there are no dependencies on this space of the graph. Avoid them
            return null;
        }
        Query query = QueryFactory.create(q);
        QueryExecution qe = QueryExecutionFactory.create(query, biggerGraphModel);
        qe.setTimeout(5000);//we have to set a limit because of the size of some of the graphs. A minute (60k millisec)
        ResultSet rs = qe.execSelect();
        ArrayList<HashMap<String,String>> results = new ArrayList<HashMap<String, String>>();
        try{
            while(rs.hasNext()){
                //Each querysolution means that the graph was found in different ways on the 
                //bigger workflow. We retrieve each and store it back.
                results.add(resultSetToHashMap(rs.next()));
            }
        }catch(Exception e){
            qe.close();
//            o2.close();
            System.err.println("\n Query between "+source.getName()+" and "+biggerGraph.getName()+" failed ");

            //1: separate the connected queries in two groups of similar size.
            OrderedQueueBySize qu = new OrderedQueueBySize(varsToQuery);
            boolean merged = true;
            while(qu.getQueue().size()>2 && merged){
                int groupOrig =0; 
                merged = false;
                while(groupOrig<qu.getQueue().size()-1 &&!merged){
                    int groupDest = groupOrig+1;
                    while (groupDest<qu.getQueue().size() &&!merged){
                        if(canBeMerged(qu.getQueue().get(groupOrig), qu.getQueue().get(groupDest), source.getAdjacencyMatrix())){
                            qu.mergeGroups(groupOrig, groupDest);
                            merged = true;
                        }
                        groupDest++;
                    }
                    groupOrig++;
                }    
                //qu.mergeGroups(0, 1);
            }
            
            //2: Identify the vars that connect the groups (if any).
            //if there are, the vars are added to the group with less vars.
            
            // Test: only considering 2. Generalize for three or more.
            
            Iterator<Integer> g1 = qu.getQueue().get(0).iterator();
            ArrayList<Integer> commonVarsInGroups = new ArrayList();
            boolean addToLeft = qu.getQueue().get(0).size()<qu.getQueue().get(1).size();
            while(g1.hasNext()){
                Integer g1Var = g1.next();
                Iterator <Integer> g2 = qu.getQueue().get(1).iterator();
                while(g2.hasNext()){
                    Integer g2Var = g2.next();
                    //we have to check on both directions to see if there is a connection
                    if((source.getAdjacencyMatrix()[g1Var][g2Var]!= null && source.getAdjacencyMatrix()[g1Var][g2Var].equals(GeneralConstants.INFORM_DEPENDENCY))||
                            (source.getAdjacencyMatrix()[g2Var][g1Var]!= null && source.getAdjacencyMatrix()[g2Var][g1Var].equals(GeneralConstants.INFORM_DEPENDENCY))){
//                        System.out.println(g1Var+","+g2Var);
                        if(addToLeft && !commonVarsInGroups.contains(g2Var)){
                            commonVarsInGroups.add(g2Var);
                        }
                        if(!addToLeft && !commonVarsInGroups.contains(g1Var)){
                            commonVarsInGroups.add(g1Var);
                        }
                    }
                }
            }
            //now we add the vars to the group with less size.            
            for(int i:commonVarsInGroups){
                if(addToLeft){
                    qu.getQueue().get(0).add(i);
                }else{
                    qu.getQueue().get(1).add(i);
                }
            }
            //assuming there are always 2: graph is connex (to be generalized)
            ArrayList<HashMap<String,String>> valuesLeft = getAsSubGraphOf(source, biggerGraphModel, biggerGraph, qu.getQueue().get(0), null);
            if (valuesLeft==null || valuesLeft.isEmpty()){
                    return null;
            }
            //do the query with the values! We can limit it to reduce the number of queries. For the moment I will just do it with one
            ArrayList<HashMap<String,String>> valuesRight = getAsSubGraphOf(source, biggerGraphModel, biggerGraph, qu.getQueue().get(1),valuesLeft.get(0));
            //return the values from both arrayLists. I just return one for this test
            HashMap<String,String> resAux = valuesLeft.get(0);
            HashMap<String,String> onlyOneFromRight = valuesRight.get(0);
            Iterator<String> it = onlyOneFromRight.keySet().iterator();
            while(it.hasNext()){
                String currResult = it.next();
                resAux.put(currResult,onlyOneFromRight.get(currResult));
            }
            results.add(resAux);//test with 1!! we must generalize it for more
            //TO DO
            //System.out.println("Va por muy buen camino");
            //end test
        }
        if(!qe.isClosed())qe.close();
//        if(!o2.isClosed())o2.close();
        return results;
    }
    
    private boolean canBeMerged(ArrayList<Integer> group1, ArrayList<Integer> group2, String[][] depMatrix){
        Iterator<Integer> g1 = group1.iterator();
        while(g1.hasNext()){
            Integer g1Var = g1.next();
            Iterator <Integer> g2 = group2.iterator();
            while(g2.hasNext()){
                Integer g2Var = g2.next();
                //we have to check on both directions to see if there is a connection
                if((depMatrix[g1Var][g2Var]!= null && depMatrix[g1Var][g2Var].equals(GeneralConstants.INFORM_DEPENDENCY))||
                        (depMatrix[g2Var][g1Var]!= null && depMatrix[g2Var][g1Var].equals(GeneralConstants.INFORM_DEPENDENCY))){
                    return true;
                }
            }
            
        }
        return false;
    }
    
    private HashMap<String,String> resultSetToHashMap(QuerySolution qs){
        HashMap<String, String> results = new HashMap<String, String>();
        Iterator<String> it = qs.varNames();
        while(it.hasNext()){
            String varName = it.next();
            //there will be no literals; all nodes are URIs.
            results.put(varName, qs.getResource(varName).getURI());
        }
        return results;
    }
    
//    private String getGroupingReuse(String folderPath){
//        //step 1: read the workflows.
//        File f = new File(folderPath);        
//        //step 1: read workflows AND grouping
//        LoniTemplateAndGroupings2Graph wfAndGroupings = new LoniTemplateAndGroupings2Graph(folderPath);
//        if(f.isDirectory()){
//            File[] files = f.listFiles();
//            for (File file : files) {
//                wfAndGroupings.transformToGraph(file.getName());
//            }
//        }
//        GraphCollection filteredWfAndGroupingCollection = DatasetFilter.removeDuplicates(wfAndGroupings.getGraphCollection());
//        
//        String stats = "Size of the collection: "+filteredWfAndGroupingCollection.getNumberOfSubGraphs()+"\n";
//        ArrayList <Graph> groupings = filteredWfAndGroupingCollection.getGraphs();
//        ArrayList <Graph> workflows = new ArrayList<Graph>();//instead of rereading, we separate them
//        //step 2: separate workflows from grouping collection (we don't want them mixed here now)
//        int i=0;
//        while(i<groupings.size()){
//            Graph wfToRemove = groupings.get(i);
//            if(wfToRemove.getName().endsWith(".PIPE")||wfToRemove.getName().endsWith(".XML")){
//                groupings.remove(wfToRemove);
//                workflows.add(wfToRemove);
//            }
//            else{
//                i++;
//            }
//        }
//        //step 4: Search groupings in workflows
//        Iterator<Graph> itGroupings = groupings.iterator();
//        String problematicGroupings = "";
//        while(itGroupings.hasNext()){
//            //check the number of times each grah is found in the collection.
//            Graph queryGroupingGraph = itGroupings.next();
//            System.out.print("Dealing with "+queryGroupingGraph.getName());
//            Iterator<Graph> itWorkflows = workflows.iterator();
//            String currentQuery = createQueryFromGraph(queryGroupingGraph);//, currentGraph.getName());
//            while(itWorkflows.hasNext()){
//                Graph currentWorkflow = itWorkflows.next();
////                System.out.println("Comparing "+ queryGraph.getName()+" and "+currentGraph.getName());
//                if(couldGraphBeIncluded(queryGroupingGraph, currentWorkflow)){
//                    if(!queryGroupingGraph.getName().equals(currentWorkflow.getName())){//if they are the same don't do the query
//                        OntModel o2 = Graph2OPMWRDFModel.graph2OPMWTemplate(currentWorkflow);
//                        System.out.print(".");
//                        Query query = QueryFactory.create(currentQuery);
//                        QueryExecution qe = QueryExecutionFactory.create(query, o2);
//                        qe.setTimeout(60000);
//                        ResultSet rs = qe.execSelect();                        
//                        try{
//                            if(rs.hasNext()){
//                                System.out.println("\n"+queryGroupingGraph.getName()+" found in "+currentWorkflow.getName());
//                                addGroupingOccurrence(queryGroupingGraph.getName());
//                            }
//                        }catch(Exception e){
//                            System.err.println("\n Query between "+queryGroupingGraph.getName()+" and "+currentWorkflow.getName()+" failed\n");
//                            problematicGroupings+=queryGroupingGraph.getName()+" with workflow "+currentWorkflow.getName()+"\n";
//                        }
//                        qe.close();
//                        o2.close();
//                    }else{
//                        addGroupingOccurrence(queryGroupingGraph.getName());
//                    }
//                }
//            }
//            System.out.println();
//        }
//        
//        stats+= "Groupings: "+groupings.size()+"\n";
//        stats+= "Workflows: "+workflows.size()+"\n";
//        stats+="Groupings reused at least 2 in two different workflows:\n";
//        stats+= getStats(groupingOccurrences, 2);
//        stats+= "Problematic groupings: ";
//        stats+= problematicGroupings;
////        stats+="Groupings reused at least 4 times:\n";
////        stats+= getStats(groupingOccurrences, 4);
//        return stats;
//    }
    
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
     * Method to determine wether the input to be found could be included
     * in the general graph. This is a helper to discard graphs and optimize 
     * the process.
     * @return true if the input graph could be included in the output graph
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
        for (String[] adjMatrix1 : adjMatrix) {
            for (int j = 0; j< adjMatrix.length; j++) {
                if (adjMatrix1[j] != null) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static void main(String [] args){
        String stats;
//        String escienceSam = "C:\\Users\\dgarijo\\Desktop\\LONI_Datasets\\problematic wfs\\";
//        String escienceSam = "LONI_Dataset\\problematic wfs\\";//
        String escienceSam = "LONI_dataset\\datasetSamuel\\";
//        String escienceBoris = "C:\\Users\\Monen\\Desktop\\LONIDatasets\\WorkflowBoris\\Pipeline123\\";//"C:\\Users\\Dani\\Desktop\\WC1\\Pipeline123\\";//
//        //String escienceIVO = "C:\\Users\\Monen\\Desktop\\LONIDatasets\\WorkflowIVO\\all\\";
//        String escienceIVO = "C:\\Users\\Dani\\Desktop\\WC2\\all\\";
//        String escienceMonthly = "C:\\Users\\Monen\\Desktop\\LONIDatasets\\dataset3months(Zhizhong)\\2014-01\\";//"C:\\Users\\Dani\\Desktop\\WC3\\2014-01\\";
        
        
        stats = "";
        
        //boris' stats
//        stats += new LONIWorkflowAndGroupingReuseStatistics().getWorkflowReuse(escienceBoris);
//        stats += new LONIWorkflowAndGroupingReuseStatistics().getGroupingReuse(escienceBoris);
        
        //Ivo's stats
//        stats += new LONIWorkflowAndGroupingReuseStatistics().getWorkflowReuse(escienceIVO);
//        stats += new LONIWorkflowAndGroupingReuseStatistics().getGroupingReuse(escienceIVO);
        
        //monthly stats
//        stats += new LONIWorkflowAndGroupingReuseStatistics().getWorkflowReuse(escienceMonthly);
//        stats += new LONIWorkflowAndGroupingReuseStatistics().getGroupingReuse(escienceMonthly);
        
        //stats = "Samuel's dataset\n";
        stats += new LONIWorkflowAndGroupingReuseStatistics().getWorkflowReuse(escienceSam);
//        stats += new LONIWorkflowAndGroupingReuseStatistics().getGroupingReuse(escienceSam);
       
        
        System.out.println(stats);
    }
}
