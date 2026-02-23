package edu.uta.cse.se;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IDHandler extends ChainOfHandler
{
    private final Map<String, CourseRecord> catalog;

    public IDHandler(Map<String, CourseRecord> catalog)
    {
        this.catalog = catalog;
    }

    @Override
    public boolean canHandle(Request r)
    {
        return extractCourseId(r.getQuestion()) != null;
    }

    @Override
    protected String handleRequest(Request r)
    {
        String id = extractCourseId(r.getQuestion());
        CourseRecord rec = catalog.get(id);

        if (rec == null)
        {
            return "I found course id " + id + " in your question, but it's not in the catalog.";
        }

        String q = r.getQuestion().toLowerCase();
        if (q.contains("title"))
        {
            return id + " title: " + rec.title();
        }

        // Default for ID-based questions: give description
        return id + " description: " + rec.description();
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