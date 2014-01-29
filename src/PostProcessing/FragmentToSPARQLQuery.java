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
package PostProcessing;

import DataStructures.Fragment;

/**
 * Abstract class to declare the main common operations used to validate a given
 * fragment. Since different fragments from different systems might be validated
 * differently, multiple extension of this class are expected.
 * @author Daniel Garijo
 */
public abstract class FragmentToSPARQLQuery {
    /**
     * Given a fragment and a candidate URI of a structure, this method creates
     * a query to see if the fragment f appears in the structure structureURI
     * @param f fragment we want to check
     * @param structureURI structure URI where we want to see if f was found.
     * @return query to check if f can be found in sctructureURI
     */
    public abstract String createQueryFromFragment(Fragment f, String structureURI);
}
