import java.util.ArrayList;
import java.util.List;

public class NoticeFilter {

    public static List<Notice> filterByKeyword(List<Notice> notices, List<String> keywords) {
        List<Notice> result = new ArrayList<>();

        for (Notice notice : notices) {
            if (matchesAnyKeyword(notice, keywords)) {
                result.add(notice);
            }
        }
        return result;
    }

    private static boolean matchesAnyKeyword(Notice notice, List<String> keywords) {
        String combined = (notice.getTitle() + " " + notice.getContent()).toLowerCase();
        for (String keyword : keywords) {
            String target = keyword.trim().toLowerCase();
            if (!target.isEmpty() && combined.contains(target)) {
                return true;
            }
        }
        return false;
    }
}
