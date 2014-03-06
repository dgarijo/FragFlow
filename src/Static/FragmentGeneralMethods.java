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
package Static;

import DataStructures.Fragment;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Daniel Garijo
 */
public class FragmentGeneralMethods {
    /**
     * Method that returns all the dependencies of a fragment. That is, not only
     * those dependencies included directly, but also those dependencies found
     * recursively.
     * @param f the fragment from which we want to find the dependencies.
     * @return All the fragments that depend directly or indirectly on f
     */
    public static ArrayList<Fragment> getFullDependenciesOfFragment(Fragment f){
        ArrayList<Fragment> result = new ArrayList<Fragment>();
        addDependencies(result, f);
        return result;
    }
    /**
     * Method to add the fragment dependencies recursively to a given array.
     * @param results the full dependencies
     * @param f fragment to be explored.
     */
    private static void addDependencies(ArrayList<Fragment> results, Fragment f){
        ArrayList<Fragment> aux = f.getListOfIncludedIDs();
        if(aux == null || aux.isEmpty())return;
        Iterator<Fragment> it = aux.iterator();
        while(it.hasNext()){
            Fragment currentDependency = it.next();
            results.add(currentDependency);
            addDependencies(results, currentDependency);
        }
    }
}
