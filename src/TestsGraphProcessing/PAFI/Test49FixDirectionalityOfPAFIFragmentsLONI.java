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
package TestsGraphProcessing.PAFI;

import DataStructures.Fragment;
import PostProcessing.Formats.PAFI.CreateStatisticsFromResultsPAFI;
import PostProcessing.Formats.PAFI.FixDirectionOfFragmentCatalog;
import Static.Configuration;
import Static.GeneralConstants;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Test designed to test if a LONI fragment has been corrected successfully
 * @author Daniel Garijo
 */
public class Test49FixDirectionalityOfPAFIFragmentsLONI {
    public static int testNumber = 49;
    public static boolean test(){
        try{
            System.out.println("\n\nExecuting test:"+testNumber+" Testing if LONI fragments can be fixed");
            String fpfile = "PAFI_TOOL\\results\\LoniDatasetFiltered.fp";
            String pcFile = "PAFI_TOOL\\results\\LoniDatasetFiltered.pc";
            String tidFile = "PAFI_TOOL\\results\\LoniDatasetFiltered.tid";
            CreateStatisticsFromResultsPAFI c = new CreateStatisticsFromResultsPAFI("LONI dataset", true, false, fpfile, pcFile, tidFile);            
            ArrayList<Fragment> aux = c.getFilteredMultiStepFragments();
            FixDirectionOfFragmentCatalog.fixDirectionOfCatalogWithLONIemplates(Configuration.getPAFIInputPath()+"LONIInPAFIFormat", aux,c.getFragmentsInTransactions());
            Iterator<Fragment> it = aux.iterator();
            boolean found = false;
            while(it.hasNext() && !found){
                Fragment f = it.next();
                //test performed on a single fragment 
                if(f.getStructureID().equals("5-164")){
                    found = true;
                    String[][] adjMatrix = f.getDependencyGraph().getAdjacencyMatrix();
                    /*for the test: 
                     * SSMA wib BiasFieldCorrector
                     * BiasField wib Reorient
                     * FLIRT wib SSMA
                     * smartline wib flirt
                     * 3dbspline wib smartline
                     */
                    if(adjMatrix[0][5]==null || !adjMatrix[0][5].equals(GeneralConstants.INFORM_DEPENDENCY)) return false;
                    if(adjMatrix[2][0]==null || !adjMatrix[2][0].equals(GeneralConstants.INFORM_DEPENDENCY)) return false;
                    if(adjMatrix[1][2]==null || !adjMatrix[1][2].equals(GeneralConstants.INFORM_DEPENDENCY)) return false;
                    if(adjMatrix[3][1]==null || !adjMatrix[3][1].equals(GeneralConstants.INFORM_DEPENDENCY)) return false;
                    if(adjMatrix[4][3]==null || !adjMatrix[4][3].equals(GeneralConstants.INFORM_DEPENDENCY)) return false;
                }
                
            }
            return true;//if it arrives to this point and it has not returned, return true
         }catch(Exception e){
            System.out.println("Test Test fix direction of PAFI fragments from lonifailed: "+ e.getMessage());
            return false;
        }
    }
    
    public static void main(String[] args){
        if(test())System.out.println("Test "+testNumber+" OK");
        else System.out.println("Test "+testNumber+" FAILED");
    }
}
