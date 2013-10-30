/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Static.Traces;

/**
 *
 * @author Daniel Garijo
 */
public class ConstantsOPMWTraces {
    
    /**
     * If the domain ontology changes, these 2 uris have to be changed,
     * at least if we are processing Wings templates and runs.
     */
    
    public static final String PREFIX_OPMW =  "http://www.opmw.org/ontology/";
    public static final String PREFIX_OPMV =  "http://purl.org/net/opmv/ns#";
    public static final String PREFIX_OPMO =  "http://openprovenance.org/model/opmo#";
    //public static final String PREFIX_PROV =  "http://www.w3.org/ns/prov#";
    
    public static final String WORKFLOW_EXECUTION_ACCOUNT = PREFIX_OPMW+"WorkflowExecutionAccount";
    
    public static final String WORKFLOW_ACCOUNT_REL = PREFIX_OPMO+"account";
    
    public static final String USED = PREFIX_OPMV+"used";
    public static final String WAS_GEN_BY = PREFIX_OPMV+"wasGeneratedBy";
    public static final String HAS_EXECUTABLE_COMPONENT = PREFIX_OPMW+"hasExecutableComponent";
    public static final String CORRESPONDS_TO_TEMPLATE = PREFIX_OPMW+"correspondsToTemplate";
            
}
