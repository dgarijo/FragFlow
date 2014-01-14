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

import Factory.OPMWTemplate2GraphProcessor;
import IO.Formats.CollectionWriterSUBDUEFormat;

/**
 *
 * @author Daniel Garijo
 */
public class TestSaveCollectionAsReducedGraphInFile {
    public static void test(){
        try{
            OPMWTemplate2GraphProcessor tp = new OPMWTemplate2GraphProcessor("http://wind.isi.edu:8890/sparql");
            tp.transformDomainToGraph("TextAnalytics");        
            CollectionWriterSUBDUEFormat writer = new CollectionWriterSUBDUEFormat();
            writer.writeReducedGraphsToFile(tp.getGraphCollection(), "TestSaveCollectionAsReducedlGraphInFile");
        }catch(Exception e){
            System.out.println("Test TestSaveCollectionAsReducedlGraphInFile case failed: "+ e.getMessage());
        }
    }
    
    public static void main(String[] args){
        test();
    }
}
