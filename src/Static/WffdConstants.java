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
package Static;

/**
 * This class contains the constants for referring to the concepts of the Workflow
 * fragment description ontology (wf-fd). For more details, see:
 * http://purl.org/net/wf-fd 
 * @author Daniel Garijo
 */
public class WffdConstants {
    //prefixes
    public static final String RDFS_SCHEMA_PREFIX = "http://www.w3.org/2000/01/rdf-schema#";
    public static final String P_PLAN_PREFIX = "http://purl.org/net/p-plan#";
    public static final String WFFD_PREFIX = "http://purl.org/net/wf-fd#";
    public static final String DCTERMS_PREFIX = "http://purl.org/dc/terms/";
    
    public static final String TITLE = DCTERMS_PREFIX+"title";
    public static final String CREATED = DCTERMS_PREFIX +"created";
    public static final String IS_PART_OF = DCTERMS_PREFIX +"isPartOf";
    
    //ontology classes and properties (p-plan)
    public static final String PLAN= P_PLAN_PREFIX+"Plan";
    public static final String STEP= P_PLAN_PREFIX+"Step";
    
    public static final String IS_STEP_OF_PLAN= P_PLAN_PREFIX+"isStepOfPlan";
    public static final String IS_PRECEEDED_BY= P_PLAN_PREFIX+"isPreceededBy";
    
    //ontology classes and properties(wf-fd)
    public static final String TIED_RESULT= WFFD_PREFIX+"TiedResultWorkflowFragment";
    public static final String DETECTED_RESULT= WFFD_PREFIX+"DetectedResultWorkflowFragment";
    public static final String WORKFLOW_FRAGMENT= WFFD_PREFIX+"WorkflowFragment";
    
    public static final String OVERLAPS_WITH= WFFD_PREFIX+"overlapsWithWorkflowFragment";
    public static final String FOUND_AS= WFFD_PREFIX+"foundAs";
    
    //properties to review
    public static final String PART_OF_FRAGMENT= WFFD_PREFIX+"partOfFragment";
    public static final String CONNECTED_TO= WFFD_PREFIX+"isConnectedTo";
}
