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

package TavernaOPMWWfOperationsAndScripts;

import DataStructures.Fragment;
import DataStructures.GraphNode.GraphNode;
import Static.Vocabularies.OPMWTemplate;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class will filter all the fragments that do not contain relevant 
 * annotations, i.e., those that have just opmw:WorkflowTemplateProcess in their
 * nodes.
 * @author Daniel Garijo
 */
public class TavernaOPMWFragmentFilter {
    
    public static HashMap<String,Fragment> filter(HashMap<String,Fragment> fragmentCollection){
        HashMap<String,Fragment> returnCollection = new HashMap<String, Fragment>();
        //iterate through the old collection to check which nodes to include.
        Iterator<String> itCollection = fragmentCollection.keySet().iterator();
        while(itCollection.hasNext()){
            Fragment currF = fragmentCollection.get(itCollection.next());
            if(hasAnnotatedValue(currF, fragmentCollection)){
                returnCollection.put(currF.getStructureID(), currF);
            }
        }
        return returnCollection;
    }
    
    /**
     * Method similar to the one for workflows with a few exceptions: if a fragment
     * includes another fragment and that fragment has an annotation, then the 
     * judged fragment is ok.
     * @param f
     * @return 
     */
     private static boolean hasAnnotatedValue(Fragment f, HashMap<String,Fragment> fragmentCollection){
         //if the type of the nodes is different from workflow template artifact 
        //and workflow template process, then it is fine
        Iterator<String> nodeIterator = f.getDependencyGraph().getNodes().keySet().iterator();
        while(nodeIterator.hasNext()){
            GraphNode currentNode = f.getDependencyGraph().getNodes().get(nodeIterator.next());
            if(currentNode.getType().startsWith("SUB")){
                if(hasAnnotatedValue(fragmentCollection.get(currentNode.getType()), fragmentCollection)) {
                    return true;
                }
            }
            else if(!currentNode.getType().equals(OPMWTemplate.WORFKLOW_TEMPLATE_PROCESS)&&
                    !currentNode.getType().equals(OPMWTemplate.WORFKLOW_TEMPLATE_ARTIFACT)){
                return true;
            }
        }
        return false;
         
     }
}
