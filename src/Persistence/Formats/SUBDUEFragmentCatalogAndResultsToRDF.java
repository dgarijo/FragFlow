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
package Persistence.Formats;

import Factory.Inference.CreateAbstractResource;
import Factory.Inference.CreateHashMapForInference;
import Factory.OPMWTemplate2GraphProcessor;
import Graph.Graph;
import Graph.GraphCollection;
import GraphNode.GraphNode;
import PostProcessing.FinalResult;
import PostProcessing.Formats.SubdueGraphReader;
import PostProcessing.SUBDUEFragmentRecongnizer;
import Static.GeneralConstants;
import Static.WffdConstants;
import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.FileManager;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class to transform the SUBDUE Fragment Catalog to RDF.
 * The model used for this is the Workflow Fragment Description Ontology.
 * Every time a new catalog is passed a new set of URIs is created.
 * For more information see http://purl.org/net/wf-fd
 * @author Daniel Garijo
 */
public class SUBDUEFragmentCatalogAndResultsToRDF {
    private String outPath;
    private OntModel repository;
    private String dateToken;

    public SUBDUEFragmentCatalogAndResultsToRDF(String outPath) {
        this.outPath = outPath;        
        repository = ModelFactory.createOntologyModel();
        //initialization of the date, which will determine the URIs
        dateToken=""+new Date().getTime();
    }
    
    /**
     * Function that accepts a catalog of fragments and prints them in RDF
     * @param catalog a hashmap with the name of the fragment and the FinalResult implementing it.
     */
    public void transformFragmentCollectionToRDF(HashMap<String,FinalResult> catalog){
        Iterator<String> CatalogIt = catalog.keySet().iterator();
        //we assume that the number of occurrences is initialzied
        while (CatalogIt.hasNext()){
            String currentKey = CatalogIt.next();
            FinalResult currentFragment = catalog.get(currentKey);
            //we just annotate multistep structures
            if(currentFragment.isMultiStepStructure()){
                //fragmentId -> URI
                String fragmentID= currentFragment.getStructureID()+"_"+dateToken;                
                addIndividual(repository, fragmentID, WffdConstants.DETECTED_RESULT, "Detected Result Workflow fragment "+fragmentID);
                //add date and title
                addDataProperty(repository, fragmentID,currentFragment.getStructureID(),WffdConstants.TITLE,XSDDatatype.XSDstring);
                addDataProperty(repository, fragmentID,new Date().toString(),WffdConstants.CREATED,XSDDatatype.XSDdate);
//                System.out.println(fragmentID);
                //for each of these, add the "overlapsWith" relationship
                ArrayList<FinalResult> includedIds = currentFragment.getListOfIncludedIDs();
                Iterator<FinalResult> includedIdsIt = includedIds.iterator();                
                while(includedIdsIt.hasNext()){
                    FinalResult currentIncludedId = includedIdsIt.next();
                    String currentID= currentIncludedId.getStructureID()+"_"+dateToken;
//                    addIndividual(repository, currentID, WffdConstants.DETECTED_RESULT, "Detected Result Workflow fragment "+fragmentID);
                    addIndividual(repository, currentID, WffdConstants.STEP, null);
                    addIndividual(repository, currentID, WffdConstants.PLAN, null);//for redundancy and interoperability
                    addProperty(repository, fragmentID, currentID, WffdConstants.OVERLAPS_WITH);                    
                    addProperty(repository, currentID, fragmentID, WffdConstants.OVERLAPS_WITH);
                    addProperty(repository, currentID, fragmentID, WffdConstants.IS_STEP_OF_PLAN);
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
                    if(!currentURItype.contains("SUB_")){
                        addIndividual(repository, fragmentID+"_NODE"+currentURI, WffdConstants.STEP, "Step "+fragmentID);
                        addIndividual(repository, fragmentID+"_NODE"+currentURI,currentURItype , null);
                        addProperty(repository, fragmentID+"_NODE"+currentURI, fragmentID, WffdConstants.IS_STEP_OF_PLAN);
//                        System.out.println(fragmentID+"_NODE"+currentURI);
                    }
                }
                //for each dependency, create the p-plan:ispreceededBy
                String[][] currentFragmentAdjMatrix = currentFragment.getDependencyGraph().getAdjacencyMatrix();
                for(int i = 1;i<currentFragmentAdjMatrix.length;i++){
                    for(int j=1 ; j<currentFragmentAdjMatrix.length;j++){
                        if(currentFragmentAdjMatrix[i][j]!=null && currentFragmentAdjMatrix[i][j].equals(GeneralConstants.INFORM_DEPENDENCY)){
                            String uriI = urisOfFragment.get(i-1);
                            String typeI = currentFragmentNodes.get(uriI).getType();
                            if(typeI.contains("SUB_")){
                                uriI = typeI+"_"+dateToken;
                            }else{
                                uriI = fragmentID+"_NODE"+uriI;
                            }
                            
                            String uriJ = urisOfFragment.get(j-1);
                            String typeJ = currentFragmentNodes.get(uriJ).getType();
                            if(typeJ.contains("SUB_I")){
                                uriJ = typeJ+"_"+dateToken;
                            }else{
                                uriJ = fragmentID+"_NODE"+uriJ;
                            }
                            addProperty(repository, uriI, uriJ, WffdConstants.IS_PRECEEDED_BY);
                        }
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
    private void transformValidatedBindingResultsOfOneTemplateToRDF(ArrayList<HashMap<String,String>> validatedBindings, String fragmentID, String templateID){
        String fragID= fragmentID+"_"+dateToken;                
        addIndividual(repository, fragID, WffdConstants.DETECTED_RESULT, "Detected Result Workflow fragment "+fragID);        
        //the fragmentID is declared as a fragment and associated to the 
        //TiedWorkflowFragments with the foundAs relationship.        
        //each set of validated is a new TiedWorkflowFragment
        Iterator<HashMap<String,String>> validatedBindingsIt = validatedBindings.iterator();
        int n =0;
        while(validatedBindingsIt.hasNext()){
            HashMap<String,String> currentBindings = validatedBindingsIt.next();
            String currentBindingURI = "binding"+new Date().getTime()+n;
            addIndividual(repository, currentBindingURI, WffdConstants.TIED_RESULT, null);
            addProperty(repository, fragID, currentBindingURI, WffdConstants.FOUND_AS);
            //System.out.println(currentBindingURI);
            //for all currentBindings, add their URIs as part of the tied result
            Iterator<String> iteratorCurrentBinding = currentBindings.keySet().iterator();
            while (iteratorCurrentBinding.hasNext()){
                String keyfromhashmap = iteratorCurrentBinding.next();
                if(keyfromhashmap.contains("http://")){
                    //if the key is a URI then we add it. Otherwise it's just the binding to the fragment, 
                    //which is not relevant here.
                    addIndividual(repository, keyfromhashmap, WffdConstants.STEP, null);
                    addProperty(repository, keyfromhashmap, currentBindingURI, WffdConstants.IS_STEP_OF_PLAN);
                    
                }
            }
            n++;
        }
        //the fragment is bound  to the templateID with the dcterms:isPartOf relationship
        addProperty(repository, fragID, templateID, WffdConstants.IS_PART_OF);
    }
       
    public void transformBindingResultsInTemplateCollection(HashMap<String,FinalResult> obtainedResults, GraphCollection templates){
        ArrayList<Graph> temps = templates.getGraphCollection();
        Iterator<Graph> itTemps = temps.iterator();
        while(itTemps.hasNext()){
            Graph currentTemplate = itTemps.next();
            currentTemplate.putReducedNodesInAdjacencyMatrix();
            transformBindingResultsOfFragmentCollectionInTemplateToRDF(obtainedResults, currentTemplate);
        }
        
    }
    
    public void transformBindingResultsOfFragmentCollectionInTemplateToRDF(HashMap<String,FinalResult> obtainedResults, Graph template){
        Iterator<String> fragments = obtainedResults.keySet().iterator();
        //it would be nice to just send the relevant fragments            
        while(fragments.hasNext()){
            FinalResult f = obtainedResults.get(fragments.next());
            transformBindingResultsOfOneFragmentAndOneTemplateToRDF(f,template);
        
        }
    }
    
    public void transformBindingResultsOfOneFragmentAndOneTemplateToRDF(FinalResult currentFragment, Graph template){        
        if(currentFragment.isMultiStepStructure()){
            SUBDUEFragmentRecongnizer fr = new SUBDUEFragmentRecongnizer();
            ArrayList<HashMap<String,String>> b = fr.generateBindingsFromFragmentInGraph(currentFragment, template);
            b = fr.validateBindings(b, template, currentFragment);
            if(!b.isEmpty()){
                transformValidatedBindingResultsOfOneTemplateToRDF(b, currentFragment.getStructureID(),template.getName());
            }
         }
    }
    
    /**
     * Function to export the stored model as an RDF file, using the format we specify.     
     * @param format  format of the output file: RDF/XML, TURTLE, N-TRIPLE or N3.
     */
    public void exportToRDFFile(String format){
        OutputStream out;
        try {
            out = new FileOutputStream(this.outPath);
            repository.write(out,format);
        } catch (FileNotFoundException ex) {
            System.out.println("Error while writing the model to file "+ex.getMessage());
        }
    }
    
    /**
     * Funtion to insert an individual as an instance of a class. If the class does not exist, it is created.
     * @param individualId Instance id. If exists it won't be created.
     * @param classURL URL of the class from which we want to create the instance
     */
    private void addIndividual(OntModel m,String individualId, String classURL, String label){
        String nombreIndividuoEnc = encode(individualId);
        OntClass c = m.createClass(classURL);
        if(individualId.contains("http://")){//is a URI already
            c.createIndividual(individualId);
        }else{
            c.createIndividual(GeneralConstants.PREFIX_FOR_RDF_GENERATION+nombreIndividuoEnc);
        }
        if(label!=null){
            this.addDataProperty(m,nombreIndividuoEnc,label,WffdConstants.RDFS_SCHEMA_PREFIX+"label");
        }
    }

    /**
     * Funtion to add a property between two individuals. If the property does not exist, it is created.
     * @param orig Domain of the property (Id, not complete URI)
     * @param dest Range of the property (Id, not complete URI)
     * @param property URI of the property
     */
    private void addProperty(OntModel m, String orig, String dest, String property){
        OntProperty propSelec = m.createOntProperty(property);
        Resource source;
        if(!orig.contains("http://")){
            source = m.getResource(GeneralConstants.PREFIX_FOR_RDF_GENERATION+ encode(orig) );
        }else{
            source = m.getResource(orig);
        }
        Individual instance = (Individual) source.as( Individual.class );
        if(dest.contains("http://")){//it is a URI
            instance.addProperty(propSelec,m.getResource(dest));            
        }else{//it is a local resource
            instance.addProperty(propSelec, m.getResource(GeneralConstants.PREFIX_FOR_RDF_GENERATION+encode(dest)));
        }
//        System.out.println("Creada propiedad "+ property+" que relaciona los individuos "+ orig + " y "+ dest);
    }

    /**
     * Function to add dataProperties. Similar to addProperty
     * @param origen Domain of the property (Id, not complete URI)
     * @param literal literal to be asserted
     * @param dataProperty URI of the data property to assign.
     */
    private void addDataProperty(OntModel m, String origen, String literal, String dataProperty){
        OntProperty propSelec;
        //lat y long son de otra ontologia, tienen otro prefijo distinto
        propSelec = m.createDatatypeProperty(dataProperty);
        //propSelec = (modeloOntologia.getResource(dataProperty)).as(OntProperty.class);
        Resource orig = m.getResource(GeneralConstants.PREFIX_FOR_RDF_GENERATION+ encode(origen) );
        m.add(orig, propSelec, literal); 
    }

    private void addDataProperty(OntModel m, String origen, String dato, String dataProperty,RDFDatatype tipo) {
        OntProperty propSelec;
        //lat y long son de otra ontologia, tienen otro prefijo distinto
        propSelec = m.createDatatypeProperty(dataProperty);
        Resource orig = m.getResource(GeneralConstants.PREFIX_FOR_RDF_GENERATION+ encode(origen));
        m.add(orig, propSelec, dato,tipo);
    }
    
    /**
     * Encoding of the name to avoid any trouble with spacial characters and spaces
     * @param name
     */
    private String encode(String name){
        name = name.replace("http://","");
        String prenom = name.substring(0, name.indexOf("/")+1);
        //remove tabs and new lines
        String nom = name.replace(prenom, "");
        if(name.length()>255){
            try {
                nom = Static.MD5.MD5(name);
            } catch (Exception ex) {
                System.err.println("Error when encoding in MD5: "+ex.getMessage() );
            }
        }        

        nom = nom.replace("\\n", "");
        nom = nom.replace("\n", "");
        nom = nom.replace("\b", "");
        //quitamos "/" de las posibles urls
        nom = nom.replace("/","_");
        nom = nom.replace("=","_");
        nom = nom.trim();
        //espacios no porque ya se urlencodean
        //nom = nom.replace(" ","_");
        //a to uppercase
        nom = nom.toUpperCase();
        try {
            //urlencodeamos para evitar problemas de espacios y acentos
            nom = new URI(null,nom,null).toASCIIString();//URLEncoder.encode(nom, "UTF-8");
        }
        catch (Exception ex) {
            try {
                System.err.println("Problem encoding the URI:" + nom + " " + ex.getMessage() +". We encode it in MD5");
                nom = Static.MD5.MD5(name);
                System.err.println("MD5 encoding: "+nom);
            } catch (Exception ex1) {
                System.err.println("Could not encode in MD5:" + name + " " + ex1.getMessage());
            }
        }
        return prenom+nom;
    }
    
    //just for tests
    public static void main(String[] args){
        OPMWTemplate2GraphProcessor test = new OPMWTemplate2GraphProcessor("http://wind.isi.edu:8890/sparql");
//        test.transformToSubdueGraph("http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_SINGLE_");
        
       //this is a test just for the workflows annotated by Idafen
       test.transformToSubdueGraph("http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI");
       test.transformToSubdueGraph("http://www.opmw.org/export/resource/WorkflowTemplate/FEATUREGENERATION");
       test.transformToSubdueGraph("http://www.opmw.org/export/resource/WorkflowTemplate/FEATURESELECTION");
       test.transformToSubdueGraph("http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLUSTERING");
       test.transformToSubdueGraph("http://www.opmw.org/export/resource/WorkflowTemplate/MODEL");
       
       String file = "SUBDUE_TOOL\\results\\Tests\\testResultReduced2";
       String ocFile = "SUBDUE_TOOL\\results\\Tests\\testResultReduced2_occurrences";
       HashMap<String,FinalResult> obtainedResults = new SubdueGraphReader().processResultsAndOccurrencesFiles(file, ocFile);
       
       //without inference
       SUBDUEFragmentCatalogAndResultsToRDF catalogNoInference = new SUBDUEFragmentCatalogAndResultsToRDF("out.ttl");
       
       catalogNoInference.transformFragmentCollectionToRDF(obtainedResults);
       catalogNoInference.transformBindingResultsInTemplateCollection(obtainedResults, test.getGraphCollection());
       //test1.transformBindingResultsOfFragmentCollectionInTemplateToRDF(obtainedResults, testGraph);
       
       catalogNoInference.exportToRDFFile("TURTLE");
       
       //with inference
       //first we get the replacement hashmap
       String taxonomyFilePath = "src\\TestFiles\\multiDomainOnto.owl"; //we assume the file has already been created.
       OntModel o = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
       InputStream in = FileManager.get().open(taxonomyFilePath);
       o.read(in, null);        
       HashMap replacements = CreateHashMapForInference.createReplacementHashMap(o);
       
       //we create the abstract collection
       GraphCollection abstractCollection = CreateAbstractResource.createAbstractCollection(test.getGraphCollection(), replacements);
       
       //we load the new files
       file = "resultsAbstractCatalog24-10-2013";
       ocFile = "resultsAbstractCatalog24-10-2013_occurrences";
       
       obtainedResults = new SubdueGraphReader().processResultsAndOccurrencesFiles(file, ocFile);
       
       //without inference
       SUBDUEFragmentCatalogAndResultsToRDF abstractCatalog = new SUBDUEFragmentCatalogAndResultsToRDF("outAbstract.ttl");
       
       abstractCatalog.transformFragmentCollectionToRDF(obtainedResults);
       abstractCatalog.transformBindingResultsInTemplateCollection(obtainedResults, abstractCollection);
       //test1.transformBindingResultsOfFragmentCollectionInTemplateToRDF(obtainedResults, testGraph);
       
       abstractCatalog.exportToRDFFile("TURTLE");
       
    }
}
