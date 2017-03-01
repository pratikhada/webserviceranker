/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservicerank.configuration;

/**
 *
 * @author webservicerank
 */
public class NetworkStatus {
    private boolean isProxy;
    private String ip;
    private String port;
    private boolean isLogin;
    private String username;
    private String password;

    public void setIsProxy(boolean isProxy){
        this.isProxy = isProxy;
    }

    public void setIP(String ip){
        this.ip = ip;
    }

    public void setPort(String port){
        this.port = port;
    }

    public void setIsLogin(boolean isLogin){
        this.isLogin = isLogin;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public boolean isProxyOn(){
        return isProxy;
    }

    public String getIp(){
        return ip;
    }

    public String getPort(){
        return port;
    }

    public boolean isLoginEnabled(){
        return isLogin;
    }
    
    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }
}
