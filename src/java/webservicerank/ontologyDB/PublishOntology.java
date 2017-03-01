

package webservicerank.ontologyDB;

import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.db.ModelRDB;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import webservicerank.configuration.FileNames;
import webservicerank.configuration.OntologyInfoManager;
import webservicerank.serviceDB.PublishDBManager;

/**
 * A Java class that is useful for publishing the ontology to the given
 * database model and for saving the given ontology in hard disk file and
 * maintaining information about the published ontologies. 
 * @author BIKASH
 */


public class PublishOntology {
   
    PublishDBManager dbManager = null;


    public PublishOntology(){
        dbManager = null;
    }


    public PublishOntology(PublishDBManager dbManager){
        this.dbManager = dbManager;
    }

  


    /**
     * A method that publishes the ontology model read in memory to the database
     * It transfers the statements of the ontology in memory to the database in such a
     * way such that no statements will be repeated in the database.
     * @param input, Model read in memory
     * @param connection, Database connection
     * @param dbModel, Model name in database in which the memory model will be published
     */

    public boolean publishInDatabase(Model input, IDBConnection connection, String dbModel){

        ModelRDB persistent = null;

        //Open the ontology model
        if ( connection.containsModel(dbModel))
            persistent = ModelRDB.open(connection, dbModel);
        else
            persistent = ModelRDB.createModel(connection, dbModel);

        //Start transaction in the model (Or transfer statements to the database)
        persistent.begin();
        StmtIterator it = input.listStatements();
        persistent.add(it);

        //Finish the transaction
        persistent.commit();

        //Close the persistent model
        persistent.close();
        
        //Message of successness
        return true;

    }


    
    /**
     * A method that saves the read ontology model in the drive for future
     * reference. Further, it maintains information about the published ontology
     * in the information file, which can be viewed at any time later.
     * @param input, ontology in the memory (i.e., read from the publisher)
     * @param owlUrl, The URL of the ontology
     * @return, true if the ontology could be saved and the information could be
     * recorded successfully.
     */

    public boolean storeInDrive(Model input, String owlUrl){
        boolean isStored = false;
        try {

            String workDir = FileNames.rootDir() + FileNames.separator();
            String[] temp = owlUrl.split("\\/");
            String[] ontName = temp[temp.length - 1].split("\\.");
            String newName = ontName[0];
            String ontPath = workDir + FileNames.separator() + "ontologies" + FileNames.separator();
            int addend = 0;

            //find out new file name (Necessary only when file with same model name are already stored)
            while ((new File(ontPath + newName + "." + ontName[1])).isFile()) {
                addend++;
                newName = ontName[0] + String.valueOf(addend);
            }

            //Store the model in the new name
            input.write(new FileWriter(ontPath + newName + "." + ontName[1]));

            //Register this model in ontology info file
            (new OntologyInfoManager()).registerOntology(ontName[0] + "." + ontName[1], owlUrl, newName + "." + ontName[1]);

            isStored = true;

        } catch (Throwable ex) {
            Logger.getLogger(PublishOntology.class.getName()).log(Level.SEVERE, null, ex);
        }

         return isStored;

    }

    /**
     * A method that finds the namespaces in the ontology model
     * @param input, Input ontology model
     * @return, list of the namespaces
     */

    ArrayList<String> getNameSpaces(Model model) {

        ArrayList<String> namespaces = new ArrayList<String>();
        String NS;

        //Default namespace
        String url = model.getNsPrefixURI("");
        if (url != null){
            NS = url.split("\\#")[0];
            if (!namespaces.contains(NS)){
                namespaces.add(NS);
            }            
        }

        //Namespaces from the elements
        ResIterator it = model.listSubjects();
        Resource resource = null;
        while(it.hasNext()){
            resource = it.nextResource();
            try {
                String[] temp = resource.getURI().split("\\#");
                if (temp != null && temp[0] != null){
                    if ( !namespaces.contains(temp[0])){
                        namespaces.add(temp[0]);
                    }
                }
            }catch(NullPointerException ex){}
        }
        return namespaces;
    }


}
