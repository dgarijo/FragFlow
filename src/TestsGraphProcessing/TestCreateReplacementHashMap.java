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
package TestsGraphProcessing;

import Factory.Inference.CreateHashMapForInference;
import DataStructures.GraphCollection;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Test designed to check wether the hashMap for the replacements can be created.
 * Note that we need a multidomain ontology beforehand
 * @author Daniel Garijo
 */
public class TestCreateReplacementHashMap {
    public static void test(){
        try{
        OntModel o = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        InputStream in = FileManager.get().open("C:\\Users\\Monen\\Dropbox\\MotifFinder\\src\\Tests\\test.owl");
        if(in!=null){
            o.read(in, null);        
            HashMap replacements = CreateHashMapForInference.createReplacementHashMap(o);
            System.out.println("OK!");
        }else{
            System.out.println("File not found");
        }
        }catch(Exception e){
            System.out.println("Error while executing test. Exception: "+e.getMessage());
        }
    }
    
    public static void main(String[] args){
        test();
    }
}
