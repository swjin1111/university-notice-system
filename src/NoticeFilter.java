import java.util.ArrayList;
import java.util.List;

/*
사용자가 설정한 키워드가 포함된 공지사항만 필터링해서 리스트로 반환
[그냥 긁어온 notice 리스트] & [사용자가 설정한 키워드 리스트] 이렇게 두개 받아서 [필터링 된 리스트] 반환
그냥 함수라고 생각하면 됨. 그래서 Static으로 구현
*/

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
        for (String keyword : keywords) {
            if (notice.getTitle().contains(keyword) || notice.getContent().contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}
