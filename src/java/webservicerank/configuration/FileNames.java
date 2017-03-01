/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
 * @author webservicerank
 */
public class FileNames {
    private static String rootDirectory = null;
    private static String fileSeparator = null;
    public static String  ROOT_CONFIG = "webservicerank.config";
    public static String  ONT_INFO = "wsr_ontologies.info";
    public static String  EXECUTION_CONFIG = "wsr_execution.config";
    public static String  UNPUBLISHED_MAPPED = "new_ont_property.xml";
    public static String  NETWORK_CONFIG= "net_config.xml";

    public static String rootDir() throws Throwable{
        if (rootDirectory == null){
            Document doc = null;
            try {
                doc = (new SAXBuilder(false)).build(new File(FileNames.ROOT_CONFIG));
            } catch (JDOMException ex) {
//                Logger.getLogger(FileNames.class.getName()).log(Level.SEVERE, null, ex);
                String error = (ex.getMessage() != null)?ex.getMessage():"General JDom error!";
                throw new Throwable(error);
            } catch (IOException ex) {
//                Logger.getLogger(FileNames.class.getName()).log(Level.SEVERE, null, ex);
                String error = (ex.getMessage() != null)?ex.getMessage():"IO Error! Application configuration file not found!";
                throw new Throwable(error);
            }
            Element root = doc.getRootElement();
            Element child = root.getChild("directory-path");
            rootDirectory = child.getText();
        }
        return rootDirectory;
    }

    public static String separator(){
        if (fileSeparator == null){
            fileSeparator = System.getProperty("file.separator");
        }
        return fileSeparator;
    }
    
    //Creates the file that contains the information of root directory
    public static void rootSetup(FileWriter writer, String workDir) throws IOException{
        Element root = new Element("wsr-configuration");
        root.addContent(
                new Comment("The configurations for deploying webservicerank"));
        root.addContent(new Element("directory-path").addContent(workDir)
                .addContent(
                new Comment("Path of directory in which the application is setup")));
        Document doc = new Document(root);
        XMLOutputter out=new XMLOutputter();
        out.output(doc, writer);
    }
    
}
