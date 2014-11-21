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
package Static.Query;

import Static.Vocabularies.OPMWTemplate;

/**
 * Queries used to retrieve the contents of a repository modeled with OPMW.
 * @author Daniel Garijo
 */
public class QueriesOPMWTempl {
    
    /**
     * Query for retieving all template URIs
     * @return
     */
    public static String queryNameWfTemplate(){
        String query = "SELECT distinct ?templateWHERE{?template a <"+OPMWTemplate.WORKFLOW_TEMPLATE+">.";                
        return query;
    }
    
    /**
     * This query is for those workflows in a file specified in rdf/xml.
     * We want to retrieve all the data variables and steps.
     * @return 
     */
    public static String getAllWTArtifactsAndTypesOfTemplate(){
        String query = "SELECT distinct ?node ?type WHERE{"
                + "{?node a <"+OPMWTemplate.DATA_VARIABLE+">."
                + "?node a ?type}"
                + "UNION"
                + "{?node a <"+OPMWTemplate.WORFKLOW_TEMPLATE_PROCESS+">."
                + "?node a ?type}"
                + "}";                
        return query;
    }
    public static String getWTArtifactsAndTypesOfTemplate(String templateURI){
        String query = "SELECT distinct ?node ?type WHERE{"
                + "{?node <"+OPMWTemplate.IS_VAR_OF_TEMPL+"> <"+templateURI+">."
                + "?node a ?type}"
                + "UNION"
                + "{?node <"+OPMWTemplate.IS_STEP_OF_TEMPL+"> <"+templateURI+">."
                + "?node a ?type}"
                + "UNION"
                + "{?node <"+OPMWTemplate.IS_PARAM_OF_TEMPL+"> <"+templateURI+">."
                + "?node a ?type}"
                + "}";                
        return query;
    }
    
    /**
     * Retrieves all usages from a template, without the "is step from template"
     * relationship. Needed when loading single templates from files.
     * @return 
     */
    public static String getAllUsagesOfTemplate(){
        String query = "SELECT distinct ?process ?artif WHERE{"
                + "?process <"+OPMWTemplate.USES+"> ?artif."
                + "}";                
        return query;
    }
    public static String getUsagesOfTemplate(String templateURI){
        String query = "SELECT distinct ?process ?artif WHERE{"
                + "?process <"+OPMWTemplate.USES+"> ?artif."
                + "?process <"+OPMWTemplate.IS_STEP_OF_TEMPL+"> <"+templateURI+">."
                + "}";                
        return query;
    }
    
    /**
     * Retrieves all usages from a template, without the "is step from template"
     * relationship. Needed when loading single templates from files.
     * @return 
     */
    public static String getAllGenerationsOfTemplate(){
        String query = "SELECT distinct ?artif ?process WHERE{"
                + "?artif <"+OPMWTemplate.IS_GEN_BY+"> ?process."
                + "}";                
        return query;
    }
    
    public static String getGenerationsOfTemplate(String templateURI){
        String query = "SELECT distinct ?artif ?process WHERE{"
                + "?artif <"+OPMWTemplate.IS_GEN_BY+"> ?process."
                + "?artif <"+OPMWTemplate.IS_VAR_OF_TEMPL+"> <"+templateURI+">."
                + "}";                
        return query;
    }
    
    public static String getTemplatesFromSpecificDomain(String domain){
        String query = "select distinct ?templ where {"+
                        "?templ a <"+OPMWTemplate.WORKFLOW_TEMPLATE+">."+
                        "?templ <http://www.opmw.org/ontology/hasNativeSystemTemplate> ?file."+
                        "FILTER regex(?file, \"/"+domain+"/\")."+
                        "}";
        return query;
    }
}
