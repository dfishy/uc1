package edu.uta.cse.se;

public class Context
{
    private Strategy strategy;

    public Context(Strategy strategy)
    {
        this.strategy = strategy;
    }

    public void setStrategy(Strategy strategy)
    {
        this.strategy = strategy;
    }

    public String retrieve(String question, int k)
    {
        if (strategy == null)
        {
            return "";
        }
        return strategy.retrieve(question, k);
    }
}