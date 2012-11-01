// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 27/10/2012 15:36:32
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   XcriRDFGenerator.java

package uk.ac.uwl.luci.rdf.course;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dom4j.*;
import org.xcri.profiles.x12.catalog.*;
import uk.ac.uwl.luci.rdf.RDFGenerator;
import uk.ac.uwl.luci.rdf.RepositoryManager;

public class XcriRDFGenerator extends RDFGenerator
{

    public XcriRDFGenerator()
    {
        cat = null;
    }

    public void setCatalog(CatalogDocument cat)
    {
        this.cat = cat;
    }

    public Model extractData()
    {
        Resource provenance = rootModel.createResource("http://luci.uwl.ac.uk/course_linkeddata#provenance");
        rootModel.createReifiedStatement(rootModel.createStatement(provenance, rootModel.createProperty("http://purl.org/dc/elements/1.1/", "publisher"), "University of West London"));
        rootModel.createReifiedStatement(rootModel.createStatement(provenance, rootModel.createProperty("http://purl.org/dc/elements/1.1/", "issued"), (new SimpleDateFormat("yyyy-MM-dd")).format(Calendar.getInstance().getTime())));
        rootModel.createReifiedStatement(rootModel.createStatement(provenance, rootModel.createProperty("http://purl.org/dc/elements/1.1/", "rights"), "http://reference.data.gov.uk/id/open-government-licence"));
        rootModel.createReifiedStatement(rootModel.createStatement(provenance, rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "source"), "http://www.uwl.ac.uk/students/prospectus.jsp"));
        Resource provider = rootModel.createResource(getURI("http://luci.uwl.ac.uk/course_linkeddata/Provider#", "uwl"));
        provider.addProperty(RDF.type, rootModel.createResource("http://luci.uwl.ac.uk/course_linkeddata/Provider")).addProperty(rootModel.createProperty("http://purl.org/dc/elements/1.1/", "title"), rootModel.createTypedLiteral("University of West London")).addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "url"), rootModel.createTypedLiteral("http://www.uwl.ac.uk")).addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "url"), rootModel.createTypedLiteral("http://www.uwl.ac.uk"));
        Resource uwlLocation = rootModel.createResource(getURI("http://luci.uwl.ac.uk/course_linkeddata/Location#", (new StringBuilder()).append("uwl_").append(cat.getCatalog().getProviderArray(0).getLocationArray(0).getTown()).toString()));
        uwlLocation.addProperty(RDF.type, rootModel.createResource("http://luci.uwl.ac.uk/course_linkeddata/Location")).addProperty(rootModel.createProperty("http://purl.org/dc/elements/1.1/", "title"), cat.getCatalog().getProviderArray(0).getLocationArray(0).getTown());
        if(cat.getCatalog().getProviderArray(0).getLocationArray(0).getUrl() != null)
            uwlLocation.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "url"), cat.getCatalog().getProviderArray(0).getLocationArray(0).getUrl());
        if(cat.getCatalog().getProviderArray(0).getLocationArray(0).getPostcode() != null)
            uwlLocation.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "postcode"), cat.getCatalog().getProviderArray(0).getLocationArray(0).getPostcode().replace(" ", ""));
        if(cat.getCatalog().getProviderArray(0).getLocationArray(0).sizeOfAddressArray() > 0)
        {
            uwlLocation.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "address"), cat.getCatalog().getProviderArray(0).getLocationArray(0).getAddressArray(0));
            uwlLocation.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "phone"), cat.getCatalog().getProviderArray(0).getLocationArray(0).getPhone());
        }
        if(cat.getCatalog().getProviderArray(0).getLocationArray(0).getFax() != null)
            uwlLocation.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "fax"), cat.getCatalog().getProviderArray(0).getLocationArray(0).getFax());
        if(cat.getCatalog().getProviderArray(0).getLocationArray(0).getEmail() != null)
            uwlLocation.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "email"), cat.getCatalog().getProviderArray(0).getLocationArray(0).getEmail());
        provider.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "location"), uwlLocation);
        CourseDType courses[] = cat.getCatalog().getProviderArray(0).getCourseArray();
        Resource presentation = null;
        Resource qualification = null;
        Resource location = null;
        for(int i = 0; i < courses.length; i++)
            try
            {
                Resource course = rootModel.createResource(getURI("http://luci.uwl.ac.uk/course_linkeddata/Course#", DocumentHelper.parseText(courses[i].getIdentifierArray(0).toString()).getRootElement().getStringValue().substring(DocumentHelper.parseText(courses[i].getIdentifierArray(0).toString()).getRootElement().getStringValue().lastIndexOf("/") + 1)));
                course.addProperty(RDF.type, rootModel.createResource("http://luci.uwl.ac.uk/course_linkeddata/Course")).addProperty(rootModel.createProperty("http://purl.org/dc/elements/1.1/", "title"), DocumentHelper.parseText(courses[i].getTitleArray(0).toString()).getRootElement().getStringValue()).addProperty(rootModel.createProperty("http://purl.org/dc/elements/1.1/", "description"), DocumentHelper.parseText(courses[i].getDescriptionArray(0).toString()).getRootElement().getStringValue()).addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "abstract"), DocumentHelper.parseText(courses[i].getAbstractArray(0).toString()).getRootElement().getStringValue()).addProperty(rootModel.createProperty("http://purl.org/dc/elements/1.1/", "subject"), DocumentHelper.parseText(courses[i].getSubjectArray(0).toString()).getRootElement().getStringValue()).addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "applicationProcedure"), DocumentHelper.parseText(courses[i].getApplicationProcedureArray(0).toString()).getRootElement().getStringValue());
                if(courses[i].sizeOfLearningOutcomeArray() > 0)
                    course.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "learningOutcome"), DocumentHelper.parseText(courses[i].getLearningOutcomeArray(0).toString()).getRootElement().getStringValue());
                if(courses[i].sizeOfQualificationArray() > 0)
                {
                    qualification = rootModel.createResource(getURI("http://luci.uwl.ac.uk/course_linkeddata/Qualification#", DocumentHelper.parseText(courses[i].getQualificationArray(0).getIdentifierArray(0).toString()).getRootElement().getStringValue().substring(DocumentHelper.parseText(courses[i].getQualificationArray(0).getIdentifierArray(0).toString()).getRootElement().getStringValue().lastIndexOf("/") + 1)));
                    qualification.addProperty(RDF.type, rootModel.createResource("http://luci.uwl.ac.uk/course_linkeddata/Qualification")).addProperty(rootModel.createProperty("http://purl.org/dc/elements/1.1/", "title"), DocumentHelper.parseText(courses[i].getQualificationArray(0).getTitleArray(0).toString()).getRootElement().getStringValue());
                    if(courses[i].getQualificationArray(0).getAbbr() != null)
                        qualification.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "abbr"), courses[i].getQualificationArray(0).getAbbr());
                    if(courses[i].getQualificationArray(0).getEducationLevel() != null)
                        qualification.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "educationLevel"), DocumentHelper.parseText(courses[i].getQualificationArray(0).getEducationLevel().toString()).getRootElement().getStringValue());
                    if(courses[i].getQualificationArray(0).getAwardedBy() != null)
                        qualification.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "awardedBy"), courses[i].getQualificationArray(0).getAwardedBy());
                    if(courses[i].getQualificationArray(0).getAccreditedBy() != null)
                        qualification.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "accreditedBy"), courses[i].getQualificationArray(0).getAccreditedBy());
                }
                PresentationDType presentations[] = courses[i].getPresentationArray();
                for(int j = 0; j < presentations.length; j++)
                {
                    presentation = rootModel.createResource(getURI("http://luci.uwl.ac.uk/course_linkeddata/Presentation#", DocumentHelper.parseText(courses[i].getPresentationArray(j).getIdentifierArray(0).toString()).getRootElement().getStringValue().substring(DocumentHelper.parseText(courses[i].getPresentationArray(j).getIdentifierArray(0).toString()).getRootElement().getStringValue().lastIndexOf("/") + 1)));
                    presentation.addProperty(RDF.type, rootModel.createResource("http://luci.uwl.ac.uk/course_linkeddata/Presentation")).addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "applyFrom"), DocumentHelper.parseText(courses[i].getPresentationArray(j).getApplyFrom().toString()).getRootElement().getStringValue()).addProperty(rootModel.createProperty("http://purl.org/dc/elements/1.1/", "title"), DocumentHelper.parseText(courses[i].getPresentationArray(j).getTitleArray(0).toString()).getRootElement().getStringValue()).addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "url"), DocumentHelper.parseText(courses[i].getPresentationArray(j).getUrl().toString()).getRootElement().getStringValue());
                    if(courses[i].getPresentationArray(j).getApplyUntil() != null)
                        presentation.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "applyUntil"), DocumentHelper.parseText(courses[i].getPresentationArray(j).getApplyUntil().toString()).getRootElement().getStringValue());
                    if(courses[i].getPresentationArray(j).getApplyTo() != null)
                        presentation.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "applyTo"), courses[i].getPresentationArray(j).getApplyTo());
                    if(courses[i].getPresentationArray(j).sizeOfAssessmentArray() > 0)
                        presentation.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "assessment"), DocumentHelper.parseText(courses[i].getPresentationArray(j).getAssessmentArray(0).toString()).getRootElement().getStringValue());
                    if(courses[i].getPresentationArray(j).getAttendanceMode() != null)
                        presentation.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "attendanceMode"), DocumentHelper.parseText(courses[i].getPresentationArray(j).getAttendanceMode().toString()).getRootElement().getStringValue());
                    if(courses[i].getPresentationArray(j).getAttendancePattern() != null)
                        presentation.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "attendancePattern"), DocumentHelper.parseText(courses[i].getPresentationArray(j).getAttendancePattern().toString()).getRootElement().getStringValue());
                    if(courses[i].getPresentationArray(j).getCost() != null)
                        presentation.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "cost"), DocumentHelper.parseText(courses[i].getPresentationArray(j).getCost().toString()).getRootElement().getStringValue());
                    if(courses[i].getPresentationArray(j).getDuration() != null)
                        presentation.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "duration"), DocumentHelper.parseText(courses[i].getPresentationArray(j).getDuration().toString()).getRootElement().getStringValue());
                    if(courses[i].getPresentationArray(j).sizeOfPrerequisiteArray() > 0)
                        presentation.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "prerequisite"), DocumentHelper.parseText(courses[i].getPresentationArray(j).getPrerequisiteArray(0).toString()).getRootElement().getStringValue());
                    if(courses[i].getPresentationArray(j).getStart() != null)
                        presentation.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "start"), DocumentHelper.parseText(courses[i].getPresentationArray(j).getStart().toString()).getRootElement().getStringValue());
                    if(courses[i].getPresentationArray(j).getStudyMode() != null)
                        presentation.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "studyMode"), DocumentHelper.parseText(courses[i].getPresentationArray(j).getStudyMode().toString()).getRootElement().getStringValue());
                    if(courses[i].getPresentationArray(j).sizeOfVenueArray() > 0)
                    {
                        location = rootModel.createResource(getURI("http://luci.uwl.ac.uk/course_linkeddata/Location#", DocumentHelper.parseText(courses[i].getPresentationArray(j).getVenueArray(0).getProvider().getIdentifierArray(0).toString()).getRootElement().getStringValue().substring(DocumentHelper.parseText(courses[i].getPresentationArray(j).getVenueArray(0).getProvider().getIdentifierArray(0).toString()).getRootElement().getStringValue().lastIndexOf("/") + 1)));
                        location.addProperty(RDF.type, rootModel.createResource("http://luci.uwl.ac.uk/course_linkeddata/Location"));
                        if(courses[i].getPresentationArray(j).getVenueArray(0).getProvider() != null)
                        {
                            if(courses[i].getPresentationArray(j).getVenueArray(0).getProvider().sizeOfTitleArray() > 0)
                                location.addProperty(rootModel.createProperty("http://purl.org/dc/elements/1.1/", "title"), DocumentHelper.parseText(courses[i].getPresentationArray(j).getVenueArray(0).getProvider().getTitleArray(0).toString()).getRootElement().getStringValue());
                            if(courses[i].getPresentationArray(j).getVenueArray(0).getProvider().getUrl() != null)
                                location.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "url"), DocumentHelper.parseText(courses[i].getPresentationArray(j).getVenueArray(0).getProvider().getUrl().toString()).getRootElement().getStringValue());
                        }
                        if(courses[i].getPresentationArray(j).getVenueArray(0).getProvider().sizeOfDescriptionArray() > 0 && courses[i].getPresentationArray(j).getVenueArray(0).getProvider().getDescriptionArray(0) != null)
                            presentation.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "school"), DocumentHelper.parseText(courses[i].getPresentationArray(j).getVenueArray(0).getProvider().getDescriptionArray(0).toString()).getRootElement().getStringValue());
                        if(courses[i].getPresentationArray(j).getVenueArray(0).getProvider().sizeOfLocationArray() > 0)
                        {
                            if(courses[i].getPresentationArray(j).getVenueArray(0).getProvider().getLocationArray(0).getPostcode() != null)
                                location.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "postcode"), courses[i].getPresentationArray(j).getVenueArray(0).getProvider().getLocationArray(0).getPostcode());
                            String strAddress = "";
                            for(int index = 0; index < courses[i].getPresentationArray(j).getVenueArray(0).getProvider().getLocationArray(0).sizeOfAddressArray(); index++)
                                strAddress = (new StringBuilder()).append(strAddress).append(courses[i].getPresentationArray(j).getVenueArray(0).getProvider().getLocationArray(0).getAddressArray(index)).append(" ").toString();

                            if(!strAddress.equals(""))
                                location.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "address"), strAddress);
                            if(courses[i].getPresentationArray(j).getVenueArray(0).getProvider().getLocationArray(0).getPhone() != null)
                                location.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "phone"), courses[i].getPresentationArray(j).getVenueArray(0).getProvider().getLocationArray(0).getPhone());
                            if(courses[i].getPresentationArray(j).getVenueArray(0).getProvider().getLocationArray(0).getFax() != null)
                                location.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "fax"), courses[i].getPresentationArray(j).getVenueArray(0).getProvider().getLocationArray(0).getFax());
                            if(courses[i].getPresentationArray(j).getVenueArray(0).getProvider().getLocationArray(0).getEmail() != null)
                                location.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "email"), courses[i].getPresentationArray(j).getVenueArray(0).getProvider().getLocationArray(0).getEmail());
                            if(courses[i].getPresentationArray(j).getVenueArray(0).getProvider().getLocationArray(0).getPostcode() != null)
                                location.addProperty(rootModel.createProperty("http://www.w3.org/2002/07/owl#", "sameAs"), (new StringBuilder()).append("http://data.ordnancesurvey.co.uk/id/postcodeunit/").append(courses[i].getPresentationArray(j).getVenueArray(0).getProvider().getLocationArray(0).getPostcode()).toString());
                        }
                    }
                    course.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "specifies"), presentation);
                    presentation.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "isOfferedAt"), location);
                }

                provider.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "offers"), course);
                course.addProperty(rootModel.createProperty("http://luci.uwl.ac.uk/course_linkeddata/", "qualification"), qualification);
            }
            catch(DocumentException ex)
            {
                Logger.getLogger(XcriRDFGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }

        rootModel.setNsPrefix("luci", "http://luci.uwl.ac.uk/course_linkeddata/");
        return rootModel;
    }

    public static void main(String args[])
    {
        XcriRDFGenerator rdfGenerator = new XcriRDFGenerator();
        rdfGenerator.filepath = "D:/My Documents/NetBeansProjects/XCRIBuilder/";
        RepositoryManager.update((new StringBuilder()).append(rdfGenerator.filepath).append("recent.rdf").toString(), "http://luci.uwl.ac.uk/course_linkeddata/recent");
    }

    private CatalogDocument cat;
}
