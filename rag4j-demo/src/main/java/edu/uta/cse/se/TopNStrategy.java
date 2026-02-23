package edu.uta.cse.se;

import org.rag4j.local.store.InternalContentStore;
import org.rag4j.rag.retrieval.RetrievalOutput;
import org.rag4j.rag.retrieval.RetrievalStrategy;
import org.rag4j.rag.retrieval.strategies.TopNRetrievalStrategy;

public class TopNStrategy implements Strategy
{
    private final InternalContentStore store;

    public TopNStrategy(InternalContentStore store)
    {
        this.store = store;
    }

    @Override
    public String retrieve(String question, int k)
    {
        RetrievalStrategy retriever = new TopNRetrievalStrategy(store);
        RetrievalOutput out = retriever.retrieve(question, k);
        return out.constructContext();
    }
}