/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservicerank.serviceDB;

import com.wsr.dto.*;
import com.wsr.factory.*;
import com.wsr.exceptions.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import webservicerank.serviceDB.data.*;
import com.wsr.dao.*;
import webservicerank.serviceRanker.MatchedService;


/**
 *
 * @author vhishma
 */
public class RankingDBManager {

    private TempData rankingData;
    private QosParameters []qParameter ;
    private ServiceDbInteracter serviceDbInteracter = null;


    public void createTemporary(boolean isCategorized){
        //Create temporary tables
        rankingData = new TempData(isCategorized);

    }


    public void cleanObject(){
        rankingData = null;
        qParameter = null;
        serviceDbInteracter = null;
    }

    //Returns the property name (String ) from the property ID.
    public String getPropertyName(int p_id) throws Throwable{
        return PropertyDaoFactory.create().findByPrimaryKey(p_id).getPropertyName();
    }


    public String getNextPropertyName(int[] prop_id,String[] cat, boolean isCreate) throws Throwable{
        if (serviceDbInteracter == null)
            serviceDbInteracter = new ServiceDbInteracter();
        return serviceDbInteracter.nexPropertyName(prop_id, cat, isCreate);
    }

    public void setPropertyDOM(int p_id, double[] dom, int prop_cat){
        rankingData.setPropertyDOM(p_id, dom, prop_cat);
    }

    
    /**
     * A method for finding relevant services based on the relevant properties
     * @throws Throwable
     */
     private void findRelServices() throws Throwable{
        ArrayList<Integer> matchedPropIDs = rankingData.getProperties();
        for(int j=0; j<matchedPropIDs.size(); j++){
            ServiceProperty sp[] = ServicePropertyDaoFactory.create().findByDynamicWhere(" property_ID = ? ",
                    new Object[]{matchedPropIDs.get(j)});
            if (sp != null || sp.length != 0){
                for(int i=0; i<sp.length; i++){
                    rankingData.addRelevantService(sp[i].getServiceId(),matchedPropIDs.get(j));
                }
            }
        }       
        rankingData.finishFindRelServices();
//        rankingData.displayPropertyDOM();
//        rankingData.displayRelService();

    }


    private void calcQosParameter() throws ServiceDaoException
    {
        //Value of QoS parameter
        ServiceDao sdao= ServiceDaoFactory.create();
        Service []sresult = null;
        ArrayList <Float> execution_rate=new ArrayList<Float>();
        ArrayList <Float> execution_time=new ArrayList<Float>();
        ArrayList <Float> service_cost=new ArrayList<Float>();
        ArrayList <Float> rep_array = new ArrayList<Float>();

        int noOfService = rankingData.getNoOfServices();
        qParameter = new QosParameters[noOfService];
        float erate;
        for(int i=0; i<noOfService; i++){
            sresult = sdao.findByDynamicWhere("service_ID = ? ",new Object[]{rankingData.getServiceId(i)});
            if (sresult != null && sresult.length != 0){
                 if (sresult[0].getInvokeSuccessCount() == 0){
                     erate = 0.0f;
                 }else{
                     erate= sresult[0].getExecuteSuccessCount()/sresult[0].getInvokeSuccessCount();
                 }
                qParameter[i] = new QosParameters();
                 execution_rate.add(erate);
                 execution_time.add((float)sresult[0].getExeDuration());
                 service_cost.add(sresult[0].getCost());
                 rep_array.add(sresult[0].getRating());
                 qParameter[i].calcAvailability(sresult[0].getInvokeSuccessCount(),sresult[0].getInvokeRequestCount());
            }
         }
        
        for(int i=0;i<noOfService;i++){
            if (qParameter[i] != null){
                qParameter[i].calcReliability(Collections.max(execution_rate),Collections.min(execution_rate), execution_rate.get(i));
                qParameter[i].calcEfficiency(Collections.max(execution_time),Collections.min(execution_time),execution_time.get(i));
                qParameter[i].calcCosteffectiveness(Collections.max(service_cost), Collections.min(service_cost), service_cost.get(i));
                qParameter[i].calcReputation(Collections.max(rep_array), Collections.min(rep_array),rep_array.get(i));
            }
        }

    }

    private float calcQosValue(float [] weight){
        //QoS value of services
        float maxQosValue = 0;
        float tempqosvalue=0;
        for(int i=0;i<qParameter.length;i++)
        {
            if (qParameter[i] != null){
                tempqosvalue=weight[0]*qParameter[i].getReliability()+weight[1]*qParameter[i].getEfficiency()+weight[2]*qParameter[i].getAvailability()
                           +weight[3]*qParameter[i].getCosteffectiveness()+weight[4]*qParameter[i].getReputation();
                if (tempqosvalue/5 > maxQosValue){
                    maxQosValue = tempqosvalue/5;
                }
                qParameter[i].setQosvalue(tempqosvalue/5);
            }
        }
        return maxQosValue;
   }


    public MatchedService[] getSearchResult(float [] weight, float[] maxValues) throws Throwable {
//        System.out.println("Before dependent properties reduction:");
//        rankingData.displayPropertyDOM();
        findRelServices();
        rankingData.displayRelService();
        rankingData.reduceDependentProperties();
//        System.out.println("After dependent property reduction");
//        rankingData.displayPropertyDOM();
        rankingData.findServiceDOM();
        calcQosParameter();
        maxValues[0] = calcQosValue(weight);        //Maximum QoS value
        int noOfService = rankingData.getNoOfServices();
        MatchedService[] result = new MatchedService[noOfService];
        int s_id;
        maxValues[1] = 0;                           //Maximum Degree of Match
        for (int i=0; i<noOfService; i++){
            if (qParameter[i] != null){
                result[i] = new MatchedService();
                s_id = rankingData.getServiceId(i);
                result[i].setServiceId(s_id);
                result[i].setDOM(rankingData.getServiceDOM(s_id));
                if (result[i].getDOM() > maxValues[1]){
                    maxValues[1] = (float)(double) result[i].getDOM();
                }
                result[i].setQosParameters(qParameter[i]);
            }
        }
        cleanObject();
        return result;
    }


    public void updateNamespaces(ArrayList<String> namespaces) throws Throwable {
        if (serviceDbInteracter == null)
            serviceDbInteracter = new ServiceDbInteracter();
        serviceDbInteracter.updateNamespaces(namespaces);
    }

    public String[] getNamespaces() throws Throwable {
        if (serviceDbInteracter == null)
            serviceDbInteracter = new ServiceDbInteracter();
        ArrayList<String> namespaces = serviceDbInteracter.getNamespaces();
        String Nss[] = new String[namespaces.size()];
        for (int i=0; i<namespaces.size(); i++){
            Nss[i] = namespaces.get(i);
        }
        return Nss;
    }


    public String getServiceNameNDescription(int serviceID) throws Throwable{
        if (serviceID == 0){
            return null;
        }
        Service service = ServiceDaoFactory.create().findWhereServiceIdEquals(serviceID)[0];
        return service.getServiceName()+";"+service.getDescription();
    }


    public String[] getServiceForClient(int s_id) throws Throwable{
         Service service = ServiceDaoFactory.create().findWhereServiceIdEquals(s_id)[0];
         return new String[]{service.getServiceName(), service.getServiceWsdl(), service.getDescription()};
    }

    public float getRating(int service_id){
        float rating = 0.0f;
        try {
            Service[] services = ServiceDaoFactory.create().findWhereServiceIdEquals(service_id);
            if (services != null && services.length != 0){
                rating = services[0].getRating();
            }
        } catch (ServiceDaoException ex) {
//            Logger.getLogger(RankingDBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rating;
    }

    
    public static void main(String[] args) throws Throwable {
        RankingDBManager r = new RankingDBManager();
//        r.setPropertyDOM(1, new double[]{1.01, 26.32}, 'c');
//        r.setPropertyDOM(2, new double[]{1.01, 26.32}, 'c');
//        r.setPropertyDOM(1, new double[]{1.01, 26.32}, 'c');
//        r.setPropertyDOM(9, new double[]{1.01, 26.32}, 'c');
//        r.setPropertyDOM(1, new double[]{1.01, 26.32}, 'c');
        r.getNamespaces();
    }



}
