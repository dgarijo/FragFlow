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

package Static.Vocabularies;

/**
 * Constants used in OPMW templates.
 * @author Daniel Garijo
 */
public class OPMWTemplate {
    
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
