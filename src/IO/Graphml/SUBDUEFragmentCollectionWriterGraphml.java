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
package IO.Graphml;

import DataStructures.Fragment;
import DataStructures.Graph;
import DataStructures.GraphNode.GraphNode;
import IO.Exception.CollectionWriterException;
import IO.Formats.SUBDUE.FragmentCollectionReaderSUBDUE;
import IO.FragmentCollectionWriter;
import Static.GeneralConstants;
import Static.TestConstants;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Class for writing a fragment collection in a graph format.
 * Right now it only accepts the SUBDUE format (until the issues with the indexes are solved
 * This will help visualizing the results to the users.
 * 
 * Note: to extend this class to the other fragments, just create an intermediate 
 * class that handles the numeration of the adj matrix.
 * 
 * @author Daniel Garijo
 */
public class SUBDUEFragmentCollectionWriterGraphml extends FragmentCollectionWriter{
    @Override
    public void writeFragmentsToFile(HashMap<String, Fragment> fc, String outputPath, HashMap<String, String> replacements) throws CollectionWriterException {
        FileWriter fstream = null; 
        BufferedWriter out = null; 
        try {
            fstream = new FileWriter(outputPath);
            out = new BufferedWriter(fstream);
            out.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"+
            "<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:y=\"http://www.yworks.com/xml/graphml\" xmlns:yed=\"http://www.yworks.com/xml/yed/3\" xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns http://www.yworks.com/xml/schema/graphml/1.1/ygraphml.xsd\">\n"+
            "<key for=\"graphml\" id=\"d0\" yfiles.type=\"resources\"/>\n"+
            "<key for=\"port\" id=\"d1\" yfiles.type=\"portgraphics\"/>\n"+
            "<key for=\"port\" id=\"d2\" yfiles.type=\"portgeometry\"/>\n"+
            "<key for=\"port\" id=\"d3\" yfiles.type=\"portuserdata\"/>\n"+
            "<key attr.name=\"url\" attr.type=\"string\" for=\"node\" id=\"d4\"/>\n"+
            "<key attr.name=\"description\" attr.type=\"string\" for=\"node\" id=\"d5\"/>\n"+
            "<key for=\"node\" id=\"d6\" yfiles.type=\"nodegraphics\"/>\n"+
            "<key attr.name=\"Description\" attr.type=\"string\" for=\"graph\" id=\"d7\"/>\n"+
            "<key attr.name=\"url\" attr.type=\"string\" for=\"edge\" id=\"d8\"/>\n"+
            "<key attr.name=\"description\" attr.type=\"string\" for=\"edge\" id=\"d9\"/>\n"+
            "<key for=\"edge\" id=\"d10\" yfiles.type=\"edgegraphics\"/>\n"+
            "<graph edgedefault=\"directed\" id=\"G\">"+
            "<data key=\"d7\"/>");
            
            Iterator <Entry<String,Fragment>> it = fc.entrySet().iterator();
            String nodes ="";
            String edges = "";
           while(it.hasNext()){
               Fragment f = it.next().getValue();
               if(f.isMultiStepFragment()){
                   nodes+=this.getTextNodesFromFragment(f);
                   edges+=this.getTextEdgeFromFragment(f);
               }               
           }
            out.write(nodes);
            out.write(edges);
            out.write("</graph>\n"+
                "<data key=\"d0\">\n"+
                "<y:Resources/>\n"+
                "</data>\n"+
                "</graphml>");
        } catch (Exception ex) {
            System.err.println("Exception while writing the graph: "+ex.getMessage());
            throw new CollectionWriterException("Exception while writing the graph. "+ex.getMessage(), ex);
        } finally {
            try {
                if(out!=null)out.close();
                if(fstream!=null)fstream.close();
            } catch (IOException ex) {
                System.err.println("Error closing the files: "+ex.getMessage());
            }
        }   
    }
    
    private String getTextNodesFromFragment(Fragment f){
        Graph g = f.getDependencyGraph();
        Iterator<Entry<String,GraphNode>> it = g.getNodes().entrySet().iterator();
        String textNodes = 
           "<node id=\""+f.getStructureID()+"\" yfiles.foldertype=\"group\">\n"+
           "<data key=\"d6\">\n"+
           "<y:ProxyAutoBoundsNode>\n"+
           "<y:Realizers active=\"0\">\n"+
           "<y:GroupNode>\n"+
              "<y:Geometry height=\"84.37658730158728\" width=\"360.0\" x=\"-15.0\" y=\"-449.3765873015873\"/>\n"+
              "<y:Fill color=\"#F5F5F5\" transparent=\"false\"/>\n"+
              "<y:BorderStyle color=\"#000000\" type=\"dashed\" width=\"1.0\"/>\n"+
              "<y:NodeLabel alignment=\"right\" autoSizePolicy=\"node_width\" backgroundColor=\"#EBEBEB\" borderDistance=\"0.0\" fontFamily=\"Dialog\" fontSize=\"15\" fontStyle=\"plain\" hasLineColor=\"false\" height=\"22.37646484375\" modelName=\"internal\" modelPosition=\"t\" textColor=\"#000000\" visible=\"true\" width=\"360.0\" x=\"0.0\" y=\"0.0\">"+
              "Fragment: "+f.getStructureID()+"("+f.getNumberOfOccurrences()+" times)</y:NodeLabel>aqui el nombre del fragment\n"+
              "<y:Shape type=\"roundrectangle\"/>\n"+
              "<y:State closed=\"false\" closedHeight=\"50.0\" closedWidth=\"50.0\" innerGraphDisplayEnabled=\"false\"/>\n"+
              "<y:Insets bottom=\"15\" bottomF=\"15.0\" left=\"15\" leftF=\"15.0\" right=\"15\" rightF=\"15.0\" top=\"15\" topF=\"15.0\"/>\n"+
              "<y:BorderInsets bottom=\"1\" bottomF=\"1.0\" left=\"0\" leftF=\"0.0\" right=\"0\" rightF=\"0.0\" top=\"1\" topF=\"1.0001224578372785\"/>\n"+
            "</y:GroupNode>\n"+
          "</y:Realizers>\n"+
        "</y:ProxyAutoBoundsNode>\n"+
      "</data>\n"+
      "<graph edgedefault=\"directed\" id=\""+f.getStructureID()+":\">\n";
        while (it.hasNext()){
            Entry<String,GraphNode> currEntry = it.next();
            String nodeId = f.getStructureID() +"_"+currEntry.getKey();
            String nodeValue = currEntry.getValue().getType();
            textNodes+=getCodeForNode(nodeId, nodeValue);
        }
        textNodes += "</graph>\n"+
                     "</node>\n";
        return textNodes;
    }
    private String cleanNodeType(String input){
        //if it has "#" get the 
        String result = input;
        if(result.contains("#")){
            result = result.substring(result.lastIndexOf("#")+1);
        }else{
            if(result.contains("/"))
                result = result.substring(result.lastIndexOf("/")+1);
        }
        return result;
    }
    private String getCodeForNode(String nodeId, String nodeText){
        String codeNode = 
          "<node id=\""+nodeId+"\">\n"+ //here put the structurename_uri
          "<data key=\"d6\">\n"+
          "    <y:ShapeNode>\n"+
          "      <y:Geometry height=\"30.0\" width=\"150.0\" x=\"65.0\" y=\"60.0\"/>\n"+
          "      <y:Fill color=\"#FFCC00\" transparent=\"false\"/>\n"+
          "      <y:BorderStyle color=\"#000000\" type=\"line\" width=\"1.0\"/>\n"+
          "      <y:NodeLabel alignment=\"center\" autoSizePolicy=\"content\" fontFamily=\"Dialog\" fontSize=\"12\" fontStyle=\"plain\" hasBackgroundColor=\"false\" hasLineColor=\"false\" height=\"18.701171875\" modelName=\"custom\" textColor=\"#000000\" visible=\"true\" width=\"10.673828125\" x=\"9.6630859375\" y=\"5.6494140625\">"+
                 cleanNodeType(nodeText) +"<y:LabelModel>\n"+//here add the type
          "          <y:SmartNodeLabelModel distance=\"4.0\"/>\n"+
          "        </y:LabelModel>\n"+
          "        <y:ModelParameter>\n"+
          "          <y:SmartNodeLabelModelParameter labelRatioX=\"0.0\" labelRatioY=\"0.0\" nodeRatioX=\"0.0\" nodeRatioY=\"0.0\" offsetX=\"0.0\" offsetY=\"0.0\" upX=\"0.0\" upY=\"-1.0\"/>\n"+
          "        </y:ModelParameter>\n"+
          "      </y:NodeLabel>\n"+
          "      <y:Shape type=\"rectangle\"/>\n"+
          "    </y:ShapeNode>\n"+
          "  </data>\n"+
          "</node>";
        return codeNode;
    }
    private String getCodeForEdge(String idSource, String idSink){
        String codeEdge = 
        "<edge id=\"e"+idSource+idSink+"\" source=\""+idSource+"\" target=\""+idSink+"\">\n"+
        "    <data key=\"d10\">\n"+
        "    <y:PolyLineEdge>\n"+
        "     <y:Path sx=\"0.0\" sy=\"15.0\" tx=\"0.0\" ty=\"-15.0\"/>\n"+
        "      <y:LineStyle color=\"#000000\" type=\"line\" width=\"1.0\"/>\n"+
        "      <y:Arrows source=\"none\" target=\"standard\"/>\n"+
        "      <y:BendStyle smoothed=\"false\"/>\n"+
        "    </y:PolyLineEdge>\n"+
        "  </data>\n"+
        "</edge>\n";
        return codeEdge;
    }
    private String getTextEdgeFromFragment(Fragment f){
        Graph g = f.getDependencyGraph();
        ArrayList<String> uris = g.getURIs();
        String[][] adjMatrix = g.getAdjacencyMatrix();
        String textEdges = "";
        for(int i=0;i<adjMatrix.length;i++){
            for(int j=0; j< adjMatrix.length; j++){
                if(adjMatrix[i][j]!=null && adjMatrix[i][j].equals(GeneralConstants.INFORM_DEPENDENCY)){
                    //instead of wasInformedBy, I'll pick the other size int he visualization
                    textEdges += getCodeForEdge(f.getStructureID()+"_"+uris.get(j-1), f.getStructureID()+"_"+uris.get(i-1));
                }
            }
        }
        return textEdges;
    }
    
//    public static void main(String[] args){
//        String file = TestConstants.SUBDUEResultFolder + "\\resultsLoni\\resultsLoniZhiFilteredSUBDUEEval1";
//        String ocFile = TestConstants.SUBDUEResultFolder + "\\resultsLoni\\resultsLoniZhiFilteredSUBDUEEval1_occurrences";
//        String outputPath = "testGraphm.graphml";
//        SUBDUEFragmentCollectionWriterGraphml instance = new SUBDUEFragmentCollectionWriterGraphml();
//        try{
//            HashMap<String,Fragment> structureResults = new FragmentCollectionReaderSUBDUE(file, ocFile).getFragmentCatalogAsHashMap();
//            instance.writeFragmentsToFile(structureResults, outputPath, null);
//        }catch(Exception e){
//            System.err.println("Test failed: something whent wrong when writing the fragments");
//        }
//    }
}
