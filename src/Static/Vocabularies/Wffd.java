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
 * This class contains the constants for referring to the concepts of the Workflow
 * fragment description ontology (wf-fd). For more details, see:
 * http://purl.org/net/wf-fd 
 * @author Daniel Garijo
 */
public class Wffd {
    //prefixes
    public static final String RDFS_SCHEMA_PREFIX = "http://www.w3.org/2000/01/rdf-schema#";
    
    public static final String WFFD_PREFIX = "http://purl.org/net/wf-fd#";
    
    //ontology classes and properties(wf-fd)
    public static final String TIED_RESULT= WFFD_PREFIX+"TiedResultWorkflowFragment";
    public static final String DETECTED_RESULT= WFFD_PREFIX+"DetectedResultWorkflowFragment";
    public static final String WORKFLOW_FRAGMENT= WFFD_PREFIX+"WorkflowFragment";
    
    public static final String PART_OF_WORKFLOW_FRAGMENT= WFFD_PREFIX+"partOfWorkflowFragment";
    public static final String FOUND_AS= WFFD_PREFIX+"foundAs";
    public static final String FOUND_IN= WFFD_PREFIX+"foundIn";
    public static final String DETECTED_BY_ALGORITHM= WFFD_PREFIX+"detectedByAlgorithm";
}
