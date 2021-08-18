package in.aravindweb.tasker.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class AssignmentData {

    /**
     * An array of sample (placeholder) items.
     */
    public static final List<AssignmentItem> ITEMS = new ArrayList<AssignmentItem>();

    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static final Map<String, AssignmentItem> ITEM_MAP = new HashMap<String, AssignmentItem>();

    private static final int COUNT = 25;

    private static void addItem(AssignmentItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A placeholder item representing a piece of content.
     */
    public static class AssignmentItem {
        public final String id;
        public final String createdAt;
        public final String deadline;
        
        public final String resource;
        public final String description;

        public AssignmentItem(String id, String createdAt, String deadline,String resource, String description) {
            this.id = id;
            this.createdAt = createdAt;
            this.deadline = deadline;
            this.resource = resource;
            this.description = description;
        }

        @Override
        public String toString() {
            return resource;
        }
    }
}