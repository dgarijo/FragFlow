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
 * General class for declaring the paths needed for writting SUBDUE scripts and
 * other configuration details.
 * @author Daniel Garijo
 */
public class Configuration {
//    private String inputFolderPath ,outputFolderPath,logFilePath, subduePath;
    private static Configuration conf = null;
    private Properties config = null;
    
    /**
     * Method created to read each of the parameters from a configuration 
     * file in a singleton.
     */
    private Configuration(){
        config = new Properties();
 
    	try {
    		config.load(new FileInputStream(GeneralConstants.PROP_FILE));
 
    	} catch (Exception ex) {
    		System.err.println("Error while reading configuration properties "+ex.getMessage());
        }
    }
    
    /**
     * Singleton method to get the configuration file.
     * @return the actual configuration file
     */
    private static Configuration getConfigurationFile(){
        if (conf == null){
            conf = new Configuration();
        }
        return conf;
    }
    
    /**
     * Getter of the SUBDUE tool path (for creating scripts)
     * @return the Subdue tool path
     */
    public static String getSubdueToolPath(){
        return getConfigurationFile().config.getProperty("subdueToolPath");
    }
    
    /**
     * Getter of the SUBDUE input folder path
     * @return input folder path
     */
    public static String getSUBDUEInputFolderPath(){
        return getConfigurationFile().config.getProperty("subdueInputFolderPath");
    }
    
    /**
     * Getter of the SUBDUE output folder path (for creating scripts)
     * @return 
     */
    public static String getSUBDUEOutputFolderPath(){
        return getConfigurationFile().config.getProperty("subdueOutputFolderPath");
    }
    
    /**
     * Getter of the log file path (for creating scripts)
     * @return the log file path
     */
    public static String getSUBDUELogFilePath(){
        return getConfigurationFile().config.getProperty("subdueLogFilePath");
    }
    
    /**
     * Getter of the executable output path (for creating scripts)
     * @return the executable output path
     */
    public static String getSubdueExecutableOutputPath(){
        return getConfigurationFile().config.getProperty("subdueExecutableScriptPath");
    }
    
    /**
     * PAFI executable path
     * @return the executable output path
     */
    public static String getPAFIExecutablePath(){
        return getConfigurationFile().config.getProperty("pafiExecutableBinaryPath");
    }
    
    /**
     * PAFI input path folder
     * @return the executable output path
     */
    public static String getPAFIInputPath(){
        return getConfigurationFile().config.getProperty("pafiInputFolderPath");
    }
    
    /**
     * Parsemis executable path
     * @return the executable output path
     */
    public static String getPARSEMISExecutablePath(){
        return getConfigurationFile().config.getProperty("paresmisExecutableBinaryPath");
    }
    
    /**
     * Parsemis input path folder
     * @return the executable output path
     */
    public static String getPARSEMISInputPath(){
        return getConfigurationFile().config.getProperty("parsemisInputFolderPath");
    }
    
    public static String getPARSEMISOutputPath(){
        return getConfigurationFile().config.getProperty("parsemisOutputFolderPath");
    }
}
