package edu.uta.cse.se;

public class ChatbotGUI
{
    private final ChatbotController controller;

    public ChatbotGUI(ChatbotController controller)
    {
        this.controller = controller;
    }

    public boolean submit(String question, int strategy) throws Exception
    {
        // In a real GUI you’d pass the strategy; here we assume controller already set it.
        String answer = controller.queryQuestion(question);
        System.out.println("Q: " + question);
        System.out.println("A: " + answer);
        return true;
    }
}