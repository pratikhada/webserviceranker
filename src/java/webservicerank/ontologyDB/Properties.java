/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservicerank.ontologyDB;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;
import java.util.ArrayList;
import webservicerank.configuration.ExecConfigHandler;

/**
 *
 * @author BIKASH
 */
public class Properties {

    //All properties with their synonyms
    //(each property in individial element of array
    private ArrayList<String> properties[];

    //Is every property entered has found match?
    private boolean isMatchFound[];

    //WordNet database
    WordNetDatabase database;

    private void initiate() throws Throwable{
        if (database == null){
            //prepare wordnet dictionary for using to get synonyms.
            String wordNetDBPath = ExecConfigHandler.getWordNetDir();
            System.setProperty("wordnet.database.dir", wordNetDBPath);
            database = WordNetDatabase.getFileInstance();
        }
    }

    //get the list of synonyms of a given word 'wordForm'
    private ArrayList<String> getWordForms(String wordForm) throws Throwable{
        initiate();
        ArrayList<String> list = new ArrayList<String>();
        Synset[] synsets = database.getSynsets(wordForm);
        String[] wordForms;
        for (int i=0; i<synsets.length; i++){
            wordForms = synsets[i].getWordForms();
            if (wordForms != null)
                for(int j=0; j<wordForms.length; j++){
                    if(!list.contains(wordForms[j]))
                        list.add(wordForms[j]);
                }
        }
        return list;
    }
    

    public Properties(){
        
    }


    //initializes properties[] and isMatchFound[]

    public Properties(String[] _properties) throws Throwable{

        if (_properties != null){

            initiate();
            isMatchFound = new boolean[_properties.length];
            for (int i=0; i<isMatchFound.length; i++)
                isMatchFound[i] = false;

            properties = (ArrayList<String>[])new ArrayList[_properties.length];

            ArrayList<String> temp = null;

            //prepare synonym lists for every user properties (without repetition)
            for (int i=0; i<_properties.length; i++){
                properties[i] = new ArrayList<String>();
                temp = getWordForms(_properties[i]);
                boolean wasStored;
                if(temp != null && !temp.isEmpty()){
                    for (int j=0; j<temp.size(); j++){
                        wasStored = false;
                        for (int k=0; k<i; k++){
                            if( properties[k].contains(temp.get(j))){
                                wasStored = true;
                                break;
                            }
                        }
                        if( ! wasStored)
                            properties[i].add(temp.get(j));
                    }
                    temp.clear();
                    temp = null;
                }
                wasStored = false;
                for (int k=0; k<=i; k++){
                    if (properties[k].contains(_properties[i])){
                        wasStored = true;
                        break;
                    }
                }
                if (!wasStored)
                    properties[i].add(_properties[i]);
            }
        }else{
            properties = null;
            isMatchFound = null;
        }
    }


    //returns the number of properties entered.
    public int getSize(){
        if (properties == null)
            return 0;
        else
            return properties.length;
    }


    public String getPropertyName(int i, int j){
        return properties[i].get(j);
    }

    public int getSynonymSize(int i){
        return properties[i].size();
    }


    public void setIsMatchFound(int i, boolean isFound){
        isMatchFound[i] = isFound;
    }

    public void display() {
        for (int i=0; i<properties.length; i++){
            for (int j=0; j<properties[i].size(); j++)
                System.out.print(properties[i].get(j)+"\t");
            System.out.println();
        }
    }

    public ArrayList<String> getSynonyms(String db_property) throws Throwable {
        initiate();
        return getWordForms(db_property);
    }


    public static void main(String[] args) throws Throwable {
        Properties p = new Properties();
        ArrayList<String> wordForms = p.getWordForms("current");
        System.out.println("kilo="+wordForms);
    }























}
