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
package SubdueInvocation;


import Static.Configuration;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;

/**
 * This class writes a .bat with the invocations to subdue
 * @author Daniel Garijo
 */
public class WriteSubdueExecutionFile {

    public WriteSubdueExecutionFile() {
    }
    
    public static void main(String[] args){
        FileWriter fstream = null; 
        BufferedWriter out = null;             
        try {            
            fstream = new FileWriter(Configuration.getSubdueExecutableOutputPath()+"SUBDUE_exec_file.bat");
            out = new BufferedWriter(fstream);
            
            File inputFileFolder = new File(Configuration.getInputFolderPath());
            File [] inputFiles = inputFileFolder.listFiles();            
            String toExecuteEval1, toExecuteEval2;
            
            //for each execution we create a new results folder, under the results path
            //that is already specified.
            Date d = new Date();
            File folderResults = new File(Configuration.getOutputFolderPath()+File.separator+"results"+d.toString().replace(":", "_").replace(" ","_"));
            folderResults.mkdir();
            
            for (int i = 0; i<inputFiles.length; i++){
                File currentFile = inputFiles[i];
                System.out.println("Writting "+currentFile.getName());
                if(currentFile.isDirectory()){
                    //for each directory in the input_graph folder, apply subdue.
                    File[] dirFiles = currentFile.listFiles();                    
                    File customResultsFolder = new File(folderResults.getAbsolutePath()+File.separator+currentFile.getName());
                    customResultsFolder.mkdir();
                    for(int j=0; j<dirFiles.length;j++){
                        File cFile = dirFiles[j];
                        toExecuteEval1 = Configuration.getSubdueToolPath()+"subdue -iterations 0 -nsubs 1 -eval 1 "
                            + "-out "+customResultsFolder.getAbsolutePath()+File.separator+cFile.getName()+"-results1 "+
                                cFile.getAbsolutePath()+">"+Configuration.getLogFilePath()+
                                    customResultsFolder.getName()+cFile.getName()+"-logEval1.txt";
                        toExecuteEval2 = toExecuteEval1.replace("-eval 1", "-eval 2");
                        toExecuteEval2 = toExecuteEval1.replace("-results1", "-results2");
                        toExecuteEval2 = toExecuteEval2.replace("-logEval1", "-logEval2");
//                        System.out.println("cmd /C "+toExecuteEval1);
//                        System.out.println("cmd /C "+toExecuteEval2);
                        out.write("cmd /C "+toExecuteEval1+"\n");
                        out.write("cmd /C "+toExecuteEval2+"\n");
                    }
                }else{
                    //for each file in the input_graph folder, apply subdue
                    toExecuteEval1 = Configuration.getSubdueToolPath()+"subdue -iterations 0 -nsubs 1 -eval 1 "
                            + "-out "+folderResults.getAbsolutePath()+File.separator+currentFile.getName()+"-results1 "+
                                currentFile.getAbsolutePath()+">"+Configuration.getLogFilePath()+
                                    currentFile.getName()+"-logEval1.txt";
                    
                    toExecuteEval2 = toExecuteEval1.replace("-eval 1", "-eval 2");
                    toExecuteEval2 = toExecuteEval2.replace("-results1", "-results2");
                    toExecuteEval2 = toExecuteEval2.replace("-logEval1", "-logEval2");                    
//                    System.out.println("cmd /C "+toExecuteEval1);
//                    System.out.println("cmd /C "+toExecuteEval2);
                    out.write("cmd /C "+toExecuteEval1+"\n");
                    out.write("cmd /C "+toExecuteEval2+"\n");
                }
            }
            out.close();
            fstream.close();
//            String s = GeneralConstants.subduePath+"subdue -iterations 0 -nsubs 1 -out "+GeneralConstants.outputFolder_graphs+"test.g "+GeneralConstants.input_folder_graphs+"graph0>"+GeneralConstants.logFile+"log.txt";
//            System.out.println(s);
//            Process p = Runtime.getRuntime().exec("cmd /C"+s);
////            String[] execute = {GeneralConstants.subduePath+"subdue","-iterations 0","-nsubs 1", "-out "+GeneralConstants.outputFolder_graphs+"test.g",GeneralConstants.input_folder_graphs+"graph0"};
////            Process p = Runtime.getRuntime().exec(execute);
//            
//            p.waitFor();
//            System.out.print(p.exitValue());
//                        Process p = Runtime.getRuntime().exec("cmd /C"+toExecuteEval1);
//                        p.waitFor();
//                        p = Runtime.getRuntime().exec("cmd /C "+toExecuteEval2);
//                        p.waitFor();
//                        break;
        } catch (Exception ex) {
            System.err.println("Error while writting the executable SUBDUE file"+ex.getMessage());
        }
    }
    
}
