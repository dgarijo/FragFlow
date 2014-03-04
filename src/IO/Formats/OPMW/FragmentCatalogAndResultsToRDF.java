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
package IO.Formats.OPMW;

import DataStructures.Fragment;
import DataStructures.Graph;
import DataStructures.GraphCollection;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Abstract Class designed to declare the general methods to transform a 
 * Fragment Catalog (result from the analysis) to RDF.
 * 
 * This class has to be expanded depending on how the fragments are detected.
 * For example, SUBDUE and the hierarchical clustering needs an special approach
 * 
 * The model used for this is the Workflow Fragment Description Ontology.
 * Every time a new catalog is passed a new set of URIs is created.
 * For more information see http://purl.org/net/wf-fd
 * @author Daniel Garijo
 */
public abstract class FragmentCatalogAndResultsToRDF {
    protected String outPath;
    protected OntModel repository;
    protected String dateToken;

    /**
     * Default constructor
     * @param outPath the path to save the result RDF file.
     */
    public FragmentCatalogAndResultsToRDF(String outPath) {
        this.outPath = outPath;        
        repository = ModelFactory.createOntologyModel();
        //initialization of the date, which will determine the URIs
        dateToken=""+new Date().getTime();
    }

    /**
     * Getter of the repository, in case someone wants to query the model.
     * 
     * @return the repository (OntModel)
     */
    public OntModel getRepository() {
        return repository;
    }
    
    
    /**
     * Function that accepts a catalog of fragments and prints them in RDF
     * @param catalog a hashmap with the name of the fragment and the 
     * FinalResult implementing it.
     */
    public abstract void transformFragmentCollectionToRDF(ArrayList<Fragment> catalog);
    
    /**
     * Given a set of fragments and a graph collection of graphs, this method 
     * checks whether any of the fragments were found in any of the graphs in 
     * the collection
     * @param obtainedResults
     * @param templates 
     */
    public abstract void transformBindingResultsInTemplateCollection(ArrayList<Fragment> obtainedResults, GraphCollection templates);
    
    /**
     * Method which given a set of fragments and a template, checks whether 
     * any of the fragments can be found in the template or not. The results 
     * are exported to RDF.
     * @param obtainedResults results obtained by a SUBDUE algorithm.
     * @param template template in which we want to search the results.
     */
    public abstract void transformBindingResultsOfFragmentCollectionInTemplateToRDF(ArrayList<Fragment> obtainedResults, Graph template);
    
    /**
     * Given a fragment and a template, this method detects where in the 
     * template has the fragment been found and trasforms the results to RDF.
     * @param currentFragment
     * @param template 
     */
    public abstract void transformBindingResultsOfOneFragmentAndOneTemplateToRDF(Fragment currentFragment, Graph template);        
    
    
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
}
