import java.util.ArrayList;
import java.util.List;

/**
 * 공지사항 목록을 필터링하는 클래스.
 * "어떤 공지를 걸러낼까?"라는 책임만 담당한다.
 */
public class NoticeFilter {

    /**
     * 공지 목록 중 키워드를 포함하는 공지만 골라서 반환한다.
     * 제목 또는 내용에 키워드 중 하나라도 포함되면 통과.
     *
     * @param notices  전체 공지 목록
     * @param keywords 사용자 관심 키워드 목록
     * @return 필터링된 공지 목록
     */
    public List<Notice> filterByKeyword(List<Notice> notices, List<String> keywords) {
        List<Notice> result = new ArrayList<>();

        for (Notice notice : notices) {
            if (matchesAnyKeyword(notice, keywords)) {
                result.add(notice);
            }
        }
        return result;
    }

    /** 공지의 제목 또는 내용에 키워드 중 하나라도 포함되는지 확인 */
    private boolean matchesAnyKeyword(Notice notice, List<String> keywords) {
        for (String keyword : keywords) {
            if (notice.getTitle().contains(keyword) || notice.getContent().contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}
