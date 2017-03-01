/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservicerank.serviceDB;


import java.util.logging.Level;
import java.util.logging.Logger;
import com.wsr.dao.*;
import com.wsr.dto.*;
import com.wsr.factory.*;
import com.wsr.exceptions.*;
import java.util.Date;
import webservicerank.uiAppInterface.ServiceDetail;


/**
 *A java class for publishing the service details, properties and categories in the service database
 * @author BIKASH
 */

public class PublishDBManager {


    ServiceDbInteracter interactor = null;


    //Property types representing INPUT=0, OUTPUT=1, PRECONDITION=2, EFFECT=3 and GENERAL=4
    String propertyCats[] = {"10000", "01000", "00100", "00010", "00001"};



    public PublishDBManager(){
        
    }

    /**
     * A method that undoes the publishing of service in case error is encountered
     * @param sId, The service ID to be unpublished.
     */
    private void undoPublish(int sId) throws Throwable {
        if (interactor == null){
            interactor = new ServiceDbInteracter();
        }

        //Remove from SERVICE table
        ServiceDaoFactory.create().delete(new ServicePk(sId));

        //Remove from SERVICECAT_SERVICE table
        ServicecatServiceDao scsDao = ServicecatServiceDaoFactory.create();
        ServicecatService[] scs = scsDao.findWhereServiceIdEquals(sId);
        for (int i=0; scs != null && i<scs.length; i++){
            scsDao.delete(new ServicecatServicePk(scs[i].getScsId()));
        }

        //Remove from SERVICE_PROPERTY table
        ServicePropertyDao spDao = ServicePropertyDaoFactory.create();
        ServiceProperty[] sp = spDao.findWhereServiceIdEquals(sId);
        for (int i=0; sp != null && i<sp.length; i++){
            spDao.delete(new ServicePropertyPk(sp[i].getSpId()));
        }

    }


    /**
     * A method that checks if the pair serviceName and wsdlUrl are in SERVICE table
     * @param wsdlUrl; WSDL url of the web service
     * @param serviceName; Name of the web service
     * @return; true if the pair exists, else false
     * @throws Throwable
     */

    public boolean isServiceStored(String wsdlUrl, String serviceName) throws Throwable{

        ServiceDao sdao = ServiceDaoFactory.create();
        String[] sqlParams = {serviceName, wsdlUrl};
        Service[] sresult = null;

        try {
            sresult = sdao.findByDynamicWhere(" service_name = ? and service_WSDL = ? ", sqlParams);
        } catch (ServiceDaoException ex) {
            Logger.getLogger(PublishDBManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new Throwable("Service database access error:"+ex.getMessage());
        }

        return (sresult.length != 0);
    }




    /**
     * A method to publish service details into service database
     * @param sd, Object that contains the details of the service
     * @return, message of success of publishing the service
     * @throws Throwable
     */

    public String publish(ServiceDetail sd, int publisherId) throws Throwable{

        String message = "The service details have been published successfully";
        //Insert into SERVICE table
        int serviceid = insertIntoServiceTable(sd, publisherId);
        if (serviceid == 0){
            undoPublish(serviceid);
            message = "Error occured while publishing service details";
        }
        //insert into SERVICECAT_SERVICE table
        insertIntoService_ServiceCategory(serviceid, sd.getCategory());

        //Insert properties in PROPERTY table and map service and properties in
        //SERVICE_PROPERTY table
        boolean publishedAny[] = new boolean[1];
        publishedAny[0] = false;
        if (sd.propertyCategorized()){
            insertIntoPropertyTable(sd.getInputProperties(), serviceid, 0, publishedAny);
            insertIntoPropertyTable(sd.getOutputProperties(), serviceid, 1, publishedAny);
            insertIntoPropertyTable(sd.getPreconditions(), serviceid, 2, publishedAny);
            insertIntoPropertyTable(sd.getEffects(), serviceid, 3, publishedAny);
        }else {
            insertIntoPropertyTable(sd.getProperties(), serviceid, 4, publishedAny);
        }
        if ( serviceid != 0 && !publishedAny[0]){
            undoPublish(serviceid);
            message = "Publishing service failed! Make sure that the property fields are not empty.";
        }
        return message;

    }




    /**
     * This method inserts the service details into the SERVICE table
     * @param servicedetail, the details of the service
     * @return, The service ID of the service in the table
     */

    private int insertIntoServiceTable(ServiceDetail servicedetail, int publisherId){

        int serviceid = 0;
        Service service=new Service();
        service.setExeDuration(servicedetail.getServiceDuration());
        service.setCost(servicedetail.getCost());
        service.setServiceName(servicedetail.getName());
        service.setServiceId(getNextId());
        service.setDescription(servicedetail.getDescription());
        service.setUserRating(0);
        service.setInvokeRequestCount(1);
        service.setInvokeSuccessCount(1);
        service.setServiceWsdl(servicedetail.getWsdlURL());
        service.setExecuteSuccessCount(1);
        service.setRatingUserCount(0);
        service.setHomeUrl(servicedetail.getHomeURL());
        service.setOwlUrl(servicedetail.getOwlUrl());
        service.setPublishedDate(new Date());
        service.setPublisher(publisherId);
        serviceid = service.getServiceId();

        try {
             ServiceDaoFactory.create().insert(service);
        } catch (ServiceDaoException ex) {
             Logger.getLogger(PublishDBManager.class.getName()).log(Level.SEVERE, null, ex);
             serviceid = 0;
        }
        return serviceid;

    }




    /**
     * A method that maps the published service with the service categories.
     * (in SERVICECAT_SERVICE table)
     * @param serviceid, The service id of the published service
     * @param categories, the categories of the published service (multiple categories of
     * a service are possible)
     */

    private void insertIntoService_ServiceCategory(int serviceid, String[] categories) throws Throwable{

        if (serviceid == 0)
            return;

        int categoryId;
        ServiceCategoryDao scdao = ServiceCategoryDaoFactory.create();
        ServiceCategory scresult[] = null;
        ServicecatServiceDao scsdao = ServicecatServiceDaoFactory.create();
        ServicecatService scsresult[] = null;
        ServicecatService scs = null;

        for (int i=0; i<categories.length; i++){

            scresult = scdao.findByDynamicWhere(" category_name = ? ", new String[]{categories[i]});

            if(scresult.length==0)
                throw new Throwable("SERVICE_CATEGORY_NOT_AVAILABLE");

            else{
                //find the primary key of the service category
                categoryId = scresult[0].getScId();

                scsresult = scsdao.findByDynamicWhere("SC_ID= ? and service_ID= ? ",
                        new Object[]{categoryId,serviceid});

                if (scsresult == null || scsresult.length == 0){
                    //Map the category with service
                    scs = new ServicecatService();
                    scs.setScsId(getNextId());
                    scs.setScId(categoryId);
                    scs.setServiceId(serviceid);
                    scsdao.insert(scs);
                }
            }
        }

    }




    /**
     * Insert the properties in PROPERTY table and map the properties with the service
     * in the SERVICE_PROPERTY table
     * @param properties, properties to be inserted
     * @param sId, the service ID of service to be mapped with
     * @param pCatIndex, index indicating the category (I,O,P,E or G) of the service
     * @throws Throwable
     */

    private void insertIntoPropertyTable(String properties[], int sId, int pCatIndex, boolean publishedAny[]) throws Throwable{

        if (sId == 0)
            return;
        
        if (properties == null){
            return;
        }
        
        PropertyDao pdao = PropertyDaoFactory.create();
        ServicePropertyDao spdao = ServicePropertyDaoFactory.create();
        ServiceProperty serviceProperties[] = null;
        ServiceProperty serviceProperty = null;
        Property presult[] = null;
        Property property = null;
        int propId = 0;

        for (int p=0; p<properties.length; p++){

            //Do not publish null properties
            if (properties[p] == null || properties[p].length() == 0 || " ".equals(properties[p]))
                continue;
            try {

                //Check whether the property was already stored in PROPERTY table
                presult = pdao.findByDynamicWhere("property_name = ? ", new String[]{properties[p]});

                if ( presult == null || presult.length == 0){
                    //The property is new, store it in the table
                    property = new Property();
                    property.setPropertyName(properties[p]);
                    property.setPropertyId(getNextId());
                    property.setCategorisation(propertyCats[pCatIndex]);
                    propId = property.getPropertyId();
                    pdao.insert( property );
                }

                else{
                    //The property is already in PROPERTY table
                    propId = presult[0].getPropertyId();
                    String type = presult[0].getCategorisation();
                    if (type.charAt(pCatIndex) != '1'){
                        String temp = "";
                        for (int i=0; i<type.length(); i++){
                            if (i != pCatIndex)
                                temp += type.charAt(i);
                            else
                                temp += "1";
                        }
                        //Update the category of the service
                        if ( interactor == null)
                            interactor = new ServiceDbInteracter();
                        interactor.updateCategorisation(propId, temp);
                    }
                }

                //Map the property and service in the SERVICE_PROPERTY table (service_ID, property_ID)
                //Check if the map was already stored
                serviceProperties = spdao.findByDynamicWhere(" service_ID= ? and property_ID= ? and property_category= ? "
                        , new Object[]{sId, propId, pCatIndex});

                if (serviceProperties == null || serviceProperties.length == 0){
                    //If the map is not found
                    serviceProperty = new ServiceProperty();
                    serviceProperty.setSpId(getNextId());
                    serviceProperty.setServiceId(sId);
                    serviceProperty.setPropertyId(propId);
                    serviceProperty.setPropertyCategory(pCatIndex); 
                    spdao.insert(serviceProperty);
                }

                //One or more properties have been published
                publishedAny[0] = true;

            } catch (PropertyDaoException ex) {
                Logger.getLogger(PublishDBManager.class.getName()).log(Level.SEVERE, null, ex);
                undoPublish(sId);
                throw new Throwable("Error while publishing the properties,Exception:"+ex.getMessage());
            }
        }
        
    }




    /**
     * A method that generates the Primary Key value of database tables
     * @return, id of the database table
     */
    private int getNextId(){
        int time = (int) System.currentTimeMillis();
        return time;
    }
  



    public static void main(String[] args) {
        PublishDBManager man = new PublishDBManager();
//        try {
//            System.out.println(man.isServiceStored("sale", "bikash"));
//        } catch (Throwable ex) {
//            Logger.getLogger(PublishDBManager.class.getName()).log(Level.SEVERE, null, ex);
//        }
        ServiceDetail sd = new ServiceDetail();
        sd.readParams1();
        try {
            String publish = man.publish(sd, 1);
            System.out.println("reply = "+publish);
        } catch (Throwable ex) {
            Logger.getLogger(PublishDBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
//        long time = System.currentTimeMillis();
//        System.out.println(time+" int="+(int)time);
//        System.out.println(man.getNextId());
    }

    

}
