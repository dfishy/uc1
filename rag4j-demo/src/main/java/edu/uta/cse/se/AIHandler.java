package edu.uta.cse.se;

import org.rag4j.integration.ollama.OllamaAccess;
import org.rag4j.integration.ollama.OllamaChatService;
import org.rag4j.rag.generation.answer.AnswerGenerator;

public class AIHandler extends ChainOfHandler
{
    private final Context context;
    private final AnswerGenerator generator;

    public AIHandler(Context context)
    {
        this.context = context;

        OllamaAccess access = new OllamaAccess();
        this.generator = new AnswerGenerator(new OllamaChatService(access));
    }

    @Override
    public boolean canHandle(Request r)
    {
        // This is the "last handler" — it can always handle.
        return true;
    }

    @Override
    protected String handleRequest(Request r)
    {
        String question = r.getQuestion();
        String ctx = context.retrieve(question, 5);

        String answer = generator.generateAnswer(question, ctx);

        return answer;
    }
}