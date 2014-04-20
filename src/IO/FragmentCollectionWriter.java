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
package IO;

import DataStructures.Fragment;
import IO.Exception.CollectionWriterException;
import java.util.HashMap;

/**
 * Abstract class for writing fragments (needed for capturing intermediate steps
 * on different parts of the workflow)
 * @author Daniel Garijo
 */
public abstract class FragmentCollectionWriter {
    
    public void writeFragmentsToFile(HashMap<String,Fragment> fc, String outputPath) throws CollectionWriterException{
        writeFragmentsToFile(fc, outputPath, null);
    }
    
    public abstract void writeFragmentsToFile(HashMap<String,Fragment> fc, String outputPath, HashMap<String,String> replacements) throws CollectionWriterException;
}
