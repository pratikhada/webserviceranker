/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservicerank.servicePublish;

import com.wsr.exceptions.PropertyDaoException;
import com.wsr.exceptions.ServiceCategoryDaoException;
import com.wsr.exceptions.ServiceDaoException;
import com.wsr.exceptions.ServicePropertyDaoException;
import com.wsr.exceptions.ServicecatServiceDaoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import webservicerank.serviceDB.PublishDBManager;
import webservicerank.uiAppInterface.ServiceDetail;

/**
 *
 * @author BIKASH
 */
public class PublishDB {

    PublishDBManager publishDBManager;


    public PublishDB(){
        publishDBManager = new PublishDBManager();
    }

    /**
     * Checks whether the service was already published
     * @param wsdlUrl
     * @param serviceName
     * @return
     * @throws Throwable
     */
    public boolean isAreadyStored(String wsdlUrl, String serviceName) throws Throwable{
        try {
            return publishDBManager.isServiceStored(wsdlUrl, serviceName);
        } catch (Throwable ex) {
            Logger.getLogger(PublishDB.class.getName()).log(Level.SEVERE, null, ex);
            throw new Throwable(" ->error in PublishDbManager:"+ex.getMessage());
        }
    }


    public PublishDBManager getPublishDBManager(){
        return publishDBManager;
    }


    /**
     * A method for publishing web service detail in service database
     * @param sd
     * @return
     * @throws Throwable
     */

    public String publish(ServiceDetail sd, int publisherId) throws Throwable {
        String message = null;
        try {
            message = publishDBManager.publish(sd, publisherId);
        } catch (PropertyDaoException ex) {
            Logger.getLogger(PublishDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServiceCategoryDaoException ex) {
            Logger.getLogger(PublishDB.class.getName()).log(Level.SEVERE, null, ex);
        }catch (ServiceDaoException ex) {
            Logger.getLogger(PublishDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServicecatServiceDaoException ex) {
            Logger.getLogger(PublishDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServicePropertyDaoException ex) {
            Logger.getLogger(PublishDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return message;
    }


}
