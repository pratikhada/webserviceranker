/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
isActivityActive = false;

//Identify if some of the settings have been running
function isActive(){
    if (isActivityActive == true){
        alert ('You are currently configuring other configurations!!')
    }
    return isActivityActive;
}

//Check whether the input value in the input field is empty
function isInputOk(input){
    if (input == 0 || input == "" || input.charAt(0) == ' '){
        alert(input+'! Input not formatted!\nThe input field mustn\'t be empty or should be formatted');
        return false;
    }else
        return true;
}

//Returns a request object that communicates with server
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

//Return true is the setup was done and is in good condition.
function checkSetup(){ 
    return false;
}

//Performs the system setup
function systemSetup(option){
    if (option == 'show'){
        if (isActive())
            return;
        isActivityActive = true;
        document.getElementById('initial_setup').style.display = '';
        document.getElementById('setupShower').style.display = 'none';
        if (checkSetup() == false){
            document.getElementById('system_submit').value = 'Perform Setup';
            document.getElementById('warning').style.display = 'none';
        }else{
            document.getElementById('system_submit').value = 'Change the Setup';
            document.getElementById('warning').style.display = '';
        }
        document.getElementById('leaveSetup').innerHTML = 'Cancel setup';
    }else if (option == 'hide'){
        isActivityActive = false;
        document.getElementById('initial_setup').style.display = 'none';
        document.getElementById('setupShower').style.display = '';
    }else if (option == 'submit'){
        //validate inputs
        var val = document.getElementById('setupDirectory').value;
        if (isInputOk(val) == false)
            return;
        var xmlhttp = getRequestObject();
        var url = '../AdminInterface';
        var param = 'configuration=setup&workingDir='+val;
        xmlhttp.onreadystatechange = function(){
            if (xmlhttp.readyState==4 && xmlhttp.status==200){
                var result = new Array();
                result = xmlhttp.responseText.split(':');
                alert (result[0]);
                if (result[1] == 'success'){
                    document.getElementById('leaveSetup').innerHTML = 'Finish setup';
                    document.getElementById('system_submit').value = 'Change the Setup';
                    document.getElementById('warning').style.display = '';
                }
            }
        }
        xmlhttp.open('get',url+'?'+param, true);
        xmlhttp.send();
    }
}

//Reads and saves the network configurations
function networkConfigure(option){
    if (option == 'show'){
        if (isActive())
            return;
        isActivityActive = true;
        document.getElementById('network_configure').style.display = '';
        document.getElementById('network_shower').style.display = 'none';
        document.getElementById('proxyselected').style.display = 'none';
        document.getElementById('loginSelector').checked = false;
        document.getElementById('proxySelector').checked = false;
        document.getElementById('network_leave').innerHTML = 'cancel'
    }else if (option == 'hide'){
        document.getElementById('network_configure').style.display = 'none';
        document.getElementById('loginSelector').checked = false;
        document.getElementById('proxySelector').checked = false;
        document.getElementById('network_shower').style.display = '';
        isActivityActive = false;
    }else if (option == 'proxy'){
        if (document.getElementById('proxySelector').checked == true){
            document.getElementById('proxyselected').style.display = '';
            document.getElementById('loginselected').style.display = 'none';
        }else{
            document.getElementById('loginSelector').checked = false;
            document.getElementById('proxyselected').style.display = 'none';
        }
    }else if (option == 'login'){
        if (document.getElementById('loginSelector').checked == true)
            document.getElementById('loginselected').style.display = '';
        else
            document.getElementById('loginselected').style.display = 'none';
    }else if (option == 'submit'){
        var url = '../AdminInterface';
        var param = 'configuration=network';
        if (document.getElementById('proxySelector').checked == true){
            param += '&isProxy=yes';
            var ip = document.getElementById('proxy').value;
            var port = document.getElementById('port').value;
            if (isInputOk(ip) == false)
                return;
            if (isInputOk(port) == false)
                return;                
            var temp = new Array();
            temp = ip.split('.');
            if (temp.length != 4){
                alert ('IP doesn\'t contain 3 periods');
                return;
            }
            for (var i=0; i<temp.length; i++){
                if ( isNaN(temp[i])){
                    alert ('IP contained non-numeric value');
                    return;
                }
            }
            if ( isNaN(port) ){
                alert('Port contains non-numeric value');
                return;
            }
            param += '&ip='+ip+'&port='+port;
            if (document.getElementById('loginSelector').checked == true){
                var un = document.getElementById('username').value;
                var pw = document.getElementById('password').value;
                if (isInputOk(un) == false) return;
                if (isInputOk(pw) == false) return;
                param += '&isLogin=yes&username='+un+'&password='+pw;
            }else{
                param += '&isLogin=no';
            }
        }else{
            param += '&isProxy=no';
        }
        var xmlhttp = getRequestObject();
        xmlhttp.onreadystatechange = function(){
            if (xmlhttp.readyState==4 && xmlhttp.status==200){
                var result = xmlhttp.responseText.split(':');
                alert (result[0]);
                if (result[1] == 'success'){
                    document.getElementById('network_leave').innerHTML = 'finished'
                }
            }
        }
        xmlhttp.open('get',url+'?'+param, true);
        xmlhttp.send();
    }
}

//Sets the wordnet dictionary directory
function wordnetSetting(option){
    if (option == 'show'){
        if(isActive())
            return;
        isActivityActive = true;
        document.getElementById('wordnet_shower').style.display = 'none';
        document.getElementById('wordnet').style.display = '';
        document.getElementById('leaveWordnet').innerHTML = 'cancel'
        document.getElementById('wordnet_dir').value = 'C:\\WordNet-3.0\\dict';
    }else if (option == 'hide'){
        document.getElementById('wordnet_shower').style.display = '';
        document.getElementById('wordnet').style.display = 'none';
        isActivityActive = false;
    }else if (option == 'submit'){
        var dir = document.getElementById('wordnet_dir').value;
        if (isInputOk(dir) == false)
            return;
        var xmlhttp = getRequestObject();
        var url = '../AdminInterface';
        var param = 'configuration=wordnet&directory='+dir;
        xmlhttp.onreadystatechange = function(){
            if (xmlhttp.readyState==4 && xmlhttp.status==200){
                var result = xmlhttp.responseText.split(':');
                alert (result[0]);
                if (result[1] == 'success'){
                    document.getElementById('leaveWordnet').innerHTML = 'finished'
                }
            }
        }
        xmlhttp.open('get',url+'?'+param, true);
        xmlhttp.send();
    }
}

//Reads and saves the service database parameters
function sdSetting(option){
    if (option == 'show'){
        if(isActive())
            return;
        isActivityActive = true;
        document.getElementById('sd_shower').style.display = 'none';
        document.getElementById('service_database').style.display = '';
        document.getElementById('sd_login').style.display = 'none';
        document.getElementById('sd_loginSelector').checked = false;
        document.getElementById('leaveSD').innerHTML = 'cancel'
        document.getElementById('sd_driver').value = 'com.mysql.jdbc.Driver';
        document.getElementById('sd_host').value = 'jdbc:mysql://localhost';
        document.getElementById('sd_port').value = '3306';
        document.getElementById('sd_username').value = 'root';
    }else if (option == 'hide'){
        document.getElementById('sd_shower').style.display = '';
        document.getElementById('service_database').style.display = 'none';
        isActivityActive = false;
    }else if (option == 'login'){
        if (document.getElementById('sd_loginSelector').checked)
            document.getElementById('sd_login').style.display = '';
        else
            document.getElementById('sd_login').style.display = 'none';
    }else if (option == 'submit'){
        var url = '../AdminInterface';
        var param = 'configuration=serviceDB';
        var driver = document.getElementById('sd_driver').value;
        var host = document.getElementById('sd_host').value;
        var port = document.getElementById('sd_port').value;
        if (isInputOk(driver) == false) return;
        if (isInputOk(host) == false) return;
        if (isInputOk(port) == false) return;
        if ( isNaN(port) ){
            alert('Port contains non-numeric value');
            return;
        }
        param += '&driver='+driver+'&host='+host+'&port='+port;
        if (document.getElementById('sd_loginSelector').checked == true){
            var un = document.getElementById('sd_username').value;
            var pw = document.getElementById('sd_password').value;
            if (isInputOk(un) == false) return;
//            if (isInputOk(pw) == false) return;
            param += '&isLogin=yes&username='+un+'&password='+pw;
        }else{
            param += '&isLogin=no';
        }
        var xmlhttp = getRequestObject();        
        xmlhttp.onreadystatechange = function(){
            if (xmlhttp.readyState==4 && xmlhttp.status==200){
                var result = xmlhttp.responseText.split(':');
                alert (result[0]);
                if (result[1] == 'success'){
                    document.getElementById('leaveSD').innerHTML = 'finished'
                }
            }
        }
        xmlhttp.open('get',url+'?'+param, true);
        xmlhttp.send();
    }
}

//Reads and saves the ontology database settings
function ontSetting(option){
    if (option == 'show'){
        if(isActive())
            return;
        isActivityActive = true;
        document.getElementById('ont_shower').style.display = 'none';
        document.getElementById('ontology_database').style.display = '';
        document.getElementById('ont_loginSelector').checked = false;
        document.getElementById('ont_login').style.display = 'none';
        document.getElementById('leaveont').innerHTML = 'cancel'
        document.getElementById('ont_driver').value = 'com.mysql.jdbc.Driver';
        document.getElementById('ont_host').value = 'jdbc:mysql://localhost';
        document.getElementById('ont_port').value = '3306';
        document.getElementById('ont_username').value = 'root';
    }else if (option == 'hide'){
        document.getElementById('ont_shower').style.display = '';
        document.getElementById('ontology_database').style.display = 'none';
        isActivityActive = false;
    }else if (option == 'login'){
        if (document.getElementById('ont_loginSelector').checked)
            document.getElementById('ont_login').style.display = '';
        else
            document.getElementById('ont_login').style.display = 'none';
    }else if (option == 'submit'){
        var url = '../AdminInterface';
        var param = 'configuration=ontologyDB';
        var driver = document.getElementById('ont_driver').value;
        var host = document.getElementById('ont_host').value;
        var port = document.getElementById('ont_port').value;        
        var dbType = document.getElementById('ont_type').value;alert(dbType);
        if (isInputOk(driver) == false) return;
        if (isInputOk(host) == false) return;
        if (isInputOk(port) == false) return;
        if ( isNaN(port) ){
            alert('Port contains non-numeric value');
            return;
        }
        param += '&driver='+driver+'&host='+host+'&port='+port+'&dbType='+dbType;
        if (document.getElementById('ont_loginSelector').checked == true){
            var un = document.getElementById('ont_username').value;
            var pw = document.getElementById('ont_password').value;
            if (isInputOk(un) == false) return;
            param += '&isLogin=yes&username='+un+'&password='+pw;
        }else{
            param += '&isLogin=no';
        }
        var xmlhttp = getRequestObject();
        xmlhttp.onreadystatechange = function(){
            if (xmlhttp.readyState==4 && xmlhttp.status==200){
                var result = xmlhttp.responseText.split(':');
                alert (result[0]);
                if (result[1] == 'success'){
                    document.getElementById('leaveont').innerHTML = 'finished'
                }
            }
        }
        xmlhttp.open('get',url+'?'+param, true);
        xmlhttp.send();
    }
}


function performCleanup(){
    var url = "../AdminInterface?action=cleanup";
    var xmlhttp = getRequestObject();
    xmlhttp.onreadystatechange = function(){
        if (xmlhttp.readyState==4 && xmlhttp.status==200){
            document.getElementById('cleanup').innerHTML = xmlhttp.responseText;
        }
    }
    xmlhttp.open('get',url, true);
    xmlhttp.send();
}

function propLessServices(){
    var url = "../AdminInterface?query=propLessServices";
    var xmlhttp = getRequestObject();
    xmlhttp.onreadystatechange = function(){       
        if (xmlhttp.readyState==4 && xmlhttp.status==200){
            var result = xmlhttp.responseText;
            if (result.split(':')[0]=='Error'){
                document.getElementById('service_with_no_props').innerHTML = result.split(':')[1];
            }else{
                var text = '';
                var services = result.split('$$$$');
                for(var i=0; i<services.length-1;i++){
                    var service = services[i].split('####');
                    text += '<br/>'+service[1]+' <a href="../AdminInterface?source=admin&action=useService&service_id='
                        +service[0]+'>view service details</a>';
                }
                document.getElementById('service_with_no_props').innerHTML = text;
            }
        }
    }
    xmlhttp.open('get',url, true);
    xmlhttp.send();
}
