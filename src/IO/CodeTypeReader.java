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
package IO;

import IO.Exception.FragmentReaderException;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Class to load the codes of the types of a dataset. It retruns a hashmap with 
 * the code and the type associated to it. (Even if the file is written in the form
 * type->code)
 * This is only needed to translate the dataset.
 * @author Daniel Garijo
 */
public class CodeTypeReader {
    
    /**
     * Method to retrieve the types of the codes associated to a dataset.
     * @param path path of the file with the codes.
     * @return a hashmap with key:code, value:type.
     */
    public static HashMap<String,String> getTypesForCodes(String path) throws FragmentReaderException{
        HashMap<String,String> codeAndType = null;
        FileInputStream fstream =null;
        DataInputStream in = null;
        try{
            fstream = new FileInputStream(path);            
            in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            codeAndType = new HashMap<String, String>();
            String currentLine;
            while((currentLine = br.readLine()) != null){
               String[] s =  currentLine.split("->");
               if(s.length>1){//in case we have just a line break
                   codeAndType.put(s[1],s[0]);
               }
            }
        }catch(Exception e){
            System.err.println("Error while processing the file "+e.getMessage());
            throw new FragmentReaderException("Error while reading the file "+e.getMessage(), e);
        }finally{
            return codeAndType;
        }
    }
    
    
//    public static void main(String []args) throws FragmentReaderException{
//        CodeTypeReader.getTypesForCodes("PARSEMIS_TOOL\\input_graphs\\LoniZhitEncoded.lg-codes");
//    }
    
}
