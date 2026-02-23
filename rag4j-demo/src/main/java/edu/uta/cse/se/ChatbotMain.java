package edu.uta.cse.se;

import java.util.Map;
import java.util.Scanner;

public class ChatbotMain
{
    public static void main(String[] args) throws Exception
    {
        // Load catalog + vector store
        CourseCatalogLoader.Loaded loaded = CourseCatalogLoader.load();
        Map<String, CourseRecord> catalog = loaded.catalog;

        // Default strategy (TopN)
        Context context = new Context(new TopNStrategy(loaded.store));

        // Chain: Title -> ID -> AI
        TitleHandler titleHandler = new TitleHandler(catalog);
        IDHandler idHandler = new IDHandler(catalog);
        AIHandler aiHandler = new AIHandler(context);

        titleHandler.create(idHandler);
        idHandler.create(aiHandler);

        ChatbotController controller = new ChatbotController(context, titleHandler);
        ChatbotGUI gui = new ChatbotGUI(controller);

        Scanner sc = new Scanner(System.in);

        System.out.println("UC1 Course Chatbot (type 'exit' to quit)");

        while (true)
        {
            System.out.println();
            System.out.println("Pick retrieval strategy:");
            System.out.println("1 = TopN, 2 = Window, 3 = Document, 4 = Hierarchical");
            System.out.print("> ");

            String s = sc.nextLine().trim();
            if (s.equalsIgnoreCase("exit"))
            {
                break;
            }

            int choice = 1;
            try
            {
                choice = Integer.parseInt(s);
            }
            catch (Exception e)
            {
                choice = 1;
            }

            Strategy strat = switch (choice)
            {
                case 2 -> new WindowStrategy(loaded.store);
                case 3 -> new DocumentStrategy(loaded.store);
                case 4 -> new HierarchicalRetrieval(loaded.store);
                default -> new TopNStrategy(loaded.store);
            };

            controller.setStrategy(strat);

            System.out.println("Enter your question (or 'exit'):");
            System.out.print("> ");
            String q = sc.nextLine();

            if (q.equalsIgnoreCase("exit"))
            {
                break;
            }

            gui.submit(q, choice);
        }

        sc.close();
        System.out.println("Bye.");
    }
}