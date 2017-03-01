/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

isParticularMethod = false;

function getRequestObject(){
    var xmlhttp;
    if (window.XMLHttpRequest)
    {
        // code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp=new XMLHttpRequest();
    }
    else
    {
        // code for IE6, IE5
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    return xmlhttp;
}


function call_service(s_id, sName, wsdlUrl){
    var url = '../ClientInterface?servicename='+sName+'&wsdlUrl='+wsdlUrl+'&method=0&service_id='+s_id;
    var xmlhttp = getRequestObject();
    xmlhttp.onreadystatechange = function(){
        if (xmlhttp.readyState==4 && xmlhttp.status==200)
        {
//            alert (xmlhttp.responseText);
            var output = "";
            var methods = new Array();
            var error = new Array();
            error = xmlhttp.responseText.split(":");
            if (error != null && error.length !=0 && error[0] == 'Error'){
                for (var i=1; i<error.length; i++)
                    output += error[i];
                document.getElementById('view_method').innerHTML = output;
            }else{
                methods = xmlhttp.responseText.split(";");
                for (var i=0; i<methods.length-1; i++){
                    output +='<p style="text-transform:uppercase;"><b>'+ methods[i]+'</b></p>'
                        +'<p><a id="'+methods[i]+'_caller" href="javascript:call_method('+"'"+s_id+"','"+methods[i]+"'"+')">Call</a></p><span><div id="'
                        +methods[i]+'_input"></div></span>';
                }
                document.getElementById("service").innerHTML = output;
                document.getElementById('view_method').style.display = 'none';
            }            
        }
    }
//    alert(sName+" "+s_id+" "+wsdlUrl);
    xmlhttp.open("GET",url,true);
    xmlhttp.send();
}


function call_method(s_id, method){
    if (isParticularMethod == true){
        alert('You are currently using another method.\n Finish/cancel it first')
        return;
    }
    isParticularMethod = true;
    document.getElementById(method+"_caller").style.display = 'none';
    var xmlhttp = getRequestObject();
    xmlhttp.onreadystatechange = function(){
        if (xmlhttp.readyState==4 && xmlhttp.status==200){
            var htmlText = '<div id="'+method+'_area">';
            var inputParts = new Array();
            var inputParams = new Array();
            var param = new Array();
            var inFieldsCount = 0;
            inputParts = xmlhttp.responseText.split("#");
            for (var i=0; i<inputParts.length-1; i++){
                inputParams = inputParts[i].split(";");
                for (var j=0; j<inputParams.length-1; j++){
                    param = inputParams[j].split(",");
                    htmlText += '<p> '+param[0]+' ('+param[1]+')'
                    +'<input type="text" id="'+method+inFieldsCount+'"/></p>';
                    inFieldsCount++;
                }
            }
           htmlText += '<p><a href="javascript:methodCalled('+"'"+s_id+"','"
               +method+"','"+inFieldsCount+"'"+')">Call the function</a>&nbsp&nbsp<span id="'+method+'_result"/></span>';
           htmlText += '<div id="'+method+'_cancel"><a href="javascript:cancelMethodCall('
               +"'"+method+"'"+')">Cancel</a></div></p>';
           document.getElementById(method+"_input").innerHTML = htmlText;
        }
    }
    var url = '../ClientInterface?methodname='+method+'&method=1&service_id='+s_id;
    xmlhttp.open("GET",url,true);
    xmlhttp.send();    
}


function methodCalled(s_id, method, fieldsCount){
    var temp;
    var inputs = '';
    for (var i=0; i<fieldsCount; i++){
        temp = method+i;
        inputs += document.getElementById(temp).value+':';
    }
    var url = '../ClientInterface?parameters='+inputs+'&method=2&service_id='+s_id;
    var xmlhttp = getRequestObject();
    xmlhttp.onreadystatechange = function(){
        if (xmlhttp.readyState==4 && xmlhttp.status==200){
            var parts = new Array();
            //alert(xmlhttp.responseText);
            parts = xmlhttp.responseText.split('$#$');
            var htmlText = '<p>The Returned Values are:<br/> ';
            var output = new Array();
            var temp = new Array();
            output = parts[0].split(';');
            for(var i=0; i<output.length-1; i++){
                temp = output[i].split(':');
                htmlText+=temp[0]+' ='+temp[1]+'</p>';
            }
            htmlText+=''+parts[1]+'';
            htmlText2='<form name="myform" action="javascript:userrate('+"'"+method+"','"+s_id+"'"+')"><p> Rate this Web Service: 1<input type="radio" name="rate" value="1"> \n\
            2<input type="radio" name="rate" value="2"> 3<input type="radio" name="rate" value="3"> 4<input type="radio" name="rate"value="4">\n\
            5<input type="radio" name="rate" value="5"><input type="submit" value="rate" class="formbotton" />';
            document.getElementById(method+'_result').innerHTML = htmlText+htmlText2;
            document.getElementById(method+'_cancel').innerHTML = '<a href="javascript:cancelMethodCall('
               +"'"+method+"'"+')">Finished</a>';
        }
    }
   xmlhttp.open("GET",url,true);
   xmlhttp.send();
}

//<form name="myform">
//onClick="userrate('+"'"+s_id+"'"+')" 
function cancelMethodCall(method){
    document.getElementById(method+"_caller").style.display = '';
    var element = document.getElementById(method+"_area");
    element.parentNode.removeChild(element);
    isParticularMethod = false;
}

 function userrate(method,service_id)
      {
          var val="";
          for (i=0; i<document.myform.rate.length; i++)
         {
            if (document.myform.rate[i].checked==true)
            {
               val+=document.myform.rate[i].value;
               var xmlhttp = getRequestObject();
               xmlhttp.onreadystatechange= function(){
                   if (xmlhttp.readyState==4 && xmlhttp.status==200){
                       document.getElementById(method+'_result').innerHTML = "<p>"+xmlhttp.responseText+"</p>";
                   }
               };
               xmlhttp.open('get', '../ClientInterface?method=3&rating='+val+'&service_id='+service_id,true);
               xmlhttp.send();
            }
         }
         
      }
