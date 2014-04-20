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
package MainGraphProcessingScripts.PARSEMIS;

import IO.Exception.FragmentReaderException;
import PostProcessing.Formats.PARSEMIS.CreateStatisticsFromResultsPARSEMIS;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Daniel Garijo
 */
public class STEP3aProduceStatisticsFromFilteredLONIResultsPARSEMIS_test {
    public static void main(String[]args){
        try {
            String inputFile = "PARSEMIS_TOOL\\results\\resultsLoniFullDatasetFiltered.lg";
            String d = new SimpleDateFormat("dd-M-yyyy_hh-mm-ss").format(new Date());
            CreateStatisticsFromResultsPARSEMIS c = new CreateStatisticsFromResultsPARSEMIS("LONI dataset", true, false, inputFile);
            c.printStatistics("statisticsParsemis-LONI-FilteredTest"+d);
        } catch (FragmentReaderException ex) {
            System.out.println("Error while executing test "+ex.getMessage());
        }
        
    }
}
