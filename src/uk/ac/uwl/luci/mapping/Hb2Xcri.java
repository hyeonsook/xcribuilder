/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.uwl.luci.mapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.purl.dc.elements.x11.DescriptionDType;
import org.purl.dc.elements.x11.SimpleLiteral;
import org.purl.net.mlo.*;
import org.xcri.profiles.x12.catalog.*;
import org.xcri.profiles.x12.catalog.CatalogDocument.Catalog;
import org.xcri.profiles.x12.catalog.TemporalDType;
import org.xcri.profiles.x12.catalog.VocabularyDType;
import uk.ac.uwl.luci.hb.*;

/**
 *
 * @author hyeokim
 */
public class Hb2Xcri {
    private CourseController controller = null;
    private CatalogDocument catalog = null;
    private XcriCourse course = null;
    private Properties prop = null;
    private int logPGCount = 0;
    private int logUGCount = 0;
    private int logDistanceLearningCount = 0;
    private int logCourseCount = 0;
    private int logCourseInstanceCount = 0;
    private int logCourseInstanceCount2 = 0;
    
    public Hb2Xcri(){
        controller = new CourseController();
        readConfiguration();
    }
    
    public CourseController getController(){
        return controller;
    }
    
    private void readConfiguration(){
        try
        {
            Logger.getLogger(XCRIGenerator.class.getName()).log(Level.INFO, "Reading properties..");
            prop = new Properties();
            prop.load(getClass().getResourceAsStream("uwl_xcri.properties"));
        }
        catch(IOException ex)
        {
            Logger.getLogger(XCRIGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public CatalogDocument buildCatalog() throws ParseException{
        //1. Create catalog element
        catalog = createCatalog();
        Logger.getLogger(XCRIGenerator.class.getName()).log(Level.INFO, "Catalog element has been created.");
        
        //2. Create provider element
        ProviderDType provider = catalog.getCatalog().addNewProvider();
        Logger.getLogger(XCRIGenerator.class.getName()).log(Level.INFO, "Provider element has been created.");
        controller.openSession();
        mappingProvider(provider);
        Logger.getLogger(XCRIGenerator.class.getName()).log(Level.INFO, "Course and the other elements have been created.");
        controller.closeSession();

        return catalog;
    }
    
    private void mappingProvider(ProviderDType arg1) throws ParseException{
        ProviderDType provider = arg1;
        
        //Provider.Description
        org.purl.dc.elements.x11.DescriptionDType description = provider.addNewDescription();
        description.setHref(prop.getProperty("provider.url"));
       
        
        
        //Provider.Identifier
        SimpleLiteral id = provider.addNewIdentifier();
        XmlString idValue = XmlString.Factory.newInstance();
        idValue.setStringValue(prop.getProperty("provider.url"));
        id.set(idValue);
        
        //Provider.Title
        SimpleLiteral title = provider.addNewTitle();
        XmlString titleValue = XmlString.Factory.newInstance();
        titleValue.setStringValue(prop.getProperty("provider.title"));
        title.set(titleValue);
        
        //Provider.Url
        SimpleLiteral url = provider.addNewUrl();
        XmlString urlValue = XmlString.Factory.newInstance();
        urlValue.setStringValue(prop.getProperty("provider.url"));
        url.set(urlValue);
        
        //Provider.Location 
        LocationDType location = provider.addNewLocation();
        location.addAddress(prop.getProperty("location1.address"));
        location.setEmail(prop.getProperty("location1.email"));
        location.setFax(prop.getProperty("location1.fax"));
        location.setPhone(prop.getProperty("location1.phone"));
        location.setPostcode(prop.getProperty("location1.postcode"));
        location.setTown(prop.getProperty("location1.town"));
        location.setUrl(prop.getProperty("location1.url"));
        /*
        location = provider.addNewLocation();
        location.addAddress(prop.getProperty("location2.address"));
        location.setEmail(prop.getProperty("location2.email"));
        location.setFax(prop.getProperty("location2.fax"));
        location.setPhone(prop.getProperty("location2.phone"));
        location.setPostcode(prop.getProperty("location2.postcode"));
        location.setTown(prop.getProperty("location2.town"));
        location.setUrl(prop.getProperty("location2.url"));
        
        location = provider.addNewLocation();
        location.addAddress(prop.getProperty("location3.address"));
        location.setEmail(prop.getProperty("location3.email"));
        location.setFax(prop.getProperty("location3.fax"));
        location.setPhone(prop.getProperty("location3.phone"));
        location.setPostcode(prop.getProperty("location3.postcode"));
        location.setTown(prop.getProperty("location3.town"));
        location.setUrl(prop.getProperty("location3.url"));
        */
        
        //Provider.Course
        CourseDType xcriCourse = null;
        XcriCourse hbCourse = null; 
        
        controller.getCourses();
        if(controller.getSize() == 0)
            Logger.getLogger(XCRIGenerator.class.getName()).log(Level.WARNING, "Populated course data from CID is zero.");
        
        //create course nodes
        for(int index=0; index < controller.getSize(); index++){
            hbCourse = controller.getNextCourse();
            createCourse(provider, hbCourse);
        }
        
    }
    
    public CatalogDocument createCatalog()
    {
        CatalogDocument newCatalogDoc = org.xcri.profiles.x12.catalog.CatalogDocument.Factory.newInstance();
        org.xcri.profiles.x12.catalog.CatalogDocument.Catalog newCatalog = org.xcri.profiles.x12.catalog.CatalogDocument.Catalog.Factory.newInstance();
        newCatalog.setGenerated(Calendar.getInstance());
        
        DescriptionDType description = newCatalog.addNewDescription();
        XmlString descValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
        descValue.setStringValue(prop.getProperty("catalog.description"));
        description.set(descValue);
        XmlCursor cursor = newCatalog.newCursor();
        cursor.toFirstContentToken();
        cursor.toLastAttribute();
        cursor.insertAttributeWithValue("schemaLocation", "http://www.w3.org/2001/XMLSchema-instance", "http://xcri.org/profiles/1.2/catalog http://www.xcri.co.uk/bindings/xcri_cap_1_2.xsd http://xcri.org/profiles/1.2/catalog/terms http://www.xcri.co.uk/bindings/xcri_cap_terms_1_2.xsd http://xcri.co.uk http://www.xcri.co.uk/bindings/courseDataProgramme.xsd");
        cursor.insertNamespace("xcriterms", "http://xcri.org/profiles/1.2/catalog/terms");
        cursor.insertNamespace("courseDataProgramme", "http://xcri.co.uk");
        cursor.insertNamespace("xhtml", "http://www.w3.org/1999/xhtml");
        
        newCatalogDoc.setCatalog(newCatalog);
        return newCatalogDoc;
    }

    public void createCourse(ProviderDType arg1, XcriCourse arg2)
        throws ParseException
    {
        controller.getCourseInstances();
        if(controller.getInstanceSize() == 0)
            return;
        logCourseCount++;
        logCourseInstanceCount2 += controller.getInstanceSize();
        CourseDType xcriCourse = arg1.addNewCourse();
        XcriCourse hbCourse = arg2;
        
        if(isValidRecord(hbCourse.getIntroduction()))
        {
            DescriptionDType description = xcriCourse.addNewDescription();
            XmlString descValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
            descValue.setStringValue(DataCleaner.getFirst4000Chars(hbCourse.getIntroduction()));
            description.set(descValue);
            
        }
 //--> changes with existing fields and new ones based on request from Lucy, 27 Nov 2012
        
        Boolean hasValidation = false;
        if(isValidRecord(hbCourse.getValidation()))
        {
            DescriptionDType pdescription = xcriCourse.addNewDescription();
            XmlCursor pdescCursor = pdescription.newCursor();
            pdescCursor.toFirstContentToken();
            pdescCursor.insertAttributeWithValue(new QName("http://www.w3.org/2001/XMLSchema-instance", "type"), "xcriterms:policy");
            pdescCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
            pdescCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
            pdescCursor.insertAttributeWithValue("class", "validation");
            pdescCursor.insertChars(DataCleaner.getFirst4000Chars(hbCourse.getValidation()));
            hasValidation = true;
        }
  /*      
        if(isValidRecord(hbCourse.getWebPageURL())){
            DescriptionDType pdescription = xcriCourse.addNewDescription();
            XmlCursor pdescCursor = pdescription.newCursor();
            if(!hasValidation){
                pdescCursor.toFirstContentToken();
                pdescCursor.insertAttributeWithValue(new QName("http://www.w3.org/2001/XMLSchema-instance", "type"), "xcriterms:policy");
                pdescCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
            }
            else {
                pdescCursor.toParent();
            }
            
            pdescCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
            pdescCursor.insertAttributeWithValue("class", "webPageURL");
            pdescCursor.insertChars(hbCourse.getWebPageURL());

             if(isValidRecord(hbCourse.getWebPageDesc())){
                pdescCursor.toParent();
                pdescCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
                pdescCursor.insertAttributeWithValue("class", "webPageDescription");
                pdescCursor.insertChars(hbCourse.getWebPageDesc());
             }

             pdescCursor.dispose();
        }
       */     

 // <-- changes
        
        if(isValidRecord(hbCourse.getNotableAspects()))
        {
            DescriptionDType description = xcriCourse.addNewDescription();
            XmlString descValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
            descValue.setStringValue(DataCleaner.getFirst4000Chars(hbCourse.getNotableAspects()));
            description.set(descValue);
            XmlCursor descCursor = description.newCursor();
            descCursor.toFirstContentToken();
            descCursor.insertAttributeWithValue(new QName("http://www.w3.org/2001/XMLSchema-instance", "type"), "xcriterms:specialFeature");
        }
        if(isValidRecord(hbCourse.getSpecialResources()))
        {
            DescriptionDType description = xcriCourse.addNewDescription();
            XmlString descValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
            descValue.setStringValue(DataCleaner.getFirst4000Chars(hbCourse.getSpecialResources()));
            description.set(descValue);
            XmlCursor descCursor = description.newCursor();
            descCursor.toFirstContentToken();
            descCursor.insertAttributeWithValue(new QName("http://www.w3.org/2001/XMLSchema-instance", "type"), "xcriterms:providedResource");
        }
        if(isValidRecord(hbCourse.getTeachingMethods()))
        {
            DescriptionDType description = xcriCourse.addNewDescription();
            XmlString descValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
            descValue.setStringValue(DataCleaner.getFirst4000Chars(hbCourse.getTeachingMethods()));
            description.set(descValue);
            XmlCursor descCursor = description.newCursor();
            descCursor.toFirstContentToken();
            descCursor.insertAttributeWithValue(new QName("http://www.w3.org/2001/XMLSchema-instance", "type"), "xcriterms:teachingStrategy");
        }
        if(isValidRecord(hbCourse.getLearningMaterials()))
        {
            DescriptionDType description = xcriCourse.addNewDescription();
            XmlString descValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
            descValue.setStringValue(DataCleaner.getFirst4000Chars(hbCourse.getLearningMaterials()));
            description.set(descValue);
            XmlCursor descCursor = description.newCursor();
            descCursor.toFirstContentToken();
            descCursor.insertAttributeWithValue(new QName("http://www.w3.org/2001/XMLSchema-instance", "type"), "xcriterms:indicativeResource");
        }
        if(isValidRecord(hbCourse.getCareerProgression()))
        {
            DescriptionDType description = xcriCourse.addNewDescription();
            XmlString descValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
            descValue.setStringValue(DataCleaner.getFirst4000Chars(hbCourse.getCareerProgression()));
            description.set(descValue);
            XmlCursor descCursor = description.newCursor();
            descCursor.toFirstContentToken();
            descCursor.insertAttributeWithValue(new QName("http://www.w3.org/2001/XMLSchema-instance", "type"), "xcriterms:careerOutcome");
        }
        if(isValidRecord(hbCourse.getStudyProgression()))
        {
            DescriptionDType description = xcriCourse.addNewDescription();
            XmlString descValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
            descValue.setStringValue(DataCleaner.getFirst4000Chars(hbCourse.getStudyProgression()));
            description.set(descValue);
            XmlCursor descCursor = description.newCursor();
            descCursor.toFirstContentToken();
            descCursor.insertAttributeWithValue(new QName("http://www.w3.org/2001/XMLSchema-instance", "type"), "xcriterms:leadsTo");
        }
        if(isValidRecord(hbCourse.getKeywords()))
        {
            DescriptionDType description = xcriCourse.addNewDescription();
            XmlString descValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
            descValue.setStringValue(hbCourse.getKeywords());
            description.set(descValue);
            XmlCursor descCursor = description.newCursor();
            descCursor.toFirstContentToken();
            descCursor.insertAttributeWithValue(new QName("http://www.w3.org/2001/XMLSchema-instance", "type"), "xcriterms:topic");
        }
        
        
        
        //Course.Abstract
        org.xcri.profiles.x12.catalog.DescriptionDType abst = xcriCourse.addNewAbstract();
        XmlString abstValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
        String strAbst = hbCourse.getIntroduction();
        if(strAbst != null && strAbst.length() > 140)
        {
            strAbst = strAbst.substring(0, 140);
            int index = strAbst.lastIndexOf(".");
            if(index != -1)
                strAbst = strAbst.substring(0, index + 1);
        }
        if(strAbst == null || strAbst.trim().equals(""))
            strAbst = prop.getProperty("novalue.string");
        abstValue.setStringValue(strAbst);
        abst.set(abstValue);
        SimpleLiteral id = xcriCourse.addNewIdentifier();
        XmlString idValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
        idValue.setStringValue((new StringBuilder()).append(prop.getProperty("course.uri.prefix")).append(hbCourse.getCourseId()).toString());
        id.set(idValue);
        if(hbCourse.getSubjectId() != null)
        {
            String subjectString = controller.getSubject(hbCourse.getSubjectId().intValue());
            if(subjectString != null && !subjectString.equals(""))
            {
                org.purl.dc.elements.x11.SubjectDType subject = xcriCourse.addNewSubject();
                XmlString subjValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
                subjValue.setStringValue(DataCleaner.cleanSubject(subjectString));
                subject.set(subjValue);
            }
        }
        if(hbCourse.getSubject2id() != null)
        {
            String subjectString = controller.getSubject(hbCourse.getSubject2id().intValue());
            if(subjectString != null && !subjectString.equals(""))
            {
                org.purl.dc.elements.x11.SubjectDType subject = xcriCourse.addNewSubject();
                XmlString subjValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
                subjValue.setStringValue(DataCleaner.cleanSubject(subjectString));
                subject.set(subjValue);
            }
        }
        if(hbCourse.getSubject3id() != null)
        {
            String subjectString = controller.getSubject(hbCourse.getSubject3id().intValue());
            if(subjectString != null && !subjectString.equals(""))
            {
                org.purl.dc.elements.x11.SubjectDType subject = xcriCourse.addNewSubject();
                XmlString subjValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
                subjValue.setStringValue(DataCleaner.cleanSubject(subjectString));
                subject.set(subjValue);
            }
        }
        if(xcriCourse.getSubjectArray().length == 0)
        {
            org.purl.dc.elements.x11.SubjectDType subject = xcriCourse.addNewSubject();
            XmlString subjValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
            subjValue.setStringValue(prop.getProperty("novalue.string"));
            subject.set(subjValue);
        }
        SimpleLiteral title = xcriCourse.addNewTitle();
        XmlString titleValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
        titleValue.setStringValue(hbCourse.getClusterTitle());
        title.set(titleValue);
        
        org.xcri.profiles.x12.catalog.DescriptionDType application = xcriCourse.addNewApplicationProcedure();
        XmlString applicationValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
        applicationValue.setStringValue(prop.getProperty("course.application.url"));
        application.set(applicationValue);
        
        if(isValidRecord(hbCourse.getExitSkills()))
        {
            org.xcri.profiles.x12.catalog.DescriptionDType lo = xcriCourse.addNewLearningOutcome();
            XmlString loValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
            loValue.setStringValue(DataCleaner.getFirst4000Chars(hbCourse.getExitSkills()));
            lo.set(loValue);
        }
        QualificationDType qualification = xcriCourse.addNewQualification();
        mappingQualification(qualification, hbCourse);
        PresentationDType xcriInstance = null;
        XcriCourseinstance hbInstance = null;
        List contactList = new ArrayList();
        
        for(int index = 0; index < controller.getInstanceSize(); index++)
        {
            hbInstance = controller.getNextInstance();
            xcriInstance = xcriCourse.addNewPresentation();
            createCourseInstance(xcriInstance, hbInstance, hbCourse);
        }

        if(xcriInstance != null)
            xcriCourse.setUrl(xcriInstance.getUrl());

    }

    private void mappingQualification(QualificationDType qualification, XcriCourse hbCourse)
    {
        SimpleLiteral qid = qualification.addNewIdentifier();
        XmlString qualValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
        qualValue.setStringValue((new StringBuilder()).append(prop.getProperty("qualification.uri.prefix")).append(hbCourse.getCourseId()).append(hbCourse.getAwardTypeId()).toString());
        qid.set(qualValue);
        SimpleLiteral title = qualification.addNewTitle();
        XmlString titleValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
        if(isValidRecord(hbCourse.getAdesc()))
            titleValue.setStringValue(hbCourse.getAdesc());
        else
            titleValue.setStringValue(prop.getProperty("novalue.string"));
        title.set(titleValue);
        if(isValidRecord(hbCourse.getAwardType()))
            qualification.setAbbr(hbCourse.getAwardType());
        if(isValidRecord(hbCourse.getStudyLevel()))
        {
            SimpleLiteral qlevel = qualification.addNewEducationLevel();
            XmlString qlevelValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
            qlevelValue.setStringValue(getEducationLevel(hbCourse.getStudyLevel()));
            qlevel.set(qlevelValue);
        }
        if(isValidRecord(hbCourse.getAbodyDesc()))
            qualification.setAwardedBy(hbCourse.getAbodyDesc());
        else
            qualification.setAwardedBy(prop.getProperty("novalue.string"));
        if(isValidRecord(hbCourse.getProfAccredit()))
            qualification.setAccreditedBy(hbCourse.getProfAccredit());
        else
            qualification.setAccreditedBy(prop.getProperty("novalue.string"));
    }

    public void createCourseInstance(PresentationDType target, XcriCourseinstance source, XcriCourse source2)
        throws ParseException
    {
        logCourseInstanceCount++;
        PresentationDType xcriInstance = target;
        XcriCourseinstance hbInstance = source;
        XcriCourse hbCourse = source2;
        TemporalDType applyFrom = xcriInstance.addNewApplyFrom();
        XmlCursor afCursor = applyFrom.newCursor();
        afCursor.toFirstContentToken();
        afCursor.insertAttributeWithValue("dtf", (new SimpleDateFormat("yyyy-MM-dd")).format(Calendar.getInstance().getTime()));
        afCursor.insertChars((new SimpleDateFormat("yyyy-MM-dd")).format(Calendar.getInstance().getTime()));
        
        //Presentation.ApplicationUrl
        if(isValidRecord(hbInstance.getApplicationUrl()))
        {
            String url = hbInstance.getApplicationUrl();
            if(url.startsWith("/"))
                url = (new StringBuilder()).append("http://uwl.ac.uk").append(url).toString();
            xcriInstance.setApplyTo(url);
            
            org.xcri.profiles.x12.catalog.DescriptionDType application = xcriInstance.addNewApplicationProcedure();
            XmlString applicationValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
            applicationValue.setStringValue(prop.getProperty("course.application.url"));
            application.set(applicationValue);
            
        } else
        {
            String educationLevel = hbCourse.getStudyLevel();
            String startYear = null;
            String applicationText = prop.getProperty("course.application.url");
            if(isValidRecord(educationLevel)){
                educationLevel = getEducationLevel(educationLevel);
                
                if(educationLevel.equals("Undergraduate")){
                    
                    if(isValidRecord(hbInstance.getStartDate().toString())){
                        startYear = (new SimpleDateFormat("yyyy")).format(hbInstance.getStartDate());
                        if(startYear.equals("2013") && isValidRecord(hbInstance.getUcascode())){
                            xcriInstance.setApplyTo(prop.getProperty("courseinstance.application.ucas.url"));
                            applicationText = prop.getProperty("courseinstance.application.procedure");
                        }
                        else {
                            xcriInstance.setApplyTo(prop.getProperty("courseinstance.application.ug.url"));
                        }
                    }else{
                        xcriInstance.setApplyTo(prop.getProperty("courseinstance.application.ug.url"));
                    }
                }else if(educationLevel.equals("Postgraduate")){
                    xcriInstance.setApplyTo(prop.getProperty("courseinstance.application.pg.url"));
                }else{
                    xcriInstance.setApplyTo(prop.getProperty("courseinstance.application.fe.url"));
                }
                
            }else {
                xcriInstance.setApplyTo(prop.getProperty("courseinstance.application.fe.url"));
            }
            
            
            org.xcri.profiles.x12.catalog.DescriptionDType application = xcriInstance.addNewApplicationProcedure();
            XmlString applicationValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
            applicationValue.setStringValue(applicationText);
            application.set(applicationValue);
        }
        
        
        if(hbInstance.getStartDate() != null && !hbInstance.getStartDate().toString().trim().equals(""))
        {
            TemporalDType applyUntil = xcriInstance.addNewApplyUntil();
            XmlCursor auCursor = applyUntil.newCursor();
            auCursor.toFirstContentToken();
            auCursor.insertAttributeWithValue("dtf", (new SimpleDateFormat("yyyy-MM-dd")).format(hbInstance.getStartDate()));
            auCursor.insertChars((new SimpleDateFormat("yyyy-MM-dd")).format(hbInstance.getStartDate()));
            auCursor.dispose();
        }
        if(isValidRecord(hbInstance.getExams()) || isValidRecord(hbInstance.getCourseWork()))
        {
            AssessmentDType assessment = xcriInstance.addNewAssessment();
            XmlCursor assCursor = assessment.newCursor();
            assCursor.toFirstContentToken();
            assCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
            boolean hasParent = false;
            if(isValidRecord(hbInstance.getExams()))
            {
                assCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
                assCursor.insertAttributeWithValue("class", "exams");
                assCursor.insertChars(hbInstance.getExams());
                hasParent = true;
            }
            if(isValidRecord(hbInstance.getCourseWork()))
            {
                if(hasParent)
                    assCursor.toParent();
                assCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
                assCursor.insertAttributeWithValue("class", "courseWork");
                assCursor.insertChars(hbInstance.getCourseWork());
            }
            assCursor.dispose();
        }
        if(isValidRecord(hbInstance.getStudyModeDesc()))
        {
            VocabularyDType attendanceMode = xcriInstance.addNewAttendanceMode();
            XmlCursor amCursor = attendanceMode.newCursor();
            amCursor.toFirstContentToken();
            amCursor.insertAttributeWithValue("identifier", getAttendanceModeId(hbInstance.getStudyModeDesc()));
            amCursor.insertChars(getAttendanceModeDesc(hbInstance.getStudyModeDesc()));
            
            VocabularyDType attendancePattern = xcriInstance.addNewAttendancePattern();
            XmlCursor apCursor = attendancePattern.newCursor();
            apCursor.toFirstContentToken();
            apCursor.insertAttributeWithValue("identifier", getAttendancePatternId(hbInstance.getStudyModeDesc()));
            apCursor.insertChars(getAttendancePatternDesc(hbInstance.getStudyModeDesc()));
            
            VocabularyDType studyMode = xcriInstance.addNewStudyMode();
            XmlCursor smCursor = studyMode.newCursor();
            smCursor.toFirstContentToken();
            smCursor.insertAttributeWithValue("identifier", getStudyModeId(hbInstance.getStudyModeDesc()));
            smCursor.insertChars(getStudyModeDescription(hbInstance.getStudyModeDesc()));
            
            
        }
        String costString = getCostDescription(hbInstance);
        if(!costString.trim().equals(""))
        {
            SimpleLiteral cost = xcriInstance.addNewCost();
            XmlString costValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
            costValue.setStringValue(costString);
            cost.set(costValue);
        }
        if(isValidRecord(hbInstance.getDurationWeeks()))
        {
            String durationString = getDurationIntervalCode(hbInstance.getDurationWeeks());
            DurationDType duration = xcriInstance.addNewDuration();
            XmlCursor drCursor = duration.newCursor();
            drCursor.toFirstContentToken();
            if(!durationString.equals("P"))
                drCursor.insertAttributeWithValue("interval", durationString);
            drCursor.insertChars(getDurationDescription(hbInstance.getDurationWeeks()));
        }
        SimpleLiteral id = xcriInstance.addNewIdentifier();
        XmlString idValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
        idValue.setStringValue((new StringBuilder()).append(prop.getProperty("presentation.url.prefix")).append(hbInstance.getCourseInstanceId()).toString());
        id.set(idValue);
        
        
        if(isValidRecord(hbInstance.getUcascode()))
        {
            id = xcriInstance.addNewIdentifier();
            idValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
            idValue.setStringValue(DataCleaner.cleanUcasCode(hbInstance.getUcascode()));
            id.set(idValue);
            XmlCursor idCursor = id.newCursor();
            idCursor.toFirstContentToken();
            idCursor.insertAttributeWithValue(new QName("http://www.w3.org/2001/XMLSchema-instance", "type"), "courseDataProgramme:ucasCode");
        }
        if(isValidRecord(hbInstance.getUcascodeB()))
        {
            id = xcriInstance.addNewIdentifier();
            idValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
            idValue.setStringValue(hbInstance.getUcascodeB());
            id.set(idValue);
            XmlCursor idCursor = id.newCursor();
            idCursor.toFirstContentToken();
            idCursor.insertAttributeWithValue(new QName("http://www.w3.org/2001/XMLSchema-instance", "type"), "courseDataProgramme:internalID");
        }
        
        //--> changes with existing fields and new ones based on request from Lucy, 27 Nov 2012
        /*
        if(isValidRecord(hbInstance.getEntryCriteria()) || isValidRecord(hbInstance.getWorkExperience()) || isValidRecord(hbInstance.getInterview()) || isValidRecord(hbInstance.getEntrySkills()))
        {
            PrerequisiteDType prerequisite = xcriInstance.addNewPrerequisite();
            XmlCursor preqCursor = prerequisite.newCursor();
            preqCursor.toFirstContentToken();
            preqCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
            boolean hasParent = false;
            if(isValidRecord(hbInstance.getEntryCriteria()))
            {
                preqCursor.toFirstContentToken();
                preqCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
                preqCursor.insertAttributeWithValue("class", "entryCriteria");
                preqCursor.insertChars(hbInstance.getEntryCriteria());
                hasParent = true;
            }
            if(isValidRecord(hbInstance.getWorkExperience()))
            {
                if(hasParent)
                    preqCursor.toParent();
                preqCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
                preqCursor.insertAttributeWithValue("class", "workExperience");
                preqCursor.insertChars(hbInstance.getWorkExperience());
                hasParent = true;
            }
            if(isValidRecord(hbInstance.getInterview()))
            {
                if(hasParent)
                    preqCursor.toParent();
                preqCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
                preqCursor.insertAttributeWithValue("class", "interview");
                preqCursor.insertChars(hbInstance.getInterview());
                hasParent = true;
            }
            if(isValidRecord(hbInstance.getEntrySkills()))
            {
                if(hasParent)
                    preqCursor.toParent();
                preqCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
                preqCursor.insertAttributeWithValue("class", "entrySkills");
                preqCursor.insertChars(hbInstance.getEntrySkills());
                hasParent = true;
            }
            preqCursor.dispose();
        }
        * */
        if(isValidRecord(hbInstance.getEntryCriteria()) || isValidRecord(hbCourse.getWorkExperience()) || isValidRecord(hbInstance.getInterview()) || isValidRecord(hbInstance.getEntrySkills()))
        {
            PrerequisiteDType prerequisite = xcriInstance.addNewPrerequisite();
            XmlCursor preqCursor = prerequisite.newCursor();
            preqCursor.toFirstContentToken();
            preqCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
            boolean hasParent = false;
            if(isValidRecord(hbInstance.getEntryCriteria()) || isValidRecord(hbInstance.getInternationalEntryCriteria()))
            {
                preqCursor.toFirstContentToken();
                preqCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
                preqCursor.insertAttributeWithValue("class", "entryCriteria");
                String tmpEntryCriteria = "";
                if(isValidRecord(hbInstance.getEntryCriteria())){
                    // 26 Feb 2013, Hyeonsook, Modification - Added string of <br/><br/> between two entrycriteria data.
                    tmpEntryCriteria = hbInstance.getEntryCriteria()+"<br/><br/>";
                }
                if(isValidRecord(hbInstance.getInternationalEntryCriteria())){
                    tmpEntryCriteria += hbInstance.getInternationalEntryCriteria();
                }
                preqCursor.insertChars(tmpEntryCriteria);
                hasParent = true;
            }
            if(isValidRecord(hbCourse.getWorkExperience()))
            {
                if(hasParent)
                    preqCursor.toParent();
                preqCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
                preqCursor.insertAttributeWithValue("class", "workExperience");
                preqCursor.insertChars(hbCourse.getWorkExperience());
                hasParent = true;
            }
            if(isValidRecord(hbInstance.getInterview()))
            {
                if(hasParent)
                    preqCursor.toParent();
                preqCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
                preqCursor.insertAttributeWithValue("class", "interview");
                preqCursor.insertChars(hbInstance.getInterview());
                hasParent = true;
            }
            if(isValidRecord(hbInstance.getEntrySkills()))
            {
                if(hasParent)
                    preqCursor.toParent();
                preqCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
                preqCursor.insertAttributeWithValue("class", "entrySkills");
                preqCursor.insertChars(hbInstance.getEntrySkills());
                hasParent = true;
            }
            preqCursor.dispose();
            
          
if(prerequisite.xmlText().length()>4000)
System.out.println("=================> Prerequisite more than 4000"+ prerequisite.xmlText());                
        }
        
        //-- changes
        
        
        StartDType start = xcriInstance.addNewStart();
        XmlCursor stCursor = start.newCursor();
        stCursor.toFirstContentToken();
        if(hbInstance.getStartDate() != null && !hbInstance.getStartDate().toString().trim().equals(""))
        {
            stCursor.insertAttributeWithValue("dtf", (new SimpleDateFormat("yyyy-MM-dd")).format(hbInstance.getStartDate()));
            stCursor.insertChars((new SimpleDateFormat("yyyy-MM-dd")).format(hbInstance.getStartDate()));
        } else
        {
            stCursor.insertAttributeWithValue("dtf", (new SimpleDateFormat("yyyy-MM-dd")).format(Calendar.getInstance().getTime()));
            stCursor.insertChars("Flexible start dates");
        }
        SimpleLiteral title = xcriInstance.addNewTitle();
        XmlString titleValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
        titleValue.setStringValue(hbInstance.getCourseTitle());
        title.set(titleValue);
        SimpleLiteral url = xcriInstance.addNewUrl();
        XmlString urlValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
        urlValue.setStringValue((new StringBuilder()).append(prop.getProperty("course.url.prefix")).append(hbInstance.getCourseInstanceId()).toString());
        url.set(urlValue);
        createVenue(xcriInstance, hbInstance, hbCourse);
        
        
        DescriptionDType description = null;
        XmlCursor descCursor = null;
        if(isValidRecord(hbInstance.getDurationNotes()))
        {
            description = xcriInstance.addNewDescription();
            descCursor = description.newCursor();
            descCursor.toFirstContentToken();
            descCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
            descCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
            descCursor.insertAttributeWithValue("class", "durationNotes");
            descCursor.insertChars(DataCleaner.getFirst4000Chars(hbInstance.getDurationNotes()));
        }
        boolean bDayTaught = false;
        if(isValidRecord(hbInstance.getDayTaught()))
        {
            bDayTaught = true;
            if(description == null)
            {
                description = xcriInstance.addNewDescription();
                descCursor = description.newCursor();
                descCursor.toFirstContentToken();
                descCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
            } else
            {
                descCursor.toNextToken();
            }
            descCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
            descCursor.insertAttributeWithValue("class", "teachingDetails");
            descCursor.beginElement("span", "http://www.w3.org/1999/xhtml");
            descCursor.insertAttributeWithValue("class", "dayTaught");
            descCursor.insertChars(DataCleaner.getFirst4000Chars(hbInstance.getDayTaught()));
        }
        if(isValidRecord(hbInstance.getTimeTaught()))
        {
            if(description == null)
            {
                description = xcriInstance.addNewDescription();
                descCursor = description.newCursor();
                descCursor.toFirstContentToken();
                descCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
                descCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
                descCursor.insertAttributeWithValue("class", "teachingDetails");
            } else
            if(!bDayTaught)
            {
                descCursor.toChild(1);
                descCursor.toNextToken();
                descCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
                descCursor.insertAttributeWithValue("class", "teachingDetails");
            } else
            {
                descCursor.toChild(2);
                descCursor.toNextToken();
            }
            descCursor.beginElement("span", "http://www.w3.org/1999/xhtml");
            descCursor.insertAttributeWithValue("class", "timeTaught");
            descCursor.insertChars(DataCleaner.getFirst4000Chars(hbInstance.getTimeTaught()));
        }
        //--> changes with existing fields and new ones based on request from Lucy, 27 Nov 2012
        if(isValidRecord(hbInstance.getWorkExperience())){
            if(description == null)
            {
                description = xcriInstance.addNewDescription();
                descCursor = description.newCursor();
                descCursor.toFirstContentToken();
                descCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
            } else
            {
                descCursor = description.newCursor();
                descCursor.toFirstChild();
                descCursor.toNextToken();
                
            }
            descCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
            descCursor.insertAttributeWithValue("class", "workExperience");
            descCursor.insertChars(DataCleaner.getFirst4000Chars(hbInstance.getWorkExperience()));
        }
        //-- changes
        /*
        if(isValidRecord(hbCourse.getSimilarCourses()))
        {
            if(description == null)
            {
                description = xcriInstance.addNewDescription();
                descCursor = description.newCursor();
                descCursor.toFirstContentToken();
                descCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
            } else
            {
                descCursor = description.newCursor();
                descCursor.toFirstChild();
                descCursor.toNextToken();
            }
            descCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
            descCursor.insertAttributeWithValue("class", "relatedCourses");
            descCursor.insertChars(hbCourse.getSimilarCourses());
        }
        */
        
        //--
        // 26 Feb 2013, Hyeonsook, Modification - Added 
        if(isValidRecord(hbInstance.getStudyModeDesc()))
        {
            if(description == null)
            {
                description = xcriInstance.addNewDescription();
                descCursor = description.newCursor();
                descCursor.toFirstContentToken();
                descCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
            } else
            {
                descCursor = description.newCursor();
                descCursor.toFirstChild();
                descCursor.toNextToken();
                
            }
            descCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
            descCursor.insertAttributeWithValue("class", "studyMode");
            descCursor.insertChars(DataCleaner.getFirst4000Chars(hbInstance.getStudyModeDesc()));
        }
        //--
        
        boolean bFeeDiv = false;
        String strHomeFees = null;
        String strOverseasFees = null;
        if(hbInstance.getFeeId() != null)
        {
            strHomeFees = hbInstance.getHomeFees2();
            strOverseasFees = hbInstance.getOverseasFees2();
        } else
        {
            strHomeFees = hbInstance.getHomeFees();
            strOverseasFees = hbInstance.getOverseasFees();
        }
        if(isValidRecord(strHomeFees))
        {
            if(description == null)
            {
                description = xcriInstance.addNewDescription();
                descCursor = description.newCursor();
                descCursor.toFirstContentToken();
                descCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
            } else
            {
                descCursor = description.newCursor();
                descCursor.toFirstChild();
                descCursor.toNextToken();
            }
            descCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
            descCursor.insertAttributeWithValue("class", "feeInformation");
            descCursor.beginElement("span", "http://www.w3.org/1999/xhtml");
            descCursor.insertAttributeWithValue("class", "homeFee");
            descCursor.insertChars(getPrefixedFee(strHomeFees));
            bFeeDiv = true;
        }
        addFeeInformation(hbInstance, xcriInstance, description, descCursor, bFeeDiv, "examFees", hbInstance.getExamFee());
        addFeeInformation(hbInstance, xcriInstance, description, descCursor, bFeeDiv, "internationalFee", strOverseasFees);
        addFeeInformation(hbInstance, xcriInstance, description, descCursor, bFeeDiv, "internationalExamFees", hbInstance.getIntExamFees());
        addFeeInformation(hbInstance, xcriInstance, description, descCursor, bFeeDiv, "feeNotes", hbInstance.getFeeNotes());
        addFeeInformation(hbInstance, xcriInstance, description, descCursor, bFeeDiv, "materialsCosts", hbInstance.getMaterialFee());
        addFeeInformation(hbInstance, xcriInstance, description, descCursor, bFeeDiv, "otherCosts", hbInstance.getAdditionalCosts());
        
        //--> changes with existing fields and new ones based on request from Lucy, 27 Nov 2012
        addFeeInformation(hbInstance, xcriInstance, description, descCursor, bFeeDiv, "fee", hbInstance.getFee());
        addFeeInformation(hbInstance, xcriInstance, description, descCursor, bFeeDiv, "concessionFees", hbInstance.getConcessionFees());
        //-- changes
        
        
        if(isValidRecord(hbInstance.getDetails()))
        {
            DescriptionDType descriptionDetails = xcriInstance.addNewDescription();
            XmlString descValueDetails = org.apache.xmlbeans.XmlString.Factory.newInstance();
            descValueDetails.setStringValue(DataCleaner.getFirst4000Chars(hbInstance.getDetails()));
            descriptionDetails.set(descValueDetails);
            XmlCursor descCursorDetails = descriptionDetails.newCursor();
            descCursorDetails.toFirstContentToken();
            descCursorDetails.insertAttributeWithValue(new QName("http://www.w3.org/2001/XMLSchema-instance", "type"), "xcriterms:structure");
        }
        if(isValidRecord(hbInstance.getExtraStudy()))
        {
            DescriptionDType descriptionDetails = xcriInstance.addNewDescription();
            XmlString descValueDetails = org.apache.xmlbeans.XmlString.Factory.newInstance();
            descValueDetails.setStringValue(DataCleaner.getFirst4000Chars(hbInstance.getExtraStudy()));
            descriptionDetails.set(descValueDetails);
            XmlCursor descCursorDetails = descriptionDetails.newCursor();
            descCursorDetails.toFirstContentToken();
            descCursorDetails.insertAttributeWithValue(new QName("http://www.w3.org/2001/XMLSchema-instance", "type"), "xcriterms:teachingStrategy");
        }
        if(isValidRecord(hbInstance.getStudentSupport()))
        {
            DescriptionDType descriptionDetails = xcriInstance.addNewDescription();
            XmlString descValueDetails = org.apache.xmlbeans.XmlString.Factory.newInstance();
            descValueDetails.setStringValue(DataCleaner.getFirst4000Chars(hbInstance.getStudentSupport()));
            descriptionDetails.set(descValueDetails);
            XmlCursor descCursorDetails = descriptionDetails.newCursor();
            descCursorDetails.toFirstContentToken();
            descCursorDetails.insertAttributeWithValue(new QName("http://www.w3.org/2001/XMLSchema-instance", "type"), "xcriterms:support");
        }
        
        
        //--> changes with existing fields and new ones based on request from Lucy, 27 Nov 2012
        
        List contactList = new ArrayList();
        /*
        if(hbInstance.getAcadContactId() != null)
            contactList.add(hbInstance.getAcadContactId());
        if(hbInstance.getAcadContactId1() != null && !contactList.contains(hbInstance.getAcadContactId1()))
            contactList.add(hbInstance.getAcadContactId1());
        if(hbInstance.getAcadContactId2() != null && !contactList.contains(hbInstance.getAcadContactId2()))
            contactList.add(hbInstance.getAcadContactId2());
        if(hbInstance.getAcadContactId3() != null && !contactList.contains(hbInstance.getAcadContactId3()))
            contactList.add(hbInstance.getAcadContactId3());
        */
        if(hbCourse.getEnquiriesID() != null ){
            Enquiries enquiry = controller.getEnquiries(hbCourse.getEnquiriesID());
            if (enquiry != null)
                contactList.add(enquiry.getEnquiryContact());
        }
        if(hbCourse.getEnquiries2ID() != null){
            Enquiries enquiry = controller.getEnquiries(hbCourse.getEnquiries2ID());
            if (enquiry != null)
                contactList.add(enquiry.getEnquiryContact());
        }
        //-- changes
        
        if(contactList.size() > 0)
        {
            String contacts = "";
            for(Iterator i$ = contactList.iterator(); i$.hasNext();)
            {
                String lecturer = (String)i$.next();
                contacts += lecturer+";";
            }

            DescriptionDType descriptionContacts = xcriInstance.addNewDescription();
            XmlString descValueContacts = org.apache.xmlbeans.XmlString.Factory.newInstance();
            descValueContacts.setStringValue(contacts);
            descriptionContacts.set(descValueContacts);
            XmlCursor descCursorContacts = descriptionContacts.newCursor();
            descCursorContacts.toFirstContentToken();
            descCursorContacts.insertAttributeWithValue(new QName("http://www.w3.org/2001/XMLSchema-instance", "type"), "xcriterms:contactPattern");
        }
    }

    private void createVenue(PresentationDType xcriInstance, XcriCourseinstance hbInstance, XcriCourse hbCourse)
    {
        VenueDType venue = xcriInstance.addNewVenue();
        ProviderDType provider = venue.addNewProvider();
        DescriptionDType description = provider.addNewDescription();
        XmlString descValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
        if(isValidRecord(hbCourse.getDname())){
            descValue.setStringValue(hbCourse.getDname());
        }else{
            descValue.setStringValue(prop.getProperty("novalue.string"));
        }
        description.set(descValue);
        SimpleLiteral pId = provider.addNewIdentifier();
        XmlString pValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
        pValue.setStringValue((new StringBuilder()).append(prop.getProperty("location.uri.prefix")).append(hbInstance.getCourseInstanceId()).append(hbInstance.getLocationId()).toString());
        pId.set(pValue);
        LocationDType location = provider.addNewLocation();
        try
        {
            if(hbInstance.getAddressId() == null)
            {
                location.addAddress(prop.getProperty("location1.address"));

                location.setPostcode(prop.getProperty("location1.postcode"));
                location.setEmail(prop.getProperty("location1.email"));
                location.setFax(prop.getProperty("location1.fax"));
                location.setUrl(prop.getProperty("location1.url"));
                location.setPhone(prop.getProperty("location1.phone"));
            } else
            {
                Address address = controller.getAddress(hbInstance.getAddressId());
                if(address == null){
                    System.out.println("-------COId:"+hbInstance.getCourseInstanceId());
                }
                
                boolean hasAddress = false;
                if(isValidRecord(address.getAddress1())){
                    location.addAddress(address.getAddress1());
                    hasAddress = true;
                }
                if(isValidRecord(address.getAddress2())){
                    location.addAddress(address.getAddress2());
                    hasAddress = true;
                }
                if(isValidRecord(address.getAddress3())){
                    location.addAddress(address.getAddress3());
                    hasAddress = true;
                }
                if(!hasAddress){
                    location.addAddress(prop.getProperty("novalue.string"));
                }
                
                if(isValidRecord(address.getPostcode()))
                    location.setPostcode(DataCleaner.cleanPostCode(address.getPostcode()));
                else
                    location.setPostcode(prop.getProperty("location1.postcode"));
                if(hbInstance.getAcadContactId() != null)
                {
                    Contacts contact = controller.getContact(hbInstance.getAcadContactId().intValue());
                    if(contact != null)
                        location.setEmail(contact.getContactEmail());
                    else
                        location.setEmail(prop.getProperty("provider.default.email"));
                } else
                {
                    location.setEmail(prop.getProperty("provider.default.email"));
                }
                if(isValidRecord(hbInstance.getTelephone()))
                    location.setPhone(hbInstance.getTelephone());
                else
                    location.setPhone(prop.getProperty("provider.default.phone"));
                if(isValidRecord(hbInstance.getFax()))
                    location.setFax(hbInstance.getFax());
                if(address.getDirectionsUrl() != null)
                {
                    String strUrl = address.getDirectionsUrl();
                    if(strUrl.trim().equals(""))
                        strUrl = prop.getProperty("location.url.default");
                    else
                    if(!strUrl.startsWith("http://") && !strUrl.startsWith("HTTP://"))
                        strUrl = (new StringBuilder()).append("http://").append(strUrl).toString();
                    location.setUrl(strUrl);
                }
                SimpleLiteral url = provider.addNewUrl();
                XmlString urlValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
                String strUrl = address.getDirectionsUrl();
                if(strUrl == null || strUrl.trim().equals(""))
                    strUrl = prop.getProperty("location.url.default");
                else
                if(!strUrl.startsWith("http://") && !strUrl.startsWith("HTTP://"))
                    strUrl = (new StringBuilder()).append("http://").append(strUrl).toString();
                urlValue.setStringValue(strUrl);
                url.set(urlValue);
            }
            SimpleLiteral title = provider.addNewTitle();
            XmlString titleValue = org.apache.xmlbeans.XmlString.Factory.newInstance();
            if(hbInstance.getSiteName() != null && !hbInstance.getSiteName().trim().equals(""))
                titleValue.setStringValue(hbInstance.getSiteName());
            else
                titleValue.setStringValue(prop.getProperty("location1.town"));
            title.set(titleValue);
        }
        catch(Exception ex)
        {
            Logger.getLogger(XCRIGenerator.class.getName()).log(Level.WARNING, null, ex);
        }
    }
/*
    private String getEntryCriteria(XcriCourseinstance hbInstance, XcriCourse hbCourse)
    {
        String strEntryCriteria = "";
        if(isValidRecord(hbInstance.getEntryCriteria()))
            strEntryCriteria = (new StringBuilder()).append(strEntryCriteria).append("<EntryCriteria>").append(hbInstance.getEntryCriteria()).append("</EntryCriteria>").toString();
        if(isValidRecord(hbInstance.getInternationalEntryCriteria()))
            strEntryCriteria = (new StringBuilder()).append(strEntryCriteria).append("<InternationalEntryCriteria>").append(hbInstance.getInternationalEntryCriteria()).append("</InternationalEntryCriteria>").toString();
        if(isValidRecord(hbInstance.getInterview()))
            strEntryCriteria = (new StringBuilder()).append(strEntryCriteria).append("<Interview>").append(hbInstance.getInterview()).append("</Interview>").toString();
        if(isValidRecord(hbCourse.getWorkExperience()))
            strEntryCriteria = (new StringBuilder()).append(strEntryCriteria).append("<WorkExperienceRequired>").append(hbCourse.getWorkExperience()).append("</WorkExperienceRequired>").toString();
        return strEntryCriteria;
    }
*/
    private String getStudyModeId(String studyMode)
    {
        if(studyMode.equals("1"))
            return "FT";
        if(studyMode.equals("7") ||studyMode.equals("8") ||studyMode.equals("9"))
            return "FL";
        
        return "PT";
    }

    private String getStudyModeDescription(String studyMode)
    {
        if(studyMode.equals("1"))
            return "Full time";
        if(studyMode.equals("7") ||studyMode.equals("8") ||studyMode.equals("9"))
            return "Flexible";
        
        return "Part time";
        
    }

    private String getDurationIntervalCode(String durationWeeks)
    {
        int days = 0;
        String intervalCode = "";
        String tmpCode = null;
        int beginIndex = -1;
        int endIndex = durationWeeks.indexOf("year");
        try
        {
            if(endIndex != -1)
            {
                if(durationWeeks.lastIndexOf(" ", endIndex - 2) != -1)
                    beginIndex = durationWeeks.lastIndexOf(" ", endIndex - 2);
                else
                    beginIndex = 0;
                tmpCode = durationWeeks.substring(beginIndex, endIndex - 1);
                tmpCode.trim();
                if(tmpCode.indexOf("-") != -1)
                    tmpCode = tmpCode.substring(0, tmpCode.indexOf("-"));
                days = (new Integer(tmpCode.trim())).intValue() * 365;
            }
            endIndex = durationWeeks.indexOf("month");
            if(endIndex != -1)
            {
                if(durationWeeks.lastIndexOf(" ", endIndex - 2) != -1)
                    beginIndex = durationWeeks.lastIndexOf(" ", endIndex - 2);
                else
                    beginIndex = 0;
                tmpCode = durationWeeks.substring(beginIndex, endIndex - 1);
                tmpCode.trim();
                days += (new Integer(tmpCode)).intValue() * 30;
            }
            endIndex = durationWeeks.indexOf("week");
            if(endIndex != -1)
            {
                if(durationWeeks.lastIndexOf(" ", endIndex - 2) != -1)
                    beginIndex = durationWeeks.lastIndexOf(" ", endIndex - 2);
                else
                    beginIndex = 0;
                tmpCode = durationWeeks.substring(beginIndex, endIndex - 1);
                tmpCode.trim();
                if(tmpCode.indexOf("-") != -1)
                    tmpCode = tmpCode.substring(tmpCode.indexOf("-")+1);
                days += (new Integer(tmpCode)).intValue() * 7;
            }
            endIndex = durationWeeks.indexOf("day");
            if(endIndex != -1)
            {
                if(durationWeeks.lastIndexOf(" ", endIndex - 2) != -1)
                    beginIndex = durationWeeks.lastIndexOf(" ", endIndex - 2);
                else
                    beginIndex = 0;
                tmpCode = durationWeeks.substring(beginIndex, endIndex - 1);
                tmpCode.trim();
                days += (new Integer(tmpCode)).intValue();
            }
            int year = Math.round(days / 365);
            int month = Math.round((days % 365) / 30);
            int day = days % 365 % 30;
            intervalCode = "P";
            if(year != 0)
                intervalCode = (new StringBuilder()).append(intervalCode).append(year).append("Y").toString();
            if(month != 0)
                intervalCode = (new StringBuilder()).append(intervalCode).append(month).append("M").toString();
            if(day != 0)
                intervalCode = (new StringBuilder()).append(intervalCode).append(day).append("D").toString();
        }
        catch(Exception ex)
        {
            Logger.getLogger(XCRIGenerator.class.getName()).log(Level.WARNING, null, ex);
            Logger.getLogger(XCRIGenerator.class.getName()).log(Level.INFO, (new StringBuilder()).append("Duration Week: ").append(durationWeeks).toString());
            return intervalCode;
        }
        return intervalCode;
    }

    private String getDurationDescription(String durationWeeks)
    {
        return durationWeeks;
    }

    private String getCostDescription(XcriCourseinstance hbInstance)
    {
        String materialFee = hbInstance.getMaterialFee();
        String additionalCosts = hbInstance.getAdditionalCosts();
        String examFee = hbInstance.getExamFee();
        String concessionFees = hbInstance.getConcessionFees();
        String feeInfo = hbInstance.getFee();
        String desc = "";
        if(hbInstance.getFeeId() != null)
        {
            if(isValidRecord(hbInstance.getHomeFees2()))
                desc = (new StringBuilder()).append(desc).append("UK fee: ").append(getPrefixedFee(hbInstance.getHomeFees2())).append("; ").toString();
            if(isValidRecord(hbInstance.getOverseasFees2()))
                desc = (new StringBuilder()).append(desc).append("Overseas Fee: ").append(getPrefixedFee(hbInstance.getOverseasFees2())).append("; ").toString();
        } else
        {
            if(isValidRecord(hbInstance.getHomeFees()))
                desc = (new StringBuilder()).append(desc).append("UK fee: ").append(getPrefixedFee(hbInstance.getHomeFees())).append("; ").toString();
            if(isValidRecord(hbInstance.getOverseasFees()))
                desc = (new StringBuilder()).append(desc).append("Overseas Fee: ").append(getPrefixedFee(hbInstance.getOverseasFees())).append("; ").toString();
        }
        if(isValidRecord(materialFee))
            desc = (new StringBuilder()).append(desc).append("Material fee: ").append(getPrefixedFee(materialFee)).append("; ").toString();
        if(isValidRecord(additionalCosts))
            desc = (new StringBuilder()).append(desc).append("Additional Costs: ").append(getPrefixedFee(additionalCosts)).append("; ").toString();
        if(isValidRecord(examFee))
            desc = (new StringBuilder()).append(desc).append("Exam fee: ").append(getPrefixedFee(examFee)).append("; ").toString();
        if(isValidRecord(concessionFees))
            desc = (new StringBuilder()).append(desc).append("Concession fees: ").append(getPrefixedFee(concessionFees)).append("; ").toString();
        if(isValidRecord(feeInfo))
            desc = (new StringBuilder()).append(desc).append(feeInfo).append("; ").toString();
        return desc;
    }

    private String getAttendanceModeId(String modeId)
    {
        
        if(modeId.equals("6"))
            return "WB";
        if(modeId.equals("7"))
            return "DS";
        if(modeId.equals("8"))
            return "DA";
        if(modeId.equals("9"))
            return "MM";
        
        return "CM";
    }

    private String getAttendanceModeDesc(String modeId)
    {
        
        if(modeId.equals("6"))
            return "Work-based";
        if(modeId.equals("7"))
            return "Distance without attendance";
        if(modeId.equals("8"))
            return "Distance with attendance";
        if(modeId.equals("9"))
            return "Mixed mode";
        
        return "Campus";
     
    }

    private String getAttendancePatternId(String modeId)
    {
        if(modeId.equals("1") ||modeId.equals("3") ||modeId.equals("5") ||modeId.equals("10"))
            return "DT";
        if(modeId.equals("2"))
            return "EV";
        if(modeId.equals("4"))
            return "WE";
        if(modeId.equals("6"))
            return "DR";
        
        return "CS";
    }

    private String getAttendancePatternDesc(String modeId)
    {
        if(modeId.equals("1") ||modeId.equals("3") ||modeId.equals("5") ||modeId.equals("10"))
            return "Daytime";
        if(modeId.equals("2"))
            return "Evening";
        if(modeId.equals("4"))
            return "Weekend";
        if(modeId.equals("6"))
            return "Day/Block release";
        
        return "Customised";
        
    }

    private String getEducationLevel(String pstudyLevel)
    {
        String edLevel = "Further Education";
        if(pstudyLevel.equals("5") || pstudyLevel.equals("9") ||pstudyLevel.equals("15")|| pstudyLevel.equals("16")
                ||pstudyLevel.equals("17") ||pstudyLevel.equals("23") ||pstudyLevel.equals("24") ||pstudyLevel.equals("25"))
        {
            logUGCount++;
            return "Undergraduate";
        }
        if(pstudyLevel.equals("10") || pstudyLevel.equals("18") ||pstudyLevel.equals("19"))
        {
            logPGCount++;
            return "Postgraduate";
        } else
        {
            return edLevel;
        }
    }


    void addFeeInformation(XcriCourseinstance hbInstance, PresentationDType xcriInstance, DescriptionDType description, XmlCursor descCursor, boolean hasFeeDiv, String name, String value)
    {
        if(isValidRecord(value))
        {
            if(description == null)
            {
                description = xcriInstance.addNewDescription();
                descCursor = description.newCursor();
                descCursor.toFirstContentToken();
                descCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
                descCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
                descCursor.insertAttributeWithValue("class", "feeInformation");
                hasFeeDiv = true;
            } else if(!hasFeeDiv)
            {
                descCursor.toChild(1);
                descCursor.toNextToken();
                descCursor.beginElement("div", "http://www.w3.org/1999/xhtml");
                descCursor.insertAttributeWithValue("class", "feeInformation");
            } else
            {
                descCursor.toChild(2);
                descCursor.toNextToken();
            }
            descCursor.beginElement("span", "http://www.w3.org/1999/xhtml");
            descCursor.insertAttributeWithValue("class", name);
            descCursor.insertChars(getPrefixedFee(value));
        }
    }

    boolean isValidRecord(String value)
    {
        return value != null && !value.trim().equals("") && !value.equals("NULL");
    }


    public Properties getProp()
    {
        return prop;
    }

    public int getLogCourseCount()
    {
        return logCourseCount;
    }

    public int getLogCourseInstanceCount()
    {
        return logCourseInstanceCount;
    }

    public int getLogCourseInstanceCount2()
    {
        return logCourseInstanceCount2;
    }

    public int getLogDistanceLearningCount()
    {
        return logDistanceLearningCount;
    }

    public int getLogPGCount()
    {
        return logPGCount;
    }

    public int getLogUGCount()
    {
        return logUGCount;
    }


    public String getPrefixedFee(String fee){
        fee = fee.trim();
        String pound = "GBP";
        if(fee.startsWith("£"))
            return fee.replaceAll("£", pound);
        if(fee.length() >1 && Character.isDigit(fee.charAt(0)))
            return pound+fee;
        
        return fee;
            
    }
}
