<%-- 
    Document   : result
    Created on : Jan 4, 2011, 12:28:54 PM
    Author     : hp
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="shortcut icon" href="../images/logo.ico"/>
        <link rel="stylesheet" type="text/css" href="../style.css" />
                <title>WSR-Result</title>
                <script type="text/javascript" src="search_setting.js"></script>
    </head>
    <body>
        <%!
            String responseText ;
        %>
        <%
            responseText =(String)session.getAttribute("rankedServices");
        %>
       <div id="wrapper">
	<div id="logo">
		<h1>Web Service Ranking</h1>
		<p><em> </em></p>
	</div>
	<!-- end #logo -->
	<div id="header">
		<div id="menu">
			<ul>
				<li><a href="../">Home</a></li>
				<li><a href="../publish/.">Publish</a></li>
                                <li><a href="../search/.">Search</a></li>
                                <li><a href="..about/.">About</a></li>
				<li><a href="../admin/.">Admin</a></li>
			</ul>
		</div>
		<!-- end #menu -->

        
	<!-- end #header -->
	<!-- end #header-wrapper -->
        <div style="padding-top:110px;padding-left: 800px;">
                                    <label><h2>Rank By:</h2></label>
                                    <select id="where" name="SearchFormAction" onchange="rankBy(this.value)">
                                    <option value="DOMNQoS">DOM + QoS</option>
                                    <option value="QoS-Value">QoS-Value</option>
                                    <option value="Degree-of-match">Degree of match</option>
                                    <option value="Availability">Availability</option>
                                    <option value="Relaibility">Reliability</option>
                                    <option value="Efficiency">Efficiency</option>
                                    <option value="Cost-Effectiveness">Cost-Effectiveness</option>
                                    <option value="Reputation">Reputation</option>
                                    </select>
        </div>
                                    			
		</div>
		<!-- end #search -->
	<div id="page" style="">
	<div id="page-bgtop">
		<div id="content">
			<div class="post">
                            <h2>Your Search Result</h2><br/>
                            <span id="service_count"></span>
                 <table cellspacing="0">

                    <tr>
                        <th>Rank</th>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Rating</th>
                        <th>Action</th>
                    </tr>
                    
                    <tbody id="result"></tbody>
                     <script type="text/javascript">
                         var resultdata = '<%=responseText%>';
                         var values =new Array();
                         var row = new Array();
                         var htmlText = "";
                         values = resultdata.split(':');
                         if (values[0] == 'Error' && values.length == 2){
                             htmlText = values[1]+' <a href="../search/index.jsp">Search again</a>';
                         }else{
                             var length = values.length-1;
                             document.getElementById('service_count').innerHTML=length+" services matched";
                             var rank=1;
                             for (var i=0; i<values.length-1; i++)
                             {   var tempText="<img alt=\"\"  src=\"../images/star.jpg\" height=\"16px\" width=\"16px\"/>"
                                 row = values[i].split(';');
                                 if(row[2]=='2')
                                 tempText=tempText+tempText;
                                 else if(row[2]=='3')
                                 tempText=tempText+tempText+tempText;
                                 else if(row[2]=='4')
                                 tempText=tempText+tempText+tempText+tempText;
                                 else if(row[2]=='5')
                                 tempText=tempText+tempText+tempText+tempText+tempText;
                                 else if(row[2]=='0')
                                 tempText="Not rated";
                                 htmlText+="<tr onmouseout=\"this.bgColor=''\" onmouseover=\"this.bgColor='lightblue'\"><td style=\"color:black\">"+rank+++"</td><td style=\"color:darkblue\">"+row[0]+"\
                                           </td><td>"+row[1]+"</td><td width=\"80px\">"+tempText+"</td><td><a href=\"../SearchInterface?service_id="+row[3]+"&rank="+i+"\">Call it!</a></td></tr>";
                              }
                         }
                        document.getElementById('result').innerHTML = htmlText;
                     </script>
                  
                 </table>
			</div>
       
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
