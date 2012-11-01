/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.uwl.luci.hb;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.uwl.luci.mapping.XCRIGenerator;

/**
 *
 * @author hyeokim
 */
public class CourseController {
    private List<XcriCourse> coList = null;
    private List<XcriCourseinstance> ciList = null;
    private XcriCourse currentCO = null;
    XcriCourseinstance currentCI = null;
    
    private CourseHelper helper = null;
    private int currentIndex = 0;
    private int currentCiIndex = 0;
    
    public CourseController(){
        helper = new CourseHelper();
    }
    


    public void getCourses() {
        coList = helper.getCourse();
        currentIndex = 0;
    }
    
    public int getSize() {
        if(coList == null)
            return 0;
        return coList.size();
    }

    void recreateList() {
        coList = null;
    }
    
    public XcriCourse getNextCourse() {
        if (currentIndex < getSize()){
            currentCO = coList.get(currentIndex++);
            return currentCO;
        }
        return null;
    }
    
    public String getSubject(int sId){
        List<Subjects> subjects = helper.getSubject(sId);
        Subjects subject = null;
        if(subjects == null || subjects.size()==0)
            return null;
       
        subject = (Subjects)(subjects.get(0));
        return subject.getSubject();
    }
    
    public Address getAddress(int aId){
        List<Address> addresses = helper.getAddress(aId);
        Address address = null;
        if(addresses.size() == 0)
            return null;
        
        address = (Address)(helper.getAddress(aId).get(0));
        return address;
    }
    
    public Contacts getContact(int cId){
        List<Contacts> contacts = helper.getContacts(cId);
        Contacts contact = null;
        if(contact == null)
            return null;
        
        contact = (Contacts)(helper.getSubject(cId).get(0));
        return contact;
    }
    
    public void getCourseInstances(){
        ciList = helper.getCourseInstance(currentCO.getCourseId());
        currentCiIndex =0;
    }
    
    public int getInstanceSize(){
        if(ciList == null)
                return 0;
        return ciList.size();
    }
    
    public XcriCourseinstance getNextInstance(){
        if(currentCiIndex <ciList.size()){
            currentCI = ciList.get(currentCiIndex++);
            return currentCI;
        }
        return null;
    }

    public void closeSession()
    {
        helper.connection = helper.session.disconnect();
    }

    public void openSession()
    {
        try
        {
            if(helper.connection != null)
                helper.session.reconnect(helper.connection);
            else
                Logger.getLogger(XCRIGenerator.class.getName()).log(Level.INFO, "helper.connection is null.");
        }
        catch(Exception e)
        {
            Logger.getLogger(XCRIGenerator.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
