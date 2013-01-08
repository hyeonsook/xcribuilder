// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 27/10/2012 15:36:59
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   EmpRDFGenerator.java

package uk.ac.uwl.luci.rdf.employment;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.RDF;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.uwl.luci.rdf.RDFGenerator;
import uk.ac.uwl.luci.rdf.RepositoryManager;

public class EmpRDFGenerator extends RDFGenerator
{

    public EmpRDFGenerator()
        throws FileNotFoundException
    {
        CSVFile = null;
        CSVFile = new BufferedReader(new FileReader((new StringBuilder()).append(filepath).append("test.csv").toString()));
        rootModel = ModelFactory.createDefaultModel();
    }

    public Model extractData()
    {
        try
        {
            Resource provenance = rootModel.createResource("http://luci.uwl.ac.uk/course_linkeddata#provenance");
            rootModel.createReifiedStatement(rootModel.createStatement(provenance, rootModel.createProperty("http://purl.org/dc/elements/1.1/", "publisher"), "University of West London"));
            rootModel.createReifiedStatement(rootModel.createStatement(provenance, rootModel.createProperty("http://purl.org/dc/elements/1.1/", "issued"), (new SimpleDateFormat("yyyy-MM-dd")).format(Calendar.getInstance().getTime())));
            rootModel.createReifiedStatement(rootModel.createStatement(provenance, rootModel.createProperty("http://purl.org/dc/elements/1.1/", "rights"), "http://reference.data.gov.uk/id/open-government-licence"));
            rootModel.createReifiedStatement(rootModel.createStatement(provenance, rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "source"), "http://luci.uwl.ac.uk/employment/employment.csv"));
            String dataRow = CSVFile.readLine();
            Resource emps = rootModel.createResource(getURI("http://luci.uwl.ac.uk/employment_linkeddata/Employment#", "uwl"));
            int uri_index = 0;
            for(; dataRow != null; dataRow = CSVFile.readLine())
            {
                String dataArray[] = dataRow.split(",");
                Resource emp = rootModel.createResource(getURI("http://luci.uwl.ac.uk/employment_linkeddata/Employment#", (new StringBuilder()).append("").append(uri_index++).toString()));
                emp.addProperty(RDF.type, rootModel.createResource("http://luci.uwl.ac.uk/employment_linkeddata/Employment")).addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/employment_linkeddata/", "category"), rootModel.createTypedLiteral(dataArray[0])).addProperty(rootModel.createProperty("http://purl.org/dc/elements/1.1/", "title"), rootModel.createTypedLiteral(dataArray[1])).addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/employment_linkeddata/", "industry"), rootModel.createTypedLiteral(dataArray[2])).addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/employment_linkeddata/", "course"), rootModel.createTypedLiteral(dataArray[3])).addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/employment_linkeddata/", "school"), rootModel.createTypedLiteral(dataArray[4]));
                emps.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/employment_linkeddata/", "has"), emp);
            }

            CSVFile.close();
            rootModel.setNsPrefix("emp", "http://luci.uwl.ac.uk/employment_linkeddata/");
        }
        catch(IOException ex)
        {
            Logger.getLogger(EmpRDFGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rootModel;
    }

    public static void main(String args[])
    {
        FileOutputStream fout = null;
        
        try
        {
            EmpRDFGenerator rdfGenerator = new EmpRDFGenerator();
            File file = new File("/Users/hyeokim/emp.rdf");
            fout = new FileOutputStream(file);
            rdfGenerator.extractData().write(fout);
            RepositoryManager.add((new StringBuilder()).append(rdfGenerator.filepath).append("emp.rdf").toString(), "http://luci.uwl.ac.uk/employment_linkeddata/recent");
        
            fout.close();
        }
        catch(Exception ex)
        {
            Logger.getLogger(EmpRDFGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try{
            fout.close();
            } catch(Exception ex)
            {
                Logger.getLogger(EmpRDFGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    private BufferedReader CSVFile;
}
