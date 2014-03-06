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
import DataStructures.GraphCollection;
import DataStructures.GraphNode.GraphNode;
import IO.Exception.FragmentReaderException;
import IO.FragmentCatalogAndResultsToRDF;
import PostProcessing.Formats.PAFI.CreateStatisticsFromResultsPAFI;
import Static.FragmentGeneralMethods;
import Static.GeneralConstants;
import Static.GeneralMethods;
import Static.WffdConstants;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class to transform a PAFI catalog and instances to RDF.
 * @author Daniel Garijo
 */
public class FragmentCatalogAndResultsToRDFPAFI extends FragmentCatalogAndResultsToRDF{

    /**
     * Default constructor.
     * @param outPath path to save the resulant
     */
    public FragmentCatalogAndResultsToRDFPAFI(String outPath) {
        super(outPath);
    }
    

    /**
     * Method to transform the detected results as WF-FD.
     * We assume that the catalog has been filtered and only the filtered multi 
     * step fragments are the ones to be exported.
     * @param filteredCatalog 
     */
    @Override
    public void transformFragmentCollectionToRDF(ArrayList<Fragment> filteredCatalog) {
        Iterator<Fragment> catalogIt = filteredCatalog.iterator();        
        //in PAFI no fragment includes directly another.
        //in PAFI the connections are not directed.
        while (catalogIt.hasNext()){
            Fragment currentFragment = catalogIt.next();
            //fragmentId -> URI
            String fragmentID= currentFragment.getStructureID()+"_"+dateToken;                
            GeneralMethods.addIndividual(repository, fragmentID, WffdConstants.DETECTED_RESULT, "Detected Result Workflow fragment "+fragmentID);
            //add date and title
            GeneralMethods.addDataProperty(repository, fragmentID,currentFragment.getStructureID(),WffdConstants.TITLE,XSDDatatype.XSDstring);
            GeneralMethods.addDataProperty(repository, fragmentID,new Date().toString(),WffdConstants.CREATED,XSDDatatype.XSDdate);
            ArrayList<Fragment> includedIds = FragmentGeneralMethods.getFullDependenciesOfFragment(currentFragment);
            if(includedIds!=null){
                Iterator<Fragment> includedIdsIt = includedIds.iterator();                
                while(includedIdsIt.hasNext()){
                    Fragment currentIncludedId = includedIdsIt.next();
                    //we just include the fragment if it also belongs to the catalog
                    if(filteredCatalog.contains(currentIncludedId)){
                        String currentID = currentIncludedId.getStructureID()+"_"+dateToken;               
                        GeneralMethods.addIndividual(repository, currentID, WffdConstants.PLAN, null);//for redundancy and interoperability
                        GeneralMethods.addProperty(repository, currentID, fragmentID, WffdConstants.PART_OF_FRAGMENT);
                    }
                }
            }
            ArrayList<String> urisOfFragment = currentFragment.getDependencyGraph().getURIs();
            HashMap<String,GraphNode> currentFragmentNodes = currentFragment.getDependencyGraph().getNodes();
            String[][] currentFragmentAdjMatrix = currentFragment.getDependencyGraph().getAdjacencyMatrix();
            //for each dependency, create the appropriate URI 
            Iterator<String> urisOfFragmentIt = urisOfFragment.iterator();
            while (urisOfFragmentIt.hasNext()){
                String currentURI = urisOfFragmentIt.next();
                String currentURItype = currentFragmentNodes.get(currentURI).getType();
                GeneralMethods.addIndividual(repository, fragmentID+"_NODE"+currentURI, WffdConstants.STEP, "Step "+fragmentID);
                GeneralMethods.addProperty(repository, fragmentID+"_NODE"+currentURI, fragmentID, WffdConstants.IS_STEP_OF_PLAN);
                GeneralMethods.addIndividual(repository, fragmentID+"_NODE"+currentURI,currentURItype , null);                
            }
            //for each dependency, create the wf-fd:isConnectedTo (graph is not directed)
            //PAFI STARTS FROM 0 DUE TO THE NODE NUMBERING
            for(int i = 0;i<currentFragmentAdjMatrix.length;i++){
                for(int j=i ; j<currentFragmentAdjMatrix.length;j++){
                    if(currentFragmentAdjMatrix[i][j]!=null && 
                            (currentFragmentAdjMatrix[i][j].equals(GeneralConstants.INFORM_DEPENDENCY))){
                        String uriI = urisOfFragment.get(i);
                        uriI = fragmentID+"_NODE"+uriI;
                        String uriJ = urisOfFragment.get(j);
                        uriJ = fragmentID+"_NODE"+uriJ;
                        GeneralMethods.addProperty(repository, uriI, uriJ, WffdConstants.CONNECTED_TO);
                        GeneralMethods.addProperty(repository, uriJ, uriI, WffdConstants.CONNECTED_TO);
                    }
                }
            }
        }
    }

    @Override
    public void transformBindingResultsInTemplateCollection(ArrayList<Fragment> obtainedResults, GraphCollection templates) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void transformBindingResultsOfFragmentCollectionInTemplateToRDF(ArrayList<Fragment> obtainedResults, Graph template) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void transformBindingResultsOfOneFragmentAndOneTemplateToRDF(Fragment currentFragment, Graph template) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    public static void main (String[] args) throws FragmentReaderException{
        String fpfile = "PAFI_TOOL\\results\\CollectionInPAFIFormat.fp";
        String pcFile = "PAFI_TOOL\\results\\CollectionInPAFIFormat.pc";
        String tidFile = "PAFI_TOOL\\results\\CollectionInPAFIFormat.tid";
        CreateStatisticsFromResultsPAFI c = new CreateStatisticsFromResultsPAFI("Text analytics", true, false, fpfile, pcFile, tidFile); 
        FragmentCatalogAndResultsToRDFPAFI aux = new FragmentCatalogAndResultsToRDFPAFI("testPafiRDF.ttl");
        aux.transformFragmentCollectionToRDF(c.getFilteredMultiStepFragments());
        aux.exportToRDFFile("TURTLE");
    }
}
