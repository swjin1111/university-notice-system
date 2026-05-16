import java.util.ArrayList;
import java.util.List;

public class NoticeFilter {

    public List<Notice> filterByKeyword(List<Notice> notices, List<String> keywords) {
        List<Notice> result = new ArrayList<>();

        for (Notice notice : notices) {
            if (matchesAnyKeyword(notice, keywords)) {
                result.add(notice);
            }
        }
        return result;
    }

    private boolean matchesAnyKeyword(Notice notice, List<String> keywords) {
        for (String keyword : keywords) {
            if (notice.getTitle().contains(keyword) || notice.getContent().contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}
