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
package PostProcessing.Formats.PARSEMIS;

import IO.Exception.FragmentReaderException;
import IO.Formats.PARSEMIS.Gspan.FragmentReaderPARSEMISGspan;
import PostProcessing.CreateStatisticsFromResults;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Main class to generate statistics for Parsemis results
 * @author Daniel Garijo
 */
public class CreateStatisticsFromResultsPARSEMIS extends CreateStatisticsFromResults{
    private HashMap<String, ArrayList<String>> fragmentsInTransactions;
  public CreateStatisticsFromResultsPARSEMIS(String domain, 
             boolean isTemplate, boolean hasInference, 
                String resultFile) throws FragmentReaderException {                        
        super(domain, "-", "Parsemis", isTemplate, hasInference, null);
        FragmentReaderPARSEMISGspan reader = new FragmentReaderPARSEMISGspan(resultFile);
        this.fragmentCatalog = reader.getFragmentCatalogAsHashMap();
        this.fragmentsInTransactions = reader.getOccurrencesOfFragmentInTransaction();     
        initializeStatistics();
    }

    /**
     * Returns in which transactions has the fragment in the first argument been found
     */
    public HashMap<String, ArrayList<String>> getFragmentsInTransactions() {
        return fragmentsInTransactions;
    }
    
//    public static void main(String[] args) throws FragmentReaderException{
//        CreateStatisticsFromResultsPARSEMIS c = new CreateStatisticsFromResultsPARSEMIS("TextAnalytics", true, false, "PARSEMIS_TOOL\\results\\run11-03-2014.txt");
//        c.printStatistics("testStatisticsParsemis");
//    }
}
