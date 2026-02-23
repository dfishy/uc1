package edu.uta.cse.se;

public class Request
{
    private final String question;
    private final int strategy;
    private String answer;

    public Request(String question, int strategy)
    {
        this.question = question;
        this.strategy = strategy;
        this.answer = "";
    }

    public String getQuestion()
    {
        return question;
    }

    public int getStrategy()
    {
        return strategy;
    }

    public String getAnswer()
    {
        return answer;
    }

    public void setAnswer(String answer)
    {
        this.answer = answer;
    }
}