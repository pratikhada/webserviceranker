/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
isCategorised = false;

isfieldvalid = true
service_name=false;
property=false;
input=false;
url=false;


 function categorizedProperties(){
        isCategorised = true;
        document.getElementById('categorized').style.display = '';
        document.getElementById('cat_shower').style.display = 'none';
    }
    function cancelCategorized(){
        isCategorised = false;
        document.getElementById('categorized').style.display = 'none';
        document.getElementById('cat_shower').style.display = '';
    }

function formValidator(form){
    var frm = document.forms[form]; 
    alert ('You submitted the '+form);
}


function check_service_name(servicename,flag)
{
   if(servicename!="")
     {
       if(flag==1)
           service_name=true;
       if(flag==2)
           property=true;
       if(flag==7)
           {
           input=true;
           if(property)
           property=false;
           }
       if(flag==8)
           {
           output=true;
           if(property)
           property=false;
           }
     }
   else
       {
       if(flag==1)
           service_name=false;
       if(flag==2)
           property=false;
       if(flag==7)
           {
           input=false;
            if(property)
            property=false;
           }
       if(flag==8)
           {
           output=false;
           if(property)
           property=false;
           }
       }

     var count = 0;

    if(servicename.length > 0 )
    {
        for(var i=0; i < servicename.length ; i++)
         {
           if(servicename.charAt(i)==" ")
            {
            count++;
            }
          }
   }
if(count>0)
    {
    isfieldvalid=false;
    if(flag==1)
    error_message(1,"The service name contains space.Its invalid");
    else if(flag==2)
    error_message(2,"The properties contains space.Its invalid");
    else if(flag==7)
    error_message(7,"The Input properties contains space.Its invalid");
    else if(flag==8)
    error_message(8,"The Output properties contains space.Its invalid");
    else if(flag==9)
    error_message(9,"The Precondition properties contains space.Its invalid");
    else if(flag==10)
    error_message(10,"The Effect properties contains space.Its invalid");
    }
}

function check_service_cost(text)
{
 if(isNaN(text))
     {
         error_message(3,"Service Cost should contain only numbers.");
         isfieldvalid=false;
     }
}

function check_URL_pattern(val,URL)
{
  if(URL!==""&&val==4)
  url=true;
  else if(URL==""&&val==4)
  url=false;
  if(URL!=""&&!URL.match(/^http[s]*\:\/\/[wW]{3}\.+[a-zA-Z0-9]+\.[a-zA-Z]{2,3}.*$|^http[s]*\:\/\/[^w]{3}[a-zA-Z0-9]+\.[a-zA-Z]{2,3}.*$|http[s]*\:\/\/[0-9]{2,3}\.[0-9]{2,3}\.[0-9]{2,3}\.[0-9]{2,3}.*$/))
    {
    isfieldvalid=false;
    if(val==4)
    error_message(4,"The WSDL URL pattern is incorrect");
    else if(val==5)
    error_message(5,"The Homepage URL pattern is incorrect");
    else if(val==6)
    error_message(6,"The OWL URL pattern is incorrect");

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
}

// ../UIAppInterface;

function submitform()
{
    document.getElementById('option_to_publish').value = 'PUBLISH_ALL';
    if(!(service_name&&url&&(property||(input&&output)))){
        alert("Fields marked with '*' are commpulsory");
        return false;
    }
    else if(isfieldvalid)
    {

        if(isCategorised)
        {
         document.getElementById("categorised").value = "true";
        }
        else
        {
         document.getElementById("categorised").value = "false";
        }

        return true;
   }

   else
   {
        alert("Any of the inputs is invalid");
        return false;
   }
}




function publishOwlOnly(){
    document.getElementById('option_to_publish').value = 'PUBLISH_ONTOLOGY_ONLY';
}