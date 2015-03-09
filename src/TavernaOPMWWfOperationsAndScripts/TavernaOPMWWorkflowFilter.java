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

import DataStructures.Graph;
import DataStructures.GraphCollection;
import DataStructures.GraphNode.GraphNode;
import Static.Vocabularies.OPMWTemplate;
import java.util.Iterator;

/**
 * This class will filter the workflows that don't have any useful annotations
 * (all are wfTemplateProcesses).
 * @author Daniel Garijo
 */
public class TavernaOPMWWorkflowFilter {

    public static GraphCollection filter(GraphCollection inputCollection){
        GraphCollection returnValue = new GraphCollection();
        System.out.println("Initial size of the dataset: "+ inputCollection.getNumberOfSubGraphs());
        //if the current workflow doesn't have dependencies besides workflow template process, remove.
        Iterator<Graph> it = inputCollection.getGraphs().iterator();
        while(it.hasNext()){
            Graph currg = it.next();
            if(hasAnnotatedValue(currg)){
                returnValue.addSubGraph(currg);
            }
            
        }
        System.out.println("Final size of the dataset: "+ returnValue.getNumberOfSubGraphs());
        return returnValue;
    }
    
    private static boolean hasAnnotatedValue(Graph g){
        //if the type of the nodes is different from workflow template artifact 
        //and workflow template process, then it is fine
        Iterator<String> nodeIterator = g.getNodes().keySet().iterator();
        while(nodeIterator.hasNext()){
            GraphNode currentNode = g.getNodes().get(nodeIterator.next());
            if(!currentNode.getType().equals(OPMWTemplate.WORFKLOW_TEMPLATE_PROCESS)&&
                    !currentNode.getType().equals(OPMWTemplate.WORFKLOW_TEMPLATE_ARTIFACT)){
                return true;
            }
        }
        return false;
    }
}
