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
package Static.Traces;

import Static.Templates.ConstantsOPMWTempl;


/**
 *
 * @author Daniel Garijo
 */
public class QueriesOPMWTraces {
    
    /**
     * Query for retieving all workflowExecutionAccount URIs
     * @return
     */
    public static String queryWfExecAccount(){
        String query = "SELECT distinct ?acc WHERE{?acc a <"+ConstantsOPMWTraces.WORKFLOW_EXECUTION_ACCOUNT+">.}";                
        return query;
    }
    
    /**
     * Method for retrieving the nodes of a trace. Note that the type will have to be refined
     * for the processes through the hasSepcificComponent relation.
     * @param traceURI
     * @return 
     */
    public static String getWfExecNodes(String traceURI){
        String query = "SELECT distinct ?node ?type WHERE{"
                + "?node <"+ConstantsOPMWTraces.WORKFLOW_ACCOUNT_REL+"> <"+traceURI+">."
                + "?node a ?type}";                
        return query;
    }
    
    /**
     * Method to return the types of each process of a trace
     * @param traceURI
     * @return 
     */
    public static String getTypesOfWfExecProcesses(String traceURI){
        String query = "SELECT distinct ?process ?type WHERE{"
                + "?process <"+ConstantsOPMWTraces.WORKFLOW_ACCOUNT_REL+"> <"+traceURI+">."
                + "?process <"+ConstantsOPMWTraces.HAS_EXECUTABLE_COMPONENT+"> ?comp."
                + "?comp a ?type}";
        return query;
    }
    
    public static String getUsagesOfTrace(String traceURI){
        String query = "SELECT distinct ?process ?artif WHERE{"
                + "?process <"+ConstantsOPMWTraces.USED+"> ?artif."
                + "?process <"+ConstantsOPMWTraces.WORKFLOW_ACCOUNT_REL+"> <"+traceURI+">."
                + "}";                
        return query;
    }
    
    public static String getGenerationsOfTrace(String traceURI){
        String query = "SELECT distinct ?artif ?process WHERE{"
                + "?artif <"+ConstantsOPMWTraces.WAS_GEN_BY+"> ?process."
                + "?artif <"+ConstantsOPMWTraces.WORKFLOW_ACCOUNT_REL+"> <"+traceURI+">."
                + "}";                
        return query;
    }
    
    //from the result of this query we will return just the namespace
    public static String getComponentOntologyURI(String accURI){
        String query = "SELECT distinct ?uri WHERE{"
                + "<"+accURI+"> <"+ConstantsOPMWTraces.CORRESPONDS_TO_TEMPLATE+"> ?template."
                + "?node <"+ConstantsOPMWTempl.IS_STEP_OF_TEMPL+"> ?template."
                + "?node a ?uri."
                + "FILTER regex(?uri,\"library\")}";
        return query;
    }
    
    //we filter the types that belong to the ontology. If we process another
    //system's traces, we will have to adapt the types to that ontology
    public static String getDataOntologyURI(String templateURI){
        String query = "SELECT distinct ?uri WHERE{"
                + "<"+templateURI+"> <"+ConstantsOPMWTraces.CORRESPONDS_TO_TEMPLATE+"> ?template."
                + "?node <"+ConstantsOPMWTempl.IS_VAR_OF_TEMPL+"> ?template."
                + "?node a ?uri."
                + "FILTER regex(?uri,\"ontology.owl#\")}";
        return query;
    }
    
    /**
     * Mthod to retrieve the local component URI of a given account.
     * Note: the returned URI must be filtered and left just with the namespace.
     * @param traceURI
     * @return 
     */
    public static String getLocalComponentOntologyURI(String traceURI){
        String query = "SELECT distinct ?localOntoURI WHERE{"
                + "?process <"+ConstantsOPMWTraces.WORKFLOW_ACCOUNT_REL+"> <"+traceURI+">."
                + "?process <"+ConstantsOPMWTraces.HAS_EXECUTABLE_COMPONENT+"> ?comp."
                + "?comp a ?localOntoURI}";
        return query;
    }
    /**
     * Retrives all execution traces from a given domain
     * @param domain that we want to retrieve. For example TextAnalytics.
     * Note: it is sensitive to capital letters.
     */
    public static String getTracesFromSpecificDomain(String domain){
        String query = "select distinct ?acc where {"+
                        "?acc <http://www.opmw.org/ontology/hasOriginalLogFile> ?file."+
                        "FILTER regex(?file, \"/"+domain+"/\")."+
                        //?acc <http://www.opmw.org/ontology/correspondsToTemplate> ?templ
                        "}";
        return query;
    }
    
}
