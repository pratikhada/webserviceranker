/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservice.wsdl;

import java.util.ArrayList;
import javax.xml.namespace.QName;
import webservice.wsdl.implementation.Parameter;

/**
 *
 * @author BIKASH
 */
public abstract class WSDLHandler {
    public abstract QName[] serviceNames();
    public abstract String[] servicePortNames(QName serviceName);
    public abstract String[] operationNames(QName serviceName, String portName);
    public abstract QName[] inputPartElementNames(QName serviceName, String portName,String operationName);
    public abstract QName[] outputPartElementNames(QName serviceName, String portName,String operationName);
    public abstract Parameter[] inputParameters(QName inputPartElementName, String[] values, int[] index);
    public abstract Parameter[] outputParameters(QName outputPartElementName, String[] values, int[] index);
    public abstract ArrayList<Parameter>[] getLeafParameters(QName partElementName[], String[] values);
    public abstract String getPortLocation(QName serviceName, String portName);
}
