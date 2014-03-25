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
package Factory;

import DataStructures.GraphCollection;

/**
 * Class designed to enable the main methods for creating collections of Graphs.
 * Different repositories may be queried to create graphs to be consumed by 
 * different algorithms, and this is the main class to be extended
 * @author Daniel Garijo
 */
public abstract class GraphCollectionCreator {
    protected GraphCollection collection;
    protected final String repositoryURI;

    
    /**
     * Constructor. The repository URI is the URL/path of the repository to
     * query for results.
     * @param repositoryURI URI of the repository
     */
    public GraphCollectionCreator(String repositoryURI) {
        collection = new GraphCollection();     
        this.repositoryURI = repositoryURI;
    }
    
    /**
     * returns the current graph collection
     * @return the current graph collection
     */
    public GraphCollection getGraphCollection(){
        return collection;
    }
    
    /**
     * Method that given a URI of template/trace in a repository, processes it
     * and creates the apropriate Graph
     * @param URI : ID of the template /run in the repository
     */
    public void transformToGraph(String URI){
        
    }
    
    /**
     * Given a domain, process the traces or templates form it (reduces the
     * search space). A domain may not be needed in certain cases.
     * @param domain domain form which we want to perform the extraction
     */
    public void transformDomainToGraph(String domain){
        
    }
    
}
