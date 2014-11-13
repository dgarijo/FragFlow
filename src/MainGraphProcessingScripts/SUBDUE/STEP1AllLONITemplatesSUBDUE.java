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
package MainGraphProcessingScripts.SUBDUE;

import DataStructures.GraphCollection;
import Factory.Loni.LoniTemplate2Graph;
import IO.Exception.CollectionWriterException;
import IO.Formats.SUBDUE.GraphCollectionWriterSUBDUE;
import IO.DatasetFilter;
import Static.Configuration;
import java.io.File;

/**
 * Script to translate the LONI dataset from Neda, Boris and Zhi (plus filtered)
 * to SUBDUE.
 * @author Daniel Garijo
 */
public class STEP1AllLONITemplatesSUBDUE {
    public static void main(String [] args) throws CollectionWriterException{
        System.out.println("\n Starting script for writing LONI collection");
        String zhiFolder = "LONI_dataset\\datasetZhiZhong\\";
        String nedaFolder = "LONI_dataset\\datasetNeda\\";
        String borisFolder = "LONI_dataset\\datasetBoris\\";
        
        //ZHI
        File f = new File(zhiFolder);
        LoniTemplate2Graph test = new LoniTemplate2Graph(zhiFolder);
        if(f.isDirectory()){
            File[] files = f.listFiles();
            for(int i=0;i<files.length;i++){
                test.transformToGraph(files[i].getName());
            }
            GraphCollectionWriterSUBDUE writer = new GraphCollectionWriterSUBDUE();
            writer.writeReducedGraphsToFile(test.getGraphCollection(),"LoniZhiSUBDUE.g", null);
            GraphCollection filteredC = DatasetFilter.removeDuplicates(test.getGraphCollection());
            writer.writeReducedGraphsToFile(filteredC,"LoniZhiFilteredSUBDUE.g", null);
        }
        
        //NEDA
        f = new File(nedaFolder);
        test = new LoniTemplate2Graph(nedaFolder);
        if(f.isDirectory()){
            File[] files = f.listFiles();
            for(int i=0;i<files.length;i++){
                test.transformToGraph(files[i].getName());
            }
            GraphCollectionWriterSUBDUE writer = new GraphCollectionWriterSUBDUE();
            writer.writeReducedGraphsToFile(test.getGraphCollection(),"LoniNedaSUBDUE.g", null);
            GraphCollection filteredC = DatasetFilter.removeDuplicates(test.getGraphCollection());
            writer.writeReducedGraphsToFile(filteredC,"LoniNedaFilteredSUBDUE.g", null);
        }
        
        //BORIS
        f = new File(borisFolder);
        test = new LoniTemplate2Graph(borisFolder);
        if(f.isDirectory()){
            File[] files = f.listFiles();
            for(int i=0;i<files.length;i++){
                test.transformToGraph(files[i].getName());
            }
            GraphCollectionWriterSUBDUE writer = new GraphCollectionWriterSUBDUE();
            writer.writeReducedGraphsToFile(test.getGraphCollection(),"LoniBorisSUBDUE.g", null);
            GraphCollection filteredC = DatasetFilter.removeDuplicates(test.getGraphCollection());
            writer.writeReducedGraphsToFile(filteredC,"LoniBorisFilteredSUBDUE.g", null);
        }
        
    }
}
