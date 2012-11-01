<%-- 
    Document   : index
    Created on : 29-Feb-2012, 16:43:41
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
        <script type="text/javascript">
        $(function() {
            $('.date-picker').datepicker();
            $( "#datepicker" ).datepicker( "option", "dateFormat", "mm/dd/yy");
        });
        </script>
       
        <title>XCRI-CAP Builder</title>
        

    </head>
    <body>
    <form action="StartXcriBuilder.jsp">

        <label for="startDate">Start Date :</label>
        <input name="startDate" id="startDate" class="date-picker" /><br/><br/>

        <input type="submit" name="start" value="Start Xcribuilder for weekly update">
        <input type="submit" name="runOnce" value="Run Xcribuilder now at once">
        <input type="submit" name="stop" value="Stop Xcribuilder">
         
    </form>
        
</body>
   
</html>
