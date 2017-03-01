<%-- 
    Document   : user
    Created on : Dec 24, 2010, 12:19:49 PM
    Author     : webservicerank
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="../style.css" rel="stylesheet" type="text/css" media="screen" />
        <link rel="shortcut icon" href="../images/logo.ico"/>
        <script type="text/javascript" src="serviceclient.js"></script>
        <title>WSR - Service Client</title>
    </head>
    <body>
       <div id="wrapper">
	<div id="logo">
		<h1>Web Service Ranking</h1>
		<p><em> </em></p>
	</div>
	<!-- end #logo -->
	<div id="header">
		<div id="menu">
			<ul>
				<li><a href="../.">Home</a></li>
				<li><a href="../publish/.">Publish</a></li>
                                <li><a href="../search/.">Search</a></li>
                                <li><a href="#">About</a></li>
				<li><a href="../admin/.">Admin</a></li>
			</ul>
		</div>
		<!-- end #menu -->

		</div>
	<!-- end #header -->
	<!-- end #header-wrapper -->
	<div id="page" style="">
	<div id="page-bgtop">
		<div id="content">
                <h2>Use the Web Service</h2><br>
            <fieldset>
                <div class="post">
                    <%!
                    String service_id ;
                    %>
                <%
            service_id = (String)session.getAttribute("service_id");
            String serviceName = //"Weather";
                    (String)session.getAttribute("webservice");
            // request.getParameter("webservice");
            String wsdlUrl = //"http://www.deeptraining.com/webservices/weather.asmx?WSDL";
                    (String)session.getAttribute("wsdl");
                    request.getParameter("wsdl");
            String description = //request.getParameter("description");
            (String)session.getAttribute("description");
            int rating = Integer.valueOf(//request.getParameter("reputation"));
            (String)session.getAttribute("reputation"));
            out.println("<font color=\"darkblue\">");
            out.println("<p>Service="+serviceName+"</p>");
            out.println("<p>WSDL="+wsdlUrl+"</p>");
            out.println("<p>Description="+description+"</p>");
            out.println("<p>Rating="+rating+"</p>");
            out.println("</font>");
        %>
                </div>
        <p>
        <span id="view_method">
            <a href="javascript:call_service('<%=service_id%>','<%=serviceName%>', '<%= wsdlUrl%>')">View Methods</a>
        </span>
        </p>
        <div id="service"></div>
        
        <%
            if ("true".equals(session.getAttribute("isAdmin"))){
                %>
                    <jsp:include page="../admin/editservice.jsp">
                        <jsp:param name="service_id" value="${service_id}"></jsp:param>
                    </jsp:include>
                <%
            }
        %>

        <!-- end #content -->
            </fieldset>
	</div>
	</div>
	</div>
        <!-- end #page -->
        <div id="footer-bgcontent">
	<div id="footer">
		<p>A final year project</p>
	</div>
	</div>
	<!-- end #footer -->
       </div>
    </body>
</html>
