package uk.ac.uwl.luci.hb;
// Generated Mar 9, 2012 6:53:15 PM by Hibernate Tools 3.2.1.GA



/**
 * XcriCourse generated by hbm2java
 */
public class XcriCourse  implements java.io.Serializable {

public String getValidation()
    {
        return validation;
    }

    public void setValidation(String validation)
    {
        this.validation = validation;
    }

    public void setSimilarCourses(String similarCourses)
    {
        this.similarCourses = similarCourses;
    }

    public String getSimilarCourses()
    {
        return similarCourses;
    }

    public XcriCourse()
    {
    }

    public XcriCourse(int courseId, String clusterTitle, String studyLevel, String adesc, String abodyDesc, String awardType, Byte awardTypeId, 
            Integer subjectId, Integer subject2id, Integer subject3id, String introduction, String profAccredit, String exitSkills, String notableAspects, 
            String specialResources, String teachingMethods, String learningMaterials, String careerProgression, String studyProgression, String workExperience, String dname, 
            String similarCourses, String validation, Integer enquiriesID, Integer enquiries2ID, String webPageURL, String webPageDesc, String keywords)
    {
        this.courseId = courseId;
        this.clusterTitle = clusterTitle;
        this.studyLevel = studyLevel;
        this.adesc = adesc;
        this.abodyDesc = abodyDesc;
        this.awardType = awardType;
        this.awardTypeId = awardTypeId;
        this.subjectId = subjectId;
        this.subject2id = subject2id;
        this.subject3id = subject3id;
        this.introduction = introduction;
        this.profAccredit = profAccredit;
        this.exitSkills = exitSkills;
        this.notableAspects = notableAspects;
        this.specialResources = specialResources;
        this.teachingMethods = teachingMethods;
        this.learningMaterials = learningMaterials;
        this.careerProgression = careerProgression;
        this.studyProgression = studyProgression;
        this.workExperience = workExperience;
        this.dname = dname;
        this.similarCourses = similarCourses;
        this.validation = validation;
        this.enquiriesID = enquiriesID;
        this.enquiries2ID = enquiries2ID;
        this.webPageURL = webPageURL;
        this.webPageDesc = webPageDesc;
        this.keywords = keywords;
    }

    public int getCourseId()
    {
        return courseId;
    }

    public void setCourseId(int courseId)
    {
        this.courseId = courseId;
    }

    public String getClusterTitle()
    {
        return clusterTitle;
    }

    public void setClusterTitle(String clusterTitle)
    {
        this.clusterTitle = clusterTitle;
    }

    public String getStudyLevel()
    {
        return studyLevel;
    }

    public void setStudyLevel(String studyLevel)
    {
        this.studyLevel = studyLevel;
    }

    public String getAdesc()
    {
        return adesc;
    }

    public void setAdesc(String adesc)
    {
        this.adesc = adesc;
    }

    public String getAbodyDesc()
    {
        return abodyDesc;
    }

    public void setAbodyDesc(String abodyDesc)
    {
        this.abodyDesc = abodyDesc;
    }

    public String getAwardType()
    {
        return awardType;
    }

    public void setAwardType(String awardType)
    {
        this.awardType = awardType;
    }

    public Byte getAwardTypeId()
    {
        return awardTypeId;
    }

    public void setAwardTypeId(Byte awardTypeId)
    {
        this.awardTypeId = awardTypeId;
    }

    public Integer getSubjectId()
    {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId)
    {
        this.subjectId = subjectId;
    }

    public Integer getSubject2id()
    {
        return subject2id;
    }

    public void setSubject2id(Integer subject2id)
    {
        this.subject2id = subject2id;
    }

    public Integer getSubject3id()
    {
        return subject3id;
    }

    public void setSubject3id(Integer subject3id)
    {
        this.subject3id = subject3id;
    }

    public String getIntroduction()
    {
        return introduction;
    }

    public void setIntroduction(String introduction)
    {
        this.introduction = introduction;
    }

    public String getProfAccredit()
    {
        return profAccredit;
    }

    public void setProfAccredit(String profAccredit)
    {
        this.profAccredit = profAccredit;
    }

    public String getExitSkills()
    {
        return exitSkills;
    }

    public void setExitSkills(String exitSkills)
    {
        this.exitSkills = exitSkills;
    }

    public String getNotableAspects()
    {
        return notableAspects;
    }

    public void setNotableAspects(String notableAspects)
    {
        this.notableAspects = notableAspects;
    }

    public String getSpecialResources()
    {
        return specialResources;
    }

    public void setSpecialResources(String specialResources)
    {
        this.specialResources = specialResources;
    }

    public String getTeachingMethods()
    {
        return teachingMethods;
    }

    public void setTeachingMethods(String teachingMethods)
    {
        this.teachingMethods = teachingMethods;
    }

    public String getLearningMaterials()
    {
        return learningMaterials;
    }

    public void setLearningMaterials(String learningMaterials)
    {
        this.learningMaterials = learningMaterials;
    }

    public String getCareerProgression()
    {
        return careerProgression;
    }

    public void setCareerProgression(String careerProgression)
    {
        this.careerProgression = careerProgression;
    }

    public String getStudyProgression()
    {
        return studyProgression;
    }

    public void setStudyProgression(String studyProgression)
    {
        this.studyProgression = studyProgression;
    }

    public String getWorkExperience()
    {
        return workExperience;
    }

    public void setWorkExperience(String workExperience)
    {
        this.workExperience = workExperience;
    }

    public String getDname()
    {
        return dname;
    }

    public void setDname(String dname)
    {
        this.dname = dname;
    }

    public Integer getEnquiriesID() {
        return enquiriesID;
    }

    public Integer getEnquiries2ID() {
        return enquiries2ID;
    }

    public String getWebPageURL() {
        return webPageURL;
    }

    public String getWebPageDesc() {
        return webPageDesc;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setEnquiriesID(Integer EnquiriesID) {
        this.enquiriesID = EnquiriesID;
    }

    public void setEnquiries2ID(Integer Enquiries2ID) {
        this.enquiries2ID = Enquiries2ID;
    }

    public void setWebPageURL(String WebPageURL) {
        this.webPageURL = WebPageURL;
    }

    public void setWebPageDesc(String WebPageDesc) {
        this.webPageDesc = WebPageDesc;
    }

    public void setKeywords(String Keywords) {
        this.keywords = Keywords;
    }

    
    
    private int courseId;
    private String clusterTitle;
    private String studyLevel;
    private String adesc;
    private String abodyDesc;
    private String awardType;
    private Byte awardTypeId;
    private Integer subjectId;
    private Integer subject2id;
    private Integer subject3id;
    private String introduction;
    private String profAccredit;
    private String exitSkills;
    private String notableAspects;
    private String specialResources;
    private String teachingMethods;
    private String learningMaterials;
    private String careerProgression;
    private String studyProgression;
    private String workExperience;
    private String dname;
    private String similarCourses;
    private String validation;
    private Integer enquiriesID;
    private Integer enquiries2ID;
    private String webPageURL;
    private String webPageDesc;
    private String keywords;

}


