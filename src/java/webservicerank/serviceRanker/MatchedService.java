/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservicerank.serviceRanker;

import webservicerank.serviceDB.data.QosParameters;

/**
 *
 * @author hp
 */
public class MatchedService {
    private int serviceId;
    private double dom;
    private QosParameters qos;

    public void setServiceId(int sId){
        serviceId = sId;
    }

    public void setDOM(double dom){
        this.dom = dom;
    }

    public void setQosParameters(QosParameters qosParams){
        qos = qosParams;
    }

    public int getServiceId(){
        return serviceId;
    }

    public Double getDOM(){
        return dom;
    }

    public double getDomNQos(float maxDOM, float maxQoS){
        return (dom/maxDOM + qos.getQosvalue()/maxQoS)/2;  //Division by max values for normalization
    }

    public QosParameters getQosParams(){
        return qos;
    }

    void display() {
        System.out.println("ServiceID= "+serviceId+"  DOM="+dom);
        qos.display();
    }
    
}
