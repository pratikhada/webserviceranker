/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservicerank.serviceDB;

import com.wsr.dao.PropertyDao;
import com.wsr.dao.ServiceCategoryDao;
import com.wsr.dao.ServiceDao;
import com.wsr.dao.ServicePropertyDao;
import com.wsr.dao.ServicecatServiceDao;
import com.wsr.dto.Property;
import com.wsr.dto.PropertyPk;
import com.wsr.dto.Service;
import com.wsr.dto.ServicePk;
import com.wsr.dto.ServiceProperty;
import com.wsr.dto.ServicePropertyPk;
import com.wsr.dto.ServicecatService;
import com.wsr.dto.ServicecatServicePk;
import com.wsr.dto.WsrUsers;
import com.wsr.exceptions.PropertyDaoException;
import com.wsr.exceptions.ServiceDaoException;
import com.wsr.exceptions.ServicePropertyDaoException;
import com.wsr.exceptions.ServicecatServiceDaoException;
import com.wsr.factory.PropertyDaoFactory;
import com.wsr.factory.ServiceCategoryDaoFactory;
import com.wsr.factory.ServiceDaoFactory;
import com.wsr.factory.ServicePropertyDaoFactory;
import com.wsr.factory.ServicecatServiceDaoFactory;
import com.wsr.factory.WsrUsersDaoFactory;
import com.wsr.jdbc.ResourceManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import webservicerank.configuration.DBAttributes;
import webservicerank.configuration.ExecConfigHandler;


/**
 *
 * @author BIKASH
 */
public class ServiceDbInteracter {

    protected static java.sql.Connection userConn;


    private static ResultSet nextProperty = null;


    /**
     * A method that updates the categorisation of the PROPERTY table
     * @param prop_ID
     * @param category
     * @throws PropertyDaoException
     */
    public void updateCategorisation( int prop_ID, String category ) throws PropertyDaoException{
        final boolean isConnSupplied = (userConn != null);
        Connection conn = null;
        PreparedStatement stmt = null;
        final String SQL = "UPDATE property SET categorisation = ? WHERE property_ID = ?";
        try {

            // get the user-specified connection or get a connection from the ResourceManager
            conn = isConnSupplied ? userConn : ResourceManager.getConnection();

            // prepare statement
            stmt = conn.prepareStatement( SQL );

            stmt.setString(1, category);
            stmt.setInt(2, prop_ID);
            stmt.executeUpdate();
        } catch (Throwable _e) {
           throw new PropertyDaoException( "Exception: " + _e.getMessage(), _e );
        }
    }



    /**
     *
     * @param modelName
     * @param isToInitialize
     * @return
     */
    public String nexPropertyName(int[] prop_id, String categories[],boolean isToInitialize) throws Throwable{
        if (isToInitialize){
            //initialize the property resultset
            String sql = "SELECT * from property ";
            if ( userConn == null)
                userConn = ResourceManager.getConnection();
            PreparedStatement ps = null;
            ps = userConn.prepareStatement(sql);
            nextProperty = ps.executeQuery();
        }
        if (nextProperty == null) {
            return null;
        }
        if ( nextProperty.next()) {
            categories[0] = nextProperty.getString("categorisation");
            prop_id[0] = nextProperty.getInt("property_ID");
            return nextProperty.getString("property_name");
        }else {
            return null;
        }
    }


    /**
     * A method that inserts new namespaces in the NAMESPACE table of webservicerank_ont
     * database
     * @param namespaces
     * @throws Throwable
     */
    void updateNamespaces(ArrayList<String> namespaces) throws Throwable{

        DBAttributes db = ExecConfigHandler.getOntologyDbAttributes();
        Class.forName(db.getDriver());
        Connection con = DriverManager.getConnection(db.getHost()+ ":" + db.getPort()
                + "/" + "webservicerank_ont", db.getUsername(), db.getPassword());

        //Remove namespaces already stored in database from 'namespaces'
        String sqlFind = "SELECT * FROM namespaces ";
        PreparedStatement ps = con.prepareStatement(sqlFind);
        ResultSet rs = ps.executeQuery();
        String NS = null;
        int lastPK = 0;
        while (rs.next()){
            NS = rs.getString("namespace");
            lastPK = rs.getInt("ns_id");
            if (namespaces.contains(NS)){
                namespaces.remove(NS);
            }
        }
        rs.close();
        ps.close();

        //Now insert the new namespaces in database
        String sqlInsert = "INSERT INTO namespaces (ns_id, namespace) values ( ? , ? )";
        PreparedStatement insert = con.prepareStatement(sqlInsert);
        for (int i=0; i<namespaces.size(); i++){
            lastPK ++;
            insert.setInt(1, lastPK);
            insert.setString(2, namespaces.get(i));
            insert.addBatch();
        }
        insert.executeBatch();

        insert.close();
        con.close();
    }


    /**
     * A method that reads the namespaces and return its list
     * @return
     */
    public ArrayList<String> getNamespaces() throws Throwable{
        DBAttributes db = ExecConfigHandler.getOntologyDbAttributes();
        Class.forName(db.getDriver());
        Connection con = DriverManager.getConnection(db.getHost()+ ":" + db.getPort()
                + "/" + "webservicerank_ont", db.getUsername(), db.getPassword());
        //Remove namespaces already stored in database from 'namespaces'
        String sql = "SELECT namespace FROM namespaces ";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet NSs = ps.executeQuery();
        ArrayList<String> namespaces = new ArrayList<String>();
        while (NSs.next()){
            namespaces.add(NSs.getString("namespace"));
        }
        NSs.close();
        ps.close();
        con.close();
        return namespaces;
        
    }


    public String doesExist(int[] userId, String username, String password) {
        String type = null;
        Connection con = null;
        PreparedStatement st = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/webservicerank", "root", "");
            String query = "select * from wsr_users where username = ? and password = ? ";

            st = con.prepareStatement(query);
            st.setString(1, username);
            st.setString(2, password);
            ResultSet result = st.executeQuery();
            if(result.next()){
                 type = result.getString("login_type");
                 userId[0] = Integer.valueOf(result.getString("userid"));
            }
            result.close();
        } catch (Exception ex) {

        }finally{
            try {
                con.close();
            } catch (Exception ex) {
//                Logger.getLogger(ServiceDbInteracter.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                st.close();
            } catch (Exception ex) {
//                Logger.getLogger(ServiceDbInteracter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return type;
    }


    public ArrayList<String> getSuggestions(String str) {
        String result = new String();
        ArrayList myresult = new ArrayList();
        try {
            String driver="com.mysql.jdbc.Driver";
            String user_name = "root";
            String password = "";

            Class.forName(driver);
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/webservicerank", user_name, password);
            String query = "select property_name from property where property_name LIKE ('" + str + "%') order by property_name LIMIT 0 , 5";
            PreparedStatement stmt = conn.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result = rs.getString("property_name");
                myresult.add(result);
            }
            rs.close();
            conn.close();
            stmt.close();
        } catch (Exception ex) {
//            Logger.getLogger(QuerySuggestions.class.getName()).log(Level.SEVERE, null, ex);
        }
        return myresult;
    }

    public String[] getService(String servicename) {
        String []returnvalue=new String[5];
        try {
            //throw new UnsupportedOperationException("Not yet implemented");
            Service[] Services = ServiceDaoFactory.create().findWhereServiceNameEquals(servicename);
            if(Services!=null&&Services.length!=0){
             returnvalue[0]=Services[0].getServiceName();
             returnvalue[1]=Services[0].getServiceWsdl();
             returnvalue[2]=Services[0].getDescription();
             returnvalue[3]=String.valueOf((int)Services[0].getRating());
             returnvalue[4]=String.valueOf(Services[0].getServiceId());
               
            }
        } catch (ServiceDaoException ex) {
            //Logger.getLogger(ServiceDbInteracter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnvalue;
    }

    public String[][] listServices() {
        String service_list[][] = null;
        try {
            Service[] services = ServiceDaoFactory.create().findAll();
            if (services != null && services.length != 0){
                service_list = new String[services.length][];
                for (int i=0; i<services.length; i++){
                    service_list[i] = new String[6];
                    service_list[i][0] = services[i].getServiceName();
                    service_list[i][1] = services[i].getServiceWsdl();
                    service_list[i][2] = services[i].getDescription();
                    service_list[i][3] = services[i].getPublishedDate().toString();
                    service_list[i][4] = String.valueOf(services[i].isCheckedByAdmin());
                    service_list[i][5] = String.valueOf(services[i].getServiceId());
                }
            }

        } catch (ServiceDaoException ex) {
//            Logger.getLogger(ServiceDbInteracter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return service_list;
    }

    public String[][] getServiceDetails(int s_id) throws Throwable {
        String[][] service = new String[10][2];
        try {
            Service list = ServiceDaoFactory.create().findByPrimaryKey(s_id);
            if (list != null){
                service[0][0] = "Service name";
                service[0][1] = list.getServiceName();
                service[1][0] = "WSDL";
                service[1][1] = list.getServiceWsdl();
                service[2][0] = "Home Url";
                service[2][1] = list.getHomeUrl();
                service[3][0] = "User Rating";
                service[3][1] = String.valueOf(list.getRating());
                service[4][0] = "Cost";
                service[4][1] = String.valueOf(list.getCost());
                service[5][0] = "Published Date";
                service[5][1] = list.getPublishedDate().toString();
                WsrUsers publisher = WsrUsersDaoFactory.create().findByPrimaryKey(list.getPublisher());
                service[6][0] = "Publisher's Detail: Username";
                service[6][1] = publisher.getUsername();
                service[7][0] = "Name";
                service[7][1] = publisher.getName()+"("+publisher.getLoginType()+")";
                service[8][0] = "Address";
                service[8][1] = publisher.getAddress();
                service[9][0] ="E-mail";
                service[9][1] =publisher.getEmail();
            }
        } catch (ServiceDaoException ex) {
//            Logger.getLogger(ServiceDbInteracter.class.getName()).log(Level.SEVERE, null, ex);
            String error = (ex.getMessage() != null)?ex.getMessage():"Error while reading database";
            throw new Throwable(error);
        }
        return service;
    }

    public String markAsChecked(int s_id) throws Throwable {
        String message = "Error encountered!";
        ServiceDao sdao = ServiceDaoFactory.create();
        try {
            Service service = sdao.findByPrimaryKey(s_id);
            if (service != null){
                if (service.isCheckedByAdmin()){
                    message = "The service was already checked by admin";
                }else{
                    service.setCheckedByAdmin(true);
                    sdao.update(new ServicePk(s_id), service);
                    message = "The service has been marked as checked successfully";
                }
            }else{
                message = "The service doesn't exist!";
            }
        } catch (ServiceDaoException ex) {
            String error = (ex.getMessage() != null)?ex.getMessage():"Error while updating";
            throw new Throwable(error);
        }
        return message;
    }

    public String deleteService(int s_id) throws Throwable {
        String message = null;
        try {
            ServiceDao sdao = ServiceDaoFactory.create();
            Service service = sdao.findByPrimaryKey(s_id);
            if (service == null){
                message = "The service was already deleted!";
                return message;
            }
            sdao.delete(new ServicePk(s_id));
            ServicePropertyDao spdao = ServicePropertyDaoFactory.create();
            ServiceProperty[] sProperties = spdao.findWhereServiceIdEquals(s_id);
            for (int i=0; sProperties != null && i<sProperties.length; i++){
                spdao.delete(new ServicePropertyPk(sProperties[i].getSpId()));
            }
            ServicecatServiceDao scsdao = ServicecatServiceDaoFactory.create();
            ServicecatService[] scservices = scsdao.findWhereServiceIdEquals(s_id);
            for (int i=0; scservices != null && i<scservices.length; i++){
                scsdao.delete(new ServicecatServicePk(scservices[i].getScsId()));
            }
            message = "The service has been deleted!";
        } catch (Exception ex) {
//            Logger.getLogger(ServiceDbInteracter.class.getName()).log(Level.SEVERE, null, ex);
            String error = (ex.getMessage() != null)?ex.getMessage():"Error while deleting.";
            throw new Throwable(error);
        }
        return message;
    }

    public String[] getServiceDetail(int service_id) {
        String []returnvalue=new String[5];
        try {
            //throw new UnsupportedOperationException("Not yet implemented");
            Service[] Services = ServiceDaoFactory.create().findWhereServiceIdEquals(service_id); 
            if(Services!=null&&Services.length!=0){
             returnvalue[0]=Services[0].getServiceName();
             returnvalue[1]=Services[0].getServiceWsdl();
             returnvalue[2]=Services[0].getDescription();
             returnvalue[3]=String.valueOf((int)Services[0].getRating());
             returnvalue[4]=String.valueOf(Services[0].getServiceId());

            }
        } catch (ServiceDaoException ex) {
            //Logger.getLogger(ServiceDbInteracter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnvalue;
    }

    public String performCleanup() {
        String message = "Error encountered";
        try {
            PropertyDao pdao = PropertyDaoFactory.create();
            ServiceDao sdao = ServiceDaoFactory.create();
            ServiceCategoryDao scdao = ServiceCategoryDaoFactory.create();
            ServicePropertyDao spdao = ServicePropertyDaoFactory.create();
            ServicecatServiceDao scsdao = ServicecatServiceDaoFactory.create();
            ServiceProperty[] sProperties = spdao.findAll();
            for (int i=0; sProperties!= null && i<sProperties.length; i++){
                if (pdao.findByPrimaryKey(sProperties[i].getPropertyId()) == null
                        || sdao.findByPrimaryKey(sProperties[i].getServiceId())==null){
                    spdao.delete(new ServicePropertyPk(sProperties[i].getSpId()));
                }
            }
            message = "Inconsistent pairs in SERVICE_PROPERTY table have been removed.";
            sProperties = null;
            ServicecatService[] scServices = scsdao.findAll();
            for (int i=0; scServices!= null && i<scServices.length; i++){
                if(sdao.findByPrimaryKey(scServices[i].getServiceId())==null
                        || scdao.findByPrimaryKey(scServices[i].getScId()) == null){
                    scsdao.delete(new ServicecatServicePk(scServices[i].getScsId()));
                }
            }
            message += " Inconsistent pairs in SERVICECAT_SERVICE table have been removed.";
            scServices = null;
            //            Service[] services = sdao.findAll();
            //            for (int i=0; services!=null && i<services.length; i++){
            //                ServiceProperty[] sproperties = spdao.findWhereServiceIdEquals(services[i].getServiceId());
            //                ServicecatService[] sscategories = scsdao.findWhereServiceIdEquals(services[i].getServiceId());
            //                if (sproperties==null || sproperties.length ==0 || sscategories==null || sscategories.length==0){
            //                    sdao.delete(new ServicePk(services[i].getServiceId()));
            //                }
            //            }
            //            message += " ";
            Property[] properties = pdao.findAll();
            for (int i=0; properties!=null && i<properties.length; i++){
                ServiceProperty[] sproperties = spdao.findWherePropertyIdEquals(properties[i].getPropertyId());
                if (sproperties==null || sproperties.length==0){
                    pdao.delete(new PropertyPk(properties[i].getPropertyId())); 
                }
            }
            message = "Cleanup has been performed successfully.";
        } catch (Exception ex) {
//            Logger.getLogger(ServiceDbInteracter.class.getName()).log(Level.SEVERE, null, ex);
            message += (ex.getMessage() != null)?ex.getMessage():"Error while performin cleanup.";
        }
        return message;
    }

    public String[][] getServiceWithNoProperties() throws Throwable {
        String[][] serviceList = null;
        try {
            ArrayList<Integer> service_ids = new ArrayList<Integer>();
            ServiceDao sdao = ServiceDaoFactory.create();
            ServicePropertyDao spdao = ServicePropertyDaoFactory.create();
            Service[] services = sdao.findAll();
            for (int i=0; services!=null && i<services.length; i++){
                ServiceProperty[] sps = spdao.findWhereServiceIdEquals(services[i].getServiceId());
                if (sps == null || sps.length == 0){
                    service_ids.add(services[i].getServiceId());
                }
            }
             
            if(!service_ids.isEmpty()){
                int length = service_ids.size();
                serviceList = new String[length][2];
                for(int i=0; i<length; i++){
                    serviceList[i][0] = service_ids.get(i).toString();
                    serviceList[i][1] = sdao.findByPrimaryKey(service_ids.get(i)).getServiceName();
                }
            }
        } catch (Exception ex) {
            String error = (ex.getMessage() != null)?ex.getMessage():"Error due to exception";
            throw new Throwable(error);
        }
        return serviceList;
    }

  


}
