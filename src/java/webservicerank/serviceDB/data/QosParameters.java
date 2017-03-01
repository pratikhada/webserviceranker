/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservicerank.serviceDB.data;

/**
 *
 * @author hp
 */
public class QosParameters {

    //System related QoS parameters
    float reliability, efficiency, availability;

    //Non-system related QoS parameter
    float cost_effectiveness, reputation;

    //Overall QoS value of the service
    float qosValue;

    public void calcReliability(float maxE, float minE, float curE){
        if (maxE == minE)
            reliability = 1.0f;
        else
            reliability = (curE-minE)/(maxE-minE);
    }

    public float getReliability(){
        return reliability;
    }

    public void calcEfficiency(float maxT,float minT,float curT){
        if (maxT == minT)
            efficiency = 1.0f;
        else
            efficiency= (maxT-curT)/(maxT-minT);

    }
     public float getEfficiency(){
        return efficiency;
    }

    public void calcAvailability(float invoke_success,float invoke_request)
    {
        if (invoke_request == 0)
            availability = 0;
        else
            availability = invoke_success/invoke_request;
    }
    public float getAvailability(){
        return availability;
    }
    public void calcCosteffectiveness(float maxC,float minC,float curC)
    {
         if (maxC == minC)
            cost_effectiveness = 1.0f;
        else
            cost_effectiveness= ((maxC-curC))/(maxC-minC);
    }
    public float getCosteffectiveness(){
        return cost_effectiveness;
    }
    public void calcReputation(float maxR, float minR, float user_rating)
    {
        if (user_rating == 0){
            reputation = 0.0f;
        }else if (maxR == minR){
            reputation =1.0f;
        }else{
            reputation = (user_rating - minR)/(maxR-minR);
        }     
    }

    public float getReputation()
    {
        return reputation;
    }

    public void setQosvalue(float qosvalue)
    {
      qosValue=qosvalue;
    }

    public float getQosvalue()
    {
        return qosValue;
    }

    public void display() {
        System.out.println("reliability="+reliability+"  efficiency="+efficiency+"  availability="+availability);
        System.out.println("cost_effectiveness="+cost_effectiveness+"  reputation="+reputation);
        System.out.println("qosValue="+qosValue);
    }
  }
