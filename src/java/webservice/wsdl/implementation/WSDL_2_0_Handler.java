/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservice.wsdl.implementation;

import java.util.ArrayList;
import javax.xml.namespace.QName;
import org.jdom.Element;
import webservice.wsdl.WSDLHandler;

/**
 *
 * @author BIKASH
 */
//A class that handles wsdl v2.0 document
public class WSDL_2_0_Handler extends WSDLHandler{

    private String wsdlUrl;
    
    public WSDL_2_0_Handler( String url, Element schemaRoot ){
        wsdlUrl = url;
    }

    @Override
    public QName[] serviceNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String[] servicePortNames(QName serviceName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String[] operationNames(QName serviceName, String portName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public QName[] inputPartElementNames(QName serviceName, String portName, String operationName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public QName[] outputPartElementNames(QName serviceName, String portName, String operationName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Parameter[] inputParameters(QName inputPartElementName, String[] values, int[] index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Parameter[] outputParameters(QName outputPartElementName, String[] values, int[] index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getPortLocation(QName serviceName, String portName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<Parameter>[] getLeafParameters(QName[] partElementName, String[] values) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
