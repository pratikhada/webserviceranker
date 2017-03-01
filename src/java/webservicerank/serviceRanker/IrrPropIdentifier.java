/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservicerank.serviceRanker;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import webservicerank.ontologyDB.OntologyManager;
import webservicerank.ontologyDB.Properties;
import webservicerank.serviceDB.RankingDBManager;
import webservicerank.uiAppInterface.ServiceDetail;

/**
 *
 * @author BIKASH
 */

//Class for identifying irrelevant properties
//Irrelevant Property Identifier
public class IrrPropIdentifier {

    private OntologyManager ontManager;
    private RankingDBManager dbManager;
    private  Properties general, input,output,precondition,effect;
    
    boolean isMatchFound[];

    
    //Represents whether the properties have priorities in the order of input
    private boolean userPreference;

    //Array to represent degree of match of a database property with each of input property
    private double degreesOfMatch[];

    //If the priority is to be considered, then the weights to be applied
    private final double preferences[] = {1.0, 0.9, 0.82, 0.74, 0.65};
    

    public IrrPropIdentifier() throws Throwable {
        try {
            ontManager = new OntologyManager();
        } catch (Throwable ex) {
//            Logger.getLogger(IrrPropIdentifier.class.getName()).log(Level.SEVERE, null, ex);
            String error = (ex.getMessage() != null)?ex.getMessage():"Ontology manager couldn't be initialized!";
            throw new Throwable(error);
        }
        general = null;
        input = null;
        output = null;
        precondition = null;
        effect = null;
        dbManager = null;
    }


    private void close(){
        ontManager = null;
        general = null;
        input = null;
        output = null;
        precondition = null;
        effect = null;
        dbManager = null;
    }

    private void initialize(ServiceDetail sd) throws Throwable{

        isMatchFound = new boolean[5];

        //Initialize properties
        if (!sd.propertyCategorized()){
            general = new  Properties(sd.getProperties());
            System.out.println("General properties are:");
            general.display();
        }else{
            input = new  Properties(sd.getInputProperties());
            input.display();
            output = new  Properties(sd.getOutputProperties());
            output.display();
            precondition = new  Properties(sd.getPreconditions());
            precondition.display();
            effect = new  Properties(sd.getEffects());
            effect.display();
        }

    }


    /**
     *A function that calculates degree of match between the database property and
     * user input properties, which is based on keyword matchmaking
     * @param properties
     * @param db_property
     * @param type
     * @return
     */
    private double[] matchKeywords(Properties properties, String db_property, boolean areMatched[]) throws Throwable {
        
        if (properties == null || properties.getSize() == 0)
            return null;

        //initialize the degree of match
        degreesOfMatch = new double[properties.getSize()];
        for (int i=0; i<degreesOfMatch.length; i++)
            degreesOfMatch[i] = 0.0;

        ArrayList<String> dbSynonyms = properties.getSynonyms(db_property);
        if (!dbSynonyms.contains(db_property)){
            dbSynonyms.add(db_property); 
        }
        //Multiplication factor
        double multiplier = 1.0f;
        boolean matched ;
        //try to match the words directly (direct string comparison)
        for (int i=0; i<degreesOfMatch.length; i++){
            matched = false;
            for (int j=0; j<properties.getSynonymSize(i); j++){
                for (int k=0; k<dbSynonyms.size(); k++){
                    if (properties.getPropertyName(i, j).equals(dbSynonyms.get(k))){
                        //This is exact match based on keyword matchmaking
                        degreesOfMatch[i] = 1.0;
                        if (userPreference){
                            //Consider the user preferences (priorities given by user)
                            if ( i<5)
                                multiplier = preferences[i];
                            else multiplier = 1*(i+1)/((i+0.01)*2);
                        }
                        degreesOfMatch[i] *= multiplier;
                        areMatched[0] = true;
                        matched = true;
                        break; //as this is the maximum degree of match for this list of synonyms
                    }
                }
                if (matched)
                    break;
            }
        }

        return degreesOfMatch;

    }
    

    
    /**
     * Matches a property (i.e., object of Properties) of certain type like
     * 'g', 'i', 'o', 'p' or 'e', with the properties related to ont_id (Ontology model)
     * based ont the open persistent ontology model.
     * @param property
     * @param ont_id
     * @param type
     * @throws Throwable
     */

    private void matchProperties(Properties properties, String property, int prop_id, int type, boolean checkInOntology) throws Throwable{
        if (properties == null)
            return;
         boolean[] areMatchedByKeyword = new boolean[1];
         boolean[] areMatchedSemantically = new boolean[]{false};
        areMatchedByKeyword[0] = false;

        //First try to match by keywords
        double dom[] = matchKeywords(properties,property, areMatchedByKeyword);
        if (checkInOntology){
            //Try to match semantically
            double dom1[] = ontManager.calcDegreeOfMatch(properties,property, type, areMatchedSemantically);
            if (areMatchedByKeyword[0] && areMatchedSemantically[0]){
                for (int i=0; i<dom.length; i++){
                    if (dom[i] < dom1[i]){
                        dom[i] = dom1[i];
                    }
                }
            }else if (areMatchedSemantically[0]){
                dom = dom1;
            }
        }
        if (areMatchedByKeyword[0] || areMatchedSemantically[0]){

            dbManager.setPropertyDOM(prop_id, dom, type);
        }
    }


   /**
    * Opens all the persistent ontology models related to the service category, and
    * matches the service properties entered by the user with the properties stored in
    * database related to the ontology model based on the ontology model.
    * @param sd
    * @param rDBManager
    * @param isPriority
    * @throws Throwable
    */

    public void matchWithOntology(ServiceDetail sd, RankingDBManager rDBManager,
            boolean isPriority) throws Throwable
    {
        //Prepare the matchmaker ready for matchmaking.
        ontManager.prepareMatchMaker(isPriority);
        dbManager = rDBManager;
        boolean categorized = sd.propertyCategorized();

        initialize(sd);

        //initialize isMatchFound
        if (! categorized ){
            long start = System.nanoTime();
            if (ontManager.doesContainUserProperties(general, 4))
                isMatchFound[4] = true;
            System.out.println("Time for initialization ="+(System.nanoTime()-start)/1000000+" ms");
            general.display();
        }else{
            if (ontManager.doesContainUserProperties(input, 0))
                isMatchFound[0] = true;
            if (ontManager.doesContainUserProperties(output, 1))
                isMatchFound[1] = true;
            if (ontManager.doesContainUserProperties(precondition, 2))
                isMatchFound[2] = true;
            if (ontManager.doesContainUserProperties(effect, 4))
                isMatchFound[3] = true;
        }

        int propId[] = new int[1];
        String[] category = new String[1];
        String property = dbManager.getNextPropertyName(propId, category, true);
        while (property != null){
            if (! categorized ){
                if (category[0].charAt(4) == '1' && isMatchFound[4])
                    matchProperties(general,property, propId[0], 4, true);
            }else{
                if (category[0].charAt(0) == '1' && isMatchFound[0])
                    matchProperties(input, property, propId[0], 0, true);
                if (category[0].charAt(1) == '1' && isMatchFound[1])
                    matchProperties(output, property, propId[0], 1, true);
                if (category[0].charAt(2) == '1' && isMatchFound[2])
                    matchProperties(precondition, property, propId[0], 2, true);
                if (category[0].charAt(3) == '1' && isMatchFound[3])
                    matchProperties(effect, property, propId[0], 3, true);
            }

            property = dbManager.getNextPropertyName(propId, category, false);
        }

        ontManager.closePersistentOntModel();
        close();
        
    }


    /**
     * Matches the user input properties with database properties with keyword matchmaking
     * @param sd
     * @param rDBManager
     * @param isPriority
     * @throws Throwable
     */
    public void matchWithDBProperties(ServiceDetail sd, RankingDBManager rDBManager, boolean isPriority) throws Throwable{

        dbManager = rDBManager;
        userPreference = isPriority;
        boolean categorized = sd.propertyCategorized();

        initialize(sd);

        int propId[] = new int[1];
        String[] category = new String[1];

        String property = dbManager.getNextPropertyName(propId, category, true);

        while (property != null){
            if (! categorized ){
                if (category[0].charAt(4) == '1')
                    matchProperties(general,property, propId[0], 4, false);
            }else{
                if (category[0].charAt(0) == '1')
                    matchProperties(input, property, propId[0], 0, false);
                if (category[0].charAt(1) == '1')
                    matchProperties(output, property, propId[0], 1, false);
                if (category[0].charAt(2) == '1')
                    matchProperties(precondition, property, propId[0], 2, false);
                if (category[0].charAt(3) == '1')
                    matchProperties(effect, property, propId[0], 3, false);
            }

            property = dbManager.getNextPropertyName(propId, category, false);
        }
        close();
    }
 


}
