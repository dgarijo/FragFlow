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
package MainGraphProcessingScripts.PAFI;

import IO.Exception.FragmentReaderException;
import PostProcessing.Formats.PAFI.CreateStatisticsFromResultsPAFI;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Daniel Garijo
 */
public class STEP3aProduceStatisticsFromLoniResultsPAFI {
    public static void main(String[] args){
        try {
            String d = new SimpleDateFormat("dd-M-yyyy_hh-mm-ss").format(new Date());
            String fpfile = "PAFI_TOOL\\results\\LoniDataset.fp";
            String pcFile = "PAFI_TOOL\\results\\LoniDataset.pc";
            String tidFile = "PAFI_TOOL\\results\\LoniDataset.tid";
            CreateStatisticsFromResultsPAFI c = new CreateStatisticsFromResultsPAFI("Loni dataset (image)", true, false, fpfile, pcFile, tidFile);            
            c.printStatistics("statisticsPafi-Loni"+d);            
        } catch (FragmentReaderException ex) {
            System.out.println("Error while executing test "+ex.getMessage());
        }
    }
}
