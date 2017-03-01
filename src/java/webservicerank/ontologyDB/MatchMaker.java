/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservicerank.ontologyDB;

/**
 *
 * @author BIKASH
 */
public interface MatchMaker {

    //Open a model from url (not from database)
    public void openModel(String url);

    //check if the model contains any of the user properties in it
    public boolean doesContainUserProperties(Properties property, int propertyType);
    
    //calculates the degree of match of the database property (db_property)
    //with user properties and returns array containing a degree of match for a user property
    public double[] degreeOfMatch(Properties property, String db_property, int propertyType, boolean[] areMatched);

    //closes the open persistent model 
    public void closePersistentOntModel();

    public boolean containsProperty(String property);
    
}
