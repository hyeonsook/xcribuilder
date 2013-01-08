// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 27/10/2012 15:35:11
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   TwitterReader.java

package uk.ac.uwl.luci.job;

import java.text.SimpleDateFormat;
import java.util.*;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import uk.ac.uwl.luci.rdf.job.JobModel;

public class TwitterReader
{

    public TwitterReader()
    {
    }

    public static Date getLastRunDate()
    {
        return lastRunDate;
    }

    public static void setLastRunDate(Date lastRunDate)
    {
        lastRunDate = lastRunDate;
    }

    public static long getLastMaxId()
    {
        return lastMaxId;
    }

    public static void setLastMaxId(long lastMaxId)
    {
        lastMaxId = lastMaxId;
    }

    public static void main(String args[])
    {
        TwitterReader tr = new TwitterReader();
        System.out.println((new StringBuilder()).append("Total job size: ").append(tr.readTweet().size()).toString());
    }

    public List readTweet()
    {
        Twitter twitter = (new TwitterFactory()).getInstance();
        List jobList = new ArrayList();
        Query query = new Query("from:GraduateJobFeed");
        query.setRpp(100);
        if(lastMaxId != -1L)
        {
            System.out.println((new StringBuilder()).append("--- Last Max Id: ").append(lastMaxId).toString());
            query.setSinceId(lastMaxId);
        }
        if(lastRunDate == null)
            query.since("2012-10-10");
        else
            query.since((new SimpleDateFormat("yyyy-MM-dd")).format(lastRunDate));
        try
        {
            QueryResult result = twitter.search(query);
            JobModel job;
            for(Iterator i$ = result.getTweets().iterator(); i$.hasNext(); System.out.println(job.toString()))
            {
                Tweet tweet = (Tweet)i$.next();
                job = buildJobModel(tweet.getId(), tweet.getFromUser(), tweet.getText(), tweet.getCreatedAt());
                jobList.add(job);
                System.out.println((new StringBuilder()).append(tweet.getFromUser()).append(tweet.getText()).append(tweet.getCreatedAt()).toString());
            }

            lastMaxId = result.getMaxId();
        }
        catch(TwitterException e)
        {
            System.out.println(e.getErrorMessage());
        }
        return jobList;
    }

    public JobModel buildJobModel(long id, String sender, String message, Date createdDate)
    {
        JobModel job = new JobModel();
        int urlIndex = message.indexOf("http://");
        job.setId((new StringBuilder()).append("").append(id).toString());
        job.setSender(sender);
        if(urlIndex > 0)
        {
            job.setAbstrat(message.substring(0, urlIndex - 1));
            job.setLink(message.substring(urlIndex, message.indexOf(" ", urlIndex)));
        } else
        {
            job.setAbstrat(message);
            job.setLink("");
        }
        job.setCategories(extractCategories(message));
        job.setCreatedDate((new StringBuilder()).append("").append(createdDate).toString());
        return job;
    }

    public String[] extractCategories(String message)
    {
        List categories = new ArrayList();
        int endIndex;
        for(int beginIndex = message.indexOf("#"); beginIndex > 0; beginIndex = message.indexOf("#", endIndex))
        {
            endIndex = message.indexOf(" ", beginIndex);
            if(endIndex < 0)
                endIndex = message.length();
            String category = message.substring(beginIndex + 1, endIndex);
            categories.add(category);
        }

        return (String[])(String[])categories.toArray(new String[categories.size()]);
    }

    private static long lastMaxId;
    private static Date lastRunDate;
}
