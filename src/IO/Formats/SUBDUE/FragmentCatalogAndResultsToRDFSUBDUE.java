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
package IO.Formats.SUBDUE;

import DataStructures.Graph;
import DataStructures.GraphCollection;
import DataStructures.GraphNode.GraphNode;
import DataStructures.Fragment;
import IO.FragmentCatalogAndResultsToRDF;
import IO.Formats.OPMW.Graph2OPMWRDFModel;
import PostProcessing.Formats.SUBDUE.FragmentToSPARQLQueryTemplateSUBDUE;
import Static.GeneralConstants;
import Static.GeneralMethods;
import Static.WffdConstants;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class to transform the SUBDUE Fragment Catalog (result from the analysis) to 
 * RDF.
 * The model used for this is the Workflow Fragment Description Ontology.
 * Every time a new catalog is passed a new set of URIs is created.
 * For more information see http://purl.org/net/wf-fd
 * @author Daniel Garijo
 */
public class FragmentCatalogAndResultsToRDFSUBDUE extends FragmentCatalogAndResultsToRDF{    

    /**
     * Default constructor
     * @param outPath the path to save the result RDF file.
     */
    public FragmentCatalogAndResultsToRDFSUBDUE(String outPath) {
        super(outPath);
    }
    
    /**
     * Function that accepts a catalog of fragments and prints them in RDF. We assume 
     * that the catalog to be published has been simplified (filtered)
     * @param catalog a hashmap with the name of the fragment and the 
     * FinalResult implementing it.
     */
    @Override
    public void transformFragmentCollectionToRDF(ArrayList<Fragment> catalog){
        Iterator<Fragment> catalogIt = catalog.iterator();
        //we assume that the number of occurrences is initialzied
        while (catalogIt.hasNext()){
            Fragment currentFragment = catalogIt.next();
            //fragmentId -> URI
            String fragmentID= currentFragment.getStructureID()+"_"+dateToken;                
            GeneralMethods.addIndividual(repository, fragmentID, WffdConstants.DETECTED_RESULT, "Detected Result Workflow fragment "+fragmentID);
            //add date and title
            GeneralMethods.addDataProperty(repository, fragmentID,currentFragment.getStructureID(),WffdConstants.TITLE,XSDDatatype.XSDstring);
            GeneralMethods.addDataProperty(repository, fragmentID,new Date().toString(),WffdConstants.CREATED,XSDDatatype.XSDdate);
//                System.out.println(fragmentID);
            //for each of these, add the "overlapsWith" relationship
            ArrayList<Fragment> includedIds = currentFragment.getListOfIncludedIDs();
            if(includedIds!=null){
                Iterator<Fragment> includedIdsIt = includedIds.iterator();                
                while(includedIdsIt.hasNext()){
                    Fragment currentIncludedId = includedIdsIt.next();
                    String currentID= currentIncludedId.getStructureID()+"_"+dateToken;
    //                    addIndividual(repository, currentID, WffdConstants.DETECTED_RESULT, "Detected Result Workflow fragment "+fragmentID);
                    GeneralMethods.addIndividual(repository, currentID, WffdConstants.STEP, null);
                    GeneralMethods.addIndividual(repository, currentID, WffdConstants.PLAN, null);//for redundancy and interoperability
                    GeneralMethods.addProperty(repository, fragmentID, currentID, WffdConstants.OVERLAPS_WITH);                    
                    GeneralMethods.addProperty(repository, currentID, fragmentID, WffdConstants.OVERLAPS_WITH);
                    GeneralMethods.addProperty(repository, currentID, fragmentID, WffdConstants.IS_STEP_OF_PLAN);
                }
            }
            //each of these are p-plan:Steps, and are "stepsOfPlan" of the fragment
            ArrayList<String> urisOfFragment = currentFragment.getDependencyGraph().getURIs();
            HashMap<String,GraphNode> currentFragmentNodes = currentFragment.getDependencyGraph().getNodes();
            //for each dependency, create the appropriate URI (SUB_X_1, SUB_X_2, etc)
            //if the URI is a fragment, we don't create a new URI, since it has already been created in the previous stepp
            Iterator<String> urisOfFragmentIt = urisOfFragment.iterator();
            while (urisOfFragmentIt.hasNext()){
                String currentURI = urisOfFragmentIt.next();
                String currentURItype = currentFragmentNodes.get(currentURI).getType();
                GeneralMethods.addIndividual(repository, fragmentID+"_NODE"+currentURI, WffdConstants.STEP, "Step "+fragmentID);
                GeneralMethods.addProperty(repository, fragmentID+"_NODE"+currentURI, fragmentID, WffdConstants.IS_STEP_OF_PLAN);
                if(currentURItype.contains("SUB_")){
                    //add owl:sameAs
                    //WHY ore:proxyFor? because if we add the precedence step in the fragments directly,
                    //we may create inconsistencies (e.g. a fragment preceeded by 2 different steps
                    //that are from different fragments. In order to avoid that, we create a phantom node
                    //and we add the ore:proxyFor with the template (as basically the proxy acts as the former)
                    GeneralMethods.addProperty(repository, fragmentID+"_NODE"+currentURI, currentURItype+"_"+dateToken, "http://www.openarchives.org/ore/terms/proxyFor");
                }else{
                    GeneralMethods.addIndividual(repository, fragmentID+"_NODE"+currentURI,currentURItype , null);
                }
            }
            //for each dependency, create the p-plan:ispreceededBy
            String[][] currentFragmentAdjMatrix = currentFragment.getDependencyGraph().getAdjacencyMatrix();
            for(int i = 1;i<currentFragmentAdjMatrix.length;i++){
                for(int j=1 ; j<currentFragmentAdjMatrix.length;j++){
                    if(currentFragmentAdjMatrix[i][j]!=null && currentFragmentAdjMatrix[i][j].equals(GeneralConstants.INFORM_DEPENDENCY)){
                        String uriI = urisOfFragment.get(i-1);
                        String typeI = currentFragmentNodes.get(uriI).getType();
//                            if(typeI.contains("SUB_")){
//                                uriI = typeI+"_"+dateToken;
//                            }else{
//                                uriI = fragmentID+"_NODE"+uriI;
//                            }
                        uriI = fragmentID+"_NODE"+uriI;
                        String uriJ = urisOfFragment.get(j-1);
                        String typeJ = currentFragmentNodes.get(uriJ).getType();
//                            if(typeJ.contains("SUB_")){
//                                uriJ = typeJ+"_"+dateToken;
//                            }else{
//                                uriJ = fragmentID+"_NODE"+uriJ;
//                            }
                        uriJ = fragmentID+"_NODE"+uriJ;
                        GeneralMethods.addProperty(repository, uriI, uriJ, WffdConstants.IS_PRECEEDED_BY);
                    }
                }
            }
        }
                
    }
    
    /**
     * Function that receives a set of validated bindings (i.e., how a fragment or a set of fragments have been 
     * found on a template) and publishes it in RDF according to the wffd model.
     * The bindings are supposed to be validated BEFORE calling this function. No further validations are made
     * within it.
     * 
     * Important: The fragment catalogue and the binding of the results have to be made at the same time.
     * Otherwise their URIs may not be the same.
     * 
     * @param validatedBindings the validated bindings to convert to RDF
     * @param fragmentID the URI of the fragment which has the bindings.
     * @param templateID  the template where the bindings where found.
     */
    /*private void transformValidatedBindingResultsOfOneTemplateToRDF(ArrayList<HashMap<String,String>> validatedBindings, String fragmentID, String templateID){
        String fragID= fragmentID+"_"+dateToken;                
        GeneralMethods.addIndividual(repository, fragID, WffdConstants.DETECTED_RESULT, "Detected Result Workflow fragment "+fragID);        
        //the fragmentID is declared as a fragment and associated to the 
        //TiedWorkflowFragments with the foundAs relationship.        
        //each set of validated is a new TiedWorkflowFragment
        Iterator<HashMap<String,String>> validatedBindingsIt = validatedBindings.iterator();
        int n =0;
        while(validatedBindingsIt.hasNext()){
            HashMap<String,String> currentBindings = validatedBindingsIt.next();
            String currentBindingURI = "binding"+new Date().getTime()+n;
            GeneralMethods.addIndividual(repository, currentBindingURI, WffdConstants.TIED_RESULT, null);
            GeneralMethods.addProperty(repository, fragID, currentBindingURI, WffdConstants.FOUND_AS);
            //System.out.println(currentBindingURI);
            //for all currentBindings, add their URIs as part of the tied result
            Iterator<String> iteratorCurrentBinding = currentBindings.keySet().iterator();
            while (iteratorCurrentBinding.hasNext()){
                String keyfromhashmap = iteratorCurrentBinding.next();
                if(keyfromhashmap.contains("http://")){
                    //if the key is a URI then we add it. Otherwise it's just the binding to the fragment, 
                    //which is not relevant here.
                    GeneralMethods.addIndividual(repository, keyfromhashmap, WffdConstants.STEP, null);
                    GeneralMethods.addProperty(repository, keyfromhashmap, currentBindingURI, WffdConstants.IS_STEP_OF_PLAN);                    
                }
            }
            n++;
        }
        //the fragment is bound  to the templateID with the dcterms:isPartOf relationship
        GeneralMethods.addProperty(repository, fragID, templateID, WffdConstants.IS_PART_OF);
    }*/
    
    /**
     * Given a set of fragments and a graph collection of graphs, this method 
     * checks whether any of the fragments were found in any of the graphs in 
     * the collection
     * @param obtainedResults
     * @param templates 
     */
    @Override
    public void transformBindingResultsInTemplateCollection(ArrayList<Fragment> obtainedResults, GraphCollection templates){
        ArrayList<Graph> temps = templates.getGraphs();
        Iterator<Graph> itTemps = temps.iterator();
        while(itTemps.hasNext()){
            Graph currentTemplate = itTemps.next();
            currentTemplate.putReducedNodesInAdjacencyMatrix();
            transformBindingResultsOfFragmentCollectionInTemplateToRDF(obtainedResults, currentTemplate);
        }
        
    }
    /**
     * Method which given a set of fragments and a template, checks whether 
     * any of the fragments can be found in the template or not. The results 
     * are exported to RDF.
     * @param obtainedResults results obtained by a SUBDUE algorithm.
     * @param template template in which we want to search the results.
     */
    @Override
    public void transformBindingResultsOfFragmentCollectionInTemplateToRDF(ArrayList<Fragment> obtainedResults, Graph template){
        Iterator<Fragment> fragments = obtainedResults.iterator();
        //it would be nice to just send the relevant fragments            
        while(fragments.hasNext()){
            Fragment f = fragments.next();
            transformBindingResultsOfOneFragmentAndOneTemplateToRDF(f,template);
        
        }
    }
    
    /**
     * Given a fragment and a template, this method detects where in the 
     * template has the fragment been found and trasforms the results to RDF.
     * @param currentFragment the fragment which we want to test
     * @param template the template on which we want to find the fragment.
     */
    @Override
    public void transformBindingResultsOfOneFragmentAndOneTemplateToRDF(Fragment currentFragment, Graph template){        
        if(currentFragment.isMultiStepStructure()){
            FragmentToSPARQLQueryTemplateSUBDUE qr = new FragmentToSPARQLQueryTemplateSUBDUE();
            OntModel o2 = Graph2OPMWRDFModel.graph2OPMWTemplate(template);
            String currentQuery = qr.createQueryFromFragment(currentFragment, template.getName());
            ResultSet rs = GeneralMethods.queryLocalRepository(o2, currentQuery);
            if(rs.hasNext()){
                String fragID= currentFragment.getStructureID()+"_"+dateToken;                
                GeneralMethods.addIndividual(repository, fragID, WffdConstants.DETECTED_RESULT, "Detected Result Workflow fragment "+fragID);
                int n = 0;
                GeneralMethods.addProperty(repository, fragID, template.getName(), WffdConstants.IS_PART_OF);
                while(rs.hasNext()){
                    QuerySolution qs = rs.nextSolution();
                    String currentBindingURI = "binding"+new Date().getTime()+n;
                    GeneralMethods.addIndividual(repository, currentBindingURI, WffdConstants.TIED_RESULT, null);
                    GeneralMethods.addProperty(repository, fragID, currentBindingURI, WffdConstants.FOUND_AS);
                    //System.out.println(currentBindingURI);
                    //for all currentBindings, add their URIs as part of the tied result
                    Iterator<String> iteratorCurrentBinding = qs.varNames();
                    while (iteratorCurrentBinding.hasNext()){
                        String currentvar = iteratorCurrentBinding.next();
                        Resource currentResourceURI = qs.getResource(currentvar);
                        GeneralMethods.addIndividual(repository, currentResourceURI.getURI(), WffdConstants.STEP, null);
                        GeneralMethods.addProperty(repository, currentResourceURI.getURI(), currentBindingURI, WffdConstants.IS_STEP_OF_PLAN);
                        GeneralMethods.addIndividual(repository, currentResourceURI.getURI(), o2.getOntResource(currentResourceURI).getRDFType().getURI(), null);
                    }
                    n++;
                } 
            }
         }
    }
    
    
    
    //this needs to be removed and adapted into scripts
//    public static void main(String[] args){
//        try{
//            OPMWTemplate2Graph test = new OPMWTemplate2Graph("http://wind.isi.edu:8890/sparql");
//    //        test.transformToSubdueGraph("http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_SINGLE_");
//
//           //this is a test just for the workflows annotated by Idafen. TO DO FOR ALL wf in the domain!
//           test.transformToGraph("http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI");
//           test.transformToGraph("http://www.opmw.org/export/resource/WorkflowTemplate/FEATUREGENERATION");
//           test.transformToGraph("http://www.opmw.org/export/resource/WorkflowTemplate/FEATURESELECTION");
//           test.transformToGraph("http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLUSTERING");
//           test.transformToGraph("http://www.opmw.org/export/resource/WorkflowTemplate/MODEL");
//
//           String file = "SUBDUE_TOOL\\results\\Tests\\testResultReduced2";
//           String ocFile = "SUBDUE_TOOL\\results\\Tests\\testResultReduced2_occurrences";
//           HashMap<String,Fragment> obtainedResults = new FragmentReaderSUBDUE().processResultsAndOccurrencesFiles(file, ocFile);
//
//           //without inference
//           FragmentCatalogAndResultsToRDFSUBDUE catalogNoInference = new FragmentCatalogAndResultsToRDFSUBDUE("out.ttl");
//
//           catalogNoInference.transformFragmentCollectionToRDF(obtainedResults);
//           catalogNoInference.transformBindingResultsInTemplateCollection(obtainedResults, test.getGraphCollection());
//           //test1.transformBindingResultsOfFragmentCollectionInTemplateToRDF(obtainedResults, testGraph);
//
//           catalogNoInference.exportToRDFFile("TURTLE");
//
//           //with inference
//           //first we get the replacement hashmap
//           String taxonomyFilePath = "src\\TestFiles\\multiDomainOnto.owl"; //we assume the file has already been created.
//           OntModel o = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
//           InputStream in = FileManager.get().open(taxonomyFilePath);
//           o.read(in, null);        
//           HashMap replacements = CreateHashMapForInference.createReplacementHashMap(o);
//
//           //we create the abstract collection
//           GraphCollection abstractCollection = CreateAbstractResource.createAbstractCollection(test.getGraphCollection(), replacements);
//
//           //we load the new files
//           file = "resultsAbstractCatalog24-10-2013";
//           ocFile = "resultsAbstractCatalog24-10-2013_occurrences";
//
//           obtainedResults = new FragmentReaderSUBDUE().processResultsAndOccurrencesFiles(file, ocFile);
//
//           //without inference
//           FragmentCatalogAndResultsToRDFSUBDUE abstractCatalog = new FragmentCatalogAndResultsToRDFSUBDUE("outAbstract28-01-2014.ttl");
//
//           abstractCatalog.transformFragmentCollectionToRDF(obtainedResults);
//           abstractCatalog.transformBindingResultsInTemplateCollection(obtainedResults, abstractCollection);
//           //test1.transformBindingResultsOfFragmentCollectionInTemplateToRDF(obtainedResults, testGraph);
//
//           abstractCatalog.exportToRDFFile("TURTLE");
//        }catch(Exception e){
//            System.err.println("Error while saving the catalog to RDF "+e.getMessage());
//        }
       
//    }
}
