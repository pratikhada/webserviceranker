/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservice.wsdl.implementation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import webservice.wsdl.WSDLHandler;
import webservicerank.configuration.NetworkConfigHandler;

/**
 *
 * @author BIKASH
 */
//A class that handles wsdl v1.0 document
public class WSDL_1_0_Handler extends WSDLHandler{

    // root of WSDL v 1.0
    private Definition definition;

    //Root of the schema (for input/output parameter of operation)
    private Element schemaRoot;

    //list of service provided in the wsdl
    private Service[] service;

    //URL of WSDL document
    private String wsdlUrl;


    //Initializes definition, schemaRoot, service, wsdlUrl
    public WSDL_1_0_Handler(String url, Element schemaRoot) throws Throwable{
        WSDLFactory factory = null;
        try {
            factory = WSDLFactory.newInstance();
        } catch (WSDLException ex) {
            Logger.getLogger(WSDL_1_0_Handler.class.getName()).log(Level.SEVERE, null, ex);
            throw new Throwable("WSDLFactory Error! A system library error.");
        }
        WSDLReader reader = factory.newWSDLReader();
        reader.setFeature("javax.wsdl.verbose", false);
        reader.setFeature("javax.wsdl.importDocuments", true);
        try {
            (new NetworkConfigHandler()).proxyAuthenticate();
            definition = reader.readWSDL(url);
        } catch (Exception ex) {
            Logger.getLogger(WSDL_1_0_Handler.class.getName()).log(Level.SEVERE, null, ex);
            if (definition == null)
                throw new Throwable("The WSDL Url couldn't be read");
        }
        Map services = definition.getServices();
        service = new Service[services.size()];
        Iterator serviceIt = services.values().iterator();
        int count = 0;
        while(serviceIt.hasNext()){
            service[count++] = (Service)serviceIt.next();
        }
        this.schemaRoot = schemaRoot;
        wsdlUrl = url;
    }


    //To find the index of required service in service[]
    private int getServiceIndex(QName toFind){
        int index =-1;
        for(int i=0; i<service.length; i++)
            if(toFind.equals(service[i].getQName())){
                index = i;
                break;
            }
        return index;
    }

    //To get the index of required port among ports[]
    private int getPortIndex(Port[] ports, String portName){
        int index = -1;
        for(int i=0; i<ports.length; i++)
            if(portName.equals(ports[i].getName())){
                index = i;
                break;
            }
        return index;
    }


    //To find the index of required operation
    private int getOperationIndex(Operation[] operation, String name){
        int index = -1;
        for(int i=0; i<operation.length; i++)
            if(name.equals(operation[i].getName())){
                index = i;
                break;
            }
        return index;
    }


    //Returns the Service names
    @Override
    public QName[] serviceNames(){
        QName[] services =  new QName[service.length];
        for(int i=0; i<service.length; i++)
            services[i] = service[i].getQName();
        return services;
    }



    private Binding[] bindings(QName serviceName, String portName){
        Service serv = service[getServiceIndex(serviceName)];
        Map ports = serv.getPorts();
        Port[] port = new Port[ports.size()];
        Iterator iterator = ports.values().iterator();
        int index = 0;
        while(iterator.hasNext()){
            port[index++] = (Port)iterator.next();
        }
        Binding[] binding = null;
        if(portName == null){
            binding = new Binding[port.length];
            for(int i=0; i<port.length; i++)
                binding[i] = port[i].getBinding();
        }else{
            binding = new Binding[1];
            binding[0] = port[getPortIndex(port,portName)].getBinding();
        }
        
        return binding;
    }


    private Operation[] operations(Binding binding){
        List bindOps = binding.getBindingOperations();
        BindingOperation bp;
        Operation [] oprns = new Operation[bindOps.size()];
        for (int i=0; i<bindOps.size(); i++){
            bp = (BindingOperation)bindOps.get(i);
            oprns[i] = bp.getOperation();
        }
        return oprns;
    }


    //Returns all the port names associated with the service
    @Override
    public String[] servicePortNames(QName serviceName){
        Map ports = service[getServiceIndex(serviceName)].getPorts();
        String [] portNames = new String[ports.size()];
        Iterator iterator = ports.values().iterator();
        int index = 0;
        while(iterator.hasNext()){
            Port port = (Port)iterator.next();
            portNames[index++] = port.getName();
        }
        return portNames;
    }


    private Operation[] operations(QName serviceName, String portName){
        Binding[] binds = bindings(serviceName, portName);
        Operation op[][] =null;
        op = new Operation[binds.length][];
        int count = 0;
        for(int z=0; z<binds.length; z++){
            op[z] = operations(binds[z]);
            count+= op[z].length;
        }
        Operation[] operations = new Operation[count];
        int index = 0;
        for(int z=0; z<op.length; z++)
            for(int k=0; k<op[z].length; k++)
                operations[index++] = op[z][k];
        return operations;
    }


    private Operation operation(QName serviceName,String portName, String operationName){
        Binding bind = definition.getService(serviceName).getPort(portName).getBinding();
        Operation[] operations = operations(bind);
        Operation operation = operations[getOperationIndex(operations,operationName)];
        return operation;
    }


    //Returns all the operation names associated with the port
    @Override
    public String[] operationNames(QName serviceName, String portName){
        Operation[] op = operations(serviceName, portName);
        String opName[] = new String[op.length];
        for(int i=0; i<op.length; i++)
            opName[i] = op[i].getName();
        return opName;
    }

    @Override
    public QName[] inputPartElementNames(QName serviceName,String portName, String operationName){
        QName[] parameter = null;
        Operation op = operation(serviceName, portName, operationName);
        Input input = op.getInput();
        Message message = input.getMessage();
        Map p = message.getParts();
        parameter = new QName[p.size()];
        Iterator iter = p.values().iterator();
        int index = 0;
        while(iter.hasNext()){
            parameter[index++] = ((Part)iter.next()).getElementName();
        }
        return parameter;
    }


    @Override
    public QName[] outputPartElementNames(QName serviceName, String portName, String operationName){
       QName[] output = null;
        Operation op = operation(serviceName, portName, operationName);
        Output opt = op.getOutput();
        if(opt != null){
            Message message = opt.getMessage();
            Map p = message.getParts();
            output = new QName[p.size()];
            Iterator iter = p.values().iterator();
            int index = 0;
            while(iter.hasNext()){
                output[index++] = ((Part)iter.next()).getElementName();
            }
        }
        return output;
    }


    private Element[] getPartElement(QName partElementName){
        Element element[] = null;
        if(schemaRoot != null){
            List children = schemaRoot.getChildren(), children1;
            Element temp[] = new Element[children.size()];
            Element temp1, temp2;
            int count = 0;
            for(int i=0; i<children.size(); i++){
                temp1 = (Element)children.get(i);
                if (temp1.getAttributeValue("name").equals(partElementName.getLocalPart())){
                    if ("element".equals(temp1.getName())){
                        children1 = temp1.getChildren();
                        for (int j=0; j<children1.size(); j++){
                            temp2 = (Element) children1.get(j);
                            if ("complexType".equals(temp2.getName())){
                                temp[count++] =temp2;
                            }
                        }
                    }else if(temp1.getName().equals("complexType") ){
                        temp[count++] = (Element)children.get(i);
                    }
                }
            }
            element = new Element[count];
            System.arraycopy(temp, 0, element, 0, count);
        }
        return element;
    }


    @Override
    public Parameter[] inputParameters(QName inputPartElementName, String[] values, int[] index){
        Parameter[] parameter = null;
        Element[] partElement = getPartElement(inputPartElementName);
        //write code for initializint parameter
        if(partElement != null)
            parameter = searchParameters(partElement, values, index);
        return parameter;
    }


    @Override
    public Parameter[] outputParameters(QName outputPartElementName, String[] values, int[] index){
        return inputParameters(outputPartElementName,values, index);
    }


    private Parameter[] searchParameters(Element[] partElement, String[] values, int[] valIndex) {
        Parameter[] parameter = new Parameter[partElement.length];
        List list = null;
        int index = 0;
        boolean hasSequence = false;
        for(int z=0; z<partElement.length; z++){
            parameter[z] = new Parameter();
            parameter[z].setName(partElement[z].getAttributeValue("name"));
            if(partElement[z].getName().equals("complexType")){
                parameter[z].setType("complexType");
                list = partElement[z].getChildren();
                hasSequence = false;
                for(int i=0; i<list.size(); i++){
                    if(((Element)list.get(i)).getName().equals("sequence")){
                        index = i;
                        hasSequence = true;
                        break;
                    }
                }
                if(hasSequence){
                    List lst = ((Element)list.get(index)).getChildren();
                    Element el[] = new Element[lst.size()];
                    for(int i=0; i<el.length; i++)
                        el[i] = (Element)lst.get(i);
                    parameter[z].setChildren(searchParameters(el, values, valIndex));
                }
            }
            else if(partElement[z].getAttributeValue("type")!= null){
                parameter[z].setType(partElement[z].getAttributeValue("type"));
                parameter[z].setChildren(null);
                if (values != null){
                    parameter[z].setValue(values[valIndex[0]]);
                    valIndex[0]++;
                }
            }
        }
        return parameter;
    }


    @Override
    public String getPortLocation(QName serviceName, String portName){
        String location = null;
        SAXBuilder builder = new SAXBuilder(false);
        Document document = null;
        try {
            (new NetworkConfigHandler()).proxyAuthenticate();
            document = builder.build(wsdlUrl);
        } catch (Throwable ex) {
            Logger.getLogger(WSDL_1_0_Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        Element root = document.getRootElement();
        List children = root.getChildren();
        Element temp = null;
        for(int i=0; i<children.size(); i++){
            temp = (Element)children.get(i);
            if(temp.getName().equals("service")
                    && temp.getAttributeValue("name").equals(serviceName.getLocalPart()))
            {
                List serviceChildren = temp.getChildren();
                for(int j=0; j<serviceChildren.size(); j++){
                    temp = (Element)serviceChildren.get(j);
                    if(temp.getName().equals("port") &&
                            temp.getAttributeValue("name").equals(portName)){
                        List soapChildren = temp.getChildren();
                        for(int k=0; k<soapChildren.size(); k++){
                            temp = (Element)soapChildren.get(k);
                            if(temp.getName().equals("address")){
                                location = temp.getAttributeValue("location");
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }
        return location;
    }

    @Override
    public ArrayList<Parameter>[] getLeafParameters(QName[] partElementName, String[] values) {
        int[] index = new int[1];
        index[0] = 0;
        ArrayList<Parameter>[] leafParams = null;
        if (partElementName != null){
            Parameter[][] params = new Parameter[partElementName.length][];
            leafParams = (ArrayList<Parameter>[])new ArrayList[partElementName.length];
            for (int i=0; i<partElementName.length; i++){
                params[i] = inputParameters(partElementName[i], values, index);
                leafParams[i] = new ArrayList<Parameter>();
                for (int j=0; j<params[i].length; j++){
                    Parameter[] temp = params[i][j].getLeafChildren();
                    leafParams[i].addAll(Arrays.asList(temp));
                }
            }
        }
        return leafParams;
    }

    
}
