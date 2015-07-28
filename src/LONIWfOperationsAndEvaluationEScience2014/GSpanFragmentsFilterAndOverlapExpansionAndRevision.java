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
package LONIWfOperationsAndEvaluationEScience2014;

import DataStructures.Fragment;
import DataStructures.GraphCollection;
import Factory.Loni.LoniTemplateAndGroupings2Graph;
import IO.DatasetFilter;
import IO.Exception.FragmentReaderException;
import IO.Formats.PARSEMIS.Gspan.FragmentCollectionReaderPARSEMISGspan;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Main step to produce the RDF from the GSPAN results.
 * This includes: creating the fragment collection and filtering it
 * @author Daniel Garijo
 */

public class GSpanFragmentsFilterAndOverlapExpansionAndRevision {
    public static ArrayList<Fragment> getHashMapAsCollection(HashMap<String,Fragment> fragmentCatalog){
        ArrayList<Fragment> filteredMultiStepFragments = new ArrayList<Fragment>();
        Iterator<String> it = fragmentCatalog.keySet().iterator();
        while(it.hasNext()){
            Fragment currF = fragmentCatalog.get(it.next());
            if(currF.isFilteredMultiStepFragment()){
                filteredMultiStepFragments.add(currF);
            }
        }
        return filteredMultiStepFragments;
    }
    
    public static void main(String[] args) throws FragmentReaderException{
        String fragmentFile;
        String repositoryFolder;
        GraphCollection endCollection;
        File f;
        
        //WC1 
        repositoryFolder = "C:\\Users\\dgarijo\\Desktop\\LONI_Datasets\\WC1\\";
        LoniTemplateAndGroupings2Graph test = new LoniTemplateAndGroupings2Graph(repositoryFolder);
        f = new File(repositoryFolder);
        if(f.isDirectory()){
            File[] files = f.listFiles();
            for (File file : files) {
                test.transformToGraph(file.getName());
            }
        }
        endCollection = DatasetFilter.removeDuplicates(test.getGraphCollection());
        
        SUBDUEFragmentAndGroupingOverlapLONIExpansionAndRevision fa2 = new SUBDUEFragmentAndGroupingOverlapLONIExpansionAndRevision();
        //these are the results obtained with closeGraph: ALL multistep fragments are ALSO filtered multistep.
        //Normally we would filter those included in others, but we don't have the frequency. Hence we have to trust
        //CloseGraph. Otherwise we would be only taking into account maximal patterns.
        //maximal patterns can be obtained through the statistics -> as in the other gSPanScripts.
        //here we are processing closeGraph fragments, which are already filtered multistep.
        //If we process the included fragments, we would only have maximal patterns.
        
        //Support 2%
//        fragmentFile = "C:\\Users\\dgarijo\\Desktop\\GSPANEvaluationJuly2015\\Results\\WC1\\Support-2\\WC1_2_closed-limited.lg";
//        FragmentCollectionReaderPARSEMISGspan reader = new FragmentCollectionReaderPARSEMISGspan(fragmentFile);
//        fa2.setOverlap(90);
//        fa2.printStatisticsOnFragmentOverlap(getHashMapAsCollection(reader.getFragmentCatalogAsHashMapWithNoIncludedFragments()), endCollection);
//        //maximal patterns        
//        reader = new FragmentCollectionReaderPARSEMISGspan(fragmentFile);
//        fa2.printStatisticsOnFragmentOverlap(getHashMapAsCollection(reader.getFragmentCatalogAsHashMap()), endCollection);
        
        //Support 5%
        fragmentFile = "C:\\Users\\dgarijo\\Desktop\\GSPANEvaluationJuly2015\\Results\\WC1\\Support-5\\WC1_5_closed.lg";
        FragmentCollectionReaderPARSEMISGspan reader = new FragmentCollectionReaderPARSEMISGspan(fragmentFile);
        fa2.setOverlap(90);
        fa2.printStatisticsOnFragmentOverlap(getHashMapAsCollection(reader.getFragmentCatalogAsHashMapWithNoIncludedFragments()), endCollection);
//        maximal patterns        
        reader = new FragmentCollectionReaderPARSEMISGspan(fragmentFile);
        fa2.printStatisticsOnFragmentOverlap(getHashMapAsCollection(reader.getFragmentCatalogAsHashMap()), endCollection);
        
        //Support 10%
//        fragmentFile = "C:\\Users\\dgarijo\\Desktop\\GSPANEvaluationJuly2015\\Results\\WC1\\Support-10\\WC1_10_closed.lg";
//        FragmentCollectionReaderPARSEMISGspan reader = new FragmentCollectionReaderPARSEMISGspan(fragmentFile);
//        fa2.setOverlap(90);
//        fa2.printStatisticsOnFragmentOverlap(getHashMapAsCollection(reader.getFragmentCatalogAsHashMapWithNoIncludedFragments()), endCollection);
//        //maximal patterns        
//        reader = new FragmentCollectionReaderPARSEMISGspan(fragmentFile);
//        fa2.printStatisticsOnFragmentOverlap(getHashMapAsCollection(reader.getFragmentCatalogAsHashMap()), endCollection);
        
        //Support 15%
//        fragmentFile = "C:\\Users\\dgarijo\\Desktop\\GSPANEvaluationJuly2015\\Results\\WC1\\Support-15\\WC1_15_closed.lg";
//        FragmentCollectionReaderPARSEMISGspan reader = new FragmentCollectionReaderPARSEMISGspan(fragmentFile);
//        fa2.setOverlap(90);
//        fa2.printStatisticsOnFragmentOverlap(getHashMapAsCollection(reader.getFragmentCatalogAsHashMapWithNoIncludedFragments()), endCollection);
//        //maximal patterns        
//        reader = new FragmentCollectionReaderPARSEMISGspan(fragmentFile);
//        fa2.printStatisticsOnFragmentOverlap(getHashMapAsCollection(reader.getFragmentCatalogAsHashMap()), endCollection);
        
        //WC2 
//        repositoryFolder = "C:\\Users\\dgarijo\\Desktop\\LONI_Datasets\\WC2\\all\\";
//        LoniTemplateAndGroupings2Graph test = new LoniTemplateAndGroupings2Graph(repositoryFolder);
//        f = new File(repositoryFolder);
//        if(f.isDirectory()){
//            File[] files = f.listFiles();
//            for (File file : files) {
//                test.transformToGraph(file.getName());
//            }
//        }
//        endCollection = DatasetFilter.removeDuplicates(test.getGraphCollection());
//        
//        SUBDUEFragmentAndGroupingOverlapLONIExpansionAndRevision fa2 = new SUBDUEFragmentAndGroupingOverlapLONIExpansionAndRevision();
        
        //Support 2%
//        fragmentFile = "C:\\Users\\dgarijo\\Desktop\\GSPANEvaluationJuly2015\\Results\\WC2\\Support-2\\WC2_2_closed_limit15.lg";
//        FragmentCollectionReaderPARSEMISGspan reader = new FragmentCollectionReaderPARSEMISGspan(fragmentFile);
//        fa2.setOverlap(90);
//        fa2.printStatisticsOnFragmentOverlap(getHashMapAsCollection(reader.getFragmentCatalogAsHashMapWithNoIncludedFragments()), endCollection);
//        //maximal patterns        
//        reader = new FragmentCollectionReaderPARSEMISGspan(fragmentFile);
//        fa2.printStatisticsOnFragmentOverlap(getHashMapAsCollection(reader.getFragmentCatalogAsHashMap()), endCollection);
        
        //Support 5%
//        fragmentFile = "C:\\Users\\dgarijo\\Desktop\\GSPANEvaluationJuly2015\\Results\\WC2\\Support-5\\WC2_5_closed.lg";
//        FragmentCollectionReaderPARSEMISGspan reader = new FragmentCollectionReaderPARSEMISGspan(fragmentFile);
//        fa2.setOverlap(90);
//        fa2.printStatisticsOnFragmentOverlap(getHashMapAsCollection(reader.getFragmentCatalogAsHashMapWithNoIncludedFragments()), endCollection);
////        maximal patterns        
//        reader = new FragmentCollectionReaderPARSEMISGspan(fragmentFile);
//        fa2.printStatisticsOnFragmentOverlap(getHashMapAsCollection(reader.getFragmentCatalogAsHashMap()), endCollection);
        
        //Support 10%
//        fragmentFile = "C:\\Users\\dgarijo\\Desktop\\GSPANEvaluationJuly2015\\Results\\WC2\\Support-10\\WC2_10_closed.lg";
//        FragmentCollectionReaderPARSEMISGspan reader = new FragmentCollectionReaderPARSEMISGspan(fragmentFile);
//        fa2.setOverlap(90);
//        fa2.printStatisticsOnFragmentOverlap(getHashMapAsCollection(reader.getFragmentCatalogAsHashMapWithNoIncludedFragments()), endCollection);
//        //maximal patterns        
//        reader = new FragmentCollectionReaderPARSEMISGspan(fragmentFile);
//        fa2.printStatisticsOnFragmentOverlap(getHashMapAsCollection(reader.getFragmentCatalogAsHashMap()), endCollection);
        
        //Support 15%
//        fragmentFile = "C:\\Users\\dgarijo\\Desktop\\GSPANEvaluationJuly2015\\Results\\WC2\\Support-15\\WC2_15_closed.lg";
//        FragmentCollectionReaderPARSEMISGspan reader = new FragmentCollectionReaderPARSEMISGspan(fragmentFile);
////        fa2.setOverlap(90);
//        fa2.printStatisticsOnFragmentOverlap(getHashMapAsCollection(reader.getFragmentCatalogAsHashMapWithNoIncludedFragments()), endCollection);
//        //maximal patterns        
//        reader = new FragmentCollectionReaderPARSEMISGspan(fragmentFile);
//        fa2.printStatisticsOnFragmentOverlap(getHashMapAsCollection(reader.getFragmentCatalogAsHashMap()), endCollection);
        
        //WC3 
//        repositoryFolder = "C:\\Users\\dgarijo\\Desktop\\LONI_Datasets\\WC3\\2014-01\\";
//        LoniTemplateAndGroupings2Graph test = new LoniTemplateAndGroupings2Graph(repositoryFolder);
//        f = new File(repositoryFolder);
//        if(f.isDirectory()){
//            File[] files = f.listFiles();
//            for (File file : files) {
//                test.transformToGraph(file.getName());
//            }
//        }
//        endCollection = DatasetFilter.removeDuplicates(test.getGraphCollection());
//        
//        SUBDUEFragmentAndGroupingOverlapLONIExpansionAndRevision fa2 = new SUBDUEFragmentAndGroupingOverlapLONIExpansionAndRevision();
       
        //Support 2%
//        fragmentFile = "C:\\Users\\dgarijo\\Desktop\\GSPANEvaluationJuly2015\\Results\\WC3\\Support-2\\WC3_2_closed.lg";
//        FragmentCollectionReaderPARSEMISGspan reader = new FragmentCollectionReaderPARSEMISGspan(fragmentFile);
//        fa2.setOverlap(90);
//        fa2.printStatisticsOnFragmentOverlap(getHashMapAsCollection(reader.getFragmentCatalogAsHashMapWithNoIncludedFragments()), endCollection);
//        //maximal patterns        
//        reader = new FragmentCollectionReaderPARSEMISGspan(fragmentFile);
//        fa2.printStatisticsOnFragmentOverlap(getHashMapAsCollection(reader.getFragmentCatalogAsHashMap()), endCollection);
        
        //Support 5%
//        fragmentFile = "C:\\Users\\dgarijo\\Desktop\\GSPANEvaluationJuly2015\\Results\\WC3\\Support-5\\WC3_5_closed.lg";
//        FragmentCollectionReaderPARSEMISGspan reader = new FragmentCollectionReaderPARSEMISGspan(fragmentFile);
//        fa2.setOverlap(90);
//        fa2.printStatisticsOnFragmentOverlap(getHashMapAsCollection(reader.getFragmentCatalogAsHashMapWithNoIncludedFragments()), endCollection);
////        maximal patterns        
//        reader = new FragmentCollectionReaderPARSEMISGspan(fragmentFile);
//        fa2.printStatisticsOnFragmentOverlap(getHashMapAsCollection(reader.getFragmentCatalogAsHashMap()), endCollection);
        
        //Support 10%
//        fragmentFile = "C:\\Users\\dgarijo\\Desktop\\GSPANEvaluationJuly2015\\Results\\WC3\\Support-10\\WC3_10_closed.lg";
//        FragmentCollectionReaderPARSEMISGspan reader = new FragmentCollectionReaderPARSEMISGspan(fragmentFile);
//        fa2.setOverlap(90);
//        fa2.printStatisticsOnFragmentOverlap(getHashMapAsCollection(reader.getFragmentCatalogAsHashMapWithNoIncludedFragments()), endCollection);
//        //maximal patterns        
//        reader = new FragmentCollectionReaderPARSEMISGspan(fragmentFile);
//        fa2.printStatisticsOnFragmentOverlap(getHashMapAsCollection(reader.getFragmentCatalogAsHashMap()), endCollection);
        
        //WC4 
//        repositoryFolder = "C:\\Users\\dgarijo\\Desktop\\LONI_Datasets\\WC4\\";
//        LoniTemplateAndGroupings2Graph test = new LoniTemplateAndGroupings2Graph(repositoryFolder);
//        f = new File(repositoryFolder);
//        if(f.isDirectory()){
//            File[] files = f.listFiles();
//            for (File file : files) {
//                test.transformToGraph(file.getName());
//            }
//        }
//        endCollection = DatasetFilter.removeDuplicates(test.getGraphCollection());
//        
//        SUBDUEFragmentAndGroupingOverlapLONIExpansionAndRevision fa2 = new SUBDUEFragmentAndGroupingOverlapLONIExpansionAndRevision();
//        
//        //Support 10%
////        fragmentFile = "C:\\Users\\dgarijo\\Desktop\\GSPANEvaluationJuly2015\\Results\\WC4\\Support-10\\WC4_10_closed-limit10.lg";
////        FragmentCollectionReaderPARSEMISGspan reader = new FragmentCollectionReaderPARSEMISGspan(fragmentFile);
//////        fa2.setOverlap(90);
////        fa2.printStatisticsOnFragmentOverlap(getHashMapAsCollection(reader.getFragmentCatalogAsHashMapWithNoIncludedFragments()), endCollection);
////        //maximal patterns        
////        reader = new FragmentCollectionReaderPARSEMISGspan(fragmentFile);
////        fa2.printStatisticsOnFragmentOverlap(getHashMapAsCollection(reader.getFragmentCatalogAsHashMap()), endCollection);
//        //Support 10%
//        fragmentFile = "C:\\Users\\dgarijo\\Desktop\\GSPANEvaluationJuly2015\\Results\\WC4\\Support-15\\WC4_15_closed.lg";
//        FragmentCollectionReaderPARSEMISGspan reader = new FragmentCollectionReaderPARSEMISGspan(fragmentFile);
////        fa2.setOverlap(90);
//        fa2.printStatisticsOnFragmentOverlap(getHashMapAsCollection(reader.getFragmentCatalogAsHashMapWithNoIncludedFragments()), endCollection);
//        //maximal patterns        
//        reader = new FragmentCollectionReaderPARSEMISGspan(fragmentFile);
//        fa2.printStatisticsOnFragmentOverlap(getHashMapAsCollection(reader.getFragmentCatalogAsHashMap()), endCollection);
        
    }
    
    
}
