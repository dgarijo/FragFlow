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
package IO.Formats.PARSEMIS;

import DataStructures.Fragment;
import DataStructures.Graph;
import DataStructures.GraphCollection;
import DataStructures.GraphNode.GraphNode;
import IO.FragmentCatalogAndResultsToRDF;
import PostProcessing.Formats.PAFI.FragmentToSPARQLQueryTemplatePAFI;
import Static.GeneralMethodsFragments;
import Static.GeneralConstants;
import Static.GeneralMethods;
import Static.Vocabularies.DCTerms;
import Static.Vocabularies.PPlan;
import Static.Vocabularies.Wffd;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class for publishing the a filtered catalog in RDF following the wffd model.
 * @author Daniel Garijo
 */
public class FragmentCatalogAndResultsToRDFPARSEMIS extends FragmentCatalogAndResultsToRDF{

    public FragmentCatalogAndResultsToRDFPARSEMIS(String outPath) {
        super(outPath);
    }
    

    @Override
    public void transformFragmentCollectionToRDF(ArrayList<Fragment> filteredCatalog) {
        Iterator<Fragment> catalogIt = filteredCatalog.iterator();        
        //in PAFI no fragment includes directly another.
        while (catalogIt.hasNext()){
            Fragment currentFragment = catalogIt.next();
            //fragmentId -> URI
            String fragmentID= currentFragment.getStructureID()+"_"+dateToken;                
            GeneralMethods.addIndividual(repository, fragmentID, Wffd.DETECTED_RESULT, "Detected Result Workflow fragment "+fragmentID);
            //add date and title
            GeneralMethods.addDataProperty(repository, fragmentID,currentFragment.getStructureID(),DCTerms.TITLE,XSDDatatype.XSDstring);
            GeneralMethods.addDataProperty(repository, fragmentID,new Date().toString(),DCTerms.CREATED,XSDDatatype.XSDdate);
            //add which algorithm found the result (in this case, PAFI).
            GeneralMethods.addDataProperty(repository, fragmentID, "PAFI", Wffd.DETECTED_BY_ALGORITHM,XSDDatatype.XSDstring);
            ArrayList<Fragment> includedIds = GeneralMethodsFragments.getFullDependenciesOfFragment(currentFragment);
            if(includedIds!=null){
                Iterator<Fragment> includedIdsIt = includedIds.iterator();                
                while(includedIdsIt.hasNext()){
                    Fragment currentIncludedId = includedIdsIt.next();
                    //we just include the fragment if it also belongs to the catalog
                    if(filteredCatalog.contains(currentIncludedId)){
                        String currentID = currentIncludedId.getStructureID()+"_"+dateToken;               
                        GeneralMethods.addIndividual(repository, currentID, PPlan.PLAN, null);//for redundancy and interoperability
                        GeneralMethods.addProperty(repository, currentID, fragmentID, Wffd.PART_OF_WORKFLOW_FRAGMENT);
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
                GeneralMethods.addIndividual(repository, fragmentID+"_NODE"+currentURI, PPlan.STEP, "Step "+fragmentID);
                GeneralMethods.addProperty(repository, fragmentID+"_NODE"+currentURI, fragmentID, PPlan.IS_STEP_OF_PLAN);
                GeneralMethods.addIndividual(repository, fragmentID+"_NODE"+currentURI,currentURItype , null);                
            }
            //PARSEMIS STARTS FROM 0 DUE TO THE NODE NUMBERING
            for(int i = 0;i<currentFragmentAdjMatrix.length;i++){
                for(int j=0 ; j<currentFragmentAdjMatrix.length;j++){
                    if(currentFragmentAdjMatrix[i][j]!=null && 
                            (currentFragmentAdjMatrix[i][j].equals(GeneralConstants.INFORM_DEPENDENCY))){
                        String uriI = urisOfFragment.get(i);
                        uriI = fragmentID+"_NODE"+uriI;
                        String uriJ = urisOfFragment.get(j);
                        uriJ = fragmentID+"_NODE"+uriJ;
                        GeneralMethods.addProperty(repository, uriI, uriJ, PPlan.IS_PRECEEDED_BY);
                    }
                }
            }
        }
    }

    @Override
    public void transformBindingResultsInTemplateCollection(ArrayList<Fragment> obtainedResults, GraphCollection templates) {
        ArrayList<Graph> temps = templates.getGraphs();
        Iterator<Graph> itTemps = temps.iterator();
        while(itTemps.hasNext()){
            Graph currentTemplate = itTemps.next();
            currentTemplate.putReducedNodesInAdjacencyMatrix();
            transformBindingResultsOfFragmentCollectionInTemplateToRDF(obtainedResults, currentTemplate);
        }
    }

    @Override
    public void transformBindingResultsOfFragmentCollectionInTemplateToRDF(ArrayList<Fragment> obtainedResults, Graph template) {
        Iterator<Fragment> fragments = obtainedResults.iterator();         
        while(fragments.hasNext()){
            Fragment f = fragments.next();
            transformBindingResultsOfOneFragmentAndOneTemplateToRDF(f,template, new FragmentToSPARQLQueryTemplatePAFI());
        }
    }
    
}
