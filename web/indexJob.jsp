<%-- 
    Document   : indexJob
    Created on : 9-Oct-2012, 16:43:41
    Author     : hyeokim
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.1/jquery.js"></script>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.min.js"></script>
        <link rel="stylesheet" type="text/css" media="screen" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/themes/base/jquery-ui.css">
        
       
        <title>Job RDF Builder</title>
        

    </head>
    <body>
    <form action="StartJobRDFGenerator.jsp">

        <label for="startDate">Repeating Period :</label>
        <input name="period"/> (eg. every hour: 3600000)<br/><br/>

        <input type="submit" name="start" value="Start JobRDFGenerator update.">
        <input type="submit" name="stop" value="Stop JobRDFGenerator">
         
    </form>
        
</body>
   
</html>
