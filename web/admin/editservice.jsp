<%-- 
    Document   : editservice
    Created on : Jan 22, 2011, 4:28:48 PM
    Author     : hp
--%>

<%@page import="java.util.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit service</title>
        <script type="text/javascript">
            function getRequestObject(){
                var xmlhttp;
                if (window.XMLHttpRequest)
                {
                    xmlhttp=new XMLHttpRequest();
                }
                else
                {
                    xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
                }
                return xmlhttp;
            }

            function viewServiceDetail(serviceid){
                if (serviceid == null){
                    alert('Error! The service ID is null.');
                    return;
                }
                var url='../AdminInterface?query=viewdetail&service_id='+serviceid;
                var xmlhttp = getRequestObject();
                xmlhttp.onreadystatechange = function(){
                    if (xmlhttp.readyState==4 && xmlhttp.status==200){
                        var outText ='';
                        var result = xmlhttp.responseText.split('$$$$$');
                        for (var i=0; i<result.length-1; i++){
                            var fields = result[i].split('####');
                                outText +='<br/>'+fields[0];
                            if (i==9){
                                outText += ":<a href=\"mailto:"+fields[1]+"\">"+fields[1]+"</a>";
                            }else{
                                outText += ":"+fields[1];
                            }
                        }
                        document.getElementById("service_details").innerHTML = outText;
                    }
                }
                xmlhttp.open('get',url, true);
                xmlhttp.send();
            }

            function markAsChecked(service_id){
                if (service_id == null){
                    alert('Error! The service ID is null.');
                    return;
                }
                var url='../AdminInterface?action=checkAsMarked&service_id='+service_id;
                var xmlhttp = getRequestObject();
                xmlhttp.onreadystatechange = function(){
                    if (xmlhttp.readyState==4 && xmlhttp.status==200){
                        document.getElementById('mark_checked').innerHTML = xmlhttp.responseText;
                    }
                }
                xmlhttp.open('get',url, true);
                xmlhttp.send();
            }

            function deleteService(service_id){
                if (service_id == null){
                    alert('Error! The service ID is null.');
                    return;
                }
                var url='../AdminInterface?action=deleteService&service_id='+service_id;
                var xmlhttp = getRequestObject();
                xmlhttp.onreadystatechange = function(){
                    if (xmlhttp.readyState==4 && xmlhttp.status==200){
                        document.getElementById('delete_service').innerHTML = xmlhttp.responseText;
                        document.getElementById('mark_checked').style.display = 'none';
                        document.getElementById('service_details').style.display = 'none';
                    }
                }
                xmlhttp.open('get',url, true);
                xmlhttp.send();
            }

        </script>
    </head>
    <body>
        <h2><u>Section for editing service details!</u></h2>
        <%!
            String service_id;
        %>
        <%
            service_id = request.getParameter("service_id");
           /*service_id = (Integer)session.getAttribute("service_id");
           String serviceName = String.valueOf(session.getAttribute("service_name"));
           String wsdl = String.valueOf(session.getAttribute("wsdl"));
           String home = String.valueOf(session.getAttribute("home"));
           String description = String.valueOf(session.getAttribute("description"));
           float cost = (Float)session.getAttribute("cost");
           Date published_date = (Date)session.getAttribute("published_date");
           boolean isCheckedByAdmin = (Boolean)session.getAttribute("isCheckedByAdmin");
           publisher_id = (Integer)session.getAttribute("publisher_id");
           String[] properties = (String[])session.getAttribute("properties");
           out.print("Service Name:"+serviceName);
           out.print("WSDL :"+wsdl);
           out.print("Home url:"+home);
           out.print("Description:"+description);
           out.print("Cost:"+cost);
           out.print("Published Date:"+published_date);
           out.print("Check status:"+isCheckedByAdmin);
           out.print("Properties:<br/>");
           for (int i=0; i<properties.length; i++)
               out.print("  "+properties[i]+"<br/>");*/
        %>
        <br/>
        <span id="service_details">
            <a href="javascript:viewServiceDetail('<%=service_id%>')">View details of the service</a>
        </span>
        <br/>
        <br/>
        <span id="mark_checked">
            <input type="submit" value="Mark as verified" onclick="markAsChecked('<%=service_id%>')"/>
        </span>
        <br/>
        <br/>
        <span id="delete_service">
            <input type="submit" value="Delete this service" onclick="deleteService('<%=service_id%>')"/>
        </span>
        <br/>
    </body>
</html>
