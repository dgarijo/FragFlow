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

import IO.Formats.PAFI.FragmentReaderPAFI;
import Static.Configuration;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * This class is a script to produce an executable script in Linux with the PAFI
 * commands. This class does not execute the script.
 * @author Daniel Garijo
 */
public class STEP2WritePAFIExecutionFile {

    public static void main(String[] args){
        FileWriter fstream = null; 
        BufferedWriter out = null;             
        try {            
            fstream = new FileWriter("PAFI_script");
            out = new BufferedWriter(fstream);
            
            File inputFileFolder = new File(Configuration.getPAFIInputPath());
            File [] inputFiles = inputFileFolder.listFiles();            
            //for each file, we must create one line. Typically there will be 2: regular and abstracted.            
            out.write("#!/bin/bash \n"
                    + "#FAPI Script to be executed in Linux. You may have to change some paths\n");
            for(int i=0;i<inputFiles.length;i++){
                //get the number of input files: process the input file 
                FileInputStream fstreamIn =null;
                DataInputStream in = null;
                fstreamIn = new FileInputStream(inputFiles[i].getAbsolutePath());            
                in = new DataInputStream(fstreamIn);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;                   
                int aux = 0;
                while ((strLine = br.readLine()) != null)   {                        
                    if(strLine.startsWith("t"))aux++;
                }
                in.close();
                fstreamIn.close();
                //now that we know the number of structures, we can calculate the minimum support
                int support = 100*2/aux;
                out.write(Configuration.getPAFIExecutablePath()+"fsg -s "+support+" -t -p "+inputFiles[i].getAbsolutePath()+"\n");
            }
            out.close();
            fstream.close();                        
        }catch(Exception e){
            System.err.println("Error while writting the executable PAFI file"+e.getMessage());
        }
    }
}
