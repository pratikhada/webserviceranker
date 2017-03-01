/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservicerank.servicePublish;

import java.util.logging.Level;
import java.util.logging.Logger;
import webservice.webserviceclient.WebServiceValidator;
import webservicerank.ontologyDB.OntologyManager;
import webservicerank.uiAppInterface.ServiceDetail;

/**
 *
 * @author BIKASH
 */
public class ServicePublish {

    private PublishDB publishDB;

    public ServicePublish() throws Throwable{        
    }


    /**
     * This method validates that the service being published is a valid service
     * and is not published already
     * @param wsdlUrl
     * @param serviceName
     * @param serviceDelay
     * @return
     * @throws Throwable
     */
    public boolean[] validate(String wsdlUrl, String serviceName,long[] serviceDelay, int[] invoke_counts)throws Throwable{
        boolean isValid[] = new boolean[2];
        isValid[0] = true;   //valid if not stored in database already
        isValid[1] = true;   //valid if the service exists (wsdl is valid)
        if (publishDB == null)
            publishDB = new PublishDB();
        try {
            isValid[0] = !publishDB.isAreadyStored(wsdlUrl, serviceName); //False if DB contains the service already
        } catch (Throwable ex) {
            Logger.getLogger(ServicePublish.class.getName()).log(Level.SEVERE, null, ex);
            String error =ex.getMessage();
            error = (error != null)?":"+error:"";
            throw new Throwable(" Database access error "+error);
        }
        if (! isValid[0])
            return isValid;
        WebServiceValidator validator = null;
        try {
            validator = new WebServiceValidator(wsdlUrl, serviceName);
        } catch (Throwable ex) {
            Logger.getLogger(ServicePublish.class.getName()).log(Level.SEVERE, null, ex);
            String error =ex.getMessage();
            error = (error != null)?":"+error:"";
            throw new Throwable(" Validation error"+error);
        }
        publishDB = new PublishDB();        
        serviceDelay[0] = 0;
        isValid[1] = validator.validate(serviceDelay, invoke_counts);                //False if the service is not valid
        return isValid;
    }


    /**
     * A method for publishing the service details and ontology
     * @param sd
     * @return
     * @throws Throwable
     */
    public String[] publish(ServiceDetail sd, int publisher_id) throws Throwable{

        String message[] = new String[2];
        if (publishDB == null)
            publishDB = new PublishDB();
        OntologyManager ontManager = new OntologyManager();
        
        try {
            message[0] = ontManager.publish(sd.getOwlUrl());
        } catch (Throwable ex) {
            message[0]="Error! Ontology couldn't be published<br/>"+ex.getMessage();
        }
        try {
            message[1] = publishDB.publish(sd, publisher_id);
        } catch (Throwable ex) {
            String error =ex.getMessage();
            error = (error != null)?"Error :"+error:"";
            message[1] = "Error! Service details couldn't be published"+error;
            Logger.getLogger(ServicePublish.class.getName()).log(Level.SEVERE, null, ex);
        }
        return message;
    }


//    public static void main(String[] args) throws Throwable {
//        ServicePublish publisher = null;
//        try {
//            publisher = new ServicePublish();
//        } catch (Throwable ex) {
//            Logger.getLogger(ServicePublish.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        long serviceDuration[] = new long[1];
//        ServiceDetail serviceDetail = new ServiceDetail();
//        serviceDetail.readParams1();
//        boolean valid[] = publisher.validate(serviceDetail.getWsdlURL(),
//                serviceDetail.getName(), serviceDuration);
//        if (valid[0] && valid[1]){
//            String[] publish = publisher.publish(serviceDetail);
//            for (int i=0; i<publish.length; i++){
//                System.out.println("Service duratin = "+serviceDuration[0]);
//                System.out.println("Message = "+publish[i]);
//            }
//        }else if ( !valid[0] ){
//            System.out.println("Already exists");
//        }else{
//            System.out.println("Invalid service");
//        }
        
        
//    }
}
