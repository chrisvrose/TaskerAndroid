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
public class StudentData {

    /**
     * An array of sample (placeholder) items.
     */
    public static final List<StudentItem> ITEMS = new ArrayList<StudentItem>();

    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static final Map<String, StudentItem> ITEM_MAP = new HashMap<String, StudentItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createPlaceholderItem(i));
        }
    }

    private static void addItem(StudentItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static StudentItem createPlaceholderItem(int position) {
        return new StudentItem(String.valueOf(position), "Item " + position, makeDetails(position));
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
    public static class StudentItem {
        public final String id;
        public final String email;
        public final String name;

        public StudentItem(String id, String email, String name) {
            this.id = id;
            this.email = email;
            this.name = name;
        }

        @Override
        public String toString() {
            return email;
        }
    }
}