<%-- 
    Document   : search
    Created on : Dec 30, 2010, 10:53:21 AM
    Author     : pratik
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../style.css" />
        <link rel="shortcut icon" href="../images/logo.ico"/>
        <title>WSR- Search Web Service</title>
        <script language="javascript" type="text/javascript" src="../wsr_lib.js"></script>
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
				<li><a href="../publish/.">Publish</a></li>
                                <li class="current_page_item"><a href="#">Search</a></li>
                                <li><a href="../about/.">About</a></li>
				<li><a href="../admin/.">Admin</a></li>
			</ul>
		</div>
            <!--end of menu-->
            </div>
        <!-- end #header -->
	<!-- end #header-wrapper -->
            <div id="search">
			<form method="get" action="../SearchInterface">
				<fieldset>
                                <label><p> Search by Service Name</p></label>
                                <input type="text" name="servicename" id="search-text" size="15" onblur="check_service_name(this.value,1)" onfocus="error_message(1,'')" />
				<input type="submit" id="search-submit" value="GO" /><br/><br/>
                                <div id="error_message1" style="color:red;margin-bottom: 10px;text-decoration: blink"></div> 
                                </fieldset>
			</form>
		</div>
        <br/><br/>
		<!-- end #search -->
        
	<div id="page">
            <%
                if(session.getAttribute("message")!=null){
                    out.print(session.getAttribute("message"));
                    session.removeAttribute("message");
                 }
            %>
	<div id="page-bgtop">
		<div id="content">
			<div class="post">
				<h2>Search Web Service</h2>
                                <br><br>
                  <div>
                  <fieldset>
                    <legend>Enter data</legend><br>
                    <font style="color:red">Fields with the symbol * are compulsory.</font><br><br>
                    
                    
    <form action="../SearchInterface" method="post" onsubmit="return validateForm();">
        <p><label for="servicecategory">Service Category:</label>
        <SELECT multiple size="3" name="category">
            <!--OPTION selected value="None Selected">--- Select Group ---</OPTION-->
            <OPTION selected value="Business and Commerce">Business and Commerce  </OPTION>
            <OPTION>Communications-Information</OPTION>
            <OPTION>Data Lookup</OPTION>
            <OPTION>Graphics and Multimedia</OPTION>
            <OPTION>Education</OPTION>
            <OPTION>Data Manipulation-Unit Conversion</OPTION>
            <OPTION>Entertainment</OPTION>
        </SELECT></p>
        <div id="cat_shower">
            <p><label>Service Properties:</label>
               <input type="text" name="properties" id="properties" value="" onblur="check_service_name(this.value,2)" onfocus="error_message(2,'')" onkeyup="showsuggestions('properties','suggestion')" />
               <font color="red">*  &nbsp;&nbsp; Multiple properties should be separated by ';'</font>
            </p>
            
              <div id="error_message2" style="color:red;margin-bottom: 10px;text-decoration: blink"></div>
               <a href="javascript:categorizedProperties()" >Click to enter categorized properties</a>


       </div>

       <div id="categorized">
        <p>Categorized Properties: <font color="red">  Multiple properties should be separated by ';'</font></p>
          <div style="margin-left: 150px">
             <p><label>Input:</label>
             <input type="text" name="input" id="input" value="" onblur="check_service_name(this.value,3)" onfocus="error_message(3,'')" onkeyup="showsuggestions('input','suggestion')"/><font color="red">*</font></p>
             <div id="error_message3" style="color:red;margin-bottom: 10px;text-decoration: blink"></div>
             <p><label>Output:</label>
             <input type="text" name="output" id="output" value="" onblur="check_service_name(this.value,4)" onfocus="error_message(4,'')" onkeyup="showsuggestions('output','suggestion')"/><font color="red">*</font></p>
             <div id="error_message4" style="color:red;margin-bottom: 10px;text-decoration: blink"></div>
             <p><label>Precondition:</label>
             <input type="text" name="precondition" id="precondition" value="" onblur="check_service_name(this.value,5)" onfocus="error_message(5,'')" onkeyup="showsuggestions('precondition','suggestion')"/></p>
             <div id="error_message5" style="color:red;margin-bottom: 10px;text-decoration: blink"></div>
             <p><label>Effect:</label>
             <input type="text" name="effect" id="effect" value="" onblur="check_service_name(this.value,6)" onfocus="error_message(6,'')" onkeyup="showsuggestions('effect','suggestion')"/></p>
             <div id="error_message6" style="color:red;margin-bottom: 10px;text-decoration: blink"></div>
             <a href="javascript:cancelCategorized()">Use general properties</a>
          </div>
      </div>

    <script type="text/javascript">
        document.getElementById('categorized').style.display = 'none';
    </script>

       <div id="suggestion">Suggestions Here...</div>
                        
        <br/><p><input name="option" id="option" type="checkbox" /> The properties entered are in the decreasing order of priorities.</p>
             <p><input name="semantic" id="semantic" type="checkbox" /> Avoid semantic matchmaking.</p>

             <div id="show_weight">
                 <a href="javascript:weightparameters()" >Weight Parameters</a>
             </div>
             <div id="weight_parameter">
                 <p>QoS Parameters:</p>
                 <div style="margin-left:150px">
                   <p><label>Availability:</label>
                   <input type="text"name="availability" value="" onblur="check_Qos(this.value,7)" onfocus="error_message(7,'')"/></p>
                   <div id="error_message7" style="color:red;margin-bottom: 10px"></div>
                   <p><label>Reliability:</label>
                   <input type="text"name="reliability" value=""onblur="check_Qos(this.value,8)" onfocus="error_message(8,'')"/></p>
                   <div id="error_message8" style="color:red;margin-bottom: 10px"></div>
                   <p><label>Efficiency:</label>
                   <input type="text"name="efficiency" value="" onblur="check_Qos(this.value,9)" onfocus="error_message(9,'')"/></p>
                   <div id="error_message9" style="color:red;margin-bottom: 10px"></div>
                   <p><label>Cost effectiveness:</label>
                   <input type="text"name="cost_effectiveness" value="" onblur="check_Qos(this.value,10)" onfocus="error_message(10,'')"/></p>
                   <div id="error_message10" style="color:red;margin-bottom: 10px"></div>
                   <p><label>Reputation:</label>
                   <input type="text"name="reputaion" value="" onblur="check_Qos(this.value,11)" onfocus="error_message(11,'')"/></p>
                   <div id="error_message11" style="color:red;margin-bottom: 10px"></div>
                   <a href="javascript:cancelweightparameters()">Cancel parameter weight</a>
                  </div>
             </div>

   <script type="text/javascript">
        document.getElementById('weight_parameter').style.display = 'none';
   </script>
             <input type="hidden" id="ispropertycategorised" name="ispropertycategorised" value="false"/>
             <input type="hidden" id="isweightQos" name="isweightQos" value="false"/>
             <br/><p><input name="send" style="margin-left: 150px;" class="formbotton" value="Send" type="submit"/><!-- onclick="sendForm()"/--></p>
     </form>
     </fieldset>
      </div>
      </div>
      </div>
            <!-- end #content -->
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

