package edu.uta.cse.se;

public abstract class ChainOfHandler
{
    protected ChainOfHandler next;
    protected String question;

    public void create(ChainOfHandler next)
    {
        this.next = next;
    }

    public abstract boolean canHandle(Request r);

    public String handle(Request r) throws Exception
    {
        this.question = r.getQuestion();

        if (canHandle(r))
        {
            return handleRequest(r);
        }

        if (next != null)
        {
            return next.handle(r);
        }

        return "Sorry, I couldn't handle that question.";
    }

    protected abstract String handleRequest(Request r) throws Exception;
}