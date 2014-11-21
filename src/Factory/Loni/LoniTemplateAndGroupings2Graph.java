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
package Factory.Loni;

import DataStructures.Graph;
import DataStructures.GraphNode.GraphNode;
import Factory.GraphCollectionCreator;
import IO.DatasetFilter;
import IO.Exception.CollectionWriterException;
import IO.Formats.SUBDUE.GraphCollectionWriterSUBDUE;
import Static.GeneralConstants;
import Static.GeneralMethods;
import java.io.File;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class to process and load the groupings of a LONI pipeline as Graphs
 * The modules are the main processors, and the wasInformedBy dependency is captured
 * amongst them.
 * This class has been created for evaluation purposes. We aim to compare the groupings
 * with the fragments detected by the algorithms.
 * @author Daniel Garijo
 */
public class LoniTemplateAndGroupings2Graph extends GraphCollectionCreator{
    private HashMap<String,String> inputsOfModules;//for storing the input and the module it belongs to.
    private HashMap<String,String> outputsOfModules;//for storing the output and the module it belongs to.
    private HashMap<String,String> links;//for storing the links among the connections
    private ArrayList<SimpleEntry<String,String>> connections;

    public LoniTemplateAndGroupings2Graph(String repositoryURI) {
        super(repositoryURI);
    }

    private Graph processGrouping(Node currentGrouping, int numeration){
//        currentGrouping.
        ArrayList<String> uris = new ArrayList<String>();
        HashMap<String,GraphNode> nodes = new HashMap<String, GraphNode>();
        //get all nodes from the grouping here.
        NamedNodeMap nodeAttributes = currentGrouping.getAttributes();
        String groupingName = GeneralConstants.PREFIX_FOR_RDF_GENERATION+GeneralMethods.encode(nodeAttributes.getNamedItem("id").getNodeValue());
//        System.out.println(groupingName);
        
        nodesFromGrouping(currentGrouping, nodes, uris, numeration);
        String[][] adjMatrix = fillAdjMatrix(nodes);
        return new Graph(uris, nodes, adjMatrix, groupingName);
    }
    
    //is count necessary?
    private void nodesFromGrouping(Node current, HashMap<String,GraphNode> nodes, ArrayList<String> uris, int count){
        NodeList child = current.getChildNodes();
        for(int i=0; i<child.getLength(); i++){
            Node io = child.item(i);
                if(io.getNodeName().equals("module")){
//                    System.out.println(io.getAttributes().getNamedItem("id"));
                    String moduleID = io.getAttributes().getNamedItem("id").getNodeValue();
                    String moduleType = GeneralConstants.PREFIX_FOR_RDF_GENERATION+GeneralMethods.clean(io.getAttributes().getNamedItem("name").getNodeValue());
                    if(!nodes.containsKey(moduleID)){
                        nodes.put(moduleID, new GraphNode(moduleID, moduleType, uris.size()+count));
                        uris.add(moduleID);
                    }
                }else if(io.getNodeName().equals("moduleGroup")){
                    nodesFromGrouping(io, nodes, uris, count);
                }
        }
    }
    /**
     * The URI is the path to the current xml file to be loaded.
     * @param uri 
     */
    @Override
    public void transformToGraph(String uri) {        
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    System.out.println("Processing "+uri);
    try{
        File f = new File(this.repositoryURI+uri);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(f); 
        NodeList nodeListConnections = document.getElementsByTagName("connection");
        if(nodeListConnections.getLength()==0){
            System.out.println("The selected workflow has no connections!\n No graph is created");
            return;
        }
        NodeList nodeListModules = document.getElementsByTagName("module");
        NodeList nodeListModuleGroups = document.getElementsByTagName("moduleGroup");
        
        System.out.println("Document with "+nodeListConnections.getLength()+ " connections, "+ nodeListModules.getLength()+" Modules and " +nodeListModuleGroups.getLength()+" module groups");
        
        connections = new ArrayList<SimpleEntry<String,String>>(); //for storing the connections: source-sink
        inputsOfModules = new HashMap<String,String>();//for storing the input and the module it belongs to.
        outputsOfModules = new HashMap<String,String>();//for storing the output and the module it belongs to.
        links = new HashMap<String, String>();//for storing the links among the connections
        //iterate through connections initializing the hashmap
        for(int i=0; i< nodeListConnections.getLength(); i++){
            Node currentConnection = nodeListConnections.item(i);
            Node source = currentConnection.getAttributes().getNamedItem("source");
            Node sink = currentConnection.getAttributes().getNamedItem("sink");
            //each connection is unique, so it will not be overwritten
            connections.add(new SimpleEntry(source.getNodeValue(),sink.getNodeValue()));
//            System.out.println(source.getNodeValue()+"-"+sink.getNodeValue());
        }
        //iterate through modules
        for(int i=0; i< nodeListModules.getLength(); i++){
            Node currentModule = nodeListModules.item(i);
            NamedNodeMap nodeAttributes = currentModule.getAttributes();
            String moduleID = nodeAttributes.getNamedItem("id").getNodeValue();
            //the module ID is unique in each wf. The module Type could be seen more than once.
            NodeList currModuleList = currentModule.getChildNodes();
            
            for(int j=0; j<currModuleList.getLength(); j++){
                Node io = currModuleList.item(j);
                if(io.getNodeName().equals("input")){
                    inputsOfModules.put(io.getAttributes().getNamedItem("id").getNodeValue(),moduleID);
//                    System.out.println("Module "+moduleID+" hasInput "+io.getAttributes().getNamedItem("id").getNodeValue());
                }else if(io.getNodeName().equals("output")){
                    outputsOfModules.put(io.getAttributes().getNamedItem("id").getNodeValue(),moduleID);
//                    System.out.println("Module "+moduleID+" hasInput "+io.getAttributes().getNamedItem("id").getNodeValue());
                }
            }
        }
        /**
         * In a module group, the inputs and outputs may be connected to other modules.
         * The input of the module group should be associated to the input of the 
         * first module it connects (with the link association)
         */
        for(int i=0;i<nodeListModuleGroups.getLength();i++){
            Node currentModule = nodeListModuleGroups.item(i);
            //the module ID is unique in each wf. The module Type could be seen more than once.
            NodeList currModuleList = currentModule.getChildNodes();
//            System.out.println(currentModule.getAttributes().getNamedItem("id"));
            for(int j=0; j<currModuleList.getLength(); j++){
                Node io = currModuleList.item(j);
                if(io.getNodeName().equals("input")||io.getNodeName().equals("output")){                    
//                    System.out.println(io.getAttributes().getNamedItem("id")+","+io.getAttributes().getNamedItem("link").getNodeValue());
                    links.put(io.getAttributes().getNamedItem("id").getNodeValue(),io.getAttributes().getNamedItem("link").getNodeValue());
                }
            }
        }
        
        for (int i=0;i<nodeListModuleGroups.getLength(); i++){
            Node currentModule = nodeListModuleGroups.item(i);
            Graph g = processGrouping(currentModule, 1);
            //change the name of the template grouping for the template URI 
            //(so we know which template it was). The id of the grouping might be different than the one from the template.
            if(currentModule.getParentNode().getNodeName().equals("pipeline")){
                g.setName(GeneralConstants.PREFIX_FOR_RDF_GENERATION+GeneralMethods.encode(uri));
            }
            if(g.getNodes().size()<=1){
                System.out.println("Structure with only one node!");
            }else{
                this.collection.addSubGraph(g);
            }
        }
    }catch(Exception e){
        System.out.println("Error while parsing the input "+e.getMessage());
    }

  }
    private String[][] fillAdjMatrix (HashMap<String,GraphNode> nodes){
        String[][] adjacencyMatrix = new String[nodes.size()+1][nodes.size()+1]; 
        //now that we have the nodes and the URIs, we iterate through the connections
        //in order to determine whether we have a wasInformedBy connection between modules.
        Iterator<SimpleEntry<String,String>> itConn = connections.iterator();
        while(itConn.hasNext()){
            SimpleEntry<String,String> currentConnection = itConn.next();
            String source = currentConnection.getKey();
            String sink = currentConnection.getValue();
            //if the source or the sink is in the links matrix, we have to 
            //recover to which module input are they associated.
            if(links.containsKey(source)){
                source = getIOFromModule(links,source);
            }
            if(links.containsKey(sink)){
                sink = getIOFromModule(links,sink);
            }
            
            if(inputsOfModules.containsKey(source)){
                if(outputsOfModules.containsKey(sink)){
                    //now that not all the nodes are stored, we have to check to get only the relevant ones
                    if(nodes.containsKey(inputsOfModules.get(source)) && nodes.containsKey(outputsOfModules.get(sink))){
                        //module of input was informed by module of the output
                        adjacencyMatrix [nodes.get(inputsOfModules.get(source)).getNumberInGraph()]
                                [nodes.get(outputsOfModules.get(sink)).getNumberInGraph()] = 
                                GeneralConstants.INFORM_DEPENDENCY;   
                    }
                }
            }else {
                if(outputsOfModules.containsKey(source)){
                    if(inputsOfModules.containsKey(sink)){
//                        System.out.println("Sink: "+sink);
                        if(nodes.containsKey(inputsOfModules.get(sink)) && nodes.containsKey(outputsOfModules.get(source))){
                            adjacencyMatrix [nodes.get(inputsOfModules.get(sink)).getNumberInGraph()]
                                [nodes.get(outputsOfModules.get(source)).getNumberInGraph()] = 
                                GeneralConstants.INFORM_DEPENDENCY; 
                        }
                    }
                }
            }
            
        }
        return adjacencyMatrix;
    }
    private String getIOFromModule(HashMap<String, String> links, String s) {
        String sAux = s;
        while(links.containsKey(sAux)){
            sAux = links.get(sAux);
        }
        return sAux;
    }
    
//    public static void main(String[] args) throws CollectionWriterException{
//        LoniTemplateAndGroupings2Graph test = new LoniTemplateAndGroupings2Graph("C:\\Users\\Monen\\Dropbox\\NetBeansProjects\\MotifFinder\\LONI_dataset\\datasetBoris\\");
////        test.transformToGraph("DTI_workflow.pipe.xml");
////        test.transformToGraph("DTI_eddy_motion_datawith2repeats.pipe.xml");
////        test.transformToGraph("MDT_v9_NL_only.pipe");
//        test.transformToGraph("MDT_v8.pipe");
////        test.transformToGraph("BrainParserTraining.pipe.xml");
//        GraphCollectionWriterSUBDUE writer = new GraphCollectionWriterSUBDUE();
//        writer.writeReducedGraphsToFile(DatasetFilter.removeDuplicates(test.getGraphCollection()), "testOverlap", null);
//    }



    
}
