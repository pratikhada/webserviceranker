/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservicerank.serviceRanker;

import java.util.ArrayList;

/**
 *
 * @author hp
 */
public class SearchResultHandler {

    private MatchedService[] result;
//  private Double result[] = {0.0, 3.45, 12.43, 32.54, 7.90,12.43};
    private double min, max;
    private ArrayList<Integer> rank;

    private float maxDOM, maxQoS;

    private double getValue(char option, int index){
        switch (option){
            case 'D':
                return getDOM(index);
            case 'd':
                return getDomNQos(index);
            case 'Q':
                return getQosValue(index);
            case 'R':
                return getReliability(index);
            case 'E':
                return getEfficiency(index);
            case 'A':
                return getAvailibility(index);
            case 'C':
                return getCostEffectiveness(index);
            case 'r':
                return getReputation(index);
        }
        return 0.0;
    }


    //A method for comparing two two services with equal value of ranking
    private boolean doAllow(int index, int get, char option){
        boolean allow = false;
        char[] options = new char[]{'d','D','Q','A','R','r','E','C'};
        for (int i=0; i<options.length; i++){
            if (option != options[i]){
                if (getValue(options[i],index) > getValue(options[i], get)){
                    allow = true;
                    break;
                }else if(getValue(options[i],index) < getValue(options[i], get)){
                    break;
                }
            }
        }
        return allow;
    }

    private void rank(double current, int index, char option){
        if ( current > max){
            rank.add(0, index);
            max = current;
//            System.out.println(index+" at position "+0+" value="+current);
        }else if (current < min ){
            rank.add(index);
            min = current;
//            System.out.println(index+" at position last value="+current);
        }else{
            for (int j=0; j<rank.size(); j++){
                if ( current > getValue(option, rank.get(j))){
                    rank.add(j, index);
//                    System.out.println(index+" at position "+j+" value="+current);
                    break;
                }else if (current == getValue(option, rank.get(j)) && doAllow(index,rank.get(j), option)){
                    rank.add(j, index);
                    break;
                }
            }
        }
    }


    public ArrayList<Integer> rankByDomNQos(){
        rank = null;
        rank = new ArrayList<Integer>();
        if (result == null || result.length == 0){
            return rank;
        }
        min = max = getDomNQos(0);
        rank.add(0);
        for (int i=0; i<result.length; i++){
            rank((double)getDomNQos(i), i, 'd');
        }

        return rank;
    }

    public ArrayList<Integer> rankByDom(){
        rank = null;
        rank = new ArrayList<Integer>();
        if (result == null || result.length == 0){
            return rank;
        }
        min = max = getDOM(0);
        rank.add(0);
        for (int i=0; i<result.length; i++){
            rank((double)getDOM(i), i, 'D');
        }

        return rank;
    }

    public ArrayList<Integer> rankByQos(){
        rank = null;
        rank = new ArrayList<Integer>();
        if (result == null || result.length == 0){
            return rank;
        }
        min = max = (double)getQosValue(0);
        rank.add(0);
        for (int i=0; i<result.length; i++){
            rank((double)getQosValue(i), i, 'Q');
        }

        return rank;

    }

    public ArrayList<Integer> rankByReliability(){
        rank = null;
        rank = new ArrayList<Integer>();
        if (result == null || result.length == 0){
            return rank;
        }
        min =  max = getReliability(0);
        rank.add(0);
        for (int i=0; i<result.length; i++){
            rank((double)getReliability(i), i, 'R');
        }

        return rank;
    }

    public ArrayList<Integer> rankByEfficiency(){
        rank = null;
        rank = new ArrayList<Integer>();
        if (result == null || result.length == 0){
            return rank;
        }
        min =  max = getEfficiency(0);
        rank.add(0);
        for (int i=0; i<result.length; i++){
            rank((double)getEfficiency(i), i, 'E');
        }

        return rank;
    }

    public ArrayList<Integer> rankByAvailability(){
        rank = null;
        rank = new ArrayList<Integer>();
        if (result == null || result.length == 0){
            return rank;
        }
        min =  max = getAvailibility(0);
        rank.add(0);
        for (int i=0; i<result.length; i++){
            rank((double)getAvailibility(i), i, 'A');
        }

        return rank;
    }

    public ArrayList<Integer> rankByCostEffectiveness(){
        rank = null;
        rank = new ArrayList<Integer>();
        if (result == null || result.length == 0){
            return rank;
        }
        min =  max = getCostEffectiveness(0);
        rank.add(0);
        for (int i=0; i<result.length; i++){
            rank((double)getCostEffectiveness(i), i, 'C');
        }

        return rank;
    }

    public ArrayList<Integer> rankByReputation(){
        rank = null;
        rank = new ArrayList<Integer>();
        if (result == null || result.length == 0){
            return rank;
        }
        min =  max = getReputation(0);
        rank.add(0);
        for (int i=0; i<result.length; i++){
            rank((double)getReputation(i), i,'r');
        }

        return rank;
    }

    public void setMatchedServices(MatchedService[] matchedServices) {
        this.result = matchedServices;
    }

    public double getDOM(int i){
        if(result[i] == null)
            return 0;
        return result[i].getDOM();
    }

    public double getDomNQos(int i){
        if(result[i] == null)
            return 0;
        return result[i].getDomNQos(maxDOM, maxQoS);
    }

    public double getQosValue(int i){
        if(result[i] == null)
            return 0;
        return result[i].getQosParams().getQosvalue();
    }

    public double getReliability(int i){
        if(result[i] == null)
            return 0;
        return result[i].getQosParams().getReliability();
    }

    public double getAvailibility(int i){
        if(result[i] == null)
            return 0;
        return result[i].getQosParams().getAvailability();
    }

    public double getEfficiency(int i){
        if(result[i] == null)
            return 0;
        return result[i].getQosParams().getEfficiency();
    }

    public double getCostEffectiveness(int i){
        if(result[i] == null)
            return 0;
        return result[i].getQosParams().getCosteffectiveness();
    }

    public double getReputation(int i){
        if(result[i] == null)
            return 0;
        return result[i].getQosParams().getReputation();
    }

    public int getServiceID(int i){
        if(result[i] == null)
            return 0;
        return result[i].getServiceId();
    }

    public static void main(String[] args) {
        SearchResultHandler handler = new SearchResultHandler();
        ArrayList<Integer> rankByDom = handler.rankByDom();
        System.out.println();
        for (int i=0; i<rankByDom.size(); i++){
            System.out.print("         "+rankByDom.get(i));
        }
    }

    void setMaxValues(float[] maxValues) {
        maxDOM = maxValues[1];
        maxQoS = maxValues[0];
        if (maxDOM == 0)
            maxDOM = 1;
        if (maxQoS == 0)
            maxQoS = 1;
    }

    
}
