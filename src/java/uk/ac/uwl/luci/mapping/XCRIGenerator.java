// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 27/10/2012 15:33:48
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   XCRIGenerator.java

package uk.ac.uwl.luci.mapping;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.*;
import org.apache.xmlbeans.XmlOptions;
import org.xcri.profiles.x12.catalog.CatalogDocument;

// Referenced classes of package uk.ac.uwl.luci.mapping:
//            Hb2Xcri

public class XCRIGenerator extends TimerTask
{
    private static Timer timer = null;
    private static XCRIGenerator xcriGenerator = null;
    private static Calendar calendar = Calendar.getInstance();
    private static Date firstDate = null;
    private static int period = -1;
    private int month;
    private int day;
    private static Logger logger;

    private XCRIGenerator(String month, String day)
    {
        this.month = 8;
        this.day = 24;
        this.month = (new Integer(month)).intValue() - 1;
        this.day = (new Integer(day)).intValue();
        if(timer == null)
            timer = new Timer();
        configureLogger();
    }

    private void configureLogger()
    {
        try
        {
            java.util.logging.Handler handler = new FileHandler("%t/Xcribuilder%g.log", 0xf4240, 100, true);
            logger = Logger.getLogger(XCRIGenerator.class.getName());
            logger.addHandler(handler);
        }
        catch(IOException ex)
        {
            Logger.getLogger(XCRIGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(SecurityException ex)
        {
            Logger.getLogger(XCRIGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static XCRIGenerator getInstance(String month, String day)
    {
        if(xcriGenerator == null)
            xcriGenerator = new XCRIGenerator(month, day);
        return xcriGenerator;
    }

    public static Logger getLogger()
    {
        return logger;
    }

    public void start()
    {
        timer = new Timer();
        Logger.getLogger(XCRIGenerator.class.getName()).log(Level.INFO, (new StringBuilder()).append("Starting timer. The configured start date is 1/").append(month + 1).append("/").append(day).append(" (yyyy/mm/dd).").toString());
        //runs at 7:05am on inputed date 
        calendar.set(calendar.get(1), month, day, 7, 5);
        firstDate = calendar.getTime();
        
        //weekly run (ms): 1000(sec) * 60 (min) * 60 (hr) * 24 (day) * 7 (week) 
        period = 1000 * 60 * 60 * 24 * 7;
        timer.scheduleAtFixedRate(this, firstDate, period);
    }

    public void runOnce()
    {
        run();
        XCRIGenerator _tmp = this;
        
    }

    public static void stop()
    {
        timer.cancel();
        timer.purge();
        xcriGenerator = null;
        Logger.getLogger(XCRIGenerator.class.getName()).log(Level.INFO, "The thread (timer task) stopped.");
    }

    public void run()
    {
        try {
            Hb2Xcri xcriObj;
            Logger.getLogger(XCRIGenerator.class.getName()).log(Level.INFO, "Started XcriBuilder thread running");
            xcriObj = new Hb2Xcri();
            CatalogDocument cat = xcriObj.buildCatalog();
            
            
            Logger.getLogger(XCRIGenerator.class.getName()).log(Level.INFO, "******************* Statistics of Xcri data ***********************");
            Logger.getLogger(XCRIGenerator.class.getName()).log(Level.INFO, (new StringBuilder()).append("**** Total Courses in Xcri: ").append(xcriObj.getLogCourseCount()).toString());
            Logger.getLogger(XCRIGenerator.class.getName()).log(Level.INFO, (new StringBuilder()).append("**** Total Offerings in Xcri: ").append(xcriObj.getLogCourseInstanceCount()).toString());
            Logger.getLogger(XCRIGenerator.class.getName()).log(Level.INFO, (new StringBuilder()).append("**** Total Offerings in Xcri 2: ").append(xcriObj.getLogCourseInstanceCount2()).toString());
            Logger.getLogger(XCRIGenerator.class.getName()).log(Level.INFO, (new StringBuilder()).append("**** Total PG Course: ").append(xcriObj.getLogPGCount()).toString());
            Logger.getLogger(XCRIGenerator.class.getName()).log(Level.INFO, (new StringBuilder()).append("**** Total UG Course: ").append(xcriObj.getLogUGCount()).toString());
            Logger.getLogger(XCRIGenerator.class.getName()).log(Level.INFO, (new StringBuilder()).append("**** Total Distance Offering: ").append(xcriObj.getLogDistanceLearningCount()).toString());
            HashMap suggestedPrefixes = new HashMap();
            suggestedPrefixes.put("http://purl.org/net/mlo", "mlo");
            suggestedPrefixes.put("http://purl.org/dc/elements/1.1/", "dc");
            suggestedPrefixes.put("http://purl.org/dc/terms/", "dcterms");
            suggestedPrefixes.put("http://purl.org/net/cm", "credit");
            suggestedPrefixes.put("http://www.w3.org/1999/xhtml", "xhtml");
            suggestedPrefixes.put("http://www.w3.org/2001/XMLSchema", "xs");
            suggestedPrefixes.put("http://www.w3.org/XML/1998/namespace", "");
            suggestedPrefixes.put("http://xcri.org/profiles/1.2/catalog", "");
            suggestedPrefixes.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
            XmlOptions opts = new XmlOptions();
            opts.setSavePrettyPrint();
            opts.setSavePrettyPrintIndent(4);
            opts.setSaveSuggestedPrefixes(suggestedPrefixes);
            opts.setSaveAggressiveNamespaces();
            opts.setSaveCDataLengthThreshold(0);
            opts.setSaveCDataEntityCountThreshold(0);
            
            
            if(xcriObj.getLogCourseCount()>0)
            {
                cat.save(new File((new StringBuilder()).append(xcriObj.getProp().getProperty("system.target.location")).append("xcri").append((new SimpleDateFormat("yyyy-MM-dd")).format(Calendar.getInstance().getTime())).append(".xml").toString()), opts);
                cat.save(new File((new StringBuilder()).append(xcriObj.getProp().getProperty("system.target.location")).append("xcri.xml").toString()), opts);
                
                Logger.getLogger(XCRIGenerator.class.getName()).log(Level.INFO, "The output has been written in xml file.");
                Logger.getLogger(XCRIGenerator.class.getName()).log(Level.INFO, "Finishes the thread running.");
            }
            else{
                Logger.getLogger(XCRIGenerator.class.getName()).log(Level.SEVERE, "Course size is zero. No xri xml was updated.");
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(XCRIGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(XCRIGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public static void main(String args[])
    {
        Logger.getLogger(XCRIGenerator.class.getName()).log(Level.INFO, "XcriGenerator started.");
        String month;
        String day;
        if(args.length < 2)
        {
            month = "10";
            day = "09";
        } else
        {
            month = args[0];
            day = args[1];
        }
        XCRIGenerator xcriGenerator = new XCRIGenerator(month, day);
        xcriGenerator.run();
    }


}
