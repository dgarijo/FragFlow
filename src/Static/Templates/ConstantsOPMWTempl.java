/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Static.Templates;

/**
 *
 * @author Daniel Garijo
 */
public class ConstantsOPMWTempl {
    
    /**
     * If the domain ontology changes, these 2 uris have to be changed,
     * at least if we are processing Wings templates and runs.
     */
    public static final String WINGS_DATA_CATALOG_URI =  "http://www.isi.edu/dc";
    public static final String WINGS_ABS_COMP_CATALOG_URI =  "http://www.isi.edu/ac";
    
    public static final String PREFIX_OPMW =  "http://www.opmw.org/ontology/";
    public static final String WORKFLOW_TEMPLATE = PREFIX_OPMW+"WorkflowTemplate";
    
    public static final String IS_VAR_OF_TEMPL = PREFIX_OPMW+"isVariableOfTemplate";
    public static final String IS_PARAM_OF_TEMPL = PREFIX_OPMW+"isParameterOfTemplate";
    public static final String IS_STEP_OF_TEMPL = PREFIX_OPMW+"isStepOfTemplate";
    
    public static final String USES = PREFIX_OPMW+"uses";
    public static final String IS_GEN_BY = PREFIX_OPMW+"isGeneratedBy";
            
}
