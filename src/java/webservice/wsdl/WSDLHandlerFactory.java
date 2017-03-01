/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservice.wsdl;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import webservice.wsdl.implementation.WSDL_1_0_Handler;
import webservice.wsdl.implementation.WSDL_2_0_Handler;
import webservicerank.configuration.NetworkConfigHandler;

/**
 *
 * @author BIKASH
 */
public class WSDLHandlerFactory {


    private static Element getSchemaRoot(Element root) throws JDOMException, IOException{
        List children = root.getChildren();
        Element types = null;
        Element schemaRoot = null;
        for(int i=0; i<children.size(); i++){
            if(((Element)children.get(i)).getName().equals("types")){
                types = (Element)children.get(i);
                break;
            }
        }
        List children2 = types.getChildren();
        for(int i=0; i<children2.size(); i++){
            if(((Element)children2.get(i)).getName().equals("schema")){
                List children3 = ((Element)children2.get(i)).getChildren();
                for(int temp=0; temp<children3.size(); temp++){
                    if(((Element)children3.get(temp)).getName().equals("import")){
                        Element imported = (Element)children3.get(temp);
                        String ns = imported.getAttributeValue("namespace");
                        String schemaLocation = imported.getAttributeValue("schemaLocation");
                        SAXBuilder builder1 = new SAXBuilder(false);
                        Document schema = null;
                        try {
                         schema = builder1.build(schemaLocation);
                         schemaRoot = schema.getRootElement();
                        }catch(Exception e){
                        }
                        break;
                    }
                }
                if(schemaRoot == null)
                    schemaRoot = (Element)children2.get(i);
                break;
            }
        }
        return schemaRoot;
    }



    public static WSDLHandler create(String url) throws Throwable{
        SAXBuilder builder = new SAXBuilder(false);
//        builder.setFeature("http://apache.org/xml/features/validation/schema", validate);
        (new NetworkConfigHandler()).proxyAuthenticate();
        Document document = null;
        try {
            document = builder.build(url);
        } catch (JDOMException ex) {
            String error = (ex.getMessage() != null)?ex.getMessage():"";
//            Logger.getLogger(WSDLHandlerFactory.class.getName()).log(Level.SEVERE, null, ex);
            throw new Throwable("The WSDL couldn't be read! Make sure that it is a valid XML document."+error);
        } catch (IOException ex) {
            String error = (ex.getMessage() != null)?ex.getMessage():"";
//            Logger.getLogger(WSDLHandlerFactory.class.getName()).log(Level.SEVERE, null, ex);
            throw new Throwable("The WSDL couldn't be read! Make sure that WSDL URL exists and there is no network problem. "+error);
        }
        Element root = document.getRootElement();

        //for version 1.0 document
        if("definitions".equals(root.getName())){
            System.out.println("version 1.0");
            return new WSDL_1_0_Handler(url, getSchemaRoot(root));
        }

        //for version 2.0 wsdl document
        else if("description".equals(root.getName())){
            System.out.println("version 2.0");
            return new WSDL_2_0_Handler(url,  getSchemaRoot(root));
        } 
        //version unknown
        throw new Throwable("The version of WSDL is unknown! Check URL of WSDL and try again.");
    }
}
