/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservicerank.configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author webservicerank
 */
public class NetworkConfigHandler extends Authenticator{
    private static boolean isRead = false;
    private static boolean isProxy;
    private static String proxyIp = null;
    private static String proxyPort = null;

    private static boolean isLogin;
    private static String username = null;
    private static String password = null;

    private static String filePath;

    private static void initialize() throws Throwable{
        if (filePath == null){
            filePath = FileNames.rootDir()
                    + FileNames.separator() +FileNames.NETWORK_CONFIG ;
        }
    }

    private static void readParams() throws Throwable{
        if (isRead == false){
            initialize();
            Document doc = (new SAXBuilder(false)).build(new File(filePath));
            Element network = doc.getRootElement();
            if ("yes".equals(network.getChild("isProxyOn").getText())){
                isProxy = true;
                Element proxy  = network.getChild("proxy");
                proxyIp = proxy.getChild("ip").getText();
                proxyPort = proxy.getChild("port").getText();
                if ("yes".equals(proxy.getChild("isLoginEnabled").getText())){
                    isLogin = true;
                    Element login = proxy.getChild("login");
                    username = login.getChild("username").getText();
                    password = login.getChild("password").getText();
                }else{
                    isLogin = false;
                }
            }else{
                isProxy = false;
            }
            isRead = true;
        }
    }

    //Initial preparation of Network Configuration file
    public static void prepareNetworkConfig(FileWriter writer) throws IOException{
        Element root = new Element("network");
        root.addContent(new Comment("This contains network connection information"));
        root.addContent(new Element("isProxyOn"));
        root.addContent(new Element("proxy"));
        Document doc = new Document(root);
        XMLOutputter out=new XMLOutputter();
        out.output(doc, writer);
        isRead = false;
    }

    //Store the network configuration information
    public static void configNetwork(NetworkStatus status) throws Throwable{
        initialize();
        Document doc = (new SAXBuilder(false)).build(new File(filePath));
        Element root = doc.getRootElement();
        Element isProxy = root.getChild("isProxyOn");
        Element proxy  = root.getChild("proxy");
        proxy.removeContent();
        isProxy.removeContent();
        if (status.isProxyOn()){
            isProxy.addContent("yes");
            proxy.addContent(new Element("ip").addContent(status.getIp()));
            proxy.addContent(new Element("port").addContent(status.getPort()));
            if (status.isLoginEnabled()){
                proxy.addContent(new Element("isLoginEnabled").addContent("yes"));
                Element login = new Element("login");
                login.addContent(new Element("username").addContent(status.getUsername()));
                login.addContent(new Element("password").addContent(status.getPassword()));
                proxy.addContent(login);
            }else{
                proxy.addContent(new Element("isLoginEnabled").addContent("no"));
            }
        }else{
            isProxy.addContent("no");
        }
        (new XMLOutputter()).output(doc, new FileWriter(filePath));
        isRead = false;
    }

    
    public void proxyAuthenticate() throws Throwable{
        readParams();
        if (isProxy){
            if (!proxyIp.equals(System.getProperty("http.proxyHost")))
                System.getProperties().put("http.proxyHost", proxyIp);
            if (!proxyPort.equals(System.getProperty("http.proxyPort")))
                System.getProperties().put("http.proxyPort", proxyPort);
            if (isLogin){
                Authenticator.setDefault(new NetworkConfigHandler());
            }
        }
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username,password.toCharArray());
    }


}
