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
package PostProcessing;

import DataStructures.Fragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class used to filter the fragments obtained. For example, if a fragment A
 * is contained in fragment B and A occurs the same amount of times as B, A is 
 * filtered. 
 * @author Daniel Garijo
 */
public class FragmentCatalogFilter {
    
    /**
     * Method that takes the filteredMultiStepFragments, (initialized
     * with all the multistep fragments) and filters those fragments that are not 
     * relevant. The rationale followed here is that if a fragment includes other 
     * fragments that occur the same number of times, those included fragments 
     * ought to be removed.
     * For example: Fragment 1 occurs 3 times, Fragment 2 occurs 4 times and 
     * Fragment 4 occurs 3 times). Fragment 2 and 1 are included in Fragment 4.
     * Since Fragment 1 occurs the same number of times as Fragment 4, we don't
     * consider significant (4 is the same but bigger). However Fragment 2 is not
     * removed, as has occurred more times (it means that it has been found outside 
     * fragment 4).
     * 
     * We assume that both catalogToFilter and its occurrences have been previously initialized.
     * @param catalogToFilter fragment catalog to be filtered.
     * @param occurrencesOfCatalogToFilter total number of occurrences of the fragment catalog
     * @return the number of occurrences of the filtered fragment catalog
     */
    public static void filterFragmentCatalog(HashMap<String,Fragment> catalogToFilter){//, int occurrencesOfCatalogToFilter){
        Iterator<String> it = catalogToFilter.keySet().iterator();
//        ArrayList<Fragment> elementsToRemove = new ArrayList<Fragment>();
        while(it.hasNext()){
            Fragment currentFragment = catalogToFilter.get(it.next());
            if(currentFragment.isMultiStepFragment()){
                ArrayList<Fragment> includedFragments = currentFragment.getListOfIncludedIDs();//GeneralMethodsFragments.getFullDependenciesOfFragment(currentFragment);
                //if any of the included structures has the same occurrences as the actual one,
                //we count the actual as the relevant structure and remove the others from the filtered 
                //multi step fragments
                if(includedFragments!=null){
                    Iterator<Fragment>includedFragmentsIt = includedFragments.iterator();
                    while(includedFragmentsIt.hasNext()){
                        Fragment currentIncludedFragment = includedFragmentsIt.next();                
                        if(catalogToFilter.containsKey(currentIncludedFragment.getStructureID()) && currentIncludedFragment.getNumberOfOccurrences() == currentFragment.getNumberOfOccurrences()){
//                            if(!elementsToRemove.contains(currentIncludedFragment))
//                                elementsToRemove.add(currentIncludedFragment);
                            //we avoid creating additional elements this way.
                            currentIncludedFragment.setIsFilteredMultiStepFragment(false);
                            currentIncludedFragment.setIsFilteredMultiStepIrreducibleFragment(false);
                        }
                    }
                }
            }else{
                currentFragment.setIsFilteredMultiStepFragment(false);
                //elementsToRemove.add(currentFragment);
            }
        }
        //now that we know the elements to remove, we remove them
//        Iterator<Fragment> itRemove = elementsToRemove.iterator();
////        occurrencesOfCatalog = occurrencesOfMultiStepStructures;        
//        while(itRemove.hasNext()){
//            Fragment fragmentToRemove = itRemove.next();
//            //while we remove, calculate the occurrences of filteredmultistep fragments here.
//            occurrencesOfCatalogToFilter -= fragmentToRemove.getNumberOfOccurrences();
//            catalogToFilter.remove(fragmentToRemove);            
//        }
//        return occurrencesOfCatalogToFilter;
    }
    
    /**
     * Simplification of the fragment filter to only filter the catalog.
     * @param catalogToFilter 
     */
//    public static void filterFragmentCatalog(ArrayList<Fragment>  catalogToFilter){
//        filterFragmentCatalog(catalogToFilter);//, 0);
//    }
    
}
