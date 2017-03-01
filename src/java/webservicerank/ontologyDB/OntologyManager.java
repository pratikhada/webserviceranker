/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservicerank.ontologyDB;

import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import webservicerank.configuration.DBAttributes;
import webservicerank.configuration.NetworkConfigHandler;
import webservicerank.configuration.ExecConfigHandler;
import webservicerank.serviceDB.RankingDBManager;

/**
 *
 * @author BIKASH
 */


public class OntologyManager {

    private PublishOntology publisher = null;
    private IDBConnection connection = null;
    private MatchMaker matchMaker = null;
    private String dbOntModelName = "wsr_ont_model";

    public OntologyManager() throws Throwable{
        try {
            publisher = new PublishOntology();
            DBAttributes db = null;
            try {
                db = ExecConfigHandler.getOntologyDbAttributes();
            } catch (Throwable ex) {
//                Logger.getLogger(OntologyManager.class.getName()).log(Level.SEVERE, null, ex);
                String error = (ex.getMessage() != null)?ex.getMessage():"Error in reading ontology database parameters!";
                throw new Throwable(error);
            }
            Class.forName(db.getDriver());
            connection = new DBConnection(db.getHost() + ":" + db.getPort() + "/" +
                    "webservicerank_ont", db.getUsername(), db.getPassword(), db.getDbType());
        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(OntologyManager.class.getName()).log(Level.SEVERE, null, ex);
            String error = (ex.getMessage() != null)?ex.getMessage():"Ontology database driver class not found!";
            throw new Throwable(error);
        }
    }




    
    /**
     * A method for publishing the ontology in the database and saving it in the drive
     * @param owlUrl; URL of the ontology
     * @return; message of success or failure
     */

    public String publish(String owlUrl) throws Throwable {

        if (owlUrl == null || "".equals(owlUrl)) {
            return "The ontology URL was Empty";
        }

        boolean isPublished = false;
        boolean isStored = false;

        Model input = null;

        try {

            //Read the ontology to the model
            (new NetworkConfigHandler()).proxyAuthenticate(); //proxyAuthentication
            input = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
            input.read(owlUrl);                                //read the ontology

        }catch (Exception ex){
            String error = ex.getMessage();
            error = (error != null)?error:"";
            throw new Throwable("Error! Check the OWL url and try again. "+error);
        }

        //Publish to database
        isPublished = publisher.publishInDatabase(input, connection, dbOntModelName);
//
        if (isPublished){
            //Store the file in drive for future referenct
            isStored = publisher.storeInDrive(input, owlUrl);

            ArrayList<String> namespaces = publisher.getNameSpaces(input);

            for (int i=0; i<namespaces.size(); i++){
                System.out.println(namespaces.get(i)); 
            }
            (new RankingDBManager()).updateNamespaces(namespaces);

        }

        if (isStored)
            return "Congratulation! The ontology has been published successfully.";
        else
            return "Something went wrong while publishing ontology!";

    }



    //Method applicable for computing degree of match
    //Method that prepares matchmaker ready for matchmaking
    public void prepareMatchMaker(boolean considerUserPreference) throws Throwable{
        String[] namespaces = (new RankingDBManager()).getNamespaces();
        matchMaker = new MatchMakerImpl(connection, dbOntModelName, considerUserPreference, namespaces);
    }


    //Method applicable for computing degree of match
    //Method that checks if the properties entered by user are in open ontology model
    public boolean doesContainUserProperties(Properties property, int propertyType){
        return matchMaker.doesContainUserProperties(property, propertyType);
    }



    //Method applicable for computing degree of match
    //Method that computes the degree of match of database property with user
    //entered  properties with the help of open ontology model.
    public double[] calcDegreeOfMatch(Properties property, String db_property, int propertyType, boolean[] areMatched){
        return matchMaker.degreeOfMatch(property, db_property, propertyType, areMatched);
    }

    //Method applicable for computing degree of match
    //Method that closes the open ontology model
    public void closePersistentOntModel(){
        matchMaker.closePersistentOntModel();
    }


    //Method applicable for both web service publishing and ranking
    //Method for closing the database connection of jena.
    public void closeConnection() throws SQLException{
        connection.close();
    }

    
    public static void main(String [] args) throws Throwable{
        OntologyManager om = new OntologyManager();
//        String message = om.publish("file:///E:/WSR_Final/trunk/webservicerank/lib/Ontologies/condition_2.owl");
//        System.out.println(message);
        om.prepareMatchMaker(false);
        String properties[] = {"Locomotive-windshield-wipers", "Roofing-nails", "OtherTomatoBasedFoodCourse"};
        Properties p = new Properties(properties);
        for (int i=0; i<p.getSize(); i++){
            for (int j=0; j<p.getSynonymSize(i); j++){
                System.out.print(p.getPropertyName(i, j)+"\t");
            }
            System.out.println();
        }
        System.out.println("\n\nstarted ... ");        
        boolean is = om.doesContainUserProperties(p, 3);
        boolean[] areMatched = new boolean[]{false};
        long start = System.nanoTime();
        double[] calcDegreeOfMatch = om.calcDegreeOfMatch(p, "OtherTomatoBasedFoodCourse", 3,areMatched);
        System.out.println(is+" in"+(System.nanoTime()-start)/1000000+" ms");
        for (int i=0; i<calcDegreeOfMatch.length; i++){
            System.out.println(calcDegreeOfMatch[i]);
        }
        calcDegreeOfMatch = om.calcDegreeOfMatch(p, "Locomotive-windshield-wipers", 3,areMatched);
        System.out.println(is+" in"+(System.nanoTime()-start)/1000000+" ms");
        for (int i=0; i<calcDegreeOfMatch.length; i++){
            System.out.println(calcDegreeOfMatch[i]);
        }
    }


    

}
