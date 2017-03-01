/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservice.webserviceclient;

import com.wsr.dao.ServiceDao;
import com.wsr.dto.Service;
import com.wsr.dto.ServicePk;
import com.wsr.exceptions.ServiceDaoException;
import com.wsr.factory.ServiceDaoFactory;
import java.util.ArrayList;
import javax.xml.namespace.QName;
import webservice.wsdl.WSDLHandler;
import webservice.wsdl.WSDLHandlerFactory;
import webservice.wsdl.implementation.Parameter;
import webservice.wsdl.implementation.SoapMessage;

/**
 *
 * @author webservicerank
 */
public class ServiceUser {

    private QName service;
    private WSDLHandler wsdl;
    private String portOperationMap[][];
    private String operation;
    private String ports[];
    private String currPort;

    public ServiceUser(String wsdlUrl, String serviceName) throws Throwable{
            try{
                wsdl = WSDLHandlerFactory.create(wsdlUrl);
            }catch(Exception ex){
                String error = (ex.getMessage() != null)?ex.getMessage():"Error in reading";
                throw new Throwable(error);
            }catch (Throwable ex){
                String error = (ex.getMessage() != null)?ex.getMessage():"Error in reading";
                throw new Throwable(error);
            }
            QName[] names = wsdl.serviceNames();
            for(int i=0; i<names.length; i++)
                if(names[i].getLocalPart().equals(serviceName)){
                    service = names[i];
                    break;
                }
            if(service == null)
                throw new Throwable("SERVICE_NAME_ERROR");
    }
    
    public String[] listOperations(){
        ports = wsdl.servicePortNames(service);
        portOperationMap = new String[ports.length][];
        int opCount = 0;
        for (int i=0; i<ports.length; i++){
            portOperationMap[i] = wsdl.operationNames(service, ports[i]);
            opCount += portOperationMap[i].length;
        }
        String ops[] = new String[opCount];
        int index = 0;
        for (int i=0; i<ports.length; i++){
            for (int j=0; j<portOperationMap[i].length; j++)
                ops[index++] = portOperationMap[i][j];
        }
        return ops;
    }

    //This method isn't complete..
    public ArrayList<String[]>[] listInputs(String methodName){
        operation = methodName;
        ArrayList<String[]> inputs[] = null;

        //find out inputParts[] corresponding to the methodName
        for (int i=0; i<ports.length; i++){
            for (int j=0; j<portOperationMap[i].length; j++){
                if (methodName.equals(portOperationMap[i][j])){
                    currPort = ports[i];
                    break;
                }
            }
        }

        ArrayList<Parameter>[] leafParams = wsdl.getLeafParameters(wsdl.inputPartElementNames
                (service, currPort, operation),null);
        //copy the leave parameters into inputs
        if (leafParams != null){
            inputs = (ArrayList<String[]>[])new ArrayList[leafParams.length];
            
            for (int i=0; i<leafParams.length; i++){
                inputs[i] = new ArrayList<String[]>();
                for (int j=0; j<leafParams[i].size(); j++){
                    String[] temp = new String[2];
                    temp[0] = leafParams[i].get(j).getName();
                    temp[1] = leafParams[i].get(j).getType();
                    inputs[i].add(temp);
                }
            }
        }
        return inputs;
    }


    public ArrayList<String[]> callMethod(String[] params, long[] serviceTime, int[] invoke_counts) throws Throwable{
        SoapMessage soapMessage = new SoapMessage();
        soapMessage.createSOAPMessage();
        soapMessage.prepareRequestBody(wsdl.inputPartElementNames
                (service, currPort, operation), wsdl, params);
        serviceTime[0] = soapMessage.interactWithService(wsdl.getPortLocation(service, currPort), invoke_counts);
        QName[] output = wsdl.outputPartElementNames(service,currPort, operation);
        if(soapMessage.doesOutputMatches(output)){
            invoke_counts[2] ++;
        }
        ArrayList<String[]> result = soapMessage.getResult();
        String values[] = new String[result.size()];
        for (int i=0; i<values.length; i++)
            values[i] = result.get(i)[1];
        QName[] outputParts = wsdl.outputPartElementNames(service, currPort, operation);
        ArrayList<Parameter>[] outLeaves = wsdl.getLeafParameters(outputParts, values);
        result = null;
        result = new ArrayList<String[]>();
        for (int i=0; i<outLeaves.length; i++){
            for (int j=0; j<outLeaves[i].size(); j++){
                result.add(new String[]{outLeaves[i].get(j).getName(), (String)outLeaves[i].get(j).getValue()}); 
            }
        }
        return result; 
    }

  

//    public static void main(String[] args) throws Throwable {
//        String sn = "DifferentiationService";
//        String url = "http://localhost:8080/WebServicesApp/DifferentiationService?WSDL";
//        ServiceUser su = new ServiceUser(url, sn);
//        String[] ops = su.listOperations();
//        for (int l=2; l<ops.length; l++){
//            System.out.println("\nmethod = "+ops[l]);
//            // System.out.println(su.getSoapRequest(ops[i]));
//            ArrayList<String[]>[] listInputs = su.listInputs(ops[l]);
//            for (int i=0; i<listInputs.length; i++){
//                for (int j=0; j<listInputs[i].size(); j++){
//                    for (int k=0; k<2; k++){
//                        System.out.print("   "+listInputs[i].get(j)[k]);
//                    }
//                    System.out.println();
//                }
//            }
//            String params[] = new String[]{"25", "15"};
//            ArrayList<String[]> callMethod = su.callMethod(params, new long[1]);
//            for (int i=0; i<callMethod.size(); i++)
//                System.out.println(callMethod.get(i)[0]+" = "+ callMethod.get(i)[1]);
//        }
//    }

    public void insertRating(int s_id, int rating) throws Throwable {
        ServiceDao factory = ServiceDaoFactory.create();
        try {
            Service[] services = factory.findWhereServiceIdEquals(s_id);
            if (services != null && services.length != 0){
                rating += services[0].getUserRating();
                services[0].setUserRating(rating);
                services[0].setRatingUserCount(services[0].getRatingUserCount()+1);
                factory.update(new ServicePk(s_id), services[0]);
            }
        } catch (ServiceDaoException ex) {
//            Logger.getLogger(ServiceUser.class.getName()).log(Level.SEVERE, null, ex);
            String error = (ex.getMessage()!=null)?":"+ex.getMessage():"";
            throw new Throwable("Error while rating..."+error);
        }
    }
    
}
