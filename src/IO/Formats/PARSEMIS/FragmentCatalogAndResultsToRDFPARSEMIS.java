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
package IO.Formats.PARSEMIS;

import DataStructures.Fragment;
import DataStructures.Graph;
import DataStructures.GraphCollection;
import IO.FragmentCatalogAndResultsToRDF;
import java.util.ArrayList;

/**
 *
 * @author Daniel Garijo
 */
public class FragmentCatalogAndResultsToRDFPARSEMIS extends FragmentCatalogAndResultsToRDF{

    public FragmentCatalogAndResultsToRDFPARSEMIS(String outPath) {
        super(outPath);
    }
    

    @Override
    public void transformFragmentCollectionToRDF(ArrayList<Fragment> catalog) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void transformBindingResultsInTemplateCollection(ArrayList<Fragment> obtainedResults, GraphCollection templates) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void transformBindingResultsOfFragmentCollectionInTemplateToRDF(ArrayList<Fragment> obtainedResults, Graph template) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
