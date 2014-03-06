/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
package PostProcessing.Formats.PAFI;

import DataStructures.Fragment;
import DataStructures.GraphNode.GraphNode;
import PostProcessing.FragmentToSPARQLQuery;
import Static.GeneralConstants;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class to transform a Fragment to a SPARQL query (PAFI).
 * @author Daniel Garijo
 */
public class FragmentToSPARQLQueryTemplatePAFI extends FragmentToSPARQLQuery {

    @Override
    public String createQueryFromFragment(Fragment f, String structureURI) {
        //to do
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public String createQueryForDirectionalityFromFragment(Fragment f, String structureURI){
        String query="SELECT distinct ";
        String[][] depMatrix = f.getDependencyGraph().getAdjacencyMatrix();
        ArrayList<String> graphURIs = f.getDependencyGraph().getURIs();
        HashMap<String,GraphNode> graphNodes = f.getDependencyGraph().getNodes();
        int currentVar = 0;
        //we add the nodes. Each node corresponds to its position on the list (and on the matrix)
        String stepsForQuery="";
        for(int i=0; i<graphURIs.size();i++){
            stepsForQuery+="?step_"+i+" a <"+graphNodes.get(graphURIs.get(i)).getType()+">.\n";
            stepsForQuery+="?step_"+i+" <http://www.opmw.org/ontology/isStepOfTemplate> <"+structureURI+">.\n";
        }
        String unionsForQuery="";
        String varsForQuery="";
        for(int i=0; i < depMatrix.length;i++){
            for(int j=i; j < depMatrix.length; j++){
                if(depMatrix[i][j]!=null && 
                            (depMatrix[i][j].equals(GeneralConstants.INFORM_DEPENDENCY))){
                    varsForQuery+=" ?dep_"+i+"_"+j+" ?dep_"+j+"_"+i;
                    unionsForQuery+="{?step_"+i+ " ?dep_"+i+"_"+j+" ?interm"+currentVar+".\n "
                            + "?interm"+currentVar+" ?dep2_"+i+"_"+j+" ?step_"+j+"}\nUNION\n"; 
                    unionsForQuery+="{?step_"+j+ " ?dep_"+j+"_"+i+" ?interm"+currentVar+".\n "
                            + "?interm"+currentVar+" ?dep2_"+j+"_"+i+" ?step_"+i+"}.\n"; 
                    //the response will have to be parsed in the same order in order to get the right
                    //dependencies.
                    currentVar++;
                }
            }
        }
        query+=varsForQuery+"\n";
        query+="WHERE{\n";
        query+=stepsForQuery;
        query+=unionsForQuery;
        query+="}LIMIT 1";        
        return query;
    }
    
    /**
     * Method to determine how many variables we need. The number corresponds
     * to the number of edges we can find in the matrix.
     * @return 
     */
//    private int getNumberOfVariablesForDirectionality(Fragment f){
//        String[][] depMatrix = f.getDependencyGraph().getAdjacencyMatrix();
//        int numDeps = 0;
//        for(int i=0; i < depMatrix.length;i++){
//            for(int j=i; j < depMatrix.length; j++){
//                if(depMatrix[i][j]!=null && 
//                            (depMatrix[i][j].equals(GeneralConstants.INFORM_DEPENDENCY))){
//                    numDeps ++;
//                }
//            }
//        }
//        return numDeps;
//    }
    
}
