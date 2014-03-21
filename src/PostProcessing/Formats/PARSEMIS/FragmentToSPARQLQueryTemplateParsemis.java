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

import DataStructures.Fragment;
import IO.Exception.FragmentReaderException;
import PostProcessing.Formats.PAFI.FragmentToSPARQLQueryTemplatePAFI;
import PostProcessing.FragmentToSPARQLQuery;

/**
 * Fragment to SPARQL query. As it is the same as in PAFI, we return an instance
 * of that class.
 * @author Daniel Garijo
 */
public class FragmentToSPARQLQueryTemplateParsemis extends FragmentToSPARQLQuery {

    @Override
    public String createQueryFromFragment(Fragment f, String structureURI) {
        return new FragmentToSPARQLQueryTemplatePAFI().createQueryFromFragment(f, structureURI);
    }
    
//    public static void main(String[] args) throws FragmentReaderException{
//        CreateStatisticsFromResultsPARSEMIS c = new CreateStatisticsFromResultsPARSEMIS("TextAnalytics", true, false, "PARSEMIS_TOOL\\results\\run11-03-2014.txt");
//        Fragment f = c.getFilteredMultiStepFragments().get(10);
//        System.out.println(new FragmentToSPARQLQueryTemplateParsemis().createQueryFromFragment(f, "http://www.opmw.org/export/resource/WorkflowTemplate/DOCUMENTCLASSIFICATION_MULTI"));
//    }
}
