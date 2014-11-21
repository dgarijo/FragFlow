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
package LONIWfOperationsAndEvaluationEScience2014;

import DataStructures.Fragment;
import DataStructures.Graph;
import DataStructures.GraphCollection;
import DataStructures.GraphNode.GraphNode;
import Factory.Loni.LoniTemplateAndGroupings2Graph;
import IO.DatasetFilter;
import IO.Exception.FragmentReaderException;
import IO.Formats.SUBDUE.FragmentCollectionReaderSUBDUE;
import Static.GeneralMethods;
import Static.GeneralMethodsFragments;
import Static.TestConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class aims to assess how much overlap is there between fragments, groupings
 * or templates. This is a class developed for assessing the results obtained by 
 * the library. Submitted to the eScience conference.
 * @author Daniel Garijo
 */
public class FragmentAndGroupingOverlapLONI {
    //this should be put into a constants file
    public static final int LIMIT_OVERLAP = 80; //the overlap should be 80 % or higher
    
    //input: Fragment collection, Graph Collection.
    //for each fragment, is it equal to any of the graphs?
    //how many nodes and connections are equal?
    //nodes + connections = percentage.
    
    /*
     * Note: this has been implemented for subdue only (for the moment).
     * For pafi and parsemis will not be necessary to call the included fragments.
     */
    private double compareFragmentToGraph(Fragment f, Graph g){        
        //in order to simpify, we will test just the connections of the substructures separately.
        int graphSize = g.getNumberOfNodes();
        int fragmentSize = f.getSize();
        int totalSize = Math.max(graphSize, fragmentSize);
        HashMap<String, Integer> fragmentNodes = getAllNodesForFragment(f);
        //compare both types!
        //HashMap<String, GraphNode> graphNodes = g.getNodes();
        int overlap = 0;
        Iterator<GraphNode> graphNodesIt = g.getNodes().values().iterator();
        while(graphNodesIt.hasNext()){
            GraphNode currentNode = graphNodesIt.next();
            if(fragmentNodes.containsKey(currentNode.getType())){
                if(fragmentNodes.get(currentNode.getType())>0){
                    overlap++;
                    fragmentNodes.put(currentNode.getType(), fragmentNodes.get(currentNode.getType())-1);
                }
            }
        }
        
        double totalOverlap = overlap*100/totalSize;
//        double graphOverlap = overlap*100/graphSize;
        //you have to calculate the overlap regarding the fragment and the overlap regarding the graph.
        
        //1) compare the overlap in the nodes.
//        if(totalOverlap>LIMIT_OVERLAP && totalOverlap<100){
//            System.out.println("Fragment "+ f.getStructureID()+" overlaps: "+ totalOverlap+" ("+overlap+" out of "+totalSize+" nodes)");
//        }else if(totalOverlap == 100){
//            System.out.println("Fragment "+ f.getStructureID()+" is equal to graph: "+g.getName());
//        }
        
        return totalOverlap;
        //2) compare the overlap in the dependences.
        //TO DO! (This is issue #102)
    }
    
   
    
    private HashMap<String,Integer> getAllNodesForFragment(Fragment f){
        ArrayList<Fragment> fullDependenciesOfFragment = GeneralMethodsFragments.getFullDependenciesOfFragment(f);
        fullDependenciesOfFragment.add(f);//we want to take into consideration the original as well.
        HashMap<String,Integer> allTypesInFragment = new HashMap<String, Integer>();
        //this is for subdue only. other results might not need it.
        Iterator<Fragment> it = fullDependenciesOfFragment.iterator();
        while(it.hasNext()){
            HashMap<String,GraphNode> nodes = it.next().getDependencyGraph().getNodes();
            Iterator<GraphNode> n = nodes.values().iterator();
            while (n.hasNext()){
                String currentType = n.next().getType();
                if(!allTypesInFragment.containsKey(currentType)){
                    allTypesInFragment.put(currentType,1);
                }else{
                    allTypesInFragment.put(currentType, allTypesInFragment.get(currentType)+1);
                }
            }
        }
        return allTypesInFragment;
    }
    
    //do here the method for a fragment and a graph collection
    //return the highest affinity to any of the input graph collection
    private double compareFragmentToGraphCollection(Fragment f, GraphCollection gc){
        Iterator<Graph> it = gc.getGraphs().iterator();
        double maxOverlap = 0;
        String graphName="";
        while(it.hasNext()){
            Graph currentG = it.next();
            double comparison = compareFragmentToGraph(f,currentG);
            if(comparison>maxOverlap){
                maxOverlap = comparison;
                graphName = currentG.getName();
            }
            //maxOverlap = Math.max(maxOverlap, compareFragmentToGraph(f, it.next()));
        }
        System.out.println("Fragment "+f.getStructureID()+" has max overlapping: "+maxOverlap+" with graph:" +graphName);
        return maxOverlap;
    }
    
    //support stands for the min frequency of the fragment
    public void printStatisticsOnFragmentOverlap(ArrayList<Fragment> fc, GraphCollection gc, int support){
        Iterator<Fragment> it = fc.iterator();
        double maxOverlap = 0;
        int overlappingFragments = 0;
        int foundGroupings = 0;
        int multiStepFragmens = 0;
        while(it.hasNext()){
            Fragment fr = it.next();
            if(fr.isMultiStepFragment() && fr.getNumberOfOccurrences()>=support){
                multiStepFragmens++;
                maxOverlap = compareFragmentToGraphCollection(fr, gc);
                if(maxOverlap>LIMIT_OVERLAP){
                    overlappingFragments++;
                    if(maxOverlap==100){
                        foundGroupings++;
                    }
                }
            }
        }
        System.out.println("A total of "+foundGroupings+ " fragments are also groupings or templates");
        System.out.println("A total of "+overlappingFragments+ " fragments out "+multiStepFragmens+" overlap at least a "+LIMIT_OVERLAP+"% with a grouping or template. Only multistep fragments considered");
    }
    
    //alternative with no support: for those for which the support is already known
    public void printStatisticsOnFragmentOverlap(ArrayList<Fragment> fc, GraphCollection gc){
        Iterator<Fragment> it = fc.iterator();
        double maxOverlap = 0;
        int overlappingFragments = 0;
        int foundGroupings = 0;
        int multiStepFragmens = 0;
        while(it.hasNext()){
            Fragment fr = it.next();
            if(fr.isMultiStepFragment() ){
                multiStepFragmens++;
                maxOverlap = compareFragmentToGraphCollection(fr, gc);
                if(maxOverlap>LIMIT_OVERLAP){
                    overlappingFragments++;
                    if(maxOverlap==100){
                        foundGroupings++;
                    }
                }
            }
        }
        System.out.println("A total of "+foundGroupings+ " fragments are also groupings or templates");
        System.out.println("A total of "+overlappingFragments+ " fragments out "+multiStepFragmens+" overlap at least a "+LIMIT_OVERLAP+"% with a grouping or template. Only multistep fragments considered");
    }
    
    
    public static void main(String[] args) throws FragmentReaderException{
        //load a fragment collection
        //TESTS
//        String file = TestConstants.SUBDUETestResultFolder+"testOverlap";
//        String ocFile = TestConstants.SUBDUETestResultFolder+"testOverlap_occurrences";
//        FragmentCollectionReaderSUBDUE f = new FragmentCollectionReaderSUBDUE(file, ocFile);
//        LoniTemplateAndGroupings2Graph test = new LoniTemplateAndGroupings2Graph("LONI_dataset\\datasetBoris\\");
//        test.transformToGraph("MDT_v8.pipe");
//        GraphCollection endCollection = DatasetFilter.removeDuplicates(test.getGraphCollection());
//        FragmentAndGroupingOverlap fa = new FragmentAndGroupingOverlap();
//        fa.printStatisticsOnFragmentOverlap(f.getFragmentCatalogAsAnArrayList(), endCollection);
        //END TESTS
        String fragmentFile = TestConstants.SUBDUEResultFolder+"eScienceResults(LONI)\\";
        String occurrencesFile = TestConstants.SUBDUEResultFolder+"eScienceResults(LONI)\\";
        String repositoryFolder;
        FragmentCollectionReaderSUBDUE fc;
        GraphCollection endCollection;
        File f;
        //eScience 2014 evaluation.
        //Corpus1 (Boris)
//        repositoryFolder = "C:\\Users\\Monen\\Desktop\\LONIDatasets\\WorkflowBoris\\Pipeline123\\";
//        fc = new FragmentCollectionReaderSUBDUE(fragmentFile+"corpus1MDL", occurrencesFile+"corpus1MDL_occurrences");
////        fc = new FragmentCollectionReaderSUBDUE(fragmentFile+"corpus1SIZE", occurrencesFile+"corpus1SIZE_occurrences");
////        ArrayList<Fragment> fragmentCatalogAsAnArrayList = fc.getFragmentCatalogAsAnArrayList();
//        LoniTemplateAndGroupings2Graph test = new LoniTemplateAndGroupings2Graph(repositoryFolder);
//        f = new File(repositoryFolder);
//        if(f.isDirectory()){
//            File[] files = f.listFiles();
//            for (File file : files) {
//                test.transformToGraph(file.getName());
//            }
//        }
//        endCollection = DatasetFilter.removeDuplicates(test.getGraphCollection());
//        System.out.println("Total size of collection with groupings "+endCollection.getGraphs().size());
//        FragmentAndGroupingOverlapLONI fa = new FragmentAndGroupingOverlapLONI();
//        //the support corresponds to: minimum (2), 2% (), 5% (23) and 10% (47)
//        fa.printStatisticsOnFragmentOverlap(fc.getFragmentCatalogAsAnArrayList(), endCollection,2);
//        fa.printStatisticsOnFragmentOverlap(fc.getFragmentCatalogAsAnArrayList(), endCollection,9);
//        fa.printStatisticsOnFragmentOverlap(fc.getFragmentCatalogAsAnArrayList(), endCollection,23);
//        fa.printStatisticsOnFragmentOverlap(fc.getFragmentCatalogAsAnArrayList(), endCollection,47);
        
        //Corpus2 (Ivo)
        repositoryFolder = "C:\\Users\\Monen\\Desktop\\LONIDatasets\\WorkflowIVO\\all\\";
//        fc = new FragmentCollectionReaderSUBDUE(fragmentFile+"corpus2MDL", occurrencesFile+"corpus2MDL_occurrences");
        fc = new FragmentCollectionReaderSUBDUE(fragmentFile+"corpus2SIZE", occurrencesFile+"corpus2SIZE_occurrences");
        LoniTemplateAndGroupings2Graph test = new LoniTemplateAndGroupings2Graph(repositoryFolder);
        f = new File(repositoryFolder);
        if(f.isDirectory()){
            File[] files = f.listFiles();
            for (File file : files) {
                test.transformToGraph(file.getName());
            }
        }
        endCollection = DatasetFilter.removeDuplicates(test.getGraphCollection());
        System.out.println("Total size of collection with groupings "+endCollection.getGraphs().size());
//        FragmentAndGroupingOverlapLONI fa = new FragmentAndGroupingOverlapLONI();
//        fa.printStatisticsOnFragmentOverlap(fc.getFragmentCatalogAsAnArrayList(), endCollection,2);
//        fa.printStatisticsOnFragmentOverlap(fc.getFragmentCatalogAsAnArrayList(), endCollection,5);
//        fa.printStatisticsOnFragmentOverlap(fc.getFragmentCatalogAsAnArrayList(), endCollection,10);
        
        //Corpus3 (Monthly)
        //to do when I get the last evaluations.
//        repositoryFolder = "C:\\Users\\Monen\\Desktop\\LONIDatasets\\dataset3months(Zhizhong)\\2014-01\\";
////        fc = new FragmentCollectionReaderSUBDUE(fragmentFile+"corpus2MDL", occurrencesFile+"corpus2MDL_occurrences");
//        fc = new FragmentCollectionReaderSUBDUE(fragmentFile+"corpus3MDL", occurrencesFile+"corpus3MDL_occurrences");
//        LoniTemplateAndGroupings2Graph test = new LoniTemplateAndGroupings2Graph(repositoryFolder);
//        f = new File(repositoryFolder);
//        if(f.isDirectory()){
//            File[] files = f.listFiles();
//            for (File file : files) {
//                test.transformToGraph(file.getName());
//            }
//        }
//        endCollection = DatasetFilter.removeDuplicates(test.getGraphCollection());
//        System.out.println("Total size of collection with groupings "+endCollection.getGraphs().size());
//        FragmentAndGroupingOverlapLONI fa = new FragmentAndGroupingOverlapLONI();
//        fa.printStatisticsOnFragmentOverlap(fc.getFragmentCatalogAsAnArrayList(), endCollection,2);
//        fa.printStatisticsOnFragmentOverlap(fc.getFragmentCatalogAsAnArrayList(), endCollection,7);
//        fa.printStatisticsOnFragmentOverlap(fc.getFragmentCatalogAsAnArrayList(), endCollection,18);
//        fa.printStatisticsOnFragmentOverlap(fc.getFragmentCatalogAsAnArrayList(), endCollection,35);
    }
    
}
