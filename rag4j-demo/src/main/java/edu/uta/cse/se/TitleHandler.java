package edu.uta.cse.se;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TitleHandler extends ChainOfHandler
{
    private final Map<String, CourseRecord> catalog;

    public TitleHandler(Map<String, CourseRecord> catalog)
    {
        this.catalog = catalog;
    }

    @Override
    public boolean canHandle(Request r)
    {
        String q = r.getQuestion().toLowerCase();
        return q.contains("title");
    }

    @Override
    protected String handleRequest(Request r)
    {
        String id = extractCourseId(r.getQuestion());
        if (id == null)
        {
            return "I think you're asking about a course title. Try: \"What is the title of 4361?\"";
        }

        CourseRecord rec = catalog.get(id);
        if (rec == null)
        {
            return "I couldn't find course " + id + " in the catalog.";
        }

        return id + " title: " + rec.title();
    }

    private String extractCourseId(String q)
    {
        Pattern p = Pattern.compile("\\b(\\d{4})\\b");
        Matcher m = p.matcher(q);
        if (m.find())
        {
            return m.group(1);
        }
        return null;
    }
}