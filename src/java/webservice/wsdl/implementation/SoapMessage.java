/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservice.wsdl.implementation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import webservice.wsdl.WSDLHandler;
import webservicerank.configuration.NetworkConfigHandler;


/**
 *
 * @author BIKASH
 */

public class SoapMessage {

    SOAPMessage soapRequest;
    SOAPMessage soapResponse;
    SOAPBodyElement dispatch;

    public SoapMessage(){
        soapRequest = null;
        soapResponse = null;
        dispatch = null;
    }

    public void createSOAPMessage(){
        try {
            soapRequest = MessageFactory.newInstance().createMessage();
            SOAPEnvelope envelope = soapRequest.getSOAPPart().getEnvelope();
            Iterator namespacePrefixes = envelope.getNamespacePrefixes();
            if (namespacePrefixes.hasNext())
                envelope.addNamespaceDeclaration((String) namespacePrefixes.next(),
                        "http://schemas.xmlsoap.org/soap/envelope/");
            else
                envelope.addNamespaceDeclaration("SOAP-ENV",
                        "http://schemas.xmlsoap.org/soap/envelope/");
            soapRequest.saveChanges();
            soapResponse = null;
        } catch (SOAPException ex) {
//            Logger.getLogger(SoapMessage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addHeaderElement(QName qname){
        try {
            SOAPPart soapPart = soapRequest.getSOAPPart();
            SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
            SOAPHeader soapHeader = soapEnvelope.getHeader();
            soapHeader.addHeaderElement(qname);
        } catch (SOAPException ex) {
//            Logger.getLogger(SoapMessage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addBodyElement(QName qname){
        try {
            SOAPPart soapPart = soapRequest.getSOAPPart();
            SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
            SOAPBody soapBody = soapEnvelope.getBody();
            Name name = soapEnvelope.createName(qname.getLocalPart(), "ns2", qname.getNamespaceURI());
            dispatch = soapBody.addBodyElement(name);
        } catch (SOAPException ex) {
//            Logger.getLogger(SoapMessage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addChildElement(String name,String text, String type){
        try {
            if (text == null)
                text = "150";
            dispatch.addChildElement(name).addTextNode(text);//.setAttribute("type", type);
        } catch (SOAPException ex) {
//            Logger.getLogger(SoapMessage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public SOAPMessage getSOAPMessage(){
        return soapRequest;
    }

    public Source getSource() throws SOAPException{
        SOAPPart soapPart = soapRequest.getSOAPPart();
        return soapPart.getContent();
    }



    /**
     * A method which sends the SOAP request to the service and accepts
     * a SOAP response from the service
     * @param portLocation, end address of the service
     * @return, the service execution duration
     * @throws Throwable
     */
     public long interactWithService(String portLocation, int[] invoke_counts) throws Throwable{
        long start_time = 0;
        try {            
            SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
            SOAPConnection conn = scf.createConnection();
            soapRequest.saveChanges();
            soapRequest.writeTo(new FileOutputStream( new File("rquestOfService.xml")));
            (new NetworkConfigHandler()).proxyAuthenticate();
            start_time = System.nanoTime();
            invoke_counts[0] ++;
            soapResponse = conn.call(soapRequest, new URL(portLocation));
            invoke_counts[1] ++;
            soapResponse.writeTo(new FileOutputStream( new File("responseOfService1.xml")));
        } catch (IOException ex) {
            soapResponse = null;
//            Logger.getLogger(SoapMessage.class.getName()).log(Level.SEVERE, null, ex);
            String error =ex.getMessage();
            error = (error != null)?"Error :"+error:"";
            throw new Throwable("Error while interacting with service! "+error);
        } catch (SOAPException ex) {
            soapResponse = null;
//            Logger.getLogger(SoapMessage.class.getName()).log(Level.SEVERE, null, ex);
            String error =ex.getMessage();
            error = (error != null)?"Error :"+error:"";
            throw new Throwable("Error while interacting with service! "+error);
        } catch (UnsupportedOperationException ex) {
            soapResponse = null;
//            Logger.getLogger(SoapMessage.class.getName()).log(Level.SEVERE, null, ex);
            String error =ex.getMessage();
            error = (error != null)?"Error :"+error:"";
            throw new Throwable("Error while interacting with service! "+error);
        }
        return System.nanoTime() - start_time;
    }



    public void prepareRequestBody(QName[] inputParts, WSDLHandler wsdl, String[] values){
        int[] index = new int[1];
        index[0] = 0;
        for(int inputCount = 0; inputCount<inputParts.length; inputCount++){
            if (inputParts[inputCount] != null){
                addBodyElement(inputParts[inputCount]);
                Parameter[] inParam = wsdl.inputParameters(inputParts[inputCount], values, index);
                if(inParam != null){
                    for(int paramCount=0; paramCount<inParam.length; paramCount++){
                        Parameter[] params = inParam[paramCount].getChildren();
                        if(params == null)
                            addChildElement(inParam[paramCount].getName(),
                                    (String) inParam[paramCount].getValue(),
                                    inParam[paramCount].getType());
                        else
                        {
                            for(int childParamCount = 0; childParamCount< params.length; childParamCount++)
                                addChildElement(params[childParamCount].getName(),
                                        (String) params[childParamCount].getValue(),
                                        params[childParamCount].getType());
                        }
                    }
                }
            }
        }
    }


    public ArrayList<String[]> getResult() throws SOAPException{
        ArrayList<String[]> result = new ArrayList<String[]>();
        Iterator responses = soapResponse.getSOAPBody().getChildElements();  
        while (responses.hasNext()){
            Node temp = (Node) responses.next();
            result.addAll(parseResponse(temp));
            temp = null;
        }
        return result;
    }

    private ArrayList<String[]> parseResponse(Node element){
        ArrayList<String[]> result = new ArrayList<String[]>();
        if(element.hasChildNodes()){
            NodeList childNodes = element.getChildNodes();
            Node temp = null;
            for (int i=0; i<childNodes.getLength(); i++){
                temp =  childNodes.item(i);
                result.addAll(parseResponse(temp));
                temp = null;
            }
        }else{
            String temp[] = new String[2];
            temp[0] = element.getNodeName();
            temp[1] = element.getNodeValue();
            result.add(temp);
        }
        return result;
    }


    public boolean doesOutputMatches(QName[] output){
        boolean doesMatch = true;
        if(output!=null){
            if(soapResponse == null)
                doesMatch = false;
            else{
                //check the formats....
            }
        }else if(soapResponse != null)
            doesMatch = false;
        return doesMatch;
    }
    
}
