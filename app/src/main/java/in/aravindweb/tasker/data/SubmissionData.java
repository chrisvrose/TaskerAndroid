package in.aravindweb.tasker.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class SubmissionData {

    /**
     * An array of sample (placeholder) items.
     */
    public static final List<SubmissionItem> ITEMS = new ArrayList<SubmissionItem>();



    private static void addItem(SubmissionItem item) {
        ITEMS.add(item);
    }




    /**
     * A placeholder item representing a piece of content.
     */
    public static class SubmissionItem {
        public final String id;
        public final String createdAt;
        public String studentId;
        public String resourceId;

        public SubmissionItem(String id, String createdAt, String studentId, String resourceId, String submission, String description) {
            this.id = id;
            this.createdAt = createdAt;
            this.studentId = studentId;
            this.resourceId = resourceId;
            this.submission = submission;
            this.description = description;
        }

        public String submission;
        public final String description;

//        public SubmissionItem(String id, String createdAt, String description) {
//            this.id = id;
//            this.createdAt = createdAt;
//            this.description = description;
//        }

        @Override
        public String toString() {
            return createdAt;
        }
    }
}