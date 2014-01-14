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
package PostProcessing;
import DataStructures.Fragment;
import Factory.Inference.CreateAbstractResource;
import Factory.Inference.CreateHashMapForInference;
import Factory.OPMWTemplate2GraphProcessor;
import DataStructures.Graph;
import DataStructures.GraphCollection;
import DataStructures.GraphNode.GraphNode;
import IO.Formats.SubdueFragmentReader;
import Static.GeneralConstants;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
/**
 *
 * @author Daniel Garijo
 */
public class SUBDUEFragmentRecongnizer {
    private int indexForStructure;//auxiliar pointer to avoid problems regarding the repetition of substructures
    //they use the same uri sometimes.
//     private ArrayList<FinalResult> listOfPointersToIncludedStructures;

    public SUBDUEFragmentRecongnizer() {
        indexForStructure = 0;
    }
    
    private Fragment getPointerToSubstructure(String substructure,  Fragment fr){        
        Fragment structureToExplore = null;
        boolean found = false;
        //get the substructure on which we are going to perform the search
        Iterator<Fragment> it = fr.getListOfIncludedIDs().iterator();
        while(!found && it.hasNext()){
            structureToExplore = (Fragment)it.next();
            if(substructure.equals(structureToExplore.getStructureID())){
                found = true;
            }
        }
        return structureToExplore;
    }
    
    private int getNextIndex(){
        return indexForStructure++;
    }
    
    /**
     * Function that given a fragment generates possible bindings to the structure.
     * Many validations are performed. However, the results must be validated 
     * (to see whether it is a connected graph). 
     * Note that if there is no adjacency matrix, it is considered a non relevant substructure
     * and the binding is not returned
     * @param fr the fragment itself
     * @param biggerGraph the graph where we are looking at the template
     * @return  a list of possible bindings (to validate)
     */
    public ArrayList<HashMap<String,String>> generateBindingsFromFragmentInGraph(Fragment fr, Graph biggerGraph){
        String [][] adjMatrix = fr.getDependencyGraph().getAdjacencyMatrix();
        HashMap<String,GraphNode> graphNodes = fr.getDependencyGraph().getNodes();
        ArrayList<String> graphURIs = fr.getDependencyGraph().getURIs();
        
        ArrayList<HashMap<String,String>> bindings = new ArrayList<HashMap<String, String>>();
        bindings.add(new HashMap<String, String>());
        if(adjMatrix == null || adjMatrix.length == 0)return bindings;
        
        int indexForStructures = getNextIndex();
        for (int i=1;i< adjMatrix.length;i++){
            for(int j=1;j<adjMatrix.length;j++){
                if(adjMatrix[i][j]!=null && adjMatrix[i][j].equals(GeneralConstants.INFORM_DEPENDENCY)){                    
                    String currentTypeI = graphNodes.get(graphURIs.get(i-1)).getType();
                    String currentTypeJ = graphNodes.get(graphURIs.get(j-1)).getType();
                    ArrayList<String> candidateURIsWithCurrentType = new ArrayList<String>();
                    ArrayList<String> bigGraphURIs = biggerGraph.getURIs();
                    HashMap<String,GraphNode> urisAndNodesBigGraph = biggerGraph.getNodes();
                    String[][] biggerGraphAdjMatrix = biggerGraph.getAdjacencyMatrix();
                    Iterator<String> bigGraphURIIterator = bigGraphURIs.iterator();
                    Iterator<HashMap<String,String>> itHashMaps;
                    ArrayList<HashMap<String,String>> otherBindings ;
                                    
                    if(currentTypeI.contains("SUB_")||currentTypeJ.contains("SUB_")){
                        if(currentTypeI.contains("SUB_")&&(currentTypeJ.contains("SUB_"))){
//                           TO DO
                            //this is a very rare case. We have to merge the bindings between both substructures
                            //there has to be a connection between them as well
//                            FinalResult subStructureI = getPointerToSubstructure(currentTypeI, fr);
//                            ArrayList<HashMap<String,String>> bindingsFromSubStructureI = this.generateBindingsFromFragmentInGraph(subStructureI, biggerGraph);
//                            FinalResult subStructureJ = getPointerToSubstructure(currentTypeJ, fr);
//                            ArrayList<HashMap<String,String>> bindingsFromSubStructureJ = this.generateBindingsFromFragmentInGraph(subStructureJ, biggerGraph);
//                            
//                            while(bigGraphURIIterator.hasNext()){
//                                    String currentURIinBiggerGraph = bigGraphURIIterator.next();
//                                    if(urisAndNodesBigGraph.get(currentURIinBiggerGraph).getType().equals(currentTypeI)){
//                                        candidateURIsWithCurrentType.add(currentURIinBiggerGraph);
//                                    }
//                                }
//
//                                otherBindings = new ArrayList<HashMap<String, String>>();
//
//                                for(int k = 0; k<candidateURIsWithCurrentType.size(); k++){                                
//                                    int lineToSearch = bigGraphURIs.indexOf(candidateURIsWithCurrentType.get(k))+1;
//                                    Iterator<HashMap<String,String>> itBindings = bindingsFromSubStructure.iterator();                                
//                                    //we analyze all possible existing bindings
//                                    while(itBindings.hasNext()){
//                                        HashMap<String,String> currentHashMap = itBindings.next();
//                                        //copy the current bindings in an auxiliar structure
//                                        HashMap<String, String> currHashMapCopy = new HashMap<String, String>();
//                                        Iterator<String> keysToCopy = currentHashMap.keySet().iterator();
//                                        while(keysToCopy.hasNext()){
//                                            String cKey = keysToCopy.next();
//                                            currHashMapCopy.put(cKey, currentHashMap.get(cKey));
//                                        }
//                                        //if there is a connection between the binding and the candidate structure, then add it as a possible binding
//                                        Iterator<String> keys = currentHashMap.keySet().iterator();
//                                        boolean connected = false; //to indicate whether there is a connection or not
//                                        while(keys.hasNext()){
//                                            String currentURI = keys.next();
//                                            int hashMapURIPosition = bigGraphURIs.indexOf(currentURI)+1;
//                                            //check if there is a dependency in the bigger graph
//                                            if(biggerGraphAdjMatrix[lineToSearch][hashMapURIPosition]!=null && biggerGraphAdjMatrix[lineToSearch][hashMapURIPosition].equals(GeneralConstants.INFORM_DEPENDENCY)){
//                                                //We don't have to match types here. We just have to create the binding (the type is SUB2)                                            
//    //                                            System.out.println("bind "+ "-"+indexForStructures+"-"+graphURIs.get(i-1) + " with "+ candidateURIsWithCurrentType.get(k));
//    //                                            System.out.println("bind "+ "-"+indexForStructures+"-"+graphURIs.get(j-1) + " with "+ bigGraphURIs.get(hashMapURIPosition-1));
//
//                                                if(!currHashMapCopy.containsKey("-"+indexForStructures+"-"+graphURIs.get(i-1))&&
//                                                        !currHashMapCopy.containsKey(candidateURIsWithCurrentType.get(k))){
//                                                    currHashMapCopy.put("-"+indexForStructures+"-"+graphURIs.get(i-1),candidateURIsWithCurrentType.get(k));
//                                                    currHashMapCopy.put(candidateURIsWithCurrentType.get(k),"-"+indexForStructures+"-"+graphURIs.get(i-1));
//                                                    connected = true;
//                                                }
//                                                if(!currHashMapCopy.containsKey("-"+indexForStructures+"-"+graphURIs.get(j-1))&&
//                                                        !currHashMapCopy.containsKey(bigGraphURIs.get(hashMapURIPosition-1))){
//                                                    currHashMapCopy.put("-"+indexForStructures+"-"+graphURIs.get(j-1),bigGraphURIs.get(hashMapURIPosition-1));
//                                                    currHashMapCopy.put(bigGraphURIs.get(hashMapURIPosition-1),"-"+indexForStructures+"-"+graphURIs.get(j-1));
//                                                    connected = true;
//                                                }
//                                            }                                        
//                                        }
//                                        //copy to otherbindings, if there is a valid connection
//                                        if(connected){otherBindings.add(currHashMapCopy);}
//                                    }                                
//                                }
//                                if(bindings.size()==1 && bindings.get(0).isEmpty()){
//                                    bindings = otherBindings;
//                                }else{
//                                    //add otherbindings to bindings
//                                    Iterator<HashMap<String,String>> otherBIterator = otherBindings.iterator();
//                                    while(otherBIterator.hasNext()){
//                                        HashMap<String,String> hToAdd = otherBIterator.next();
//                                        bindings.add(hToAdd);
//                                    }
//                                }
                        }
                        else{
                            if(currentTypeI.contains("SUB_")){
                                //this bit of code is equal to the other SUB part. Make a method
                                Fragment subStructure = getPointerToSubstructure(currentTypeI, fr);
                                ArrayList<HashMap<String,String>> bindingsFromSubStructure = this.generateBindingsFromFragmentInGraph(subStructure, biggerGraph);
                                //in the aux bindings now we have all the possible valid bindings for the fragment
                                //iterate over the bindings
                                //generate candidate uris
                                while(bigGraphURIIterator.hasNext()){
                                    String currentURIinBiggerGraph = bigGraphURIIterator.next();
                                    if(urisAndNodesBigGraph.get(currentURIinBiggerGraph).getType().equals(currentTypeJ)){
                                        candidateURIsWithCurrentType.add(currentURIinBiggerGraph);
                                    }
                                }
                                otherBindings = new ArrayList<HashMap<String, String>>();

                                for(int k = 0; k<candidateURIsWithCurrentType.size(); k++){                                
                                    int lineToSearch = bigGraphURIs.indexOf(candidateURIsWithCurrentType.get(k))+1;
                                    Iterator<HashMap<String,String>> itBindings = bindingsFromSubStructure.iterator();                                
                                    //we analyze all possible existing bindings
                                    while(itBindings.hasNext()){
                                        HashMap<String,String> currentHashMap = itBindings.next();
                                        //copy the current bindings in an auxiliar structure
                                        HashMap<String, String> currHashMapCopy = new HashMap<String, String>();
                                        Iterator<String> keysToCopy = currentHashMap.keySet().iterator();
                                        while(keysToCopy.hasNext()){
                                            String cKey = keysToCopy.next();
                                            currHashMapCopy.put(cKey, currentHashMap.get(cKey));
                                        }
                                        //if there is a connection between the binding and the candidate structure, then add it as a possible binding
                                        Iterator<String> keys = currentHashMap.keySet().iterator();
                                        boolean connected = false; //to indicate whether there is a connection or not
                                        while(keys.hasNext()){
                                            String currentURI = keys.next();
                                            int hashMapURIPosition = bigGraphURIs.indexOf(currentURI)+1;
                                            //check if there is a dependency in the bigger graph
                                            if(biggerGraphAdjMatrix[lineToSearch][hashMapURIPosition]!=null && biggerGraphAdjMatrix[lineToSearch][hashMapURIPosition].equals(GeneralConstants.INFORM_DEPENDENCY)){
                                                //We don't have to match types here. We just have to create the binding (the type is SUB2)                                            
//                                                if(!currHashMapCopy.containsKey("-"+indexForStructures+"-"+graphURIs.get(i-1))&&
//                                                        !currHashMapCopy.containsKey(candidateURIsWithCurrentType.get(k))){
//                                                    currHashMapCopy.put("-"+indexForStructures+"-"+graphURIs.get(i-1),candidateURIsWithCurrentType.get(k));
//                                                    currHashMapCopy.put(candidateURIsWithCurrentType.get(k),"-"+indexForStructures+"-"+graphURIs.get(i-1));
//                                                    connected = true;
//                                                }
                                                if(!currHashMapCopy.containsKey("-"+indexForStructures+"-"+graphURIs.get(j-1))&&
                                                        !currHashMapCopy.containsKey(bigGraphURIs.get(hashMapURIPosition-1))){
                                                    currHashMapCopy.put("-"+indexForStructures+"-"+graphURIs.get(j-1),bigGraphURIs.get(hashMapURIPosition-1));
                                                    currHashMapCopy.put(bigGraphURIs.get(hashMapURIPosition-1),"-"+indexForStructures+"-"+graphURIs.get(j-1));
                                                    connected = true;
                                                }
                                            }                                        
                                        }
                                        //copy to otherbindings, if there is a valid connection
                                        if(connected){otherBindings.add(currHashMapCopy);}
                                    }                                
                                }
                                if(bindings.size()==1 && bindings.get(0).isEmpty()){
                                    bindings = otherBindings;
                                }else{
                                    //add otherbindings to bindings
                                    Iterator<HashMap<String,String>> otherBIterator = otherBindings.iterator();
                                    while(otherBIterator.hasNext()){
                                        HashMap<String,String> hToAdd = otherBIterator.next();
                                        bindings.add(hToAdd);
                                    }
                                }
                            }
                            if(currentTypeJ.contains("SUB_")){
                                //first we get the substructure pointer
                                Fragment subStructure = getPointerToSubstructure(currentTypeJ, fr);
                                ArrayList<HashMap<String,String>> bindingsFromSubStructure = this.generateBindingsFromFragmentInGraph(subStructure, biggerGraph);
                                //in the aux bindings now we have all the possible valid bindings for the fragment
                                //iterate over the bindings
                                //generate candidate uris
                                while(bigGraphURIIterator.hasNext()){
                                    String currentURIinBiggerGraph = bigGraphURIIterator.next();
                                    if(urisAndNodesBigGraph.get(currentURIinBiggerGraph).getType().equals(currentTypeI)){
                                        candidateURIsWithCurrentType.add(currentURIinBiggerGraph);
                                    }
                                }

                                otherBindings = new ArrayList<HashMap<String, String>>();

                                for(int k = 0; k<candidateURIsWithCurrentType.size(); k++){                                
                                    int lineToSearch = bigGraphURIs.indexOf(candidateURIsWithCurrentType.get(k))+1;
                                    Iterator<HashMap<String,String>> itBindings = bindingsFromSubStructure.iterator();                                
                                    //we analyze all possible existing bindings
                                    while(itBindings.hasNext()){
                                        HashMap<String,String> currentHashMap = itBindings.next();
                                        //copy the current bindings in an auxiliar structure
                                        HashMap<String, String> currHashMapCopy = new HashMap<String, String>();
                                        Iterator<String> keysToCopy = currentHashMap.keySet().iterator();
                                        while(keysToCopy.hasNext()){
                                            String cKey = keysToCopy.next();
                                            currHashMapCopy.put(cKey, currentHashMap.get(cKey));
                                        }
                                        //if there is a connection between the binding and the candidate structure, then add it as a possible binding
                                        Iterator<String> keys = currentHashMap.keySet().iterator();
                                        boolean connected = false; //to indicate whether there is a connection or not
                                        while(keys.hasNext()){
                                            String currentURI = keys.next();
                                            int hashMapURIPosition = bigGraphURIs.indexOf(currentURI)+1;
                                            //check if there is a dependency in the bigger graph
                                            if(biggerGraphAdjMatrix[lineToSearch][hashMapURIPosition]!=null && biggerGraphAdjMatrix[lineToSearch][hashMapURIPosition].equals(GeneralConstants.INFORM_DEPENDENCY)){
                                                //We don't have to match types here. We just have to create the binding (the type is SUB2)                                            
    //                                            System.out.println("bind "+ "-"+indexForStructures+"-"+graphURIs.get(i-1) + " with "+ candidateURIsWithCurrentType.get(k));
    //                                            System.out.println("bind "+ "-"+indexForStructures+"-"+graphURIs.get(j-1) + " with "+ bigGraphURIs.get(hashMapURIPosition-1));

                                                if(!currHashMapCopy.containsKey("-"+indexForStructures+"-"+graphURIs.get(i-1))&&
                                                        !currHashMapCopy.containsKey(candidateURIsWithCurrentType.get(k))){
                                                    currHashMapCopy.put("-"+indexForStructures+"-"+graphURIs.get(i-1),candidateURIsWithCurrentType.get(k));
                                                    currHashMapCopy.put(candidateURIsWithCurrentType.get(k),"-"+indexForStructures+"-"+graphURIs.get(i-1));
                                                    connected = true;
                                                }
//                                                if(!currHashMapCopy.containsKey("-"+indexForStructures+"-"+graphURIs.get(j-1))&&
//                                                        !currHashMapCopy.containsKey(bigGraphURIs.get(hashMapURIPosition-1))){
//                                                    currHashMapCopy.put("-"+indexForStructures+"-"+graphURIs.get(j-1),bigGraphURIs.get(hashMapURIPosition-1));
//                                                    currHashMapCopy.put(bigGraphURIs.get(hashMapURIPosition-1),"-"+indexForStructures+"-"+graphURIs.get(j-1));
//                                                    connected = true;
//                                                }
                                            }                                        
                                        }
                                        //copy to otherbindings, if there is a valid connection
                                        if(connected){otherBindings.add(currHashMapCopy);}
                                    }                                
                                }
                                if(bindings.size()==1 && bindings.get(0).isEmpty()){
                                    bindings = otherBindings;
                                }else{
                                    //add otherbindings to bindings
                                    Iterator<HashMap<String,String>> otherBIterator = otherBindings.iterator();
                                    while(otherBIterator.hasNext()){
                                        HashMap<String,String> hToAdd = otherBIterator.next();
                                        bindings.add(hToAdd);
                                    }
                                }
                            }
                        }
                    }else{
                        //we search and bind the variables.
                        //Search all the variables with a promising type.
                        
                        while(bigGraphURIIterator.hasNext()){
                            String currentURIinBiggerGraph = bigGraphURIIterator.next();
                            if(urisAndNodesBigGraph.get(currentURIinBiggerGraph).getType().equals(currentTypeI)){
                                candidateURIsWithCurrentType.add(currentURIinBiggerGraph);
                            }
                        }                    
                        //for each URI, check whether there is a connection to a node of the type node j or not.
                        for(int k = 0; k<candidateURIsWithCurrentType.size(); k++){                        
                            //search in the adjacency matriz of the big graph for the connections
                            int lineToSearch = bigGraphURIs.indexOf(candidateURIsWithCurrentType.get(k))+1;
//                            boolean foundInLine = false;//this is not needed due to the possibility of multiple bindings in a fragment
                            for(int p = 0; p<biggerGraphAdjMatrix.length;p++){
                                if(biggerGraphAdjMatrix[lineToSearch][p]!=null && biggerGraphAdjMatrix[lineToSearch][p].equals(GeneralConstants.INFORM_DEPENDENCY)){
                                    //if the type of the URI (j) in the bigger graph is the same as current j, we bind it
                                    //we add the possible combinations of bindings in case we find more than one
                                    itHashMaps = bindings.iterator();
                                    otherBindings = new ArrayList<HashMap<String, String>>();
                                    HashMap<String,String> aux;
                                    boolean added; //aux to know whether we add a key or not
                                    while(itHashMaps.hasNext()){
                                        added = false;
                                        HashMap<String,String> currentHashMap = itHashMaps.next();
                                        aux = null; //to keep the track of other attachments
                                        //if the connection is there, we add the binding
                                        if(urisAndNodesBigGraph.get(bigGraphURIs.get(p-1)).getType().equals(currentTypeJ)){                                                                                                                        
                                            if(!currentHashMap.containsKey("-"+indexForStructures+"-"+graphURIs.get(i-1))&&
                                                    !currentHashMap.containsKey(candidateURIsWithCurrentType.get(k))){
                                                
                                                //if the hashmap contains the other part (i.e., it is bound), then we must make sure it is a correct binding
                                                if(currentHashMap.containsKey("-"+indexForStructures+"-"+graphURIs.get(j-1))){
                                                    //we are asking: Is the URI we are going to add the binding for a valid binding?                                                    
                                                    int indexOfUriIInBG = bigGraphURIs.indexOf(candidateURIsWithCurrentType.get(k))+1;
                                                    String uriBound = currentHashMap.get("-"+indexForStructures+"-"+graphURIs.get(j-1));
                                                    int indexOfUriJInBG = bigGraphURIs.indexOf(uriBound)+1;
//                                                    String testToDelete = biggerGraphAdjMatrix[indexOfUriIInBG][indexOfUriJInBG];
                                                    if((biggerGraphAdjMatrix[indexOfUriIInBG][indexOfUriJInBG]!=null)&&(biggerGraphAdjMatrix[indexOfUriIInBG][indexOfUriJInBG].equals(GeneralConstants.INFORM_DEPENDENCY))){
                                                        currentHashMap.put("-"+indexForStructures+"-"+graphURIs.get(i-1),candidateURIsWithCurrentType.get(k));
                                                        currentHashMap.put(candidateURIsWithCurrentType.get(k),"-"+indexForStructures+"-"+graphURIs.get(i-1));
                                                        added = true;
                                                    }
                                                }else{
                                                    currentHashMap.put("-"+indexForStructures+"-"+graphURIs.get(i-1),candidateURIsWithCurrentType.get(k));
                                                    currentHashMap.put(candidateURIsWithCurrentType.get(k),"-"+indexForStructures+"-"+graphURIs.get(i-1));
                                                    added = true;
                                                }
//                                                System.out.println("bind "+graphURIs.get(i-1)+" with "+candidateURIsWithCurrentType.get(k));
                                            }                                            
                                            if(!currentHashMap.containsKey("-"+indexForStructures+"-"+graphURIs.get(j-1))&&
                                                    !currentHashMap.containsKey(bigGraphURIs.get(p-1))){
                                                //if the hashmap contains the other part (i.e., it is bound), then we must make sure it is a correct binding
                                                if(currentHashMap.containsKey("-"+indexForStructures+"-"+graphURIs.get(i-1))){
                                                    //we are asking: Is the URI we are going to add the binding for a valid binding?                                                    
                                                    int indexOfUriJInBG = bigGraphURIs.indexOf(bigGraphURIs.get(p-1))+1;
                                                    String uriBound = currentHashMap.get("-"+indexForStructures+"-"+graphURIs.get(i-1));
                                                    int indexOfUriIInBG = bigGraphURIs.indexOf(uriBound)+1;
//                                                    String testToDelete = biggerGraphAdjMatrix[indexOfUriIInBG][indexOfUriJInBG];
                                                    if((biggerGraphAdjMatrix[indexOfUriIInBG][indexOfUriJInBG]!=null)&&(biggerGraphAdjMatrix[indexOfUriIInBG][indexOfUriJInBG].equals(GeneralConstants.INFORM_DEPENDENCY))){
                                                        currentHashMap.put("-"+indexForStructures+"-"+graphURIs.get(j-1),bigGraphURIs.get(p-1));
                                                        currentHashMap.put(bigGraphURIs.get(p-1),"-"+indexForStructures+"-"+graphURIs.get(j-1));
                                                        added = true;
                                                    }
                                                }else{
                                                    currentHashMap.put("-"+indexForStructures+"-"+graphURIs.get(j-1),bigGraphURIs.get(p-1));
                                                    currentHashMap.put(bigGraphURIs.get(p-1),"-"+indexForStructures+"-"+graphURIs.get(j-1));
                                                    added = true;
                                                }
//                                                currentHashMap.put("-"+indexForStructures+"-"+graphURIs.get(j-1),bigGraphURIs.get(p-1));
//                                                currentHashMap.put(bigGraphURIs.get(p-1),"-"+indexForStructures+"-"+graphURIs.get(j-1));
//                                                added = true;
//                                                System.out.println("bind "+graphURIs.get(j-1)+" with "+bigGraphURIs.get(p-1));
                                            }
                                            //if the hashmap contains both keys, we have to add it as a new valid binding
                                            if(!added && currentHashMap.containsKey("-"+indexForStructures+"-"+graphURIs.get(i-1))
                                                    && currentHashMap.containsKey("-"+indexForStructures+"-"+graphURIs.get(j-1))){
                                                //copy the bound values to a new hashmap (except for the old i,j)
                                                aux = new HashMap<String, String>();
                                                Iterator<String> keysToCopy = currentHashMap.keySet().iterator();                                                
                                                while(keysToCopy.hasNext()){
                                                    String key = keysToCopy.next();
                                                    String value = currentHashMap.get(key);
                                                    //do not add the bound keys for this variables, or any other 
                                                    //uri bound to the keys. We don't want a uri bound to more than one node.
                                                    if(!key.equals("-"+indexForStructures+"-"+graphURIs.get(i-1))&&
                                                            !key.equals("-"+indexForStructures+"-"+graphURIs.get(j-1))&&
                                                                !value.equals("-"+indexForStructures+"-"+graphURIs.get(i-1))&&
                                                                    !value.equals("-"+indexForStructures+"-"+graphURIs.get(j-1))){
                                                        aux.put(key,currentHashMap.get(key));
                                                    }
                                                }
                                                //we can not fully validate whether it is a valid binding or not
                                                //since we may not have bound the full structure. We will validate this in the end
                                                aux.put("-"+indexForStructures+"-"+graphURIs.get(i-1),candidateURIsWithCurrentType.get(k));
                                                aux.put("-"+indexForStructures+"-"+graphURIs.get(j-1),bigGraphURIs.get(p-1));
                                                aux.put(candidateURIsWithCurrentType.get(k),"-"+indexForStructures+"-"+graphURIs.get(i-1));
                                                aux.put(bigGraphURIs.get(p-1),"-"+indexForStructures+"-"+graphURIs.get(j-1));
                                                //attach them to the hashMap arrayList
                                                otherBindings.add(aux);
                                            }
                                        }
                                    }
                                    //now that we have the other bindings, append them to the arrayList
                                    Iterator<HashMap<String,String>> otherBIterator = otherBindings.iterator();
                                    while(otherBIterator.hasNext()){
                                        HashMap<String,String> hToAdd = otherBIterator.next();
                                        bindings.add(hToAdd);
                                    }
                                }                             
                            }
                        }
                    }
                }
            }            
        }
        return bindings;        
    }
    /**
     * Function that given a set of possible bindings, it validates it and removes duplicates
     * @param bindingsToValidate the set of bindings to validate
     * @param g to which the bindings have been bound
     * @param fragment the fragment that corresponds to the bindings
     * @return  an ArrayList  of validated bindings containing the URIs to which each fragment is bound.
     */
    public ArrayList<HashMap<String,String>> validateBindings(ArrayList<HashMap<String,String>> bindingsToValidate, Graph g, Fragment fragment){
        ArrayList<HashMap<String,String>> validatedBindings = new ArrayList<HashMap<String, String>>();
        Iterator<HashMap<String,String>> itBindings = bindingsToValidate.iterator();
        while(itBindings.hasNext()){
            HashMap<String,String> current = itBindings.next();
            if(validateBinding(current, g,fragment)){
                //check whether the binding is there already!
                if(!isRepeatedBinding(validatedBindings, current)){
                    validatedBindings.add(current);
                }
            }        
        }
        return validatedBindings;        
    }
    
    /**
     * Function created to check whether a binding has already been recorded as such or not
     * @param validatedBindings
     * @param currentBinding
     * @return boolean specifying whether the binding is in the list or not.
     */
    private boolean isRepeatedBinding( ArrayList<HashMap<String,String>> validatedBindings, HashMap<String,String> bindingToCompare){
        Iterator <HashMap<String,String>> currentBindingsIterator = validatedBindings.iterator();
        while (currentBindingsIterator.hasNext()){
            boolean theyAreTheSame = true;
            HashMap<String,String> currentBinding = currentBindingsIterator.next();
            //is the binding to compare the same as the current one? 
            //are all its keys contained? are the values the same?
            if(currentBinding.size() == bindingToCompare.size()){
                Iterator<String> currentBindingKeys = currentBinding.keySet().iterator();
                while(currentBindingKeys.hasNext() && theyAreTheSame){
                    //if if a key is not contained or its value is not the same, then they are not the same.
                    String currentBindingKey = currentBindingKeys.next();
                    //if all the URIs are contained in the other hashmap, they are the same (same bindings)
                    if(currentBindingKey.contains("http://")){
                        if(!bindingToCompare.containsKey(currentBindingKey)) theyAreTheSame = false;
//                        else{
//                            if(!bindingToCompare.get(currentBindingKey).equals(bindingToCompare.get(currentBindingKey))){
//                                theyAreTheSame = false;
//                            }
//                        }
                    }
                }
                if(theyAreTheSame){
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Function that given a binding, it determines whether is valid or not.
     * @param toValidate the set of bindings to validate
     * @param g the graph where we want to check whether the fragment has been found or not
     * @param fragment the fragment from which the bindings come
     * @return 
     */
    private boolean validateBinding(HashMap<String,String> toValidate, Graph g, Fragment fragment){
        if(toValidate.isEmpty())return false;
        //if the size of the bindings found is smaller than the fragment itself, then it is not valid
        if((toValidate.size()/2)!=fragment.getSize())return false;
        //are all the URIs connected? We start placing the URI in a group. If 
        //we try to merge the groups and there are uris that belong to separated ones,
        //it means that it is an invalid binding. All the uris should end up in a group        
        String[][] adyMatrix = g.getAdjacencyMatrix();
        ArrayList<String> urisBiggerGraph = g.getURIs();
        Iterator <String> bindings= toValidate.keySet().iterator();
        boolean finished = false;
        ArrayList<String> urisToValidate = new ArrayList<String>();
        ArrayList<ArrayList<String>> groups = new ArrayList<ArrayList<String>>();

        //initialization: we get all the URIs to validate
        while(bindings.hasNext()){
            String aux = bindings.next();
            if(urisBiggerGraph.contains(aux)){
                urisToValidate.add(aux);
            }
        }
        //generate first round of groups. The uris must be those within the uris to validate
        Iterator<String> itUris = urisToValidate.iterator();
        while(itUris.hasNext()){
            String currURI = itUris.next();
            int rowAndColumnInMatrix = urisBiggerGraph.indexOf(currURI)+1;
            ArrayList<String> newGroup = new ArrayList<String>();
            newGroup.add(currURI);
            for(int i=rowAndColumnInMatrix;i<adyMatrix.length;i++){
                if(adyMatrix[rowAndColumnInMatrix][i]!=null && adyMatrix[rowAndColumnInMatrix][i].equals(GeneralConstants.INFORM_DEPENDENCY)){
                    if(urisToValidate.contains(urisBiggerGraph.get(i-1))){
                        newGroup.add(urisBiggerGraph.get(i-1));
                    }                    
                }
                if(adyMatrix[i][rowAndColumnInMatrix]!=null && adyMatrix[i][rowAndColumnInMatrix].equals(GeneralConstants.INFORM_DEPENDENCY)){
                    if(urisToValidate.contains(urisBiggerGraph.get(i-1))){
                        newGroup.add(urisBiggerGraph.get(i-1));
                    }
                }
            }
            //if the group is not just the uri itself, add it as a new group
            if(newGroup.size()>1){
                groups.add(newGroup);
            }
        }
        //now we have all the groups. Do any of the groups share a URI? If they do,
        //merge the groups until we can't continue. If what remains is 1 group, the binding is valid
        while(!finished){
            if(groups.size() == 1){
                return true;
            }
            ArrayList<ArrayList<String>> newGroups = new ArrayList<ArrayList<String>>();
            ArrayList<ArrayList<String>> mergedGroups = new ArrayList<ArrayList<String>>();
            for(int i = 0; i< groups.size(); i++){
                ArrayList<String> currentGroup = groups.get(i);
                ArrayList<String> possibleNewGroup = new ArrayList<String>();
                //we copy the current group in the iterator
                Iterator<String> copyIt = currentGroup.iterator();
                while(copyIt.hasNext()){
                    possibleNewGroup.add(copyIt.next());
                }
                boolean newGroupExpanded = false;
                //for each member of the group, can it be found in the other ones?
                Iterator<String> currMemberOfTheGroupIt = currentGroup.iterator();
                while(currMemberOfTheGroupIt.hasNext()){
                    String currMemberOfTheGroup = currMemberOfTheGroupIt.next();
                    for(int j = i+1; j<groups.size();j++){
                        if(!mergedGroups.contains(groups.get(j)) &&
                            groups.get(j).contains(currMemberOfTheGroup)){
                            mergedGroups.add(groups.get(i));
                            mergedGroups.add(groups.get(j));
                            //add all the current group as part of new group
                            Iterator<String> it = groups.get(j).iterator();
                            newGroupExpanded = true;
                            while(it.hasNext()){
                                String toAdd = it.next();                                 
                                if(!possibleNewGroup.contains(toAdd)){
                                    possibleNewGroup.add(toAdd);                                    
                                }
                            }
                        }                            
                    }   
                }
                //if the new group has been expanded, add it as new.
                //if not, add the original group(as long as it hasn't been merged)
                if(newGroupExpanded){
                    newGroups.add(possibleNewGroup);
                }else{
                    if(!mergedGroups.contains(currentGroup)){
                        newGroups.add(currentGroup);
                    }
                }
            }
            if(newGroups.size()<groups.size()){
                groups = newGroups;
            }else{
                if(newGroups.size()>1)finished = true; //that means that we have not reduced and there is more than 1 group
            }
            
        }
        return false;
    }
    
    //Just for tests. Will have to do a proper invocation script later.
    public static void main(String[] args){
        //get one graph from the workflow collection
        //tests on a template
        OPMWTemplate2GraphProcessor test = new OPMWTemplate2GraphProcessor("http://wind.isi.edu:8890/sparql");
//        test.transformToSubdueGraph("http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_SINGLE_");
        test.transformToGraph("http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI");
//        test.transformToSubdueGraph("http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLUSTERING");
//        test.transformToSubdueGraph("http://www.opmw.org/export/resource/WorkflowTemplate/FEATURESELECTION");

        Graph testGraph = test.getGraphCollection().getGraphCollection().get(0);
        testGraph.putReducedNodesInAdjacencyMatrix();
        //get one final result (or more)
//        String file = "SUBDUE_TOOL\\results\\Tests\\testResultReducedFake";
//        String file = "SUBDUE_TOOL\\results\\Tests\\testResultReduced2";
//        String ocFile = "SUBDUE_TOOL\\results\\Tests\\testResultReduced2_occurrences";
        String file = "resultsAbstractCatalog24-10-2013";
        String ocFile = "resultsAbstractCatalog24-10-2013_occurrences";
        HashMap<String,Fragment> obtainedResults = new SubdueFragmentReader().processResultsAndOccurrencesFiles(file, ocFile);
        
        //with inference
       //first we get the replacement hashmap
       String taxonomyFilePath = "src\\TestFiles\\multiDomainOnto.owl"; //we assume the file has already been created.
       OntModel o = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
       InputStream in = FileManager.get().open(taxonomyFilePath);
       o.read(in, null);        
       HashMap replacements = CreateHashMapForInference.createReplacementHashMap(o);
       
       //we create the abstract collection
       GraphCollection abstractCollection = CreateAbstractResource.createAbstractCollection(test.getGraphCollection(), replacements);
        //check whether the fragment is found properly or not.
        //we ask for the first one (which is included)
//        System.out.println("This test should be true: .... "+obtainedResults.get("SUB_1").isPartOfGraph(testGraph));
        //then we try with another one that is composed and also included
//        System.out.println("This test should be true: .... "+obtainedResults.get("SUB_2").isPartOfGraph(testGraph));
        //then we try with another one which is even more complex to see wheter it's included
//        System.out.println("This test should be true: .... "+obtainedResults.get("SUB_8").isPartOfGraph(testGraph));
        //then we get one that overlaps partly but one of the subparts is not included.
//        System.out.println("This test should be false: .... "+obtainedResults.get("SUB_3").isPartOfGraph(testGraph));
//        //then we try the same with one where the core parts are not even included
//        System.out.println("This test should be false: .... "+obtainedResults.get("SUB_4").isPartOfGraph(testGraph));
        
//        System.out.println("This test should be false: .... "+obtainedResults.get("SUB_6").isPartOfGraph(testGraph));
        //should do a test where the fragment is included but not in the same sequence.
        
        //tests on a sequence.
//        FinalResult f = obtainedResults.get("SUB_7");//SUB_3
////        FinalResult f = obtainedResults.get("SUB_7");
//        ArrayList<HashMap<String,String>> b = f.generateBindingsFromFragmentInGraph(f, testGraph);
//        b = f.validateBindings(b, testGraph);
//        System.out.println("Bindings size (number of occurrences of fragment in graph): "+b.size());
        
        Iterator<String> fragments = obtainedResults.keySet().iterator();
        //it would be nice to just send the relevant fragments    
        SUBDUEFragmentRecongnizer fr = new SUBDUEFragmentRecongnizer();
        while(fragments.hasNext()){
            Fragment f = obtainedResults.get(fragments.next());
            System.out.println("Size of fragment "+f.getStructureID()+" is "+ f.getSize());
            if(f.isMultiStepStructure()){
                ArrayList<HashMap<String,String>> b = fr.generateBindingsFromFragmentInGraph(f, testGraph);
                b = fr.validateBindings(b, testGraph, f);
                //step here to delete duplicated bindings (in some remote cases it could happen)
                System.out.println("Number of occurrences of fragment "+f.getStructureID()+" in graph: "+b.size());
            }
        }
        
//        if(!b.isEmpty())System.out.println("FOUND");        
//        ArrayList sequencesToCheck = f.getNumberSequencesFromAdjMatrix(f.getDependencyGraph().getAdjacencyMatrix());
//        Iterator<String> it = sequencesToCheck.iterator();
//        while(it.hasNext()){
//            String current= it.next();
//            System.out.println(current);
//            SequenceTree currentTree = f.getTreeTypedSequenceFromNumberSequence(current, f); 
//            System.out.println("Structure computed successfully");
//        }
//        System.out.println("Execution successful");
//        
//        queda: hacer que compruebe la secuencia, quitar esto aparte, poner bien los tests y 
//                escribir el rdf.
        
//        System.out.println(f.testSequenceInBiggerGraph(test, bigger));        
        //this test should be true
        //testing sequence with an array on it
        //testing sequence with various dependencies on it
    }
    
}
