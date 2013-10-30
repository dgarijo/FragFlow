/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
