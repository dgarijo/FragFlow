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
import Factory.Loni.LoniTemplate2Graph;
import IO.DatasetFilter;
import Static.GeneralConstants;
import Static.GeneralMethods;
import java.io.File;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class designed to generate statistics on the groupings in a LONI graph collection.
 * This class belongs to this package because it is for plotting statistics for
 * the eScience conference.
 * @author Daniel Garijo
 */
public class GroupingStatisticsLONI {
    private String repositoryURI;
    private int totalGroupings;
    private int averageGroupingsPerWorkflow;
    private int maxGroupingSize;
    private int minGroupingSize;
    private int totalFilteredWorkflowsWithGroupings;    
    private String maxStr,minStr;

    public GroupingStatisticsLONI(String folder) {
        repositoryURI = folder;
        totalGroupings = 0;
        averageGroupingsPerWorkflow = 0;
        maxGroupingSize = 0;
        minGroupingSize = 100;
        
    }
    
    /**
     * Small method to calculate the size of a grouping.
     * @param current
     * @return 
     */
    private int getSizeOfGrouping(Node current){
        NodeList child = current.getChildNodes();
        int totalSize = 0;
        for(int i=0; i<child.getLength(); i++){
            Node io = child.item(i);
                if(io.getNodeName().equals("module")){
                    totalSize++;
                }else if(io.getNodeName().equals("moduleGroup")){
                    totalSize+= getSizeOfGrouping(io);
                }
        }
        return totalSize;
    }
    public void getGroupingsFromFile(String uri){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        System.out.println("Processing "+uri);
        try{
            File f = new File(this.repositoryURI+uri);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(f); 
            NodeList nodeListConnections = document.getElementsByTagName("connection");
            NodeList nodeListModules = document.getElementsByTagName("module");
            NodeList nodeListModuleGroups = document.getElementsByTagName("moduleGroup");

//            System.out.println("Document with "+nodeListConnections.getLength()+ " connections, "+ nodeListModules.getLength()+" Modules and " +nodeListModuleGroups.getLength()+" module groups");
            
            this.totalGroupings+=nodeListModuleGroups.getLength()-1;
            if(nodeListModuleGroups.getLength()>1)totalFilteredWorkflowsWithGroupings++;
            //for each modulegroup, iterate through them to see the size of the groupings.
            int groupingSize;
            for (int i=0; i< nodeListModuleGroups.getLength(); i++){
                //get here size of grouping
                Node currentNode = nodeListModuleGroups.item(i);
                //we select all of them except for the modulegroup representing the pipeline itsetlf.
                if(!currentNode.getParentNode().getNodeName().equals("pipeline")){
                    groupingSize = getSizeOfGrouping(nodeListModuleGroups.item(i));
                    if(groupingSize>maxGroupingSize){
//                        System.out.print("current max: "+groupingSize);
                        maxGroupingSize = groupingSize;
                        maxStr = uri;
                    }
                    if(groupingSize<minGroupingSize){
                        minGroupingSize = groupingSize;
                        minStr = uri;
                    }
                }
            }
        }catch(Exception e){
            System.out.println("Error while parsing the input "+e.getMessage());
        }
    }
    
    private String printStats(){
        String returnValue="";
        returnValue+="Max size groupings: "+maxGroupingSize+"(found in "+maxStr+")\n";
        returnValue+="Min size groupings: "+minGroupingSize+"(found in "+minStr+")\n";
        returnValue+="Total groupings in all workflows: "+totalGroupings+"\n";
        returnValue+="Workflows with groupings: "+totalFilteredWorkflowsWithGroupings+"\n";
        float avg = (float)totalGroupings/(float)totalFilteredWorkflowsWithGroupings;
        returnValue+="Total groupings per workflow: "+avg+"\n";
        return returnValue;
    }
    
    public static String getStatsFromFolder(String folderPath){
        GroupingStatisticsLONI g = new GroupingStatisticsLONI(folderPath);
        File f = new File(folderPath);
        if(f.isDirectory()){
            File[] files = f.listFiles();
            for(int i=0;i<files.length;i++){
                g.getGroupingsFromFile(files[i].getName());
            }
        }
        return g.printStats();
    }
    
    
    
    /**
     * Given a filteredCollection, we calculate the stats for ONLY those templates
     * in the filtered collection, which are the ones that are meaningful
     * @param gc
     * @return 
     */
    public static String getStatsFilteredFolder(String folderPath){
        //unfortunately when processed, the original URI of the file path is lost.
        //we have to compare each of the filtered with the files in the repository
        //to find the one to process.
        File f = new File(folderPath);
        LoniTemplate2Graph test = new LoniTemplate2Graph(folderPath);
        if(f.isDirectory()){
            File[] files = f.listFiles();
            for(int i=0;i<files.length;i++){
                test.transformToGraph(files[i].getName());
            }
        }
        GraphCollection filteredCollection = DatasetFilter.removeDuplicates(test.getGraphCollection());
        Iterator<Graph> it = filteredCollection.getGraphs().iterator();
        GroupingStatisticsLONI g = new GroupingStatisticsLONI(folderPath);
        String fileName;
        while(it.hasNext()){
            fileName = getFileName(folderPath, it.next().getName());
            g.getGroupingsFromFile(fileName);
        }
        return g.printStats();
    }
    
    private static String getFileName(String folderPath, String uri){
        //aux method to look for the name
        File f = new File(folderPath);
        if(f.isDirectory()){
            File[] files = f.listFiles();
            for(int i=0; i< files.length;i++){
                if((GeneralConstants.PREFIX_FOR_RDF_GENERATION+GeneralMethods.encode(files[i].getName())).equals(uri)){  
                    return files[i].getName();
                }
            }
        }
        return null;
    }
    
    //test method
    public static void main(String[] args){
        //for each template, count the groupings, the average number of groupings per template.
        //for each group of templates, the maximum size of a grouping and the minimum size
        //we have to count the filtered templates, in order to be consistent with the results
        String stats;
        String escienceBoris = "C:\\Users\\Monen\\Desktop\\LONIDatasets\\WorkflowBoris\\Pipeline123\\";
        String escienceIvo = "C:\\Users\\Monen\\Desktop\\LONIDatasets\\WorkflowIVO\\all\\";
        String escienceMonthly = "C:\\Users\\Monen\\Desktop\\LONIDatasets\\dataset3months(Zhizhong)\\2014-01\\";
        
        stats = "All Boris dataset\n";
        stats += getStatsFilteredFolder(escienceBoris);
        stats +=  "Public Dataset (+ Ivo) \n";
        stats += getStatsFilteredFolder(escienceIvo);
        stats += "Monthly Dataset\n";
        stats += getStatsFilteredFolder(escienceMonthly);
        
        System.out.println(stats);
    }
}
