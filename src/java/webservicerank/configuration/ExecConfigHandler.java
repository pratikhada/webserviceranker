/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//Call the functions by Class name such as ExecConfigHandler.setValues();
package webservicerank.configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author BIKASH
 */
public class ExecConfigHandler {

    private static DBAttributes serviceDb = null, ontologyDb = null ;
    private static String wordNetDir = null ;
    private static String filePath;


    private static void setFilePath() throws Throwable{
        if (filePath == null){
            try {
                filePath = FileNames.rootDir() + FileNames.separator() + FileNames.EXECUTION_CONFIG;
            } catch (Throwable ex) {
//                Logger.getLogger(ExecConfigHandler.class.getName()).log(Level.SEVERE, null, ex);
                String error = (ex.getMessage() != null)?ex.getMessage():"Base configuration file not found!";
                throw new Throwable(error);
            }
        }
    }

    private static void setConfigParams() throws Throwable{
        try {
            setFilePath();
        } catch (Throwable ex) {
//            Logger.getLogger(ExecConfigHandler.class.getName()).log(Level.SEVERE, null, ex);
            String error = (ex.getMessage() != null)?ex.getMessage():"Base configuration file not found!";
            throw new Throwable(error);
        }
        if (serviceDb == null || ontologyDb == null || wordNetDir == null){
            Document doc;
            try {
                doc = (new SAXBuilder(false)).build(new File(filePath));
            } catch (JDOMException ex) {
//                Logger.getLogger(ExecConfigHandler.class.getName()).log(Level.SEVERE, null, ex);
                String error = (ex.getMessage() != null)?ex.getMessage():"General JDom error!";
                throw new Throwable(error);
            } catch (IOException ex) {
//                Logger.getLogger(ExecConfigHandler.class.getName()).log(Level.SEVERE, null, ex);
                String error = (ex.getMessage() != null)?ex.getMessage():"Application configuration file not found!";
                throw new Throwable(error);
            }
            Element root = doc.getRootElement();

            //Initialize wordNetDir
            if (wordNetDir == null){
                wordNetDir = root.getChild("WordNet-dir")
                        .getChild(System.getProperty("user.name")).getText();
            }

            //Initialize service database configurations
            if (serviceDb == null){
                serviceDb = new DBAttributes();
                Element service = root.getChild("Service-Database");
                serviceDb.setDriver(service.getChildText("sd-driver"));
                serviceDb.setHost(service.getChildText("sd-host"));
                serviceDb.setPort(service.getChildText("sd-port"));
                serviceDb.setUsername(service.getChildText("sd-username"));
                serviceDb.setPassword(service.getChildText("sd-password"));
                serviceDb.setDbType(null);
            }

            //Initialize the ontology database setting parameters.
            if (ontologyDb == null){
                ontologyDb = new DBAttributes();
                Element ontology = root.getChild("Ontology-repository");
                ontologyDb.setDriver(ontology.getChildText("ont-driver"));
                ontologyDb.setHost(ontology.getChildText("ont-host"));
                ontologyDb.setPort(ontology.getChildText("ont-port"));
                ontologyDb.setDbType(ontology.getChildText("ont-DBType"));
                ontologyDb.setUsername(ontology.getChildText("ont-username"));
                ontologyDb.setPassword(ontology.getChildText("ont-password"));
            }
        }
    }

    //Method that creates the configuration file
    public static void prepareConfiguration(FileWriter writer) throws IOException{
        Element root = new Element("webservicerank-configuration");
        root.addContent(new Comment("This contains the configuration information"));
        root.addContent(new Element("WordNet-dir"));
        root.addContent(new Element("Service-Database"));
        root.addContent(new Element("Ontology-repository"));
        Document doc = new Document(root);
        XMLOutputter out=new XMLOutputter();
        out.output(doc, writer);
    }

    //Saves the wordnet directory configuration
    public static void configWordnetDir(String dir) throws Throwable{
        setFilePath();
        Document doc = (new SAXBuilder(false)).build(new File(filePath));
        Element wordnet = doc.getRootElement().getChild("WordNet-dir");
        String sysUser = System.getProperty("user.name");
        Element user = wordnet.getChild(sysUser);
        if ( user == null){
            user = new Element(sysUser).addContent(dir);
            wordnet.addContent(user);
        }else{
            user.removeContent();
            user.addContent(dir);
        }
        (new XMLOutputter()).output(doc, new FileWriter(filePath));
    }

    //Stores service database configuratin to file
    public static void configureServiceDB(DBAttributes db) throws Throwable{
        setFilePath();
        Document doc = (new SAXBuilder(false)).build(new File(filePath));
        Element serviceDB = doc.getRootElement().getChild("Service-Database");
        serviceDB.removeContent();
        serviceDB.addContent(new Element("sd-driver").addContent(db.getDriver()));
        serviceDB.addContent(new Element("sd-host").addContent(db.getHost()));
        serviceDB.addContent(new Element("sd-port").addContent(db.getPort()));
        serviceDB.addContent(new Element("sd-username").addContent(db.getUsername()));
        serviceDB.addContent(new Element("sd-password").addContent(db.getPassword()));
        (new XMLOutputter()).output(doc, new FileWriter(filePath));
    }

    //Stores ontology repository settings to file
    public static void configureOntologyDB(DBAttributes db, String dbType) throws Throwable{
        setFilePath();
        Document doc = (new SAXBuilder(false)).build(new File(filePath));
        Element ontologyDB = doc.getRootElement().getChild("Ontology-repository");
        ontologyDB.removeContent();
        ontologyDB.addContent(new Element("ont-driver").addContent(db.getDriver()));
        ontologyDB.addContent(new Element("ont-host").addContent(db.getHost()));
        ontologyDB.addContent(new Element("ont-port").addContent(db.getPort()));
        ontologyDB.addContent(new Element("ont-DBType").addContent(dbType));
        ontologyDB.addContent(new Element("ont-username").addContent(db.getUsername()));
        ontologyDB.addContent(new Element("ont-password").addContent(db.getPassword()));
        (new XMLOutputter()).output(doc, new FileWriter(filePath));
    }

    public static DBAttributes getServiceDbAttributes() throws Throwable{
        setConfigParams();
        return serviceDb;
    }

    public static DBAttributes getOntologyDbAttributes() throws Throwable{
        try {
            setConfigParams();
        } catch (Throwable ex) {
//            Logger.getLogger(ExecConfigHandler.class.getName()).log(Level.SEVERE, null, ex);
            String error = (ex.getMessage() != null)?ex.getMessage():"Error in reading ontology database parameters!";
            throw new Throwable(error);
        }
        return ontologyDb;
    }

    public static String getWordNetDir() throws Throwable{
        setConfigParams();
        return wordNetDir;
    }
    public static void main(String[] args){
        //Code for checking the operation
//        ExecConfigHandler.setValues();
    }
}
