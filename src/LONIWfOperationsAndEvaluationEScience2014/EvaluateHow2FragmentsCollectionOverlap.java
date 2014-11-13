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
import DataStructures.GraphNode.GraphNode;
import IO.Exception.FragmentReaderException;
import IO.Formats.SUBDUE.FragmentCollectionReaderSUBDUE;
import Static.GeneralMethodsFragments;
import Static.TestConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class/script to compare two collections of workflows (in this case, in SUBDUE format).
 * This class could be generalized and 
 * @author Daniel Garijo
 */
public class EvaluateHow2FragmentsCollectionOverlap {
    double LIMIT_OVERLAP = 80;
    //Method repeated from FragmentAndGroupingOverlap Class (adapted for fragments)
    private double compareFragmentToFragment(Fragment f, Fragment g){        
        //in order to simpify, we will test just the connections of the substructures separately.
        int graphSize = g.getSize();
        int fragmentSize = f.getSize();
        int totalSize = Math.max(graphSize, fragmentSize);
        HashMap<String, Integer> fragmentNodes = getAllNodesForFragment(f);
        //compare both types!
        //HashMap<String, GraphNode> graphNodes = g.getNodes();
        int overlap = 0;
        Iterator<GraphNode> graphNodesIt = g.getDependencyGraph().getNodes().values().iterator();
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
        return totalOverlap;
    }
    //method repeated from FragmentAndGroupingOverlap Class
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
    //method repeated from FragmentAndGroupingOverlap Class
    private double compareFragmentToFragmentCollection(Fragment f, ArrayList<Fragment> gc, int support){
        Iterator<Fragment> it = gc.iterator();
        double maxOverlap = 0;
        String graphName="";
        while(it.hasNext()){
            Fragment currentG = it.next();
            double comparison = compareFragmentToFragment(f,currentG);
            if(comparison>maxOverlap && f.getNumberOfOccurrences()>support && currentG.getNumberOfOccurrences()>support){
                maxOverlap = comparison;
                graphName = currentG.getStructureID();
            }
            //maxOverlap = Math.max(maxOverlap, compareFragmentToGraph(f, it.next()));
        }
//        System.out.println("Fragment "+f.getStructureID()+" has max overlapping: "+maxOverlap+" with graph:" +graphName);
        return maxOverlap;
    }
    //method repeated from FragmentAndGroupingOverlap Class
    public void printStatisticsOnFragmentOverlap(ArrayList<Fragment> fc, ArrayList<Fragment> gc, int support){
        Iterator<Fragment> it = fc.iterator();
        double maxOverlap = 0;
        int overlappingFragments = 0;
        int foundFragments = 0;
        int multiStepFragmens = 0;
        while(it.hasNext()){
            Fragment fr = it.next();
            if(fr.isMultiStepFragment()){
                multiStepFragmens++;
                maxOverlap = compareFragmentToFragmentCollection(fr, gc, support);
                if(maxOverlap>LIMIT_OVERLAP){
                    overlappingFragments++;
                    if(maxOverlap==100){
                        foundFragments++;
                    }
                }
            }
        }
        System.out.println("A total of "+foundFragments+ " fragments overlap completely");
        System.out.println("A total of "+overlappingFragments+ " fragments out of "+multiStepFragmens+" overlap at least a "+LIMIT_OVERLAP+"% with another fragment.");
    }
    
    //to do method that takes 3 collections and compares them
    private void printStatisticsThreeFragmentDatasets(ArrayList<Fragment> fc, ArrayList<Fragment> sc, ArrayList<Fragment> tc){
        Iterator<Fragment> itFirscC = fc.iterator();
        int numberOfEqualFragments = 0;
        while(itFirscC.hasNext()){
            Fragment currentF = itFirscC.next();
            if(currentF.isMultiStepFragment()){
                //compare a fragment of the first collection with the second collection, and get the exact overlap.
                Iterator<Fragment> itSecondC = sc.iterator();
                boolean found = false;
                while(itSecondC.hasNext() && !found){
                    Fragment currentS = itSecondC.next();
                    double comparison = compareFragmentToFragment(currentF,currentS);
                    if(comparison>=100){//exact
                       found=true;
//                       System.out.println(currentF.getStructureID()+" and "+currentS.getStructureID());
                    }
                }
                //if found, can that fragment be found in the third collection? If yes, then select.
                if(found){
                    Iterator<Fragment> itThirdC = tc.iterator();
                    while(itThirdC.hasNext()){
                        Fragment currentT = itThirdC.next();
                        double comparison = compareFragmentToFragment(currentF,currentT);
                        if(comparison>=100){//exact
                           numberOfEqualFragments++;
                           System.out.println("Fragment equal found! "+currentF.getStructureID());
                        }
                    }
                }
            }
        }
        System.out.println("The number of multistep fragments shared among the collections: "+numberOfEqualFragments);
    }
    
    public static void main(String[] args) throws FragmentReaderException{
        //load the three corpora as fragment collections.
        String file = TestConstants.SUBDUEResultFolder +"eScienceResults(LONI)\\";
        FragmentCollectionReaderSUBDUE corpus1 = new FragmentCollectionReaderSUBDUE(file+"corpus1SIZE", file+"corpus1SIZE_occurrences");
        FragmentCollectionReaderSUBDUE corpus2 = new FragmentCollectionReaderSUBDUE(file+"corpus2SIZE", file+"corpus2SIZE_occurrences");
        FragmentCollectionReaderSUBDUE corpus3 = new FragmentCollectionReaderSUBDUE(file+"corpus3SIZE", file+"corpus3SIZE_occurrences");
        EvaluateHow2FragmentsCollectionOverlap test = new EvaluateHow2FragmentsCollectionOverlap();
        //comparison between corpus 1 and corpus 2
        System.out.println("Corpus 1 and 2 (Size)");
        test.printStatisticsOnFragmentOverlap(corpus1.getFragmentCatalogAsAnArrayList(),corpus2.getFragmentCatalogAsAnArrayList(),1);
        System.out.println("Corpus 1 and 3 (Size)");
        //comparison between corpus 1 and corpus 3
        test.printStatisticsOnFragmentOverlap(corpus1.getFragmentCatalogAsAnArrayList(),corpus3.getFragmentCatalogAsAnArrayList(),1);
        System.out.println("Corpus 2 and 3 (Size)");
        //comparison between corpus 2 and corpus 3
        test.printStatisticsOnFragmentOverlap(corpus2.getFragmentCatalogAsAnArrayList(),corpus3.getFragmentCatalogAsAnArrayList(),1);
        
        corpus1 = new FragmentCollectionReaderSUBDUE(file+"corpus1MDL", file+"corpus1MDL_occurrences");
        corpus2 = new FragmentCollectionReaderSUBDUE(file+"corpus2MDL", file+"corpus2MDL_occurrences");
        corpus3 = new FragmentCollectionReaderSUBDUE(file+"corpus3MDL", file+"corpus3MDL_occurrences");
        System.out.println("Corpus 1 and 2 (MDL)");
        test.printStatisticsOnFragmentOverlap(corpus1.getFragmentCatalogAsAnArrayList(),corpus2.getFragmentCatalogAsAnArrayList(),1);
        System.out.println("Corpus 1 and 3 (MDL)");
        //comparison between corpus 1 and corpus 3
        test.printStatisticsOnFragmentOverlap(corpus1.getFragmentCatalogAsAnArrayList(),corpus3.getFragmentCatalogAsAnArrayList(),1);
        System.out.println("Corpus 2 and 3 (MDL)");
        //comparison between corpus 2 and corpus 3
        test.printStatisticsOnFragmentOverlap(corpus2.getFragmentCatalogAsAnArrayList(),corpus3.getFragmentCatalogAsAnArrayList(),1);
        
        System.out.println("Increasing the minimum frequency of fragment (just size eval)");
        int freq = 2;
        int maxFreq = 30;
        corpus1 = new FragmentCollectionReaderSUBDUE(file+"corpus1Size", file+"corpus1Size_occurrences");
        corpus2 = new FragmentCollectionReaderSUBDUE(file+"corpus2Size", file+"corpus2Size_occurrences");
        corpus3 = new FragmentCollectionReaderSUBDUE(file+"corpus3Size", file+"corpus3Size_occurrences");
        while (freq<maxFreq){
            System.out.println("freq > "+freq);
            System.out.println("Corpus 1 and 2 (Size)");
            test.printStatisticsOnFragmentOverlap(corpus1.getFragmentCatalogAsAnArrayList(),corpus2.getFragmentCatalogAsAnArrayList(),freq);
            System.out.println("Corpus 1 and 3 (Size)");
            //comparison between corpus 1 and corpus 3
            test.printStatisticsOnFragmentOverlap(corpus1.getFragmentCatalogAsAnArrayList(),corpus3.getFragmentCatalogAsAnArrayList(),freq);
            System.out.println("Corpus 2 and 3 (Size)");
            //comparison between corpus 2 and corpus 3
            test.printStatisticsOnFragmentOverlap(corpus2.getFragmentCatalogAsAnArrayList(),corpus3.getFragmentCatalogAsAnArrayList(),freq);
            freq+=2;
        }
        
        //this shows the common fragments among the three corpora
          test.printStatisticsThreeFragmentDatasets(corpus1.getFragmentCatalogAsAnArrayList(), corpus2.getFragmentCatalogAsAnArrayList(), corpus3.getFragmentCatalogAsAnArrayList());
//        ahora comprobar si hay un overlap entre los 3 datasets y si los fragments que tienen overlap son tb los de mayor frecuencia.
    }
    
}
