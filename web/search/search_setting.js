/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
isfieldvalid=true;
iscategorized=false;
isweightQos=false;
property=false;
input=false;
output=false;

            function categorizedProperties()
            {
                iscategorized=true;
                document.getElementById('categorized').style.display ='';
                document.getElementById('cat_shower').style.display = 'none';
            }

            function cancelCategorized()
            {
                iscategorizes=false;
                document.getElementById('categorized').style.display = 'none';
                document.getElementById('cat_shower').style.display = '';
            }

            function weightparameters()
            {
                isweightQos=true;
                document.getElementById('weight_parameter').style.display='';
                document.getElementById('show_weight').style.display='none';
            }

            function cancelweightparameters()
            {
                isweightQos=false;
                document.getElementById('weight_parameter').style.display='none';
                document.getElementById('show_weight').style.display='';
            }

function validateForm(){
    if(!(property ||(input && output))){
        alert("Fields marked with '*' are commpulsory");
        return false;
    }else if(isfieldvalid){
         if (iscategorized)
         {
            document.getElementById("ispropertycategorised").value = "true";
         }
         if (isweightQos)
         {
            document.getElementById("isweightQos").value = "true";
         }
         return true;
   } else{
         alert("Any of the input is Invalid!");
//                     document.searchForm.setAction('../SearchInterface');
//                     document.searchForm.send();
        return false;
   }
}


function check_service_name(servicename,flag)
{
    if(servicename!="")
     {
       if(flag==2)
           property=true;
       if(flag==3)
       {
           input=true;
           if(property)
                property=false;
       }
       if(flag==4)
       {
           output=true;
           if(property)
           property=false;
       }
     }
   else
       {
       if(flag==2)
           property=false;
       if(flag==3)
           {
           input=false;
            if(property)
            property=false;
           }
       if(flag==4)
           {
           output=false;
           if(property)
           property=false;
           }
       }
    var count = 0;

    if(servicename.length > 0 ){
    for(var i=0; i < servicename.length ; i++)
    {
        if(servicename.charAt(i)==" ")
        {
            count++;
        }
    }
}
if(count>0){
    if(flag==1)
    error_message(1,"The service name contains<br/>space.Its invalid");
    else if(flag==2)
    error_message(2,"The properties contains space.Its invalid");
    else if(flag==3)
    error_message(3,"The Input properties contains space.Its invalid");
    else if(flag==4)
    error_message(4,"The Output properties contains space.Its invalid");
    else if(flag==5)
    error_message(5,"The Precondition properties contains space.Its invalid");
    else if(flag==6)
    error_message(6,"The Effect properties contains space.Its invalid");
    isfieldvalid=false;
    }else{
        isfieldvalid = true;
    }
}

function check_Qos(text,val)
{
 if(isNaN(text))
     {
         if(val==7)
         error_message(7,"Availabilty should be in number.");
         if(val==8)
         error_message(8,"Reliability should be in number.");
         if(val==9)
         error_message(9,"Efficiency should be in number.");
         if(val==10)
         error_message(10,"CostEffectiveness should be in number.");
         if(val==11)
         error_message(11,"Reputation should be in number.");
         isfieldvalid=false;
     }else{
         isfieldvalid = true;
     }
}

function error_message(val,message)
{
    if(val==1)
    document.getElementById("error_message1").innerHTML=message;
    else if(val==2)
    document.getElementById("error_message2").innerHTML=message;
    else if(val==3)
    document.getElementById("error_message3").innerHTML=message;
    else if(val==4)
    document.getElementById("error_message4").innerHTML=message;
    else if(val==5)
    document.getElementById("error_message5").innerHTML=message;
    else if(val==6)
    document.getElementById("error_message6").innerHTML=message;
    else if(val==7)
    document.getElementById("error_message7").innerHTML=message;
    else if(val==8)
    document.getElementById("error_message8").innerHTML=message;
    else if(val==9)
    document.getElementById("error_message9").innerHTML=message;
    else if(val==10)
    document.getElementById("error_message10").innerHTML=message;
    else if(val==11)
    document.getElementById("error_message11").innerHTML=message;
}


function rankBy(option){
    var xmlhttp = getRequestObject();
    var url = '../SearchInterface?rankBy='+option;
    xmlhttp.onreadystatechange = function(){
        if (xmlhttp.readyState==4 && xmlhttp.status==200){
            updateResult(xmlhttp.responseText);
        }
    }
    xmlhttp.open('get', url, true);
    xmlhttp.send();
}


function updateResult(resultdata){
     var values =new Array();
     var row = new Array();
     var htmlText = "";
     values = resultdata.split(':');
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
    document.getElementById('result').innerHTML = htmlText;
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
