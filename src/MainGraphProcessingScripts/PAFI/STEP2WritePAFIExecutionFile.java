/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
package MainGraphProcessingScripts.PAFI;

import Static.Configuration;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * This class is a script to produce an executable script in Linux with the PAFI
 * commands. This class does not execute the script.
 * @author Daniel Garijo
 */
public class STEP2WritePAFIExecutionFile {
//  to do
//  
//  Recordar que esto tiene que producir un script de shell unix.
    
    //con inferencia y sin.
    
    public static void main(String[] args){
        FileWriter fstream = null; 
        BufferedWriter out = null;             
        try {            
            fstream = new FileWriter(Configuration.getSubdueExecutableOutputPath()+"PAFI_script");
            out = new BufferedWriter(fstream);
            
            File inputFileFolder = new File(Configuration.getPAFIInputPath());
            File [] inputFiles = inputFileFolder.listFiles();            
            //for each file, we must create one line. Typically there will be 2: regular and abstracted.
            
            //the only requirement here is to know the numer of templates. We have to process the input for that.
        }catch(Exception e){
            System.err.println("Error while writting the executable PAFI file"+e.getMessage());
        }
    }
}
