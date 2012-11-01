/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.uwl.luci.web;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.xmlbeans.XmlOptions;
import org.xcri.profiles.x12.catalog.CatalogDocument;
import uk.ac.uwl.luci.mapping.Hb2Xcri;

/**
 *
 * @author hyeokim
 */
public class NewServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        Hb2Xcri test = new Hb2Xcri();
        CatalogDocument cat;
        
            
       
        try {
            cat = test.buildCatalog();
            HashMap suggestedPrefixes = new HashMap();
            suggestedPrefixes.put("http://purl.org/net/mlo", "mlo");
            suggestedPrefixes.put("http://purl.org/dc/elements/1.1/", "dc");
            suggestedPrefixes.put("http://purl.org/dc/terms/", "dcterms");
            suggestedPrefixes.put("http://purl.org/net/cm", "credit");
            suggestedPrefixes.put("http://www.w3.org/1999/xhtml", "xhtml");
            suggestedPrefixes.put("http://www.w3.org/2001/XMLSchema", "xs");
            suggestedPrefixes.put("http://www.w3.org/XML/1998/namespace", "");
            suggestedPrefixes.put("http://xcri.org/profiles/1.2/catalog", "");
            XmlOptions opts = new XmlOptions();
            opts.setSavePrettyPrint();
            opts.setSavePrettyPrintIndent(4);
            opts.setSaveSuggestedPrefixes(suggestedPrefixes);
            opts.setSaveAggressiveNamespaces();
            //opts.setSaveImplicitNamespaces(suggestedPrefixes);
            String output = cat.xmlText(opts); 
            cat.save(new File("xcri_full.xml"), opts);
            
            /*
             * TODO output your page here. You may use following sample code.
             */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet NewServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet NewServlet at " + request.getContextPath() + "</h1>");
            out.println("<b> Produced Xcri Cap data </b> ");
            out.println(output);
            out.println("</body>");
            out.println("</html>");
        } catch (Exception ex) {
            Logger.getLogger(Hb2Xcri.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {            
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
