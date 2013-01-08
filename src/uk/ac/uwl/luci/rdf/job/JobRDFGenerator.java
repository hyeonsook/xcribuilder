// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 27/10/2012 15:37:07
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   JobRDFGenerator.java

package uk.ac.uwl.luci.rdf.job;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.uwl.luci.job.TwitterReader;
import uk.ac.uwl.luci.rdf.RDFGenerator;
import uk.ac.uwl.luci.rdf.RepositoryManager;

// Referenced classes of package uk.ac.uwl.luci.rdf.job:
//            JobModel

public class JobRDFGenerator extends RDFGenerator
{

    public JobRDFGenerator()
    {
        jobList = null;
        timer = null;
    }

    public static JobRDFGenerator getInstance()
    {
        if(jobRdfGen != null);
        jobRdfGen = new JobRDFGenerator();
        return jobRdfGen;
    }

    public static void setPeriod(int paramPeriod)
    {
        period = paramPeriod;
    }

    public void setJobList(List jobList)
    {
        this.jobList = jobList;
    }

    public Model extractData()
    {
        Resource provenance = rootModel.createResource("http://luci.uwl.ac.uk/course_linkeddata#provenance");
        rootModel.createReifiedStatement(rootModel.createStatement(provenance, rootModel.createProperty("http://purl.org/dc/elements/1.1/", "publisher"), "University of West London"));
        rootModel.createReifiedStatement(rootModel.createStatement(provenance, rootModel.createProperty("http://purl.org/dc/elements/1.1/", "issued"), (new SimpleDateFormat("yyyy-MM-dd")).format(Calendar.getInstance().getTime())));
        rootModel.createReifiedStatement(rootModel.createStatement(provenance, rootModel.createProperty("http://purl.org/dc/elements/1.1/", "rights"), "http://reference.data.gov.uk/id/open-government-licence"));
        rootModel.createReifiedStatement(rootModel.createStatement(provenance, rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "source"), "http://twitter.com/GraduateJobFeed"));
        for(int index = 0; index < jobList.size(); index++)
        {
            JobModel job = (JobModel)jobList.get(index);
            try
            {
                Resource jobResource = rootModel.createResource(getURI("http://luci.uwl.ac.uk/job_linkeddata/job#", job.getId()));
                jobResource.addProperty(RDF.type, rootModel.createResource("http://luci.uwl.ac.uk/job_linkeddata/Job")).addProperty(rootModel.createProperty("http://purl.org/dc/elements/1.1/", "abstract"), rootModel.createTypedLiteral(job.getAbstrat())).addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "hyperlink"), rootModel.createTypedLiteral(job.getLink())).addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "createdDate"), rootModel.createTypedLiteral(job.getCreatedDate())).addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "sender"), rootModel.createTypedLiteral(job.getSender())).addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "category"), rootModel.createTypedLiteral(job.getCategoriesString()));
            }
            catch(Exception ex)
            {
                Logger.getLogger(JobRDFGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        rootModel.setNsPrefix("job", "http://luci.uwl.ac.uk/job_linkeddata/");
        return rootModel;
    }

    public void start()
    {
        int delay = 5000;
        if(period < 0)
            period = 300000;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run()
            {
                System.out.println("---------- Start the JobRDFGenerator    ------------");
                (new JobRDFGenerator()).run();
                System.out.println("---------- Finished the JobRDFGenerator ------------");
            }
        }, delay, period);
    }

    public void run()
    {
        FileOutputStream fout = null;
        
        try{
            JobRDFGenerator rdfGenerator = new JobRDFGenerator();
            rdfGenerator.filepath = "/srv/www/htdocs/job_rdf/";
            File file = new File((new StringBuilder()).append(rdfGenerator.filepath).append("recent_job.rdf").toString());
            fout = new FileOutputStream(file);
            rdfGenerator.setJobList((new TwitterReader()).readTweet());
            rdfGenerator.extractData().write(fout);
            RepositoryManager.setREPOSITORY_ID("job");
            RepositoryManager.update((new StringBuilder()).append(rdfGenerator.filepath).append("recent_job.rdf").toString(), "http://luci.uwl.ac.uk/job_linkeddata/job");
            Calendar cal = Calendar.getInstance();
            cal.add(6, 1);
            TwitterReader.setLastRunDate(cal.getTime());
            fout.close();
        }
        catch(Exception ex)
        {
            Logger.getLogger(JobRDFGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try{
                fout.close();
            }
            catch(Exception ex)
            {
                Logger.getLogger(JobRDFGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }   
        }
        
    }

    public void stop()
    {
        if(timer != null)
            timer.cancel();
    }

    public static void main(String args[])
    {
        setPeriod(360000);
        getInstance().start();
    }

    private List jobList;
    private Timer timer;
    private static JobRDFGenerator jobRdfGen = null;
    private static int period = 0;

}
