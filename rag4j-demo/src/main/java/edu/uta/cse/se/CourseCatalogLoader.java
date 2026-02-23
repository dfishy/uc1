package edu.uta.cse.se;

import java.util.*;
import org.rag4j.indexing.InputDocument;
import org.rag4j.indexing.Splitter;
import org.rag4j.indexing.splitters.SentenceSplitter;
import org.rag4j.integration.ollama.OllamaAccess;
import org.rag4j.integration.ollama.OllamaEmbedder;
import org.rag4j.local.store.InternalContentStore;
import org.rag4j.rag.embedding.Embedder;
import org.rag4j.rag.model.Chunk;
import org.rag4j.util.resource.JsonlReader;

public class CourseCatalogLoader
{
    public static class Loaded
    {
        public final Map<String, CourseRecord> catalog;
        public final InternalContentStore store;

        public Loaded(Map<String, CourseRecord> catalog, InternalContentStore store)
        {
            this.catalog = catalog;
            this.store = store;
        }
    }

    public static Loaded load()
    {
        // Embedding + store
        OllamaAccess ollamaAccess = new OllamaAccess();
        Embedder embedder = new OllamaEmbedder(ollamaAccess, "nomic-embed-text");
        InternalContentStore store = new InternalContentStore(embedder);

        // Read jsonl
        List<String> properties = List.of("id", "title", "description");
        JsonlReader reader = new JsonlReader(properties, "course-desc.jsonl");
        List<Map<String, String>> records = reader.getLines();

        Map<String, CourseRecord> catalog = new HashMap<>();

        List<InputDocument> documents =
            records.stream()
                .map(map ->
                {
                    String id = map.getOrDefault("id", "");
                    String title = map.getOrDefault("title", "");
                    String desc = map.getOrDefault("description", "");

                    catalog.put(id, new CourseRecord(id, title, desc));

                    String text = String.join(" ", id, title, desc);

                    return InputDocument.builder()
                        .documentId(id)
                        .text(text)
                        .properties(
                            Map.of(
                                "documentId", id,
                                "title", title,
                                "name", title,
                                "description", desc,
                                "content", desc))
                        .build();
                })
                .toList();

        Splitter splitter = new SentenceSplitter();
        List<Chunk> allChunks = documents.stream().flatMap(d -> splitter.split(d).stream()).toList();
        store.store(allChunks);

        return new Loaded(catalog, store);
    }
}
