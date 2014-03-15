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
 * Class for stating the constants of the p-plan vocabulary.
 * For more information, see http://purl.org/net/p-plan
 * @author Daniel Garijo
 */
public class PPlan {
    
    public static final String P_PLAN_PREFIX = "http://purl.org/net/p-plan#";
    
    //ontology classes and properties (p-plan)
    public static final String PLAN= P_PLAN_PREFIX+"Plan";
    public static final String STEP= P_PLAN_PREFIX+"Step";
    public static final String MUTLISTEP= P_PLAN_PREFIX+"MultiStep";
    
    public static final String IS_STEP_OF_PLAN= P_PLAN_PREFIX+"isStepOfPlan";
    public static final String IS_PRECEEDED_BY= P_PLAN_PREFIX+"isPreceededBy";
    public static final String IS_DECOMPOSED_AS_PLAN= P_PLAN_PREFIX+"isDecomposedAsPlan";
    
    
    
}
