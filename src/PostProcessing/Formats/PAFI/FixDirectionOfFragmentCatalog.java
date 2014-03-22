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
package PostProcessing.Formats.PAFI;

import DataStructures.Fragment;
import DataStructures.GraphCollection;
import Factory.Inference.CreateAbstractResource;
import Factory.Inference.CreateHashMapForInference;
import Factory.OPMW.OPMWTemplate2Graph;
import IO.Exception.FragmentReaderException;
import IO.Formats.OPMW.Graph2OPMWRDFModel;
import Static.Configuration;
import Static.GeneralConstants;
import Static.GeneralMethods;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class that receives a catalog and fix the direction of the edges.
 * Note that this is produced for the PAFI algorithm, where the edges of the 
 * fragment are undirected. The class operates over a catalog of fragments that
 * has been previously filtered.
 * @author Daniel Garijo
 */
public class FixDirectionOfFragmentCatalog {
    
    /**
     * Given a Fragment Catalog, this method fixed the directionality of the 
     * fragments. Note that a copy of the catalog is NOT returned. The input catalog
     * is modified.
     * @return 
     */
    public static ArrayList<Fragment> fixDirectionOfCatalog(String pathFileWrittenCollection,
            ArrayList<Fragment> filteredCatalog, HashMap<String, ArrayList<String>> occurrencesOfFragments, 
            boolean isAbstract) 
            throws FragmentReaderException{
        HashMap<String,String> numberOfUriAndURI = new HashMap<String, String>();
        //read from the file the id and name of the structure.
        FileInputStream fstream =null;
        DataInputStream in = null;
        FragmentToSPARQLQueryTemplatePAFI queryGenerator = new FragmentToSPARQLQueryTemplatePAFI();
        try{
            fstream = new FileInputStream(pathFileWrittenCollection);            
            in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            int currentStructure = 0;
            while ((strLine = br.readLine()) != null)   {
                if(!strLine.startsWith("#")){                    
                    if(strLine.startsWith("t")){
                        String[] splitLine = strLine.split(" # ");
                        String uri = splitLine[1];//position 0 is "t"
                        numberOfUriAndURI.put(""+currentStructure, uri);
                        currentStructure++;
                    }
                }
            }
        String taxonomyFilePath = "src\\TestFiles\\multiDomainOnto.owl"; //we assume the file has already been created.
        OntModel oRep = null;
        InputStream inRep = null;
        HashMap replacements = null;
        if(isAbstract){
            oRep = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
            inRep = FileManager.get().open(taxonomyFilePath);
            oRep.read(inRep, null);
            replacements = CreateHashMapForInference.createReplacementHashMap(oRep);
        }
        Iterator<Fragment> itFragments = filteredCatalog.iterator();
        while(itFragments.hasNext()){
            Fragment currF = itFragments.next();
//            System.out.println("Fragment "+currF.getStructureID());
            String fragmentFoundIn = occurrencesOfFragments.get(currF.getStructureID()).get(0); 
            //we just query the first structure where it appears (no need for more for fixing the direction of the fragments)
            OPMWTemplate2Graph downloadedTemplate = new OPMWTemplate2Graph("http://wind.isi.edu:8890/sparql");    
            downloadedTemplate.transformToGraph(numberOfUriAndURI.get(fragmentFoundIn));
            OntModel o2;
            if(isAbstract){
                GraphCollection gAux  = CreateAbstractResource.createAbstractCollection(downloadedTemplate.getGraphCollection(), replacements);
                o2 = Graph2OPMWRDFModel.graph2OPMWTemplate(gAux.getGraphs().get(0));   
            }
            else{
                o2 = Graph2OPMWRDFModel.graph2OPMWTemplate(downloadedTemplate.getGraphCollection().getGraphs().get(0));   
            }
            //we download the template because the query performs quicker that way.
            String currentQuery = queryGenerator.createQueryForDirectionalityFromFragment(currF, numberOfUriAndURI.get(fragmentFoundIn));
            ResultSet rs = GeneralMethods.queryLocalRepository(o2, currentQuery);
            while(rs.hasNext()){
                QuerySolution qs = rs.next();//there is only one solution, but just in case
                Iterator<String> varNamesIterator = qs.varNames();
                while(varNamesIterator.hasNext()){
                    String currentVar = varNamesIterator.next();
                    if(qs.get(currentVar)!=null){
//                        System.out.println(currentVar);
                        String[][] fragmentDepMatrix = currF.getDependencyGraph().getAdjacencyMatrix();
                        //fix the adjacency matrix of each fragment here
                        currentVar = currentVar.replace("dep_", "");//we remove the prefix
                        String[] aux = currentVar.split("_");
                        int row = Integer.parseInt(aux[0]);
                        int column = Integer.parseInt(aux[1]);
                        fragmentDepMatrix[row][column] = GeneralConstants.INFORM_DEPENDENCY;
                        fragmentDepMatrix[column][row] = null;
                    }
                }
            }
        }
        }catch(Exception e){
            throw new FragmentReaderException("Unable to process the PAFI input file. "+e.getMessage());
        }
        return filteredCatalog;
    }
    
//    public static void main(String[] args) throws FragmentReaderException{
//        String fpfile = "PAFI_TOOL\\results\\CollectionInPAFIFormat.fp";
//        String pcFile = "PAFI_TOOL\\results\\CollectionInPAFIFormat.pc";
//        String tidFile = "PAFI_TOOL\\results\\CollectionInPAFIFormat.tid";
//        CreateStatisticsFromResultsPAFI c = new CreateStatisticsFromResultsPAFI("Text analytics", true, false, fpfile, pcFile, tidFile);            
//        fixDirectionOfCatalog(Configuration.getPAFIInputPath()+"CollectionInPAFIFormat", c.getFilteredMultiStepFragments(),c.getFragmentsInTransactions());
//    }
    
}
