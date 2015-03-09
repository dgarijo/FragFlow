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

import DataStructures.Graph;
import java.util.ArrayList;

/**
 * Class to have an ordered set of sets. They will be ordered by their size,
 * from min to max.
 * @author Daniel Garijo
 */
public class OrderedQueueBySize {
    private final ArrayList<ArrayList<Integer>> queue;
    public OrderedQueueBySize() {
        queue = new ArrayList<ArrayList<Integer>>();
    }
    /**
     * A queue can be initialized with the variables of a graph.
     * @param g 
     */
    public OrderedQueueBySize(Graph g){
        queue = new ArrayList<ArrayList<Integer>>();
        for(int i=1; i<g.getAdjacencyMatrix().length;i++){
            ArrayList<Integer> set = new ArrayList<Integer>();
            set.add(i);
            queue.add(set);
        }
    }
    /**
     * A queue can be initialized with the variables of a graph, restricting to 
     * those variables in the vars parameter.
     * @param vars 
     */
    public OrderedQueueBySize(ArrayList<Integer> vars){
        queue = new ArrayList<ArrayList<Integer>>();
        for (int i: vars){
            ArrayList<Integer> set = new ArrayList<Integer>();
            set.add(i);
            queue.add(set);
        }
    }
    
    public void insert(ArrayList<Integer> newSet){
        this.orderedInsert(newSet, 0, queue.size());
    }
    
    /**
     * Insertion in the queue using a typical divide and conquer approach
     * @param newSet
     * @param begin
     * @param end 
     */
    private void orderedInsert(ArrayList<Integer> newSet, int begin, int end){
        int positionToInsert = begin+((end-begin)/2);
        if(begin>=end){
            queue.add(begin,newSet);
        }else{
            //check the size
            int currPosSize = queue.get(positionToInsert).size();
            if(currPosSize == newSet.size()){
                queue.add(positionToInsert,newSet);
            }else{
                if(currPosSize<newSet.size()){
                    orderedInsert(newSet, positionToInsert+1, end);
                }else{
                    orderedInsert(newSet, begin, positionToInsert-1);
                }
            }
        }
    }

    public ArrayList<ArrayList<Integer>> getQueue() {
        return queue;
    }
    
    
    public ArrayList<Integer> getMinSet(){
        if(queue!=null){
            return queue.get(0);
        }else{
            return null;
        }
    }
    
    /**
     * Method to merge groups in posGroup1 and posGroup2, reinserting the result
     * Both groups will be removed from the queue.
     * @param posGroup1
     * @param posGroup2 
     */
    public void mergeGroups(int posGroup1, int posGroup2){
        ArrayList<Integer> g1 = queue.get(posGroup1);
        ArrayList<Integer> g2 = queue.get(posGroup2);
        ArrayList<Integer> mergedGroup = new ArrayList<Integer>();
        for(Integer i:g1){
            mergedGroup.add(i);
        }
        for(Integer i:g2){
            mergedGroup.add(i);
        }
        insert(mergedGroup);
        queue.remove(g1);
        queue.remove(g2);
    }

//  public static void main(String[] args){
//        OrderedQueueBySize qu = new OrderedQueueBySize();
//        ArrayList<Integer> a1 = new ArrayList<Integer>();
//        a1.add(2);a1.add(3);a1.add(4);
//        ArrayList<Integer> a2 = new ArrayList<Integer>();
//        a2.add(2);a2.add(3);
//        ArrayList<Integer> a3 = new ArrayList<Integer>();
//        a3.add(2);a3.add(3);a3.add(1);a3.add(1);
//        ArrayList<Integer> a4 = new ArrayList<Integer>();
//        a4.add(2);
//        qu.insert(a2);
//        qu.insert(a1);
//        qu.insert(a3);
//        qu.insert(a4);
//        qu.insert(new ArrayList<Integer>());
//        qu.mergeGroups(2, 3);
//        System.out.println("Done");
//    }   
   
}


