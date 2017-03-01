

<%--
    Document   : publish
    Created on : Sep 27, 2010, 5:52:21 PM
    Author     : Pratik
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>WSR - Web Service Ranking</title>
<meta name="keywords" content="" />
<meta name="description" content="This is web service ranking page." />
<link href="style.css" rel="stylesheet" type="text/css" media="screen" />
<link rel="shortcut icon" href="images/logo.ico"/>
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
				<li class="current_page_item"><a href="index.jsp">Home</a></li>
				<li><a href="publish/.">Publish</a></li>
                                <li><a href="search/.">Search</a></li>
                                <li><a href="about/.">About</a></li>
				<li><a href="admin/.">Admin</a></li>
			</ul>
                    
		</div>
		<!-- end #menu -->
		
        </div>
        <!-- end #header -->
	<!-- end #header-wrapper -->
                <%
        String message = (String) session.getAttribute("message");
        session.removeAttribute("message");

        //session.setAttribute("logged_user", "vhishma");
        if("logout".equals(request.getParameter("action"))){
            session.removeAttribute("logged_user");
            session.removeAttribute("logged_type");
            session.removeAttribute("message");
            session.removeAttribute("user_id");
            session.removeAttribute("isAdmin");
            response.sendRedirect(request.getContextPath()+"");
        }
        
        
        if(session.getAttribute("logged_user")==null){
            //out.print("Welcome User!!!");
            %>
            
         <div id="page" style="">
	
	
	<div id="log">
                <fieldset>
            <form action="./AuthServlet" method="post">
                <label>User Name</label> <input type="text" name="username" value="" /><br/>
                <label>Password</label><input type="password" name="password" value="" />&nbsp;
                   <input type="submit" name="submit" value="Login" />
            </form>
           </fieldset>
            </div>
        <div id="page-bgtop">
        <div id="content">
        <div class="post">
             <%
            
        }else{%>
      
        <div id="page-bgtop">
        <div id="content">
        <div class="post">
        
            
        <%

            out.print( "Welcome, "+ session.getAttribute("logged_user") +"!" );
            out.print("  <a href=\"index.jsp?action=logout\">logout</a>");
        }
         if(message != null){
                out.print(""+message);
            }
        %>
                           <h2>Welcome to web service ranking</h2>
				
				<div class="entry"><p>This is the page where you can publish your web service or search the most efficient among the published web service and use it.
                                        To publish your web service or search for one click on the links below. To publish you must log in.</p></div>
			</div>      
        <div class="ref"><a href="publish/.">Publish Web Service</a>
        <a href="search/.">Search Web Service</a></div>
        <!-- end #content -->
        
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
