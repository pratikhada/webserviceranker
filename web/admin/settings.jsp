<%-- 
    Document   : setup
    Created on : Dec 28, 2010, 10:12:36 PM
    Author     : webservicerank
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="admin_settings.js"></script>
        <title>system settings</title>
    </head>
    <body>
        <br/><br/>
        <!-- Initial setup of the system-->
        <a id="setupShower" href="javascript:systemSetup('show')">Click here for initial setup</a>
        <div id="initial_setup">
            (The directory should not contain any directory named 'webservicerank')<br/><br/>
            Setup Directory <input type="text" id="setupDirectory"/><br/>            
            <input type="submit" value="perform setup" onclick="systemSetup('submit')" id="system_submit"/>
            <div id="warning">(You will need to delete all folders and files previously created manually)
            </div><br/>
            <a id="leaveSetup" href="javascript:javascript:systemSetup('hide')">cancel setup</a>
        </div>         

        <!-- Network options configuration-->
        <br/><br/>
        <a id="network_shower" href="javascript:networkConfigure('show')">Network Settings</a><br/><br/>
        <div id="network_configure">
            <input type="checkbox" id="proxySelector" onchange="networkConfigure('proxy')"/> Proxy is enabled<br/>
            <div id="proxyselected">
                Proxy IP <input type="text" id="proxy"/><br/>
                Proxy Port <input type="text" id="port"/><br/>
                <input type="checkbox" id="loginSelector" onchange="networkConfigure('login')"/>
                Login is required<br/><br/>
                <div id="loginselected">
                    Username <input type="text" id="username"><br/>
                    Password <input type="password" id="password"/><br/>
                </div>                
            </div>
            <input type="submit" value="Submit" onclick="networkConfigure('submit')"/><br/>
            <a href="javascript:networkConfigure('hide')" id="network_leave">Cancel</a>
        </div>
        <br/>

        <!-- WordNet's Directory Setting -->        
        <a id="wordnet_shower" href="javascript:wordnetSetting('show')">Set WordNet directory's path</a><br/>
        <div id="wordnet">
            Path of WordNet Dictionary (Directory)
            <input type="text" id="wordnet_dir"/>
            <input type="submit" value="OK" id="wordnet_ok" onclick="wordnetSetting('submit')"/>
            <br/><a href="javascript:wordnetSetting('hide')" id="leaveWordnet">cancel</a>
        </div>
        <br/>

        <!-- Settings for service repository 'webservicerank' -->
        <a id="sd_shower" href="javascript:sdSetting('show')">Set service database parameters</a><br/>
        <div id="service_database">
            <br/>Database Driver <input id="sd_driver" type="text"/>
            <br/>Database Host <input id="sd_host" type="text"/>
            <br/>Port <input id="sd_port" type="text"/>
            <br/> <input type="checkbox" id="sd_loginSelector" onclick="sdSetting('login')"/>
            Database needs login
            <div id="sd_login">
                <br/>Username <input type="text" id="sd_username"/>
                <br/>Password <input type="password" id="sd_password"/>
            </div>
            <br/><input type="submit" value="OK" id="sd_ok" onclick="sdSetting('submit')"/>
            <br/><a href="javascript:sdSetting('hide')" id="leaveSD">cancel</a>
        </div>
        <br/>

        <!-- Settings for ontology repository 'webservicerank_ont' -->
        <a id="ont_shower" href="javascript:ontSetting('show')">Set ontology database parameters</a>
        <div id="ontology_database">
            <br/>JDBC Driver <input type="text" id="ont_driver"/>
            <br/>Database Host <input type="text" id="ont_host"/>
            <br/>Database Port <input type="text" id="ont_port"/>
            <br/>Database Type 
            <SELECT id="ont_type">
                <option>MySQL</option>
                <option>PostgreSQL</option>
                <option>Oracle</option>
                <option>SQL Server</option>
                <option>Derby</option>
                <option>HSQLDB</option>
            </SELECT>
            <br/> <input type="checkbox" id="ont_loginSelector" onclick="ontSetting('login')"/>
            Ontology database needs login
            <div id="ont_login">
                <br/>Username <input type="text" id="ont_username"/>
                <br/>Password <input type="password" id="ont_password"/> 
            </div>
            <br/><input type="submit" value="OK" id="ont_ok" onclick="ontSetting('submit')"/>
            <br/><a href="javascript:ontSetting('hide')" id="leaveont">cancel</a>
        </div>
        <br/>

        <script type="text/javascript">
            document.getElementById('initial_setup').style.display = 'none';
            document.getElementById('network_configure').style.display = 'none';
            document.getElementById('wordnet').style.display = 'none';
            document.getElementById('service_database').style.display = 'none';
            document.getElementById('ontology_database').style.display = 'none';
        </script>

        <br/><br/>
        <a href="index.jsp">Go to admin's home</a>
    </body>
</html>
