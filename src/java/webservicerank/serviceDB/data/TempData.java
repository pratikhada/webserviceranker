/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservicerank.serviceDB.data;

import com.sun.faces.util.CollectionsUtils.ConstMap;
import com.wsr.factory.PropertyDaoFactory;
import com.wsr.factory.ServiceDaoFactory;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author BIKASH
 */
public class TempData {

    //Hashmaps to store properties, i.e., (property ID, degree of match[]) pairs.
    private HashMap<Integer, double[]> general, input, output, precondition, effect;

    //HashMap that represents the combined form of relevant properties
    private HashMap<Integer, double[]> properties;

    //Hashmap to store the service IDs of relevant services and the associated property IDs.
    private HashMap<Integer, ArrayList<Integer>> relevantServices;

    //ArrayList that represents the equivalent classes
    private ArrayList<ArrayList<Integer>> equivalences;

    //An array that contains the service IDs of relevant services
    private int serviceIds[];
    //Hashmap to store services with their degree of match, i.e.,
    //(service id, degree of match ) pairs.
    private HashMap<Integer,double[]> serviceDom;

    //If the properties are categorised
    private boolean categorized;

    public TempData(boolean isCategorized){
        this.categorized = isCategorized;
        general = null;
        input = null;
        output = null;
        precondition = null;
        effect = null;
        serviceDom = null;
//        if (!categorized)
//            general = new HashMap();
//        else{
//            input = new HashMap();
//            output = new HashMap();
//            precondition = new HashMap();
//            effect = new HashMap();
//        }
        properties = new ConstMap<Integer, double[]>();
        serviceIds = null;
        relevantServices = new HashMap<Integer, ArrayList<Integer>>();
    }


    /**
     * A method that returns the list of relevant property id's
     * @return
     */
    public ArrayList<Integer> getProperties(){
//        System.out.println("Matched properties="+properties);
        ArrayList<Integer> ids = new ArrayList<Integer>();
        Object[] p_ids = properties.keySet().toArray();
        for (int i=0; i<p_ids.length; i++){
            if (!ids.contains((Integer)p_ids[i]))
                ids.add((Integer) p_ids[i]);
        }
        return ids;
        /*
        if (!categorized){
            Set<Integer> keySet = general.keySet();
            Iterator<Integer> iterator = keySet.iterator();
            while(iterator.hasNext()){
                ids.add(iterator.next());
            }
        }else{
            Set<Integer> keySet = input.keySet();
            Iterator<Integer> iterator = keySet.iterator();
            while(iterator.hasNext()){
                ids.add(iterator.next());
            }
            Set<Integer> keySet1 = output.keySet();
            iterator = keySet1.iterator();
            while(iterator.hasNext()){
                ids.add(iterator.next());
            }
            Set<Integer> keySet2 = precondition.keySet();
            iterator = keySet2.iterator();
            while(iterator.hasNext()){
                ids.add(iterator.next());
            }
            Set<Integer> keySet3 = effect.keySet();
            iterator = keySet3.iterator();
            while(iterator.hasNext()){
                ids.add(iterator.next());
            }
        }
        return ids;
         * */
    }

    public int getNoOfServices(){
        if (relevantServices == null)
            return 0;
        else
            return relevantServices.size();
    }

    public int getServiceId(int position){
        return serviceIds[position];
//        return (Integer)relevantServices.get(position);
    }

    public double getServiceDOM(int serviceId) {
        double[] dom = null;
        for (int i=0; i<equivalences.size(); i++){
            if (equivalences.get(i).contains(serviceId)){
                dom = serviceDom.get(equivalences.get(i).get(0));
                break;
            }
        }
         
        double sum = 0.0;
        if (dom != null)
            for (int i=0; i<dom.length; i++)
                sum += dom[i]/dom.length;
        return sum;
    }


    /**
     * Sets the degree of match to the property
     * @param p_id; the property id which degree of match is to be set
     * @param dom; degree of match value
     * @param prop_cat, the category of property
     */
    public void setPropertyDOM(int p_id, double[] dom, int prop_cat){
        double[] result ;
        if (properties.containsKey(p_id)){
            double stored[] = properties.get(p_id);
            int length = (dom.length > stored.length)?dom.length:stored.length;
            result = new double[length];
            for (int i=0; i<length; i++){
                if (i<dom.length && i<stored.length){
                    result[i] = (dom[i]>stored[i])?dom[i]:stored[i];
                }else if (i<dom.length){
                    result[i] = dom[i];
                }else if (i<stored.length){
                    result[i] = length;
                }
            }
        }else{
            result = dom;
        }

        properties.put(p_id, result);
        /*
        switch (prop_cat){
            case 0:
                setPropertysDOM(input, p_id, dom);
                break;

            case 1:
                setPropertysDOM(output, p_id, dom);
                break;

            case 2:
                setPropertysDOM(precondition, p_id, dom);
                break;

            case 3:
                setPropertysDOM(effect, p_id, dom);
                break;

            case 4:
                setPropertysDOM(general, p_id, dom);
                break;
            default:
        }
        */

    }

 
    private float getDOM(double[] dom){
        float dom1=0;
        for (int i=0; i<dom.length; i++){
            dom1+= dom[i];
        }
        return dom1/dom.length;
    }

    public void displayPropertyDOM() throws Throwable {
//        showPropertyDOM(properties);
        /*
        if (!categorized){
            System.out.println();
            System.out.println("General");
            showPropertyDOM(general);
        }else{
            System.out.println();
            System.out.println("Inputs");
            showPropertyDOM(input);
            System.out.println();
            System.out.println("Outputs");
            showPropertyDOM(output);
            System.out.println();
            System.out.println("Preconditions");
            showPropertyDOM(precondition);
            System.out.println();
            System.out.println("Effects");
            showPropertyDOM(effect);
        }

         */
    }

    private void showPropertyDOM(HashMap<Integer, double[]> property)throws Throwable{
        if (property == null)
            return;
        Iterator<Integer> iterator = property.keySet().iterator();
        String tofile = "property_ID,property_name,DOM\n";
        while (iterator.hasNext()){
            int key = iterator.next();
            double[] dom = property.get(key);
            int l = dom.length;
            double domR = 0;
            tofile += key+","+PropertyDaoFactory.create().findWherePropertyIdEquals(key)[0].getPropertyName()+",";
            for (int i=0; i<l; i++){
                domR += dom[i]/l;
                tofile += dom[i]+",";
            }
            tofile+= domR+"\n";
            System.out.print("Key="+key+"  dom = "+domR+" propertyName="
                    +PropertyDaoFactory.create().findWherePropertyIdEquals(key)[0].getPropertyName()+" "+dom.toString());
//            for (int i=0; i<dom.length; i++)
//                System.out.print(dom[i]+"  ");
            System.out.println();
        }
        FileWriter writer = new FileWriter("matched_properties(Dep_prop_non_reduced).csv");
        writer.append(tofile);
        writer.flush();
        writer.close();
    }

    
    /**
     * 
     * @param s_id
     * @param p_id
     */
    public void addRelevantService(int s_id, int p_id){
        ArrayList currentpid = null;
        // save relevant services id with properties ids in hashmap
//        System.out.println(" s_id="+s_id);
        if(relevantServices.containsKey(s_id)){
//            System.out.println("Service has been already listed, so adding properties on that. ");
            currentpid = (ArrayList) relevantServices.get(s_id);
        }else{
            currentpid = new ArrayList();
        }
        if (!currentpid.contains(p_id)){
            currentpid.add(p_id);
            relevantServices.put(s_id, currentpid);
        }
    }


    public void displayRelService() {
        System.out.println(relevantServices.toString());
    }

    public void finishFindRelServices() {
        Object[] toArray = relevantServices.keySet().toArray();
        serviceIds = new int[toArray.length];
        for (int i=0; i<toArray.length; i++){
            serviceIds[i] = (Integer) toArray[i];
        }
//        try {
//            showPropertyDOM(properties);
//        } catch (Throwable ex) {
//            Logger.getLogger(TempData.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    /**
     * A method to calculate the degree of match of equivalent class of the service
     */
    public void findServiceDOM() throws Throwable{
//        computeEquivalentClasses();
        serviceDom = new HashMap<Integer, double[]>();
        for (int i=0; i<equivalences.size(); i++ ){
            if (serviceDom.containsKey(equivalences.get(i).get(0))){

            }else {
                serviceDom.put(equivalences.get(i).get(0),
                        degreeOfMatchOfService(properties, equivalences.get(i).get(0)));
            }
        }

/*
        //To represent the average dom of properties of different types
        double[][] dom = new double[5][];
        for (int i=0; i<serviceIds.length; i++ ){
            if (serviceDom.containsKey(serviceIds[i])){

            }else {
                if (!categorized){
                    dom[4] = degreeOfMatchOfService(general, serviceIds[i]);
                    serviceDom.put(serviceIds[i], dom[4]);
                }else{
                    dom[0] = degreeOfMatchOfService(input, serviceIds[i]);
                    dom[1] = degreeOfMatchOfService(output, serviceIds[i]);
                    dom[2] = degreeOfMatchOfService(precondition, serviceIds[i]);
                    dom[3] = degreeOfMatchOfService(effect, serviceIds[i]);
                    int maxLength = dom[0].length;
                    for (int j=1; j<=3; j++){
                        if (dom[j] != null && maxLength < dom[j].length)
                            maxLength = dom[j].length;
                    }
                    double[] domResult = new double[maxLength];
                    for (int j=0; j<maxLength; j++){
                        domResult[j] = 0.0;
                        for (int k=0; k<=3; k++){
                            if (dom[k] != null && j < dom[k].length)
                                domResult[j] += dom[k][j]/maxLength;
                        }
                    }
                    serviceDom.put(serviceIds[i], domResult);
                }
            }
        }
 * */
/**
        System.out.println("Relevnt="+relevantServices.toString());
        System.out.println("Equivalent="+equivalences.toString());
        System.out.println("Servie Dom ="+serviceDom);
        System.out.println("Overal service dom =");
        for (int i=0; i<equivalences.size(); i++){
            int key = equivalences.get(i).get(0);
            double[] get = serviceDom.get(key);
            double sum = 0.0;
            if (get != null)
            for (int z=0; z<get.length; z++)
                sum += get[z]/get.length;
            try{
                ArrayList<Integer> get1 = equivalences.get(i);
                for (int z=0; z<get1.size();z++)
            System.out.println(i+" id="+get1.get(z)+" dom="+sum+" serviceName="
                    +ServiceDaoFactory.create().findWhereServiceIdEquals(get1.get(z))[0].getServiceName()+" "+get.toString());
            }catch (Exception e){}
        }
 */
    }


    /**
     *
     * @param property
     * @param s_id
     * @return
     */
    private double[] degreeOfMatchOfService(HashMap<Integer, double[]> property, int s_id) {
        ArrayList<Integer> tempProperties = relevantServices.get(s_id);
        if (tempProperties == null || tempProperties.isEmpty())
            return null;
        if (property == null || property.get(tempProperties.get(0)) == null)
            return null;
        double[] dom = new double[property.get(tempProperties.get(0)).length];
        for (int i=0; i<dom.length; i++){
            dom[i] = 0.0;
        }
        //calculate the sum first
        for (int i=0; i<tempProperties.size(); i++){
            double[] prop_dom = property.get(tempProperties.get(i));
            if (prop_dom != null){
                for (int j=0; j<prop_dom.length; j++){
                    dom[j] += prop_dom[j]/prop_dom.length;
                }
            }
        }
        return dom;
    }


    /**
     * A method that computes the equivalent classes of the relevant services
     * @param rel
     */
    private void computeEquivalentClasses(){

        equivalences = new ArrayList<ArrayList<Integer>>();
        boolean isClassed;
        int s_id;
        ArrayList<Integer> temp;
        for (int i=0; i<serviceIds.length; i++){
           isClassed = false;
           for (int j=0; j<equivalences.size(); j++){
               s_id = equivalences.get(j).get(0);
               if (relevantServices.get(serviceIds[i]).equals(relevantServices.get(s_id))){
                   equivalences.get(j).add(serviceIds[i]);
                   isClassed = true;
                   break;
               }
           }
           if (! isClassed ){
               temp = new ArrayList<Integer>();
               temp.add(serviceIds[i]);
               equivalences.add(temp);
           }
        }
/*
        System.out.println("Equivalent classes = "+equivalences);
        ArrayList<ArrayList<Integer>> prop = new ArrayList<ArrayList<Integer>>();
        for (int i=0; i<equivalences.size(); i++){
            prop.add(relevantServices.get(equivalences.get(i).get(0)));
        }
        System.out.println("Props="+prop);
        */
    }



    /**
     * A method that reduces the dependent properties
     * @param properties
     */
    public void reduceDependentProperties() {
        computeEquivalentClasses();
     
        ArrayList<ArrayList<Integer>> combinations;
        ArrayList<Integer> dependent = new ArrayList<Integer>();
        ArrayList<Integer> listOfProperties = new ArrayList<Integer>();
        Object[] p_ids = properties.keySet().toArray();
        for (int i=0; i<p_ids.length; i++){
            listOfProperties.add((Integer) p_ids[i]);
        }
        boolean isReduced = false;
        //find maximum combination of properties to be indecisive
        for (int combinationSize = properties.size()-1; combinationSize>=2; combinationSize--){
            combinations = getCombinations(combinationSize, 0, listOfProperties);
            dependent = getDependentCombination(combinations);
            if (dependent != null && !dependent.isEmpty()){
                listOfProperties.removeAll(dependent);
                isReduced = true;
                break;
            }
        }
        //Remove the dependent properties
        if (isReduced){
            for (int i=0; i<p_ids.length; i++){
                if (!listOfProperties.contains((Integer)p_ids[i])){
                    properties.remove((Integer)p_ids[i]);
                }
            }
            //Remove dependent properties from equivalence services
            for (int i=0; i<serviceIds.length; i++){
                relevantServices.get(serviceIds[i]).removeAll(dependent);
            }

//            for (int i=0; i<equivalences.size(); i++){
//                relevantServices.get(equivalences.get(i).get(0)).removeAll(dependent);
//            }

        }
    }



    /**
     * A method that returns the combinations of properties of certain size starting from jth position
     * @param combinationSize
     * @param j
     * @param properties
     * @return
     */
    public ArrayList<ArrayList<Integer>> getCombinations(int combinationSize, int j, ArrayList<Integer> properties) {
        ArrayList<ArrayList<Integer>> combinations = new ArrayList<ArrayList<Integer>>();
        if (j == properties.size() - 1){
            ArrayList<Integer> combination = new ArrayList<Integer>();
            combination.add(properties.get(j));
            combinations.add(combination);
        }else if (combinationSize == 1){
            ArrayList<Integer> temp; 
            for (int z=j; z<properties.size(); z++){
                temp = new ArrayList<Integer>();
                temp.add(properties.get(z)); 
                combinations.add(temp); 
                temp = null;
            }
        } else if (j + combinationSize == properties.size()) {
            //Only final elements are remaining
            ArrayList<Integer> combination = new ArrayList<Integer>();
            for (int z=j; z<properties.size(); z++){
                combination.add(properties.get(z));
            }
            combinations.add(combination);
        }else if (j + combinationSize >= properties.size()){
            
        }else
          {
            //Prepare combinations including current jth element
            ArrayList<ArrayList<Integer>> temp = getCombinations(combinationSize-1, j+1, properties);
            for (int z=0; z<temp.size(); z++){
                temp.get(z).add(0, properties.get(j));
                combinations.add(temp.get(z));
            }

            //Prepare combinations exclusing current jth element
            ArrayList<ArrayList<Integer>> temp2 = getCombinations(combinationSize, j+1, properties);
            for (int z=0; z<temp2.size(); z++){
                combinations.add(temp2.get(z));
            }

        }

        return combinations;
    }



    /**
     * Finds a combination that's is indecisive from a list of combinations
     * @param combinations
     * @return
     */
    private ArrayList<Integer> getDependentCombination(ArrayList<ArrayList<Integer>> combinations) {
        ArrayList<Integer> combination = null;
        ArrayList<ArrayList<Integer>> classProperties ;
        ArrayList<Integer> temp ;
        int size;
        boolean isDependent;
        for (int i=0; i<combinations.size(); i++){
            combination = combinations.get(i);
            isDependent = true;
            classProperties = new ArrayList<ArrayList<Integer>>();
            for (int z=0; z<equivalences.size(); z++){
                temp = relevantServices.get(equivalences.get(z).get(0));
                classProperties.add(new ArrayList<Integer>());
                size = classProperties.size();
                for (int y=0; y<temp.size(); y++)
                    classProperties.get(size-1).add(temp.get(y));
                classProperties.get(size-1).removeAll(combination);
                for (int y=0; y<size-1; y++){
                    if (classProperties.get(y).equals(classProperties.get(size-1))){
                        isDependent = false;
                        break;
                    }
                }
            }
            if (isDependent){
                break;
            }
            combination = null;
        }
        return combination;
    }


    /**
     * Method for checking relevant services, equivalent classes and dependent property reduction
     * @param args
     */
    public static void main(String[] args) {

        TempData data = new TempData(false);

         
        HashMap<Integer, ArrayList<Integer>> rel = new HashMap<Integer, ArrayList<Integer>>();
        ArrayList<Integer> temp = new ArrayList<Integer>();
        temp.add(34); temp.add(78); // temp.add(789);
        rel.put(23, temp);
        ArrayList<Integer> temp1 = new ArrayList<Integer>();
        temp1.add(34); temp1.add(78); temp1.add(789);
        rel.put(230, temp1);
        ArrayList<Integer> temp2 = new ArrayList<Integer>();
        temp2.add(34);  temp2.add(78); temp2.add(789);
        rel.put(203, temp2);
        ArrayList<Integer> temp3 = new ArrayList<Integer>();
        temp3.add(34);  temp3.add(798); temp3.add(789);
        rel.put(235, temp3);
        ArrayList<Integer> temp4 = new ArrayList<Integer>();
        temp4.add(34); temp4.add(78);// temp4.add(789);
        rel.put(239, temp4);
        ArrayList<Integer> temp5 = new ArrayList<Integer>();
        temp5.add(34);  temp5.add(798); temp5.add(789);
        rel.put(298, temp5);
        data.computeEquivalentClasses(/*rel*/);
    
        ArrayList<Integer> properties = new ArrayList<Integer>();
        properties.add(34); properties.add(78);
        properties.add(789); properties.add(798);
//        properties.add(920); //properties.add(9870);
//        ArrayList<Integer> temp1 = new ArrayList<Integer>();
//        temp1.add(34);
//        temp1.add(920);
//        System.out.println("Properties = "+properties);
//        ArrayList<ArrayList<Integer>> temp = new ArrayList<ArrayList<Integer>>();
//        temp.add(new ArrayList<Integer>());
//        for (int i=0; i<properties.size(); i++){
//            temp.get(0).add(properties.get(i));
//        }
////        temp.add(properties);
//        System.out.println("temp = "+temp);
//        temp.get(0).removeAll(temp1);
//        System.out.println("temp = "+temp);
        System.out.println("Input Properties = "+properties);

//        data.reduceDependentProperties(properties);
        System.out.println("Input Properties = "+properties);
//        ArrayList<ArrayList<Integer>> combinations = data.getCombinations(2, 0, properties);
//        System.out.println(combinations);
    }

    public void printData(){

    }
}
