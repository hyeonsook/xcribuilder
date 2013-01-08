/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.uwl.luci.hb;

import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import uk.ac.uwl.luci.mapping.XCRIGenerator;


/**
 *
 * @author hyeokim
 */
public class CourseHelper {
    Session session = null;
    Connection connection = null;
    
    public CourseHelper() {
        this.session = HibernateUtil.getSessionFactory().getCurrentSession();
    }
    
    public List getCourse() {

        List<XcriCourse> coList = null;
        try {
         
            org.hibernate.Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from XcriCourse ");
            //Query q = session.createQuery("from XcriCourse as co where co.clusterTitle like 'Acting%'");
            
            coList = (List<XcriCourse>) q.list();
            Logger.getLogger(XCRIGenerator.class.getName()).log(Level.INFO, (new StringBuilder()).append("***** Total Courses from DB: ").append(coList.size()).toString());
        
        } catch (Exception e) {
            Logger.getLogger(XCRIGenerator.class.getName()).log(Level.SEVERE, null, e);
        }
        return coList;
    }
    
    public List getDummyCourse() {

        List<XcriCourse> coList = null;
        try {
         
            org.hibernate.Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from XcriCourse as co where co.clusterTitle like 'A%'");
            
            coList = (List<XcriCourse>) q.list();
            Logger.getLogger(XCRIGenerator.class.getName()).log(Level.INFO, (new StringBuilder()).append("***** Total Courses from DB: ").append(coList.size()).toString());
        
        } catch (Exception e) {
            Logger.getLogger(XCRIGenerator.class.getName()).log(Level.SEVERE, null, e);
        }
        return coList;
    }
    
    public List getCourseInstance(int coId){
        List<XcriCourseinstance> ciList = null;
        try {
            org.hibernate.Transaction tx = session.beginTransaction();
            //String sqlQuery = "from Courseinstances as ci where ci.courseId =59" // + coId + ")");
            String sqlQuery = "from XcriCourseinstance as ci where ci.courseId = ?"; 
            Query q = session.createQuery(sqlQuery); 
            q.setParameter(0,coId); 

            ciList = (List<XcriCourseinstance>) q.list();

        } catch (Exception e) {
            Logger.getLogger(CourseHelper.class.getName()).log(Level.SEVERE, null, e);
        }

        return ciList;
    }
    
    public List getSubject(int subjId){
        List<Subjects> sList = null;       
        try {
            org.hibernate.Transaction tx = session.beginTransaction();
            String sqlQuery = "from Subjects as sbj where sbj.subjectId = ?"; 
            Query q = session.createQuery(sqlQuery); 
            q.setParameter(0,subjId); 
            sList = (List<Subjects>) q.list();

        } catch (Exception e) {
            Logger.getLogger(CourseHelper.class.getName()).log(Level.SEVERE, null, e);
        }

        return sList;
        
    }
    
    public List getAddress(int addrId){
        List<Address> aList = null;
        
        try {
            org.hibernate.Transaction tx = session.beginTransaction();
            String sqlQuery = "from Address as addr where addr.addressId = ?"; 
            Query q = session.createQuery(sqlQuery); 
            q.setParameter(0,addrId); 

            aList = (List<Address>) q.list();
        } catch (Exception e) {
            Logger.getLogger(CourseHelper.class.getName()).log(Level.SEVERE, null, e);
        }

        return aList;
        
    }
    
    public List getContacts(int contId){
        List<Address> cList = null;
        
        try {
            org.hibernate.Transaction tx = session.beginTransaction();
            String sqlQuery = "from Contacts as cont where cont.contactId = ?"; 
            Query q = session.createQuery(sqlQuery); 
            q.setParameter(0,contId); 

            cList = (List<Address>) q.list();

        } catch (Exception e) {
            Logger.getLogger(CourseHelper.class.getName()).log(Level.SEVERE, null, e);
        }

        return cList;
        
    }

    public List getEnquiries(int enquiriesId){
        List<Enquiries> eList = null;
        
        try {
            org.hibernate.Transaction tx = session.beginTransaction();
            String sqlQuery = "from Enquiries as enq where enq.enquiriesID = ?"; 
            Query q = session.createQuery(sqlQuery); 
            q.setParameter(0,enquiriesId); 

            eList = (List<Enquiries>) q.list();

        } catch (Exception e) {
            Logger.getLogger(CourseHelper.class.getName()).log(Level.SEVERE, null, e);
        }

        return eList;
        
    }
}
