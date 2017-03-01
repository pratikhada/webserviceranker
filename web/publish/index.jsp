<%--
    Document   : publish
    Created on : Sep 27, 2010, 5:52:21 PM
    Author     : vhishma
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <script language="javascript" type="text/javascript" src="../wsr_lib.js"></script>
        <script type="text/javascript" src="servicepublisher.js" ></script>
        <link rel="stylesheet" type="text/css" href="../style.css" />
        <link rel="shortcut icon" href="../images/logo.ico"/>
        <title>WSR - Publish Web Service</title>
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
				<li class="current_page_item"><a href="#">Publish</a></li>
                                <li><a href="../search/.">Search</a></li>
                                <li><a href="../about/.">About</a></li>
				<li><a href="../admin/.">Admin</a></li>
                        </ul>
		</div>
        </div>
           
				    
		<!-- end #search -->
 
            <div id="page">
	<div id="page-bgtop">
		<div id="content">
			<div class="post">

                            <h2>Publish Your Web Services</h2><br><br>
              
<fieldset>
<legend>Enter data</legend>
<form id="publishform" action="../PublisherInterface" method="post"><!-- onsubmit="return submitform();"-->
   
     <font color="red">
        <%
        //display error message corresponding to the getAttribute for message[]
        String[] errmessage = new String[10];
        errmessage[0] = "Error While Publishing Web Service!";
        errmessage[1] = "WSDL Validation Failed!";
        errmessage[2] = "error message two";
        errmessage[3] = "error message three";
        errmessage[4] = "error message four";
        errmessage[5] = "error message five";
        errmessage[6] = "error message six";
        errmessage[7] = "error message seven";
        errmessage[8] = "error message eight";
        errmessage[9] = "error message nine";

        if( "error".equals(session.getAttribute("message[0]"))){
            out.println("<b><i>"+errmessage[0]+"</i></b><br/>");
            session.removeAttribute("message[0]");
        }

        String str;
        for(int z=0; z<10; z++){
                str = "message["+z+"]";
                //request.getSession().setAttribute(str, "error");  //to test the setAttribute of UIAppInterface
                if("error".equals(session.getAttribute(str)))
                    //if(z!=0){ //error message shown already for z=0
                        out.println("<br/>Error : "+errmessage[z]);
                        session.removeAttribute(str);
                   // }
        }

        %>
              Fields with the symbol * are compulsory.<br/><br/><br/>
      </font>
      <p><label for="servicecategory" title="Select which category your WebService falls under.">Service Category:</label>
    <!--input type="text" name="category" value=""/-->
    <SELECT multiple size="3" name="category">
        <!--OPTION selected value="None Selected">--- Select Group ---</OPTION-->
        <OPTION selected value="Business and Commerce">Business and Commerce  </OPTION>
        <OPTION>Communications/Information</OPTION>
        <OPTION>Data Lookup</OPTION>
        <OPTION>Graphics and Multimedia</OPTION>
        <OPTION>Education</OPTION>
        <OPTION>Data Manipulation/Unit Conversion</OPTION>
        <OPTION>Entertainment</OPTION>
    </SELECT></p>
   
    <p><label title="Enter your WebService's name">Service Name:</label>
    <input type="text" name="servicename" value="" onblur="check_service_name(this.value,1)" onfocus="error_message(1,'')"/><font color="red">*</font></p>
    <div id="error_message1" style="color:red;margin-bottom: 10px;text-decoration: blink"></div>

    <p><label title="Enter your WebService's WSDL URL"> Service WSDL URL:</label>
    <input type="text" name="wsdlURL" value=""onblur="" onfocus="error_message(4,'')"/><font color="red">*</font></p>
    <div id="error_message4" style="color:red;margin-bottom: 10px;text-decoration: blink"></div>

    <p><label title="Enter your WebService's Homepage URL">Home page URL:</label>
    <input type="text" name="homeURL" value="" onblur="" onfocus="error_message(5,'')"/><p>
    <div id="error_message5" style="color:red;margin-bottom: 10px;text-decoration: blink"></div>

    <p><label title="Enter your WebService's QWL URL">OWL URL (optional):</label>
    <input type="text" name="owlURL" value="" onblur="" onfocus="error_message(6,'')" />
    <input name="publish owl" type="submit" class="formbotton"  value="Publish OWL only" onclick="publishOwlOnly()"/></p>
    <input type="hidden" name="option_to_publish" id="option_to_publish" value="PUBLISH_ALL"/>
    <div id="error_message6" style="color:red;margin-bottom: 10px;text-decoration: blink"></div>

    <p><label title="Enter your WebService's cost">Service Cost($):</label>
    <input type="text" name="cost" value="0" onblur="check_service_cost(this.value)" onfocus="error_message(3,'')" /></p>
    <div id="error_message3" style="color:red;margin-bottom: 10px;text-decoration: blink"></div>

    <p><label title="Describe your WebService">Description:</label>
    <textarea cols="37"  rows="11" name="description" id="message"></textarea></p>

    <div id="cat_shower">
    <p><label title="Enter your WebService's properties">Service Properties:</label>
        <input type="text" name="properties" value="" onblur="check_service_name(this.value,2)" onfocus="error_message(2,'')" id="uncat"/><font color="red">*  &nbsp;&nbsp; Multiple properties should be separated by ';'</font></p>
    <div id="error_message2" style="color:red;margin-bottom: 10px;text-decoration: blink"></div>
    <a href="javascript:categorizedProperties()" >Click to enter categorized properties</a>
    </div>

    <div id="categorized">
        <p><label title="Enter Categorized properties">Categorized Properties:</label><font color="red"> Multiple properties should be separated by ';'</font></p>
        <div style="margin-left: 150px">
        <p><label title="What may be the Inputs to your Webservice?">Input:</label>
        <input type="text" name="input" value="" id="input" onblur="check_service_name(this.value,7)" onfocus="error_message(7,'')"/><font color="red">*</font></p>
        <div id="error_message7" style="color:red;margin-bottom: 10px;text-decoration: blink"></div>
        <p><label title="What may be the Output of your WebService?">Output:</label>
        <input type="text" name="output" value="" onblur="check_service_name(this.value,8)" onfocus="error_message(8,'')"/><font color="red">*</font></p>
        <div id="error_message8" style="color:red;margin-bottom: 10px;text-decoration: blink"></div>
        <p><label title="What are the Preconditions to your WebService?">Precondition:</label>
        <input type="text" name="precondition" value="" onblur="check_service_name(this.value,9)" onfocus="error_message(9,'')"/></p>
        <div id="error_message9" style="color:red;margin-bottom: 10px;text-decoration: blink"></div>
        <p><label title="What are the Effects of your WebService?">Effect:</label>
        <input type="text" name="effect" value="" onblur="check_service_name(this.value,10)" onfocus="error_message(10,'')"/></p>
        <div id="error_message10" style="color:red;margin-bottom: 10px;text-decoration: blink"></div>
        <a href="javascript:cancelCategorized()">Use general properties</a>
        </div>
    </div>
    <script type="text/javascript">
        document.getElementById('categorized').style.display = 'none';
    </script>
    
    <br/><br/>
    <input name="submit" style="margin-left: 150px" type="submit" class="formbotton"  value="Send"/><!-- onclick="submitform()"/-->
    <input name="reset" type="reset" class="formbotton" value="Reset"/>
            
</form>
</fieldset>
        </div>
        </div>
        </div>
            </div>
         <div id="footer-bgcontent">
	<div id="footer">
		<p>A final year project</p>
	</div>
	</div>
        </div>
    </body>

</html>
