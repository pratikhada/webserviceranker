/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservicerank.serviceRanker;

import com.wsr.factory.ServiceDaoFactory;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import webservicerank.serviceDB.RankingDBManager;
import webservicerank.uiAppInterface.ServiceDetail;

/**
 *
 * @author BIKASH
 */
public class ServiceRanker {

    private RankingDBManager rankingDBManager;
    private IrrPropIdentifier irrProperty;
    private SearchResultHandler resultHandler;
    private ArrayList<Integer> ranks;

    public ServiceRanker() throws Throwable{
        rankingDBManager = new RankingDBManager();
        irrProperty = new IrrPropIdentifier();
        ranks = null;
    }

    /**
     * A method for ranking the services
     * @param sd
     * @param isPriority
     */
    public String rank(ServiceDetail sd, float [] weight, boolean isPriority, boolean isAvoidSemanticMatchmake){
        String response = "";
        try {

            rankingDBManager.createTemporary(sd.propertyCategorized());

            //Perform matchmaking based on ontologies between the service properties
            //entered by the user and service properties stored in the databse.
            if (!isAvoidSemanticMatchmake)
                irrProperty.matchWithOntology(sd, rankingDBManager, isPriority);
            else
                irrProperty.matchWithDBProperties(sd, rankingDBManager, isPriority);

            irrProperty = null;

            float[] maxValues = new float[2];
            resultHandler = new SearchResultHandler();
            resultHandler.setMatchedServices(rankingDBManager.getSearchResult(weight, maxValues));
            resultHandler.setMaxValues(maxValues);
//            resultHandler.display();
            ranks = null;
            ranks = resultHandler.rankByDomNQos();
            for (int i=0; i<ranks.size(); i++){
//                System.out.println(i);
                if (resultHandler.getServiceID(ranks.get(i)) != 0){
                    response += rankingDBManager.getServiceNameNDescription(resultHandler.getServiceID(ranks.get(i)))+";" ;
                    response += String.valueOf((int)rankingDBManager.getRating(resultHandler.getServiceID(ranks.get(i))))+";"+resultHandler.getServiceID(ranks.get(i))+":";
//                    System.out.println("Final DOMNQoS="+resultHandler.getDomNQos(ranks.get(i)));
                }
            }
//            String tofile = "service_id,service_name,DOM,QoS,Ranking Value,availability,Efficiency,reliability,cost-effectiveness,reputation\n";
//            for (int i=0; i<ranks.size(); i++){
//                int s_id = resultHandler.getServiceID(ranks.get(i));
//                tofile += s_id+","+ServiceDaoFactory.create().findWhereServiceIdEquals(s_id)[0].getServiceName()+",";
//                tofile += resultHandler.getDOM(ranks.get(i))+","+resultHandler.getQosValue(ranks.get(i))+","+resultHandler.getDomNQos(ranks.get(i))+",";
//                tofile += resultHandler.getAvailibility(ranks.get(i))+","+resultHandler.getEfficiency(ranks.get(i))+","+
//                        resultHandler.getReliability(ranks.get(i))+","+resultHandler.getCostEffectiveness(ranks.get(i))+","+resultHandler.getReputation(ranks.get(i))+"\n";
//            }
//            FileWriter writer = new FileWriter("F:\\test_final_Result.csv");
//            writer.append(tofile);
//            writer.flush();
//            writer.close();
//            System.out.println("Filewritten");
        } catch (Throwable ex) {
            response = "Error:";
            response += (ex.getMessage() != null)?ex.getMessage():"The task couldn't be performed";
        }
        return response;
    }


    public String rankByOptions(int option) throws Throwable{
        ranks = null;
        switch(option){
            case 0:
                ranks = resultHandler.rankByDomNQos();
                break;
            case 1:
                ranks = resultHandler.rankByAvailability();
                break;
            case 2:
                ranks = resultHandler.rankByReliability();
                break;
            case 3:
                ranks = resultHandler.rankByEfficiency();
                break;
            case 4:
                ranks = resultHandler.rankByCostEffectiveness();
                break;
            case 5:
                ranks = resultHandler.rankByReputation();
                break;
            case 6:
                ranks = resultHandler.rankByQos();
                break;
            case 7:
                ranks = resultHandler.rankByDom();
                break;
        }
        String response = "";
        for (int i=0; i<ranks.size(); i++){
//            System.out.println(i);
            if (resultHandler.getServiceID(ranks.get(i)) != 0){
                response += rankingDBManager.getServiceNameNDescription(resultHandler.getServiceID(ranks.get(i)))+";" ;
                response += String.valueOf((int)rankingDBManager.getRating(resultHandler.getServiceID(ranks.get(i))))+";"+resultHandler.getServiceID(ranks.get(i))+":";
//                System.out.println("Final rep="+resultHandler.getReputation(ranks.get(i)));
            }
        }
        return response;
    }


    public String[] getService(int s_id, int rank) throws Throwable {
//        System.out.println("the service_id======================="+s_id);
        String[] service1 = rankingDBManager.getServiceForClient(s_id);
        String[] service = new String[service1.length+1];
        for (int i=0; i<service1.length; i++){
            service[i] = service1[i];
        }
//        System.out.println("Rank="+(int)(resultHandler.getReputation(ranks.get(rank))*10));
        service[service1.length]= String.valueOf((int)rankingDBManager.getRating(s_id));
//        System.out.println("In string="+service[service1.length]);
        return service;
    }




    public static void main(String[] args) throws Throwable {
        ServiceRanker ranker = new ServiceRanker();
        ServiceDetail sd = new ServiceDetail();
        sd.readParams1();
        float[] weight = {0.2f, 0.2f, 0.2f, 0.2f, 0.2f};
        String rank = ranker.rank(sd, weight, false, false);
        System.out.println("response = "+rank);
//        HashMap<Integer, String> hm = new ConstMap<Integer, String>();
//        hm.put(12,"Bikash");
//        hm.put(34, "Pratik");
//        hm.put(456, "Bhishma");
//        Integer[] toArray = (Integer[]) hm.keySet().toArray();
//        for (int i=0; i<toArray.length; i++)
//            System.out.println(toArray[i]);
    }


}
