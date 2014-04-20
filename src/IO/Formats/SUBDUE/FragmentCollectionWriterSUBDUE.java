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
package IO.Formats.SUBDUE;

import DataStructures.Fragment;
import IO.Exception.CollectionWriterException;
import IO.FragmentCollectionWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author Daniel Garijo
 */
public class FragmentCollectionWriterSUBDUE extends FragmentCollectionWriter{

    
    @Override
    public void writeFragmentsToFile(HashMap<String, Fragment> fc, String outputPath, HashMap<String, String> replacements) throws CollectionWriterException {
        FileWriter fstream = null; 
        BufferedWriter out = null; 
        GraphWriterSUBDUE gw = new GraphWriterSUBDUE();
        try {
            fstream = new FileWriter(outputPath);
            out = new BufferedWriter(fstream);
            int structureNumber = 1;
            for(int i=1; i<=fc.size();i++){
                out.write("S\n");
                //The fragments in SUBDUE HAVE to be written in order.
                Fragment f = fc.get("SUB_"+structureNumber);
                structureNumber++;
                //if some fragments are not there, just continue looking
                while(f == null){
                    f = fc.get("SUB_"+structureNumber);
                    structureNumber++;
                }
                gw.writeReducedGraphToFile(f.getDependencyGraph(), out,0, replacements);
                out.write("\n");
            }            
        } catch (Exception ex) {
            System.err.println("Exception while writing the graph: "+ex.getMessage());
            throw new CollectionWriterException("Exception while writing the graph. "+ex.getMessage(), ex);
        } finally {
            try {
                if(out!=null)out.close();
                if(fstream!=null)fstream.close();
            } catch (IOException ex) {
                System.err.println("Error closing the files: "+ex.getMessage());
            }
        }   
    }
    
}
