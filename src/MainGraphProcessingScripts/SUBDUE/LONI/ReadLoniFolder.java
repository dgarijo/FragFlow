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
package MainGraphProcessingScripts.SUBDUE.LONI;

import DataStructures.GraphCollection;
import Factory.Loni.LoniTemplate2Graph;
import IO.DatasetFilter;
import IO.Formats.SUBDUE.GraphCollectionWriterSUBDUE;
import java.io.File;

/**
 *
 * @author Daniel Garijo
 */
public class ReadLoniFolder {
    public static void main(String[] args){
        //parse arguments -l (latex), -s (save as) and -c (creator)
        int i=0;
        String currentArg="", loniDatasetFolder ="", outputFile="";
        try{
            while (i<args.length){
                currentArg = args[i];
                if(currentArg.equals("-i")){
                    loniDatasetFolder = args[i+1];
                }else if(currentArg.equals("-o")){
                    outputFile = args[i+1];
                }
                i++;
            }  
        }catch(Exception e){
            System.err.println(e.getMessage()+"\nWrong usage of commands. Example: java -jar ReadAndFilterLoniFolder -i InputFolder -o OutputPath");
            return;
        }
        try{
            System.out.println("\n Reading LONI files from a folder");
//            System.out.println(loniDatasetFolder);
            File f = new File(loniDatasetFolder);
            LoniTemplate2Graph test = new LoniTemplate2Graph(loniDatasetFolder);
            if(f.isDirectory()){
                File[] files = f.listFiles();
                for(int j=0;j<files.length;j++){
                    test.transformToGraph(files[j].getName());
                }
                GraphCollectionWriterSUBDUE writer = new GraphCollectionWriterSUBDUE();                
                writer.writeReducedGraphsToFile(test.getGraphCollection(),outputFile, null);
                System.out.println("Final dataset size: "+test.getGraphCollection().getNumberOfSubGraphs());
            }else{
                System.err.println("The input file is not a directory");
            }
        }catch(Exception e){
            System.err.println("\nError while reading or writing the LONI file"+e.getMessage());
        }
    }
}
