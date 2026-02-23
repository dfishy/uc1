package edu.uta.cse.se;

import org.rag4j.local.store.InternalContentStore;
import org.rag4j.rag.retrieval.RetrievalOutput;
import org.rag4j.rag.retrieval.RetrievalStrategy;
// If available in your rag4j:
// import org.rag4j.rag.retrieval.strategies.HierarchicalRetrievalStrategy;
import org.rag4j.rag.retrieval.strategies.TopNRetrievalStrategy;

public class HierarchicalRetrieval implements Strategy
{
    private final InternalContentStore store;

    public HierarchicalRetrieval(InternalContentStore store)
    {
        this.store = store;
    }

    @Override
    public String retrieve(String question, int k)
    {
        // If your rag4j has HierarchicalRetrievalStrategy, use it:
        // RetrievalStrategy retriever = new HierarchicalRetrievalStrategy(store);

        // Fallback:
        RetrievalStrategy retriever = new TopNRetrievalStrategy(store);

        RetrievalOutput out = retriever.retrieve(question, k);
        return out.constructContext();
    }
}