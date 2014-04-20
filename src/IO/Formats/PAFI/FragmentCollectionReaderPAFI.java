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
package IO.Formats.PAFI;

import DataStructures.Fragment;
import DataStructures.Graph;
import DataStructures.GraphNode.GraphNode;
import IO.Exception.FragmentReaderException;
import IO.FragmentCollectionReader;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that processes the 3 output files produced by PAFI:
 * The result file with the fragments (.fp file)
 * The result file with the dependences (pc file)
 * The result file with the number of templates in which the fragment appears
 * (once per template) (.tid file)
 * 
 * NOTE THAT THE FRAGMENTS IN PAFI ARE NOT DIRECTED. They have to be "fixed" by 
 * calling the FixDirectionOfFragments Class.
 * 
 * @author Daniel Garijo
 */
public class FragmentCollectionReaderPAFI extends FragmentCollectionReader {
    //PAFI captures which transaction numbers support a fragment.
    //for example 1-1 0 1 5 means that the fragment was found in the graphs
    //0, 1 and 5 of the input file. We store this information in this hashmap.
    //it will be used later on the RDF generation.
    private HashMap<String,ArrayList<String>> occurrencesOfFragmentInTransaction;
    private String depFile, tidFile;

    public FragmentCollectionReaderPAFI(String resultFile, String depFile, String tidFile) {        
        this.resultFile = resultFile;
        this.depFile = depFile;
        this.tidFile = tidFile;
        this.finalResults = new HashMap<String, Fragment>(); 
    }
    
    

    /**
     * Support of the different fragments in the different transactions
     * @return fragmentID, set of transactions where it appears.
     */
    public HashMap<String, ArrayList<String>> getOccurrencesOfFragmentInTransaction() {
        return occurrencesOfFragmentInTransaction;
    }
    
    
    /**
     * Main method to process the 3 files produced by PAFI.
     * @param resultsFile PAFI result file with the catalog
     * @param depFile PAFI dependency file
     * @param tidFile PAFI occurrences file
     * @return
     * @throws FragmentReaderException 
     */
    @Override
    public HashMap<String,Fragment> getFragmentCatalogAsHashMap()throws FragmentReaderException{
        if(!finalResults.isEmpty())return finalResults;
        if(resultFile==null||depFile==null||tidFile==null)
            throw new FragmentReaderException("pc file, fp File or tid file not provided");
        this.processResultFile(resultFile);
        this.processPCFile(depFile);
        this.processTIDFile(tidFile);
        return finalResults;
    }

    @Override
    protected void processResultFile(String graphFile) throws FragmentReaderException {
        FileInputStream fstream =null;
        DataInputStream in = null;
        try{
            fstream = new FileInputStream(graphFile);            
            in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine, fragmentID = null, numOcc, aux;
            String[] idAndOcc;
            ArrayList<String> URIs = null; 
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
                            Fragment currentStructure = new Fragment(fragmentID, 0,auxg,null,true);
                            //all PAFI structures are multistep. The dependency list is updated by processing another file
                            finalResults.put(fragmentID, currentStructure) ;
                        }
                        aux = strLine.replace(" ", "");
                        aux = aux.substring(aux.indexOf("#")+1, aux.length());
                        idAndOcc = aux.split(",");
                        fragmentID = idAndOcc[0];
                        numOcc = idAndOcc[1];
                        //new fragment
//                        System.out.println("fragment "+fragmentID+","+numOcc);
                        URIs = new ArrayList<String>();
                        nodes = new HashMap<String, GraphNode>();
                        adjacencyMatrix = null; // we can only create it once we have all the nodes
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
                    }else if (strLine.startsWith("u")){
                        //add dependences.
                        if(adjacencyMatrix == null){
                            adjacencyMatrix = new String[URIs.size()][URIs.size()];
                        }
                        //retrieve positions: d row column relation
                        currLine = strLine.split(" ");
                        adjacencyMatrix[Integer.parseInt(currLine[1])][Integer.parseInt(currLine[2])] = currLine[3];
                        //it is not necessary, but in PAFI the edges are UNDIRECTED. Therefore the matrix would be in both directions.
//                        System.out.println(nodes.get(currLine[1]).getType()+" "+currLine[3]+" "+nodes.get(currLine[2]).getType());
                                                
                        }
                    }
                }
            }
            //add last fragment
            if(fragmentID!=null){
                Graph auxg = new Graph(URIs, nodes, adjacencyMatrix, fragmentID);
                Fragment currentStructure = new Fragment(fragmentID, 0,auxg,null,true);
                //all PAFI structures are multistep. The dependency list is updated by processing another file
                finalResults.put(fragmentID, currentStructure) ;
            }
//            System.out.print(numberOffragments);
            in.close();
            fstream.close();
        }catch(Exception e){
            System.err.println("Error while processing the file "+e.getMessage());
            throw new FragmentReaderException("Error while reading the file "+e.getMessage(), e);
        }
    }

    /**
     * Method designed to parse the dependency file
     * @param depFile 
     */
    private void processPCFile(String depFile) throws FragmentReaderException{
        FileInputStream fstream =null;
        DataInputStream in = null;
        if(finalResults==null){
            throw new FragmentReaderException("Fragment results (.fp file) must be processed before .pc file");
        }
        try{
            fstream = new FileInputStream(depFile);            
            in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;            
            while ((strLine = br.readLine()) != null)   {
                String[] idAndDep= strLine.split(" ");
                if(idAndDep.length>1){
                    String fragID = idAndDep[0].trim();                
                    ArrayList<Fragment> includedFragments = new ArrayList();
//                    System.out.println("fragment "+fragID+" contains:");
                    for(int i=1; i<idAndDep.length;i++){
                        includedFragments.add(finalResults.get(idAndDep[i]));
//                        System.out.println("\t"+finalResults.get(idAndDep[i]).getStructureID());
                    }
                    Fragment currentFragment = finalResults.get(fragID);
                    currentFragment.setListOfIncludedIDs(includedFragments);
                }
            }
            in.close();
            fstream.close();
        }catch(Exception e){
            System.err.println("Error while processing the file "+e.getMessage());
            throw new FragmentReaderException("Error while reading the file "+e.getMessage(), e);
        }
    }

    /**
     * Method designed to parse the TID file (occurrences)
     * @param tidFile 
     */
    private void processTIDFile(String tidFile) throws FragmentReaderException{
        FileInputStream fstream =null;
        DataInputStream in = null;
        try{
            fstream = new FileInputStream(tidFile);            
            in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            this.occurrencesOfFragmentInTransaction = new HashMap<String, ArrayList<String>>();
            while ((strLine = br.readLine()) != null)   {
                String[] idAndOccurrences = strLine.split(" ");
                String fragID = idAndOccurrences[0].trim();
                ArrayList<String> supportedTransactions = new ArrayList();
//                System.out.println("Fragment "+fragID+" appears "+(idAndOccurrences.length-1)+" times. In structures: ");
                for(int i=1; i<idAndOccurrences.length;i++){
                    supportedTransactions.add(idAndOccurrences[i]);
//                    System.out.println("\t"+idAndOccurrences[i]);
                }
                finalResults.get(fragID).setNumberOfOccurrences(idAndOccurrences.length-1);
                occurrencesOfFragmentInTransaction.put(fragID, supportedTransactions);
            }
            in.close();
            fstream.close();
        }catch(Exception e){
            System.err.println("Error while processing the file "+e.getMessage());
            throw new FragmentReaderException("Error while reading the file "+e.getMessage(), e);
        }
    }
    
}
