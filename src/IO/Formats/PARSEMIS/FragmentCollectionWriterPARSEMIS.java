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
package IO.Formats.PARSEMIS;

import DataStructures.Fragment;
import IO.Exception.CollectionWriterException;
import IO.Formats.PAFI.GraphWriterPAFI;
import IO.FragmentCollectionWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 *
 * @author Daniel Garijo
 */
public class FragmentCollectionWriterPARSEMIS extends FragmentCollectionWriter{

    @Override
    public void writeFragmentsToFile(HashMap<String, Fragment> fc, String outputPath, HashMap<String, String> replacements) throws CollectionWriterException {
        writeFragmentsToFile(fc, outputPath, replacements, null);
    }
    
    /**
     * In Parsemis we have an optional method to collect the frequencies, which
     * are included in the same file as the main fragments.
     * @param fc
     * @param outputPath
     * @param replacements
     * @param connections
     * @throws CollectionWriterException 
     */
    public void writeFragmentsToFile(HashMap<String, Fragment> fc, String outputPath, HashMap<String, String> replacements, HashMap<String,ArrayList<String>> connections) throws CollectionWriterException {
        FileWriter fstream = null; 
        BufferedWriter out = null;
        GraphWriterPARSEMIS gw = new GraphWriterPARSEMIS();
        Iterator<Entry<String,Fragment>> it = fc.entrySet().iterator();
        try {
            fstream = new FileWriter(outputPath);
            out = new BufferedWriter(fstream);
            while (it.hasNext()){
                Fragment currF = it.next().getValue();
                gw.writeReducedGraphToFile(currF.getDependencyGraph(), out, 0, replacements);
                if(connections!=null && connections.containsKey(currF.getStructureID())){
                    Iterator<String> it2 = connections.get(currF.getStructureID()).iterator();
                    while(it2.hasNext()){
                        out.write("#=> "+it2.next());
                        out.newLine();
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println("Exception while writing the graph: "+ex.getMessage());
            throw new CollectionWriterException("Exception while writing the graph: "+ex.getMessage(), ex);
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
