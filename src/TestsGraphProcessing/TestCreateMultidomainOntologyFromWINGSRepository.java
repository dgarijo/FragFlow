/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TestsGraphProcessing;

import Factory.Inference.CreateMultidomainTaxonomyWings;

/**
 * Test to create a multidomain taxonomy from all Wings domains and create and
 * merge it. The URL of the endpoint is needed.
 * @author Daniel Garijo
 */
public class TestCreateMultidomainOntologyFromWINGSRepository {
    public static void test(){
        System.out.println("Testing create a multidomain ontology");
        try{
            CreateMultidomainTaxonomyWings c = new CreateMultidomainTaxonomyWings("http://wind.isi.edu:8890/sparql");
            c.saveMultiDomainOntologyToFile("testOntology.owl");
        }catch(Exception e){
            System.out.println("Error in test. Exception: "+e.getMessage());
        }
    }
    
    public static void main(String[] args){
        test();
    }
    
}
