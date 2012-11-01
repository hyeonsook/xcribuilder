// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 27/10/2012 15:38:03
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   JobModel.java

package uk.ac.uwl.luci.rdf.job;


public class JobModel
{

    public JobModel()
    {
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(String createdDate)
    {
        this.createdDate = createdDate;
    }

    public String getAbstrat()
    {
        return abstrat;
    }

    public String[] getCategories()
    {
        return categories;
    }

    public String getDescription()
    {
        return description;
    }

    public String getLink()
    {
        return link;
    }

    public String getSender()
    {
        return sender;
    }

    public String getTitle()
    {
        return title;
    }

    public void setAbstrat(String abstrat)
    {
        this.abstrat = abstrat;
    }

    public void setCategories(String categories[])
    {
        this.categories = categories;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public void setSender(String sender)
    {
        this.sender = sender;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String toString()
    {
        return (new StringBuilder()).append("JobModel{id=").append(id).append(", title=").append(title).append(", categories=").append(getCategoriesString()).append(", abstrat=").append(abstrat).append(", description=").append(description).append(", sender=").append(sender).append(", link=").append(link).append(", createdDate=").append(createdDate).append('}').toString();
    }

    public String getCategoriesString()
    {
        String categoriesString = "|";
        for(int index = 0; index < categories.length; index++)
            categoriesString = (new StringBuilder()).append(categoriesString).append(categories[index]).append("|").toString();

        return categoriesString;
    }

    private String id;
    private String title;
    private String categories[];
    private String abstrat;
    private String description;
    private String sender;
    private String link;
    private String createdDate;
}
