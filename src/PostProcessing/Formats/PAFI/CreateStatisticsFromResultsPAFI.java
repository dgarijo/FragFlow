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
package PostProcessing.Formats.PAFI;

import IO.Exception.FragmentReaderException;
import IO.Formats.PAFI.FragmentCollectionReaderPAFI;
import PostProcessing.CreateStatisticsFromResults;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class designed to create the statistics from 
 * @author Daniel Garijo
 */
public class CreateStatisticsFromResultsPAFI extends CreateStatisticsFromResults{
    private HashMap<String,ArrayList<String>> fragmentsInTransactions;


    /**
     * Main constructor
     * @param domain domain of the dataset (e.g., text analytics)
     * @param isTemplate is the dataset made of templates?
     * @param hasInference has inference been applied to the dataset?
     * @param resultFile PAFI result file (fp file)
     * @param pcFile PAFI pc file
     * @param tidFile PAFI tid file
     * @throws FragmentReaderException 
     */
    public CreateStatisticsFromResultsPAFI(String domain, 
             boolean isTemplate, boolean hasInference, 
                String resultFile, String pcFile, String tidFile) throws FragmentReaderException {                        
        super(domain, "-", "PAFI", isTemplate, hasInference, null);
        FragmentCollectionReaderPAFI reader = new FragmentCollectionReaderPAFI(resultFile,pcFile,tidFile);
        System.out.println("Reading results...");
        this.fragmentCatalog = reader.getFragmentCatalogAsHashMap();
        System.out.println("Getting occurrences of Fragment in transaction...");
        this.fragmentsInTransactions = reader.getOccurrencesOfFragmentInTransaction();
        System.out.println("Initializing statistics....");
        initializeStatistics();
    }

    /**
     * Returns in which transactions has the fragment in the first argument been found
     */
    public HashMap<String, ArrayList<String>> getFragmentsInTransactions() {
        return fragmentsInTransactions;
    }
    
}
