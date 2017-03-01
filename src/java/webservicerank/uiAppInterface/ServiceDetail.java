/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservicerank.uiAppInterface;

import java.io.PrintWriter;

/**
 *
 * @author vhishma
 */
public class ServiceDetail {

    private String categories[];
    private String servicename;
    private String wsdlURL;
    private String properties[], inputs[],
            outputs[], preconditions[], effects[];
    private String homeURL;
    private String owlURL;
    private String description;
    private int serviceDuration;
    private float cost;
    private boolean arePropertiesCategorized;

    public void readParams1(){
        categories = new String[]{"Education"};
        servicename = "DifferentiationService";
        wsdlURL = "http://localhost:8080/WebServicesApp/DifferentiationService?WSDL";
        properties = new String[]{"linear-unit","mile","fathom","kilo","measuring-unit"};
//        properties = new String[]{"multiplication","Locomotive-windshield-wipers", "gain", "calculate","Transportation-components-and-systems"};
//        inputs = new String[]{"parameters", "prime-numbers"};
//        outputs = new String[]{"even-numbers", "addition", "division"};
//        preconditions = new String[] {"prime-factors", "simple"};
//        effects = new String[]{"decimal", "double"};
        homeURL = "www.myhome.com/service2e";
        owlURL = "file:///E:/WSR_Final/trunk/webservicerank/lib/Ontologies/data_center.owl";
        description = "This service performs multiplication and division of two numbers.";
        cost = 36.253f; //cost in dollar
        arePropertiesCategorized = false;
    }
    
    public void setCategory(String parameter[]){
        this.categories = parameter;
    }

    public String[] getCategory() {
        return categories;
    }

    public void setName(String parameter){
        this.servicename = parameter;
    }

    public String getName() {
        return servicename;
    }

    public void setWsdlURL(String parameter){
        this.wsdlURL = parameter;
    }

    public String getWsdlURL() {
        return wsdlURL;
    }

    public void categorizeProperties(boolean categorize){
        arePropertiesCategorized = categorize;
    }

    public boolean propertyCategorized(){
        return arePropertiesCategorized;
    }

    public void setProperties(String properties[]){
        this.properties = properties;
    }

    public String[] getProperties(){
        return properties;
    }

    public void setInputProperties(String inputProperties[]){
        this.inputs = inputProperties;
    }

    public String[] getInputProperties(){
        return inputs;
    }

    public void setOutputProperties(String outputProperties[]){
        this.outputs = outputProperties;
    }

    public String[] getOutputProperties(){
        return outputs;
    }

    public void setPreconditions(String preconditions[]){
        this.preconditions = preconditions;
    }

    public String[] getPreconditions(){
        return preconditions;
    }

    public void setEffects(String effects[]){
        this.effects = effects;
    }

    public String[] getEffects(){
        return effects;
    }

    public void setHomeURL(String parameter){
        this.homeURL = parameter;
    }

    public String getHomeURL(){
        return homeURL;
    }

    public void setOwlURL(String parameter){
        this. owlURL= parameter;
    }

    public String getOwlUrl() {
        return owlURL;
    }

    public void setDescription(String parameter){
        this. description= parameter;
    }

    public String getDescription()
    {
        return description;
    }

    public void setCost(float parameter) {
        cost = parameter;
    }

    public float getCost(){
        return cost;
    }

    public void setServiceDuration(int duration){
        serviceDuration = duration;
    }

    public int getServiceDuration(){
        return serviceDuration;
    }

    public  void serviceDisplay(PrintWriter out){
        out.println(" <br/>--Category : ");
        for(int i=0; i<categories.length; i++){
            out.println("<br/> ---- "+categories[i]);
        }
        out.println(" <br/>--ServiceName : "+getName());
        out.println(" <br/>--wsdlURL : "+getWsdlURL());
        out.println(" <br/>--homeURL : "+getHomeURL());
        out.println(" <br/>--owlURL : "+getOwlUrl());
        out.println(" <br/>--description : "+getDescription());
        out.println(" <br/>--servicveDuration : "+getServiceDuration());
        out.println(" <br/>--serviceCost : "+getCost());
        out.println(" <br/>--arePropertiesCategorized; : "+propertyCategorized());
        if (propertyCategorized()){
            out.println("<br/>Inputs:");
            for (int i=0; i<inputs.length; i++)
                out.println("   "+inputs[i]);
            out.println("<br/>Outputs:");
            for (int i=0; i<outputs.length; i++)
                out.println("   "+outputs[i]);
            out.println("<br/>Preconditions:");
            for (int i=0; i<preconditions.length; i++)
                out.println("   "+preconditions[i]);
            out.println("<br/>Effects:");
            for (int i=0; i<effects.length; i++)
                out.println("   "+effects[i]);
        }else{
            out.println("<br/>Properties:");
            for (int i=0; i<properties.length; i++)
                out.println("   "+properties[i]);
        }
    }

    
}
