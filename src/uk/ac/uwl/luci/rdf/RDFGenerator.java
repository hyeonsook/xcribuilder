// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 27/10/2012 15:36:18
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RDFGenerator.java

package uk.ac.uwl.luci.rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public abstract class RDFGenerator
{

    public RDFGenerator()
    {
        filepath = "/Users/hyeokim/";
        rootModel = ModelFactory.createDefaultModel();
    }

    public String getURI(String prefix, String resourceId)
    {
        return (new StringBuilder()).append(prefix).append(resourceId).toString();
    }

    public abstract Model extractData();

    public String filepath;
    public Model rootModel;
}
