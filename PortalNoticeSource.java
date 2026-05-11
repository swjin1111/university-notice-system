import java.util.Arrays;
import java.util.List;

/**
 * 학교 포털 사이트의 공지사항을 제공하는 클래스.
 * 실제 환경에서는 HTTP 요청으로 데이터를 가져오지만,
 * 여기서는 하드코딩된 샘플 데이터를 반환한다.
 */
public class PortalNoticeSource implements NoticeSource {

    @Override
    public List<Notice> fetchNotices() {
        return Arrays.asList(
            new Notice(
                "2025-1학기 수강신청 일정 안내",
                "수강신청은 2025년 2월 17일(월) 오전 9시부터 시작됩니다.",
                "학사",
                "학교 포털",
                "2025-01-20"
            ),
            new Notice(
                "장학금 신청 기간 공지",
                "교내 장학금 신청을 희망하는 학생은 2월 말까지 포털에서 신청하세요.",
                "장학",
                "학교 포털",
                "2025-01-22"
            ),
            new Notice(
                "도서관 열람실 이용 시간 변경",
                "겨울방학 기간 중 도서관 운영 시간이 오전 9시~오후 6시로 변경됩니다.",
                "시설",
                "학교 포털",
                "2025-01-23"
            )
        );
    }
}
