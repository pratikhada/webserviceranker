/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservicerank.configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
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
public class OntologyInfoManager {

    public String fPath = null;

    public OntologyInfoManager() throws Throwable{
        fPath = FileNames.rootDir()+FileNames.separator()+FileNames.ONT_INFO;
    }

    public OntologyInfoManager(String workDir) throws Throwable{
        fPath = workDir + FileNames.separator()+FileNames.ONT_INFO;
    }

    //Creates the file for storing ontology information
    public static void prepareOntologyInfo(FileWriter writer) throws IOException{
        Element root = new Element("ontologies");
        root.addContent(new Comment("This contains the information about all the published ontologies"));
        Document doc = new Document(root);
        XMLOutputter out=new XMLOutputter();
        out.output(doc, writer);
    }



    /**
     * A method to keep the record of the published ontology
     * @param ontName; the name of ontology while publishing originally
     * @param owlUrl; The URL from which ontology was read
     * @param filename; New model name while saving the ontology in directory
     * @throws Throwable
     */
    public void registerOntology(String ontName, String owlUrl, String filename) throws Throwable{

        //Prepare a Element for the entry
        Element ontology = new Element("ontology");
        ontology.addContent((new Element("ont-name")).addContent(ontName));
        ontology.addContent(new Element("file-name").addContent(filename));
        ontology.addContent((new Element("owl-url")).addContent(owlUrl));
        ontology.addContent((new Element("published-date")).addContent((new Date()).toString()));
        ontology.setAttribute("ID", ontName);

        //Store the entry to file
        Document doc = (new SAXBuilder(false)).build(new File(fPath));
        doc.getRootElement().addContent(ontology);
        (new XMLOutputter()).output(doc, new FileWriter(fPath));
    }


    public void enterDatabaseInfo(String ontName,String ontModels[]) throws Throwable{
        Document doc = (new SAXBuilder(false)).build(new File(fPath));
        List children = doc.getRootElement().getChildren();
        Element currElem = null;
        for (int i=0; i<children.size(); i++){
            Element temp = (Element) children.get(i);
            if (ontName.equals(temp.getAttribute("ID").toString())){
                currElem = temp;
                break;
            }
        }
        currElem.addContent(new Element("published-date").addContent((new Date()).toString()));
        Element modelNames = new Element("ontology-models");
        currElem.addContent(modelNames);
        for (int i=0; i<ontModels.length; i++){
            modelNames.addContent(new Element("ontology-model").addContent(ontModels[i]));
        }
        (new XMLOutputter()).output(doc, new FileWriter(fPath));
    }

    String[] listServiceCategories(String ontName) throws Throwable {
        Document doc = null;
        try {
            doc = (new SAXBuilder(false)).build(new File(fPath));
        } catch (JDOMException ex) {
            Logger.getLogger(OntologyInfoManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new Throwable(ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(OntologyInfoManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new Throwable(ex.getMessage());
        }
        List children = doc.getRootElement().getChildren();
        Element currElem = null;
        for (int i=0; i<children.size(); i++){
            Element temp = (Element) children.get(i);
            if (ontName.equals(temp.getAttribute("ID").toString())){
                currElem = temp.getChild("service-categories");
                break;
            }
        }
        String categories[] = null;
        if (currElem != null){
            List children1 = currElem.getChildren();
            categories = new String[children1.size()];
            for (int i=0; i<categories.length; i++){
                categories[i] = ((Element)children1.get(i)).getText();
            }
        }
        return categories;
    }


}
