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
package Static;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * General class for declaring the paths needed for writting SUBDUE scripts
 * @author Daniel Garijo
 */
public class Configuration {
//    private String inputFolderPath ,outputFolderPath,logFilePath, subduePath;
    private static Configuration conf = null;
    private Properties config = null;
    
    //read each of these parameters from a configuration file in a singleton
    private Configuration(){
        config = new Properties();
 
    	try {
    		config.load(new FileInputStream(GeneralConstants.PROP_FILE));
 
    	} catch (Exception ex) {
    		System.err.println("Error while reading configuration properties "+ex.getMessage());
        }
    }
    
    private static Configuration getConfigurationFile(){
        if (conf == null){
            conf = new Configuration();
        }
        return conf;
    }
    
    public static String getSubdueToolPath(){
        return getConfigurationFile().config.getProperty("subdueToolPath");
    }
    
    public static String getInputFolderPath(){
        return getConfigurationFile().config.getProperty("inputFolderPath");
    }
    
    public static String getOutputFolderPath(){
        return getConfigurationFile().config.getProperty("outputFolderPath");
    }
    
    public static String getLogFilePath(){
        return getConfigurationFile().config.getProperty("logFilePath");
    }
    
    public static String getSubdueExecutableOutputPath(){
        return getConfigurationFile().config.getProperty("executableSUEBDUEScriptPath");
    }
    
    
//    public static void main(String[] args){
//        System.out.println(ConfigurationFile.getSubdueToolPath());
//        System.out.println(ConfigurationFile.getInputFolderPath());
//        System.out.println(ConfigurationFile.getOutputFolderPath());
//        System.out.println(ConfigurationFile.getLogFilePath());
//        System.out.println(ConfigurationFile.getSubdueExecutableOutputPath());
//        
//        //print the old values so I can replace them in the prop file
////        String subdueFolderPath = "C:"+File.separator+"Users"+File.separator+"Monen"+File.separator+"Documents"+File.separator+"NetBeansProjects"+File.separator+"MotifFinder"+File.separator+"SUBDUE_TOOL";
////        System.out.println(subdueFolderPath+File.separator+"input_graphs"+File.separator);        
////        System.out.println(subdueFolderPath+File.separator+"results"+File.separator);    
////        System.out.println(subdueFolderPath+File.separator+"Log"+File.separator);
////        System.out.println(subdueFolderPath+File.separator+"bin"+File.separator);
//    }
}
