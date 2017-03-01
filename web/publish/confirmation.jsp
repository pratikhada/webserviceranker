<%-- 
    Document   : confirmation
    Created on : Oct 1, 2010, 9:27:15 PM
    Author     : vhishma
--%>

<%@page import="webservicerank.uiAppInterface.ServiceDetail"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../style.css" />
        <link rel="shortcut icon" href="images/logo.ico"/>
        <title>WSR- Search Web Service</title>
        <script language="javascript" type="text/javascript" src="search_setting.js"></script>
    </head>
     </head>
    <body>
        <div id="wrapper">
        <div id="logo">
		<h1>Web Service Ranking</h1>
		<p><em></em></p>
	</div>
        <div id="header">
		<div id="menu">
			<ul>
				<li><a href="../index.jsp">Home</a></li>
				<li><a href="index.jsp">Publish</a></li>
                                <li><a href="../search/.">Search</a></li>
                                <li><a href="../.">About</a></li>
				<li><a href="../admin/.">Admin</a></li>
			</ul>
		</div>
            <!--end of menu-->
            </div>
        <!-- end #header -->
	<!-- end #header-wrapper -->
        <div id="page" style="">
	<div id="page-bgtop">
		<div id="content">
			<div style="color:red">

<%

if( session.getAttribute("PUBLISHED") != null && "true".equals(session.getAttribute("PUBLISHED"))){
        request.getSession().setAttribute("PUBLISHED", "null");
        out.println("<table>");
        out.println("<tr><td colspan=\"2\"><b>You Just Registered a Web Service with the following Details:</b><br/></td><td>");
        out.println("<tr><td>Service Category </td><td>: "+session.getAttribute("sd1")+"</td></tr>");
        out.println("<tr><td>Service Name </td><td>: "+session.getAttribute("sd2")+"</td></tr>");
        out.println("<tr><td>WSDL URL </td><td>: "+session.getAttribute("sd3")+"</td></tr>");
        out.println("<tr><td>Properties </td><td>: "+session.getAttribute("sd4")+"</td></tr>");
        out.println("<tr><td>Home URL </td><td>: "+session.getAttribute("sd5")+"</td></tr>");
        out.println("<tr><td>OWL URL </td><td>: "+session.getAttribute("sd6")+"</td></tr>");
        out.println("<tr><td>Description </td><td>: "+session.getAttribute("sd7")+"</td></tr>");
        out.println("<table>");

        //clear all above variables
        String str;
        for(int z=0; z<8; z++){
                str = "sd"+z;
                session.removeAttribute(str);
        }
        //check if attribute is clear or not
        //out.println("Service Category: "+session.getAttribute("sd1"));

    out.println("<br/><br/>Web Service Published Successfully!<br/><br/>");
    out.println("<br/><h3>THANK YOU!</h3><br/><br/>");

}
out.println("<br/>" + session.getAttribute("message[0]"));
out.println("<br/>" + session.getAttribute("message[1]"));
session.removeAttribute("message[0]");
session.removeAttribute("message[1]");
%>
                        </div>
                </div>
        </div>
        </div>
   <div id="footer-bgcontent">
	<div id="footer">
		<p>A final year project</p>
	</div>
	</div>
	<!-- end #footer -->
</div>
 </body>
</html>
