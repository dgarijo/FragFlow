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
import IO.DatasetFilter;
import IO.Exception.CollectionWriterException;
import IO.Formats.SUBDUE.GraphCollectionWriterSUBDUE;
import java.io.File;

/**
 *
 * @author Daniel Garijo
 */
public class EScience2014Extension_STEP1 {
    private static void processFolder(String folderPath, String outName) throws CollectionWriterException{
        //aux method to look for the name
        File f = new File(folderPath);
        if(f.isDirectory()){
            LoniTemplate2Graph test = new LoniTemplate2Graph(folderPath);
            File[] files = f.listFiles();
            for(int i=0; i< files.length;i++){
                test.transformToGraph(files[i].getName());
            }
            GraphCollection filteredC = DatasetFilter.removeDuplicates(test.getGraphCollection());
            GraphCollectionWriterSUBDUE writer = new GraphCollectionWriterSUBDUE();
            writer.writeReducedGraphsToFile(filteredC,outName, null);
        }
    }
    public static void main(String [] args) throws CollectionWriterException{
        System.out.println("\n Writing Samuel dataset in SUBDUE format");
        String escienceSamuel = "C:\\Users\\Monen\\Desktop\\LONIDatasets\\datasetSamuel\\";
        processFolder(escienceSamuel, "corpusSamuel.g");
             
    }
}
