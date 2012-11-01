<%-- 
    Document   : StartJobRDFGenerator
    Created on : 9-Oct-2012, 13:36:42
    Author     : hyeokim
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="uk.ac.uwl.luci.rdf.job.JobRDFGenerator"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Build Job RDF Data</title>
    </head>
    <body>
        
        <%
        
        if (request.getParameter("start") != null) {
            int period = new Integer(request.getParameter("period"));
            JobRDFGenerator.setPeriod(period);
            JobRDFGenerator.getInstance().start();
            
            out.println("The jobRDFGenerator has been started and will repeat running every "+period+" ms.");
            
        } else if (request.getParameter("stop") != null) {
            JobRDFGenerator.getInstance().stop();
             out.println("The jobRDFGenerator has been stopped.");
        }
        
        %>
    </body>
</html>
