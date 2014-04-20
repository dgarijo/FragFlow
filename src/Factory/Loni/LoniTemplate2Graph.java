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
 * Class to process and load a LONI pipeline as a Graph
 * The modules are the main processors, and the wasInformedBy dependency is captured
 * amongst them.
 * The LONI templates will be processes from a folder.
 * @author Daniel Garijo
 */
public class LoniTemplate2Graph extends GraphCollectionCreator{
    private HashMap<String,String> inputsOfModules;//for storing the input and the module it belongs to.
    private HashMap<String,String> outputsOfModules;//for storing the output and the module it belongs to.
    private HashMap<String,String> links;//for storing the links among the connections

    public LoniTemplate2Graph(String repositoryURI) {
        super(repositoryURI);
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
//        (puede que el id de plantilla no sea una buena idea.)
        int countNodes = 1;
        ArrayList<String> uris = new ArrayList<String>();
        HashMap<String,GraphNode> nodes = new HashMap<String, GraphNode>();
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
        
        ArrayList<SimpleEntry<String,String>> connections = new ArrayList<SimpleEntry<String,String>>(); //for storing the connections: source-sink
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
            String moduleType = GeneralConstants.PREFIX_FOR_RDF_GENERATION+GeneralMethods.clean(nodeAttributes.getNamedItem("name").getNodeValue());
//            String moduleType = GeneralMethods.clean(nodeAttributes.getNamedItem("name").getNodeValue());
            nodes.put(moduleID, new GraphNode(moduleID, moduleType, countNodes));
                countNodes++;
            uris.add(moduleID);            
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
        
        String[][] adjacencyMatrix = new String[countNodes][countNodes]; 
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
                    //module of input was informed by module of the output
                    adjacencyMatrix [nodes.get(inputsOfModules.get(source)).getNumberInGraph()]
                            [nodes.get(outputsOfModules.get(sink)).getNumberInGraph()] = 
                            GeneralConstants.INFORM_DEPENDENCY;    
                }
            }else {
                if(outputsOfModules.containsKey(source)){
                    if(inputsOfModules.containsKey(sink)){
//                        System.out.println("Sink: "+sink);
                        adjacencyMatrix [nodes.get(inputsOfModules.get(sink)).getNumberInGraph()]
                            [nodes.get(outputsOfModules.get(source)).getNumberInGraph()] = 
                            GeneralConstants.INFORM_DEPENDENCY; 
                    }
                }
            }
        }
        //we make it URIs so as to be able to perform the cleaning tasks later.
        Graph graph = new Graph(uris, nodes, adjacencyMatrix, GeneralConstants.PREFIX_FOR_RDF_GENERATION+GeneralMethods.encode(uri));
        
        this.collection.addSubGraph(graph);
    }catch(Exception e){
        System.out.println("Error while parsing the input "+e.getMessage());
//        e.printStackTrace();
    }

  }
    
    private String getIOFromModule(HashMap<String, String> links, String s) {
        String sAux = s;
        while(links.containsKey(sAux)){
            sAux = links.get(sAux);
        }
        return sAux;
    }
    
//    public static void main(String[] args) throws CollectionWriterException{
//        LoniTemplate2GraphNew test = new LoniTemplate2GraphNew("C:\\Users\\Monen\\Dropbox\\NetBeansProjects\\MotifFinder\\LONI_dataset\\");
////        test.transformToGraph("DTI_workflow.pipe.xml");
////        test.transformToGraph("DTI_eddy_motion_datawith2repeats.pipe.xml");
////        test.transformToGraph("CorticalSurfaceLabeling.pipe.xml");
//        test.transformToGraph("AutoROIExtraction_LONI.pipe.xml");
////        test.transformToGraph("BrainParserTraining.pipe.xml");
//        CollectionWriterPARSEMIS writer = new CollectionWriterPARSEMIS();
//        writer.writeReducedGraphsToFile(test.getGraphCollection(), "TEST_LONI", null);
//    }



    
}
