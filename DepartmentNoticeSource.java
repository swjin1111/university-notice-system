import java.util.Arrays;
import java.util.List;

/**
 * 학과 홈페이지의 공지사항을 제공하는 클래스.
 */
public class DepartmentNoticeSource implements NoticeSource {

    @Override
    public List<Notice> fetchNotices() {
        return Arrays.asList(
            new Notice(
                "소프트웨어공학 강의 보강 안내",
                "1월 30일(목) 소프트웨어공학 강의가 보강 예정입니다. 장소: 공학관 302호",
                "강의",
                "학과 홈페이지",
                "2025-01-25"
            ),
            new Notice(
                "졸업논문 제출 마감 공지",
                "2025년 2월 졸업 예정자는 논문을 1월 31일까지 제출해야 합니다.",
                "학사",
                "학과 홈페이지",
                "2025-01-24"
            ),
            new Notice(
                "캡스톤 디자인 팀 모집",
                "이번 학기 캡스톤 디자인 프로젝트 참여 팀을 모집합니다. 관심 있는 학생은 신청하세요.",
                "프로젝트",
                "학과 홈페이지",
                "2025-01-26"
            )
        );
    }
}
