package events;

public final class EventPipelineApp {
    public static void main(String[] args) throws Exception {
        int events = 300;
        int consumers = 6;
        int capacity = 64;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--events" -> events = Integer.parseInt(args[++i]);
                case "--consumers" -> consumers = Integer.parseInt(args[++i]);
                case "--capacity" -> capacity = Integer.parseInt(args[++i]);
                case "-h", "--help" -> {
                    System.out.println("""
                            Usage: EventPipelineApp [--events N] [--consumers N] [--capacity N]
                            Producer → BlockingQueue → consumers → in-memory repository.
                            """);
                    return;
                }
                default -> throw new IllegalArgumentException("Unknown arg: " + args[i]);
            }
        }

        try (EventPipeline pipeline = new EventPipeline(capacity, consumers)) {
            System.out.printf("Starting pipeline: events=%d consumers=%d capacity=%d%n",
                    events, consumers, capacity);
            pipeline.run(events);
        }
    }

    private EventPipelineApp() {}
}
