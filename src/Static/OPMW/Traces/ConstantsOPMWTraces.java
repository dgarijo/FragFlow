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

package Static.OPMW.Traces;

/**
 * General constants to tackle OPMW traces.
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
    
    public static final String WORKFLOW_OPMO_ACCOUNT = PREFIX_OPMO+"account";
    
    public static final String USED = PREFIX_OPMV+"used";
    public static final String WAS_GEN_BY = PREFIX_OPMV+"wasGeneratedBy";
    public static final String HAS_EXECUTABLE_COMPONENT = PREFIX_OPMW+"hasExecutableComponent";
    public static final String CORRESPONDS_TO_TEMPLATE = PREFIX_OPMW+"correspondsToTemplate";
            
}
