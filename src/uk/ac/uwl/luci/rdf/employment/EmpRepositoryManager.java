// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 27/10/2012 15:36:55
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   EmpRepositoryManager.java

package uk.ac.uwl.luci.rdf.employment;

import info.aduna.iteration.Iterations;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openrdf.OpenRDFException;
import org.openrdf.model.*;
import org.openrdf.repository.*;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.sail.memory.MemoryStore;

public class EmpRepositoryManager
{

    public EmpRepositoryManager()
    {
    }

    public static void add(String filePath, String superContextStr)
    {
        Repository luciRepository = null;
        RepositoryConnection con = null;
        
        try{
            luciRepository = new HTTPRepository("http://luci.uwl.ac.uk/openrdf-sesame", "employment");
            luciRepository.initialize();
            con = luciRepository.getConnection();
            File file = new File(filePath);
            ValueFactory valFactory = luciRepository.getValueFactory();
            URI superContext = valFactory.createURI(superContextStr);
            con.add(file, "http://luci.uwl.ac.uk/course_linkeddata/", RDFFormat.RDFXML, new Resource[] {
                superContext
            });
            con.close();
        } catch (Exception ex){
            Logger.getLogger(EmpRDFGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try{
            con.close();
            } catch(Exception ex)
            {
                Logger.getLogger(EmpRDFGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
    }

    public static void remove(String filePath, String superContextStr)
    {
        RepositoryConnection con = null;
        ValueFactory valFactory = null;
        RepositoryConnection tempRepCon = null;
        URI tempContext = null;
        
        try{
        
            Repository luciRepository = new HTTPRepository("http://luci.uwl.ac.uk/openrdf-sesame", "employment");
            luciRepository.initialize();
            con = luciRepository.getConnection();
            valFactory = luciRepository.getValueFactory();
            Repository tempRep = new SailRepository(new MemoryStore());
            tempRep.initialize();
            tempRepCon = tempRep.getConnection();
            tempContext = valFactory.createURI((new StringBuilder()).append(superContextStr).append(filePath.substring(filePath.lastIndexOf("/"))).toString());
            URI superContext = valFactory.createURI(superContextStr);
            tempRepCon.add(new File(filePath), null, RDFFormat.RDFXML, new Resource[] {
                tempContext
            });
            org.openrdf.repository.RepositoryResult stmtsToRemove = tempRepCon.getStatements(null, null, null, true, new Resource[] {
                tempContext
            });
            List stmtsToRemoveList = (List)Iterations.addAll(stmtsToRemove, new ArrayList());
            con.remove(stmtsToRemoveList, new Resource[] {
                superContext
            });
            tempRepCon.clear(new Resource[] {
                tempContext
            });
            con.close();
            tempRepCon.close();
        } catch (Exception ex){
            Logger.getLogger(EmpRDFGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try{
            con.close();
            tempRepCon.close();
            } catch(Exception ex)
            {
                Logger.getLogger(EmpRDFGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    public static void update(String filePath, String superContextStr)
    {
        remove(filePath, superContextStr);
        add(filePath, superContextStr);
    }

    private static final String REPOSITORY_URI = "http://luci.uwl.ac.uk/openrdf-sesame";
    private static final String REPOSITORY_ID = "employment";
}
