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
package LONIWfOperationsAndEvaluationEScience2014;

import DataStructures.GraphCollection;
import Factory.Loni.LoniTemplate2Graph;
import IO.DatasetFilter;
import java.io.File;

/**
 * Small script to count the templates that have been actually filtered from 
 * each dataset (removed duplicates).
 * @author Daniel Garijo
 */
public class CountLONIFilteredTemplates {
    public static String countFilterTemplates(String repositoryFolder){
        File f = new File(repositoryFolder);
        LoniTemplate2Graph test = new LoniTemplate2Graph(repositoryFolder);
        int originalTemplates = 0,filteredTemplates = 0;
        String s ="";
        try{
        if(f.isDirectory()){
            File[] files = f.listFiles();
            for(int i=0;i<files.length;i++){
                test.transformToGraph(files[i].getName());
                if(files[i].getName().endsWith("pipe")||files[i].getName().endsWith("xml")){
                    originalTemplates++;
                }
            }
            //just by processing some templates we are sometimes filtering them (if it is no bigger than 1 node).
            s+="Number of original templates: "+originalTemplates+"\n";
            GraphCollection filteredC = DatasetFilter.removeDuplicates(test.getGraphCollection());
            filteredTemplates = filteredC.getNumberOfSubGraphs();
            s+="Number of filtered templates (no duplicates, only templates with more than 1 node): "+filteredTemplates+"\n";
        }
        return s;
        }catch(Exception e){
            System.out.println("Error while counting the templates");
            return "Error while calculating the stats";
        }
    }
    public static void main(String[] args){
        String stats;
        String escienceBoris = "C:\\Users\\Monen\\Desktop\\LONIDatasets\\WorkflowBoris\\Pipeline123\\";
        stats = "All Boris dataset\n";
        stats += countFilterTemplates(escienceBoris);
        String escienceIvo = "C:\\Users\\Monen\\Desktop\\LONIDatasets\\WorkflowIVO\\all\\";
        stats +=  "Public Dataset (+ Ivo) \n";
        stats += countFilterTemplates(escienceIvo);
        String escienceMonthly = "C:\\Users\\Monen\\Desktop\\LONIDatasets\\dataset3months(Zhizhong)\\2014-01\\";
        stats += "Monthly Dataset\n";
        stats += countFilterTemplates(escienceMonthly);
        System.out.println(stats);
    }
}
