package edu.uta.cse.se;

public class ChatbotController
{
    private final Context context;
    private final ChainOfHandler head;

    public ChatbotController(Context context, ChainOfHandler head)
    {
        this.context = context;
        this.head = head;
    }

    public boolean setStrategy(Strategy strategy)
    {
        if (strategy == null)
        {
            return false;
        }
        context.setStrategy(strategy);
        return true;
    }

    public String queryQuestion(String question) throws Exception
    {
        // strategy int is selected by GUI; store it in Request for completeness
        Request r = new Request(question, -1);
        return head.handle(r);
    }
}