/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservicerank.configuration;

/**
 *
 * @author webservicerank
 */
public class DBAttributes {
    private String driver;
    private String host;
    private String port;
    private String username;
    private String password;
    private String dbType;

    public void setDriver(String param){
        driver = param;
    }

    public void setHost(String param){
        host = param;
    }

    public void setPort(String param){
        port = param;
    }

    public void setUsername(String param){
        username = param;
    }

    public void setPassword(String param){
        password = param;
    }

    public void setDbType(String param){
        dbType = param;
    }

    public String getDriver(){
        return driver;
    }

    public String getHost(){
        return host;
    }

    public String getPort(){
        return port;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String getDbType(){
        return dbType;
    }

}
