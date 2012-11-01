<%-- 
    Document   : StartXcriBuilder
    Created on : 19-Jul-2012, 13:36:42
    Author     : hyeokim
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="uk.ac.uwl.luci.mapping.XCRIGenerator"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        
        <%
        
        if (request.getParameter("start") != null) {
           // try{
            if(request.getParameter("startDate") == null){
                out.println("Please input a start date.");
            }
            else{
                XCRIGenerator xcriGenerator = null; 
                String date = request.getParameter("startDate");
                
                xcriGenerator = XCRIGenerator.getInstance(date.substring(0, 2), date.substring(3, 5));
                
                xcriGenerator.start();
                out.println("The xcribuilder is going to start on "+ date+"."+date.substring(0, 1)+ date.substring(3, 4));
            }
            /* }
            catch(Exception e){
                out.println("Sorry! Your input is wrong.");
            } */
                //IllegalStateException
                //
        } else if (request.getParameter("stop") != null) {
            XCRIGenerator xcriGenerator = XCRIGenerator.getInstance("1", "1");
            xcriGenerator.stop();
             out.println("The xcribuilder has been stopped.");
        }else if (request.getParameter("runOnce") != null) {
            XCRIGenerator xcriGenerator = XCRIGenerator.getInstance("1", "1");
            xcriGenerator.runOnce();
             out.println("The xcribuilder has been runned.");
        }
        %>
    </body>
</html>
