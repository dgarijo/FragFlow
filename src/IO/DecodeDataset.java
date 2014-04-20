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

import IO.Exception.CollectionWriterException;
import IO.Exception.FragmentReaderException;
import java.util.HashMap;

/**
 * Class to read and translate a dataset given the set of codes.
 * Input: a dataset and the codes.
 * Output: a translated dataset.
 * @author Daniel Garijo
 */
public class DecodeDataset {
    //TO DO: Move this class to an IO.utils?
    //since we don't know which type of algorithm it has been used, we use the fragment reader.
    public static void decodeDataset(FragmentCollectionReader r, String inputPathCodes, FragmentCollectionWriter w, String outputPath) throws FragmentReaderException, CollectionWriterException{
        HashMap<String, String> codes = CodeTypeReader.getTypesForCodes(inputPathCodes);
        w.writeFragmentsToFile(r.getFragmentCatalogAsHashMap(), outputPath, codes);     
    }
    
}
