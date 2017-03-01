function showsuggestions(source, suggestionarea){
    mytext = document.getElementById(source).value;
    myvalues = mytext.split(';');

    //alert("Hello!!!" + myvalues[myvalues.length-1]);

//Returns a request object that communicates with server
//function getRequestObject(){
    var xmlhttp;
    if (window.XMLHttpRequest)
    {
        xmlhttp=new XMLHttpRequest();
    }
    else
    {
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
//    return xmlhttp;
//}
xmlhttp.onreadystatechange= function(){
   //alert("HI");
   if (xmlhttp.readyState==4 && xmlhttp.status==200){
       //alert(xmlhttp.responseText);
       document.getElementById(suggestionarea).innerHTML=xmlhttp.responseText;
   }
};
xmlhttp.open('get', '../QuerySuggestions?suggest='+myvalues[myvalues.length-1],true);
xmlhttp.send();
}