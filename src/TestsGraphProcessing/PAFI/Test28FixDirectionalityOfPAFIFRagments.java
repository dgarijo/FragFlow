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
import IO.Exception.FragmentReaderException;
import IO.Formats.PAFI.FragmentReaderPAFI;
import PostProcessing.Formats.PAFI.CreateStatisticsFromResultsPAFI;
import PostProcessing.Formats.PAFI.FixDirectionOfFragmentCatalog;
import PostProcessing.FragmentCatalogFilter;
import Static.Configuration;
import Static.GeneralConstants;
import java.util.ArrayList;

/**
 * Class designed to test if the filtered fragments of the PAFI catalog have been 
 * properly fixed.
 * @author Daniel Garijo
 */
public class Test28FixDirectionalityOfPAFIFragments {
    public static int testNumber = 28;
    public static boolean test(){
         try {
            System.out.println("\n\nExecuting test:"+testNumber+" Fix directionality of PAFI fragments."); 
            String fpfile = "PAFI_TOOL\\results\\CollectionInPAFIFormat.fp";
            String pcFile = "PAFI_TOOL\\results\\CollectionInPAFIFormat.pc";
            String tidFile = "PAFI_TOOL\\results\\CollectionInPAFIFormat.tid";
            CreateStatisticsFromResultsPAFI c = new CreateStatisticsFromResultsPAFI("Text analytics", true, false, fpfile, pcFile, tidFile); 
//            FragmentReaderPAFI reader= new FragmentReaderPAFI(fpfile, pcFile, tidFile);
//            ArrayList<Fragment> fixedCatalog = reader.getFragmentCatalogAsAnArrayList();
//            FragmentCatalogFilter.filterFragmentCatalog(fixedCatalog);
//            fixedCatalog = FixDirectionOfFragmentCatalog.fixDirectionOfCatalog(Configuration.getPAFIInputPath()+"CollectionInPAFIFormat", fixedCatalog,reader.getOccurrencesOfFragmentInTransaction(), false);
            ArrayList<Fragment> filteredFixedFragmentCatalog = FixDirectionOfFragmentCatalog.fixDirectionOfCatalog(Configuration.getPAFIInputPath()+"CollectionInPAFIFormat", c.getFilteredMultiStepFragments(),c.getFragmentsInTransactions(), false);
            
            //for these parameters, the first fragment has to have the following matrix:
            //[0,3]=[1,0]=[2,1]=wasInformedBy dependency. This is different from the original one which was:
            //[0,1]=[0,3]=[1,2]=wasInformedBy.
            Fragment f3_1 = filteredFixedFragmentCatalog.get(0);
            String[][] fAdjMatrix = f3_1.getDependencyGraph().getAdjacencyMatrix();
            if(fAdjMatrix[0][3].equals(fAdjMatrix[1][0]) && 
                fAdjMatrix[1][0].equals(fAdjMatrix[2][1]) && fAdjMatrix[2][1].equals(GeneralConstants.INFORM_DEPENDENCY)) 
                    return true;
            return false;
        } catch (FragmentReaderException ex) {
            System.out.println("Error while executing test "+ex.getMessage());
            return false;
        }
    }
    
    public static void main(String[] args){
        if(test())System.out.println("Test "+testNumber+" OK");
        else System.out.println("Test "+testNumber+" FAILED");
    }
}
