package edu.uta.cse.se;

import java.util.*;
import org.rag4j.indexing.InputDocument;
import org.rag4j.indexing.Splitter;
import org.rag4j.indexing.splitters.SentenceSplitter;
import org.rag4j.integration.ollama.OllamaAccess;
import org.rag4j.integration.ollama.OllamaChatService;
import org.rag4j.integration.ollama.OllamaEmbedder;
import org.rag4j.local.store.InternalContentStore;
import org.rag4j.rag.embedding.Embedder;
import org.rag4j.rag.generation.answer.AnswerGenerator;
import org.rag4j.rag.model.Chunk;
import org.rag4j.rag.retrieval.RetrievalOutput;
import org.rag4j.rag.retrieval.RetrievalStrategy;
//import org.rag4j.rag.retrieval.strategies.DocumentRetrievalStrategy;
//import org.rag4j.rag.retrieval.strategies.HierarchicalRetrievalStrategy;
import org.rag4j.rag.retrieval.strategies.TopNRetrievalStrategy;
//import org.rag4j.rag.retrieval.strategies.WindowRetrievalStrategy;
import org.rag4j.util.resource.JsonlReader;

public class SimpleRagApp {

  public static void main(String[] args) throws Exception {
    // 1. Embedding + vector store
    OllamaAccess ollamaAccess = new OllamaAccess();
    //Embedder embedder = new OllamaEmbedder(ollamaAccess, "mxbai-embed-large");
    Embedder embedder = new OllamaEmbedder(ollamaAccess, "nomic-embed-text");
    //Embedder embedder = new OllamaEmbedder(ollamaAccess);

    InternalContentStore vectorStore = new InternalContentStore(embedder);

    // 2. Splitter
    Splitter splitter = new SentenceSplitter();

    // 3. Read documents
    List<String> properties = List.of("id", "title", "description");
    JsonlReader reader = new JsonlReader(properties, "course-desc.jsonl");

    List<Map<String, String>> records = reader.getLines();

    List<InputDocument> documents =
        records.stream()
            .map(
                map -> {
                  String text =
                      String.join(
                          " ",
                          map.getOrDefault("id", ""),
                          map.getOrDefault("title", ""),
                          map.getOrDefault("description", ""));

                  return InputDocument.builder()
                      .documentId(map.get("id"))
                      .text(text)
                      .properties(
                          Map.of(
                              "documentId", map.get("id"),
                              "title", map.get("title"),
                              "name", map.get("title"),
                              "description", map.get("description"),
                              "content", map.get("description")))
                      .build();
                })
            .toList();

    // 4. Split + index + store
    List<Chunk> allChunks =
        documents.stream().flatMap(doc -> splitter.split(doc).stream()).toList();

    vectorStore.store(allChunks);

    // 5. Retrieval + generation
    RetrievalStrategy retriever = new TopNRetrievalStrategy(vectorStore);
    String question = String.join(" ", args); 
    RetrievalOutput retrievalResult = retriever.retrieve(question, 5);
    String context = retrievalResult.constructContext();
    System.out.println("********* begin context *************");
    System.out.println(context);
    System.out.println("********* end context *************");

    AnswerGenerator answerGenerator = new AnswerGenerator(new OllamaChatService(ollamaAccess));
    String answer = answerGenerator.generateAnswer(question, context);

    System.out.println("Q: " + question);
    System.out.println("A: " + answer);
  }
}
