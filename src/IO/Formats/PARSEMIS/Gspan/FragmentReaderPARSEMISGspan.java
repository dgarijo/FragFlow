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
package IO.Formats.PARSEMIS.Gspan;

import DataStructures.Fragment;
import DataStructures.Graph;
import DataStructures.GraphNode.GraphNode;
import IO.Exception.FragmentReaderException;
import IO.FragmentReader;
import Static.GeneralConstants;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Extension of the fragment reader class to accept fragment files obtained by 
 * the PARSEMIS framework.
 * @author Daniel Garijo
 */
public class FragmentReaderPARSEMISGspan extends FragmentReader{
    private HashMap<String,ArrayList<String>> occurrencesOfFragmentInTransaction;
    //auxiliar structure to determine which fragment is included in which easily.
    private HashMap<String,ArrayList<String>> connectionsOfFragment;

    /**
     * Parsemis fragment reader: the result file holds all the information we 
     * need to parse the results.
     * @param resultFile 
     */
    public FragmentReaderPARSEMISGspan(String resultFile) {
        this.resultFile = resultFile;
        finalResults = new HashMap<String, Fragment>();
    }
    

    @Override
    protected void processResultFile(String graphFile) throws FragmentReaderException {
        //de process the number of occurrences of each fragment in each template
        //here as well.
        FileInputStream fstream =null;
        DataInputStream in = null;
        this.occurrencesOfFragmentInTransaction = new HashMap<String, ArrayList<String>>();
        connectionsOfFragment = new HashMap<String, ArrayList<String>>();
        try{
            fstream = new FileInputStream(graphFile);            
            in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine, fragmentID = null, aux;
            int numberOfOccurrencesOfFragment = 0;
            ArrayList<String> URIs = null,currentConnections = null; 
            HashMap<String, GraphNode> nodes = null;
            String[][] adjacencyMatrix = null;
            String [] currLine;
//            int numberOffragments = 0;
            while ((strLine = br.readLine()) != null)   {
                if(!strLine.startsWith("#")){                    
                    if(strLine.startsWith("t")){
                        //new structure, store the previous one
                        if(URIs!=null && fragmentID!=null){
                            Graph auxg = new Graph(URIs, nodes, adjacencyMatrix, fragmentID);
                            Fragment currentStructure = new Fragment(fragmentID, numberOfOccurrencesOfFragment,auxg,null,true);
                            //all PAFI structures are multistep. The dependency list is updated by processing another file
                            finalResults.put(fragmentID, currentStructure) ;
                            connectionsOfFragment.put(fragmentID, currentConnections);
                        }
                        aux = strLine.replace(" ", "");
//                        aux = aux.substring(aux.indexOf("#")+1, aux.length());                        
                        fragmentID = aux.split("#")[1];
//                        System.out.println("fragment "+fragmentID);
                        URIs = new ArrayList<String>();
                        currentConnections = new ArrayList<String>();
                        nodes = new HashMap<String, GraphNode>();
                        adjacencyMatrix = null; // we can only create it once we have all the nodes
                        numberOfOccurrencesOfFragment = 0;
                    }else{
                        if (strLine.startsWith("v")){
                        //add vertex to the uris and hashmap. Since we dont know
                        //URIs (we just work with the types), we use ids.
                        //we can't use the types as uris, otherwise the hashmap
                        //would be messed up (more than one node is of the same
                        //type
                        currLine = strLine.split(" ");
                        String id = currLine[1].trim();
                        String type = currLine[2].trim();
//                        System.out.println("v "+id+",type "+type);
                        URIs.add(id);                        
                        nodes.put(id, new GraphNode(id, type, Integer.parseInt(id)));
                    }else if (strLine.startsWith("e")){
                        //add dependences.
                        if(adjacencyMatrix == null){
                            adjacencyMatrix = new String[URIs.size()][URIs.size()];
                        }
                        //retrieve positions: d row column relation
                        currLine = strLine.split(" ");
                        adjacencyMatrix[Integer.parseInt(currLine[1])][Integer.parseInt(currLine[2])] = GeneralConstants.INFORM_DEPENDENCY;
                        currentConnections.add(currLine[3]);
                        //it is not necessary, but in PAFI the edges are UNDIRECTED. Therefore the matrix would be in both directions.
//                        System.out.println(nodes.get(currLine[1]).getType()+" "+GeneralConstants.INFORM_DEPENDENCY+" "+nodes.get(currLine[2]).getType());
                        }
                    }
                }
                if(strLine.startsWith("#=>")){
                    //add the numbers here
                    String templates = strLine.split(" ")[1];
//                    System.out.println("Fragment "+fragmentID+" appears in "+templates);
                    if(occurrencesOfFragmentInTransaction.containsKey(fragmentID)){
                        ArrayList<String> existingTransactions = occurrencesOfFragmentInTransaction.get(fragmentID);
                        if(!existingTransactions.contains(templates)){
                            existingTransactions.add(templates);
                            numberOfOccurrencesOfFragment++;
                        }
                        //this.occurrencesOfFragmentInTransaction.get(fragmentID).add(templates);
                    }else{
                        ArrayList<String> valueToAdd = new ArrayList<String>();
                        valueToAdd.add(templates);
                        this.occurrencesOfFragmentInTransaction.put(fragmentID,valueToAdd );
                        numberOfOccurrencesOfFragment++;
                    }
                }
            }
            //add last fragment
            if(fragmentID!=null){
                Graph auxg = new Graph(URIs, nodes, adjacencyMatrix, fragmentID);
                Fragment currentStructure = new Fragment(fragmentID, numberOfOccurrencesOfFragment,auxg,null,true);
                //all PAFI structures are multistep. The dependency list is updated by processing another file
                finalResults.put(fragmentID, currentStructure) ;
                connectionsOfFragment.put(fragmentID,currentConnections);
            }
//            System.out.print(numberOffragments);
            in.close();
            fstream.close();
        }catch(Exception e){
            System.err.println("Error while processing the file "+e.getMessage());
            throw new FragmentReaderException("Error while reading the file "+e.getMessage(), e);
        }
    }
    
    private void processIncludedFragments(){
        Iterator <String> keys = this.connectionsOfFragment.keySet().iterator();
        while(keys.hasNext()){
            String currentFragment = keys.next();
            //for each fragment, check whether if it is included in any of the rest
            Iterator<String> keys2 = connectionsOfFragment.keySet().iterator();
            while(keys2.hasNext()){
                String otherFragment = keys2.next();
                //check with every fragment except the same
                if(!currentFragment.equals(otherFragment)){
                    //check only if the current can be included in the other (less number of nodes).
                    if(finalResults.get(currentFragment).getDependencyGraph().getNodes().size()<
                            finalResults.get(otherFragment).getDependencyGraph().getNodes().size() ||
                            ((finalResults.get(currentFragment).getDependencyGraph().getNodes().size()==
                            finalResults.get(otherFragment).getDependencyGraph().getNodes().size()) && 
                                connectionsOfFragment.get(currentFragment).size()<connectionsOfFragment.get(otherFragment).size())){
                        //if the nodes are the same then the edges have to be lower to be included.
                        //if the current dependencies are in the other, then it is included.
                        ArrayList<String> currentDependencies = connectionsOfFragment.get(currentFragment);
                        ArrayList<String> otherDependencies = connectionsOfFragment.get(otherFragment);
                        boolean included = true;
                        Iterator<String> currentDepIt = currentDependencies.iterator();
                        while(currentDepIt.hasNext() && (included == true)){
                            included = otherDependencies.contains(currentDepIt.next());
                        }
                        if(included){
                            //add that the current fragment is included in the other fragment.
                            //That is, we add the current fragment in the other fragment list of included ids
//                            System.out.println("Fragment "+ currentFragment+" is included in "+otherFragment);
                            Fragment aux = finalResults.get(otherFragment);
                            if(aux.getListOfIncludedIDs()!=null){
                                aux.getListOfIncludedIDs().add(finalResults.get(currentFragment));
                            }else{
                                ArrayList<Fragment> newList = new ArrayList<Fragment>();
                                newList.add(finalResults.get(currentFragment));
                                aux.setListOfIncludedIDs(newList);
                            }
                        }
                    }
                }
            }
        }
    }

    public HashMap<String, ArrayList<String>> getOccurrencesOfFragmentInTransaction() {
        return occurrencesOfFragmentInTransaction;
    }

    @Override
    public HashMap<String, Fragment> getFragmentCatalogAsHashMap() throws FragmentReaderException {
        if(!finalResults.isEmpty())return finalResults;
        if(resultFile==null)
            throw new FragmentReaderException("Error while reading result file");
        this.processResultFile(resultFile);
        this.processIncludedFragments();
        return finalResults;
    }
    
//    public static void main(String[] args){
//        try{
//            String file = "PARSEMIS_TOOL\\results\\run11-03-2014.txt";
//            HashMap<String,Fragment> structureResults = new FragmentReaderPARSEMIS(file).getFragmentCatalogAsHashMap();
//        }catch(Exception e){
//            System.out.println("Error executing test. Exception: "+e.getMessage());
//        }
//    }
    
}
