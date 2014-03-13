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

import java.io.File;

/**
 * General constants used through the project.
 * @author Daniel Garijo
 */
public class GeneralConstants {     
    
    //unify traces and templates under the same nomenclature.
    public static final String USAGE_DEPENDENCY = "use";
    public static final String GENERATION_DEPENDENCY = "genBy";
    public static final String INFORM_DEPENDENCY = "informBy";
    public static final int INFORM_EGDE = 1; //needed for algorithms admitting numbered edges.
    public static final String PROP_FILE = "config"+File.separator+"config.properties";
    public static final String PREFIX_FOR_RDF_GENERATION = "http://vocab.linkeddata.es/resource/";
}
