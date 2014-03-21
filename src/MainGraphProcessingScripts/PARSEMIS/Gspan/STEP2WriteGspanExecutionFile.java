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
package MainGraphProcessingScripts.PARSEMIS.Gspan;

import Static.Configuration;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Class to produce the Parsemis (gspan) script from the input file folder.
 * This class does not execute the Parsemis script.
 * @author Daniel Garijo
 */
public class STEP2WriteGspanExecutionFile {
     public static void main(String[] args){
        FileWriter fstream = null; 
        BufferedWriter out = null;             
        try {            
            fstream = new FileWriter("Parsemis_gspan_script");
            out = new BufferedWriter(fstream);
            
            File inputFileFolder = new File(Configuration.getPARSEMISInputPath());
            File [] inputFiles = inputFileFolder.listFiles();
            File executablePath = new File(Configuration.getPARSEMISExecutablePath());
            File resultsFile = new File(Configuration.getPARSEMISOutputPath());
            //for each file, we must create one line. Typically there will be 2: regular and abstracted.            
            for(int i=0;i<inputFiles.length;i++){
                out.write("cmd /C java -jar "+executablePath.getAbsolutePath()+File.separator+
                        "parsemis-2008-09-04.jar --graphFile="+inputFiles[i].getAbsolutePath()+
                        " --minimumFrequency=2 --outputFile="+resultsFile.getAbsolutePath()+File.separator+
                        "results"+inputFiles[i].getName()+" --storeEmbeddings=true --minimumNodeCount=2  \n");
            }
            out.close();
            fstream.close();                        
        }catch(Exception e){
            System.err.println("Error while writting the executable PAFI file"+e.getMessage());
        }
    }
}
