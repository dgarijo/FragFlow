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


import DataStructures.Fragment;
import DataStructures.Graph;
import DataStructures.GraphNode.GraphNode;
import Static.Vocabularies.Wffd;
import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
/**
 *
 * Code to create MD5 Hash
 * @author Daniel Garijo
 */
public class GeneralMethods {

    /**
     * Auxiliar method to convert data to hexadecimal
     * @param data
     * @return 
     */
    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        }
        return buf.toString();
    }

    /**
     * Method to create MD5 codifications of the input data
     * @param text text to convert to MD5
     * @return the String in MD5
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException 
     */
    public static String MD5(String text)
    throws NoSuchAlgorithmException, UnsupportedEncodingException  {
        MessageDigest md;
        md = MessageDigest.getInstance("MD5");
        byte[] md5hash = new byte[32];
        md.update(text.getBytes("UTF-8"), 0, text.length());
        md5hash = md.digest();
        return convertToHex(md5hash);

    }
    
    /**
     * Method for querying a SPARQL repository
     * @param endpointURL repository URI
     * @param queryIn query to perforn
     * @return result of the query
     */
    public static ResultSet queryOnlineRepository(String endpointURL, String queryIn){
        Query query = QueryFactory.create(queryIn);
        //System.out.println(queryIn);
        QueryExecution qe = QueryExecutionFactory.sparqlService(endpointURL, query);
        ResultSet rs = qe.execSelect();        
        return rs;
    }
    
    /**
     * Method to perform a SELECT query to a local repository
     * @param model which we want to query
     * @param queryIn query to be launched at the model
     * @return results
     */
    public static ResultSet queryLocalRepository(OntModel model, String queryIn){
        Query query = QueryFactory.create(queryIn);
        //System.out.println(queryIn);
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet rs = qe.execSelect(); 
        return rs;
    }
    
    /**
     * Method to perform an ASK query to a local repository 
     * @param model which we want to query
     * @param queryIn query to be launched at the model
     * @return results
     */
    public static boolean askLocalRepository(OntModel model, String queryIn){
        Query query = QueryFactory.create(queryIn);
        //System.out.println(queryIn);
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        return qe.execAsk();                
    }
    
    /**
     * Funtion to insert an individual as an instance of a class. If the class does not exist, it is created.
     * @param individualId Instance id. If exists it won't be created.
     * @param classURL URL of the class from which we want to create the instance
     */
    public static void addIndividual(OntModel m,String individualId, String classURL, String label){
        String nombreIndividuoEnc = encode(individualId);
        OntClass c ;
        if(classURL.startsWith("http://")){
            c = m.createClass(classURL);
        }else{
            //this is an auxiliar thing caused when the model types does are not uris
            c = m.createClass(GeneralConstants.PREFIX_FOR_RDF_GENERATION+encode(classURL));
        }
        if(individualId.contains("http://")){//is a URI already
            c.createIndividual(individualId);
        }else{
            c.createIndividual(GeneralConstants.PREFIX_FOR_RDF_GENERATION+nombreIndividuoEnc);
        }
        if(label!=null){
            addDataProperty(m,nombreIndividuoEnc,label,Wffd.RDFS_SCHEMA_PREFIX+"label");
        }
    }
    
    /**
     * Method analogous to addIndividual but without encoding the name or doing anything
     * to change the URI prefix. This method should not be used for generating RDF
     * @param m model where we will insert the triples
     * @param individualId Instance id. If exists it won't be created.
     * @param classURL URL of the class from which we want to create the instance
     */
    public static void addSimpleIndividual(OntModel m,String individualId, String classURL){
        OntClass c = m.createClass(classURL);
        c.createIndividual(individualId);
    }

    /**
     * Funtion to add a property between two individuals. If the property does not exist, it is created.
     * @param orig Domain of the property (Id, not complete URI)
     * @param dest Range of the property (Id, not complete URI)
     * @param property URI of the property
     */
    public static void addProperty(OntModel m, String orig, String dest, String property){
        OntProperty propSelec = m.createOntProperty(property);
        Resource source;
        if(!orig.contains("http://")){
            source = m.getResource(GeneralConstants.PREFIX_FOR_RDF_GENERATION+ encode(orig) );
        }else{
            source = m.getResource(orig);
        }
        Individual instance = (Individual) source.as( Individual.class );
        if(dest.contains("http://")){//it is a URI
            instance.addProperty(propSelec,m.getResource(dest));            
        }else{//it is a local resource
            instance.addProperty(propSelec, m.getResource(GeneralConstants.PREFIX_FOR_RDF_GENERATION+encode(dest)));
        }
//        System.out.println("Creada propiedad "+ property+" que relaciona los individuos "+ orig + " y "+ dest);
    }
    
    /**
     * Method analogous to addProperty, but without adding any namespace, encoding, etc.
     * This method is to be used by auxiliar tests, but not for generating RDF
     * @param m model where we assert the triples.
     * @param orig Domain of the property (Id, not complete URI)
     * @param dest Range of the property (Id, not complete URI)
     * @param property URI of the property
     */
    public static void addSimpleProperty(OntModel m, String orig, String dest, String property){
        OntProperty propSelec = m.createOntProperty(property);
        Resource source = m.getResource(orig);
        Individual instance = (Individual) source.as( Individual.class );
        instance.addProperty(propSelec,m.getResource(dest));            
    }

    /**
     * Function to add dataProperties. Similar to addProperty
     * @param m Ontology model where we are operating
     * @param origen Domain of the property (Id, not complete URI)
     * @param literal literal to be asserted
     * @param dataProperty URI of the data property to assign.
     */
    public static void addDataProperty(OntModel m, String origen, String literal, String dataProperty){
        OntProperty propSelec;
        //lat y long son de otra ontologia, tienen otro prefijo distinto
        propSelec = m.createDatatypeProperty(dataProperty);
        //propSelec = (modeloOntologia.getResource(dataProperty)).as(OntProperty.class);
        Resource orig = m.getResource(GeneralConstants.PREFIX_FOR_RDF_GENERATION+ encode(origen) );
        m.add(orig, propSelec, literal); 
    }
    
    /**
     * Function to add dataProperties. 
     * @param m the Ontology model where we are adding the data
     * @param source subject of the triple
     * @param data object of the triple
     * @param dataProperty predicate of the triple
     * @param type datatype of the data (String, integer, etc.)
     */
    public static void addDataProperty(OntModel m, String source, String data, String dataProperty,RDFDatatype type) {
        OntProperty propSelec;
        //lat y long son de otra ontologia, tienen otro prefijo distinto
        propSelec = m.createDatatypeProperty(dataProperty);
        Resource orig = m.getResource(GeneralConstants.PREFIX_FOR_RDF_GENERATION+ encode(source));
        m.add(orig, propSelec, data,type);
    }
    
    /**
     * Encoding of the name to avoid any trouble with spacial characters and spaces
     * @param name
     */
    public static String encode(String name){
        name = name.replace("http://","");
        String prenom = name.substring(0, name.indexOf("/")+1);
        //remove tabs and new lines
        String nom = name.replace(prenom, "");
        if(name.length()>255){
            try {
                nom = Static.GeneralMethods.MD5(name);
            } catch (Exception ex) {
                System.err.println("Error when encoding in MD5: "+ex.getMessage() );
            }
        }        

        nom = nom.replace("\\n", "");
        nom = nom.replace("\n", "");
        nom = nom.replace("\b", "");
        //quitamos "/" de las posibles urls
        nom = nom.replace("/","_");
        nom = nom.replace("=","_");
        nom = nom.trim();
        //espacios no porque ya se urlencodean
        //nom = nom.replace(" ","_");
        //a to uppercase
        nom = nom.toUpperCase();
        try {
            //urlencodeamos para evitar problemas de espacios y acentos
            nom = new URI(null,nom,null).toASCIIString();//URLEncoder.encode(nom, "UTF-8");
        }
        catch (Exception ex) {
            try {
                System.err.println("Problem encoding the URI:" + nom + " " + ex.getMessage() +". We encode it in MD5");
                nom = Static.GeneralMethods.MD5(name);
                System.err.println("MD5 encoding: "+nom);
            } catch (Exception ex1) {
                System.err.println("Could not encode in MD5:" + name + " " + ex1.getMessage());
            }
        }
        return prenom+nom;
    }
    
    /**
     * Method to remove "[#,_, ,-]" from the input 
     * @param input
     * @return 
     */
    public static String clean(String input){
//        input = input.replace("(", "");
//        input = input.replace(")", "");
//        input = input.replace(".", "");
        input = input.replace("_", "");
        input = input.replace(",", "");
        input = input.replace(" ", "");
        input = input.replace("-", "");
        return input;
    }
    
    
    /*****This next methods are a bit a workaround for LONI and should be refined*****/
    
    /**
     * Method that given a graph, it transforms the types of the nodes to URIS.
     * This is a workaround for those systems that don't work with URIS
     * @param g 
     */
    public static void setStringTypesToURIs(Graph g){
        HashMap<String,GraphNode> nodes = g.getNodes();
        Iterator<String> it = nodes.keySet().iterator();
        while(it.hasNext()){
            GraphNode currentNode = nodes.get(it.next());
            String currType = currentNode.getType();
            if(!currType.startsWith("http://")&&!currType.startsWith("SUB_")){//in case this is invoked from subdue. workaround! (must be deleted)
                currentNode.setType(GeneralConstants.PREFIX_FOR_RDF_GENERATION+GeneralMethods.encode(currType));
            }
        }
    }
    
    /**
     * Auxiliary method to change LONI types to URIs. 
     * @param f 
     */
    public static void setTypesOfCurrentFragment(Fragment f){
        setStringTypesToURIs(f.getDependencyGraph());    
    }
}
