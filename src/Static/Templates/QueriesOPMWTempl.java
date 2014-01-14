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
package Static.Templates;

/**
 *
 * @author Daniel Garijo
 */
public class QueriesOPMWTempl {
    
    /**
     * Query for retieving all template URIs
     * @return
     */
    public static String queryNameWfTemplate(){
        String query = "SELECT distinct ?templateWHERE{?template a <"+ConstantsOPMWTempl.WORKFLOW_TEMPLATE+">.";                
        return query;
    }
    
    public static String getWTArtifactsAndTypesOfTemplate(String templateURI){
        String query = "SELECT distinct ?node ?type WHERE{"
                + "{?node <"+ConstantsOPMWTempl.IS_VAR_OF_TEMPL+"> <"+templateURI+">."
                + "?node a ?type}"
                + "UNION"
                + "{?node <"+ConstantsOPMWTempl.IS_STEP_OF_TEMPL+"> <"+templateURI+">."
                + "?node a ?type}"
                + "UNION"
                + "{?node <"+ConstantsOPMWTempl.IS_PARAM_OF_TEMPL+"> <"+templateURI+">."
                + "?node a ?type}"
                + "}";                
        return query;
    }
    public static String getUsagesOfTemplate(String templateURI){
        String query = "SELECT distinct ?process ?artif WHERE{"
                + "?process <"+ConstantsOPMWTempl.USES+"> ?artif."
                + "?process <"+ConstantsOPMWTempl.IS_STEP_OF_TEMPL+"> <"+templateURI+">."
                + "}";                
        return query;
    }
    
    public static String getGenerationsOfTemplate(String templateURI){
        String query = "SELECT distinct ?artif ?process WHERE{"
                + "?artif <"+ConstantsOPMWTempl.IS_GEN_BY+"> ?process."
                + "?artif <"+ConstantsOPMWTempl.IS_VAR_OF_TEMPL+"> <"+templateURI+">."
                + "}";                
        return query;
    }
    
    public static String getTemplatesFromSpecificDomain(String domain){
        String query = "select distinct ?templ where {"+
                        "?templ a <"+ConstantsOPMWTempl.WORKFLOW_TEMPLATE+">."+
                        "?templ <http://www.opmw.org/ontology/hasNativeSystemTemplate> ?file."+
                        "FILTER regex(?file, \"/"+domain+"/\")."+
                        "}";
        return query;
    }
}
