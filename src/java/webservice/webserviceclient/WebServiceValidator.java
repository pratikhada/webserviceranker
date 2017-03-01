/*
 * To change this inputCountlate, choose Tools | Templates
 * and open the inputCountlate in the editor.
 */

package webservice.webserviceclient;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import webservice.wsdl.WSDLHandler;
import webservice.wsdl.WSDLHandlerFactory;
import webservice.wsdl.implementation.SoapMessage;

/**
 *
 * @author BIKASH
 */
public class WebServiceValidator {

    QName service;
    WSDLHandler wsdl;


    public WebServiceValidator(String wsdlUrl, String serviceName) throws Throwable{
        try {
            wsdl = WSDLHandlerFactory.create(wsdlUrl);
        } catch (Throwable ex) {
            Logger.getLogger(WebServiceValidator.class.getName()).log(Level.SEVERE, null, ex);
            String error =ex.getMessage();
            error = (error != null)?"Error :"+error:"";
            throw new Throwable("Error initializing validator! "+error);
        }
        QName[] names = wsdl.serviceNames();
        for(int i=0; i<names.length; i++)
            if(names[i].getLocalPart().equals(serviceName)){
                service = names[i];
                break;
            }
        if(service == null)
            throw new Throwable("Service name doesn't match with service names in WSDL");
    }


    private String[] getPorts(){
        return wsdl.servicePortNames(service);
    }


    private boolean validatePort(String port, long[] serviceDelay, int[] invoke_counts) throws Throwable{
        boolean isValid = false;
        String[] operations = wsdl.operationNames(service, port);
        SoapMessage soapMessage = null;

        //check for all operations
        for(int op =0; op<operations.length; op++){
            soapMessage = new SoapMessage();
            soapMessage.createSOAPMessage();
            QName[] input = wsdl.inputPartElementNames(service,port, operations[op]);
            if(input != null)
                soapMessage.prepareRequestBody(input, wsdl, null);
            serviceDelay[0] = soapMessage.interactWithService(wsdl.getPortLocation(service, port), invoke_counts);
            QName[] output = wsdl.outputPartElementNames(service,port, operations[op]);
            isValid = soapMessage.doesOutputMatches(output);
            if(isValid){   //If any of the operations is invoked successfully, the service is valid
                invoke_counts[2]++;
                break;
            }
            soapMessage = null;
        }
        return isValid;
    }




    /**
     * Check whether the service is valid/exists and also computes the service delay
     * @param serviceDelay, for storing the service delay
     * @return, true if valid
     */
    public boolean validate(long[] serviceDelay, int[] invoke_counts) throws Throwable{
        boolean isValid = false;
        try {
            String[] ports = getPorts();            
            for (int i = 0; i < ports.length; i++) {
                if (validatePort(ports[i], serviceDelay, invoke_counts)) {
                    isValid = true;               //true if the service exists
                    break;
                }
            }
        } catch (Throwable ex) {
            isValid = false;
            String error =ex.getMessage();
            error = (error != null)?"Error :"+error:"";
            throw new Throwable("Error in validation! "+error);
        }
        return isValid;
    }


//    public static void main(String[] args) {
//        String url = "http://localhost:8080/EELab4Webservice/MyDatabaseService?WSDL";
//        String serviceName = "MyDatabaseService";
//        try {
//            WebServiceValidator validator = new WebServiceValidator(url, serviceName);
//            long[] sd = new long[1];
//            boolean valid = validator.validate(sd);
//            System.out.println(valid);
//            System.out.println("service time="+sd[0]+" nanoseconds");
//        } catch (Throwable ex) {
//            Logger.getLogger(WebServiceValidator.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

}
