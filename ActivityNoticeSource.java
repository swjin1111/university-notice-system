import java.util.Arrays;
import java.util.List;

/**
 * 비교과 활동 사이트의 공지사항을 제공하는 클래스.
 */
public class ActivityNoticeSource implements NoticeSource {

    @Override
    public List<Notice> fetchNotices() {
        return Arrays.asList(
            new Notice(
                "교내 프로그래밍 경진대회 참가자 모집",
                "2월 15일 진행되는 교내 프로그래밍 대회 참가 신청을 받습니다. 개인 또는 2인 팀 참가 가능.",
                "대회",
                "비교과 사이트",
                "2025-01-27"
            ),
            new Notice(
                "취업 특강: 대기업 SW 직무 면접 준비",
                "현직 개발자가 직접 알려주는 코딩 테스트 및 기술 면접 준비 특강을 개최합니다.",
                "취업",
                "비교과 사이트",
                "2025-01-28"
            ),
            new Notice(
                "영어 말하기 스터디 그룹 모집",
                "매주 화요일 저녁 7시, 학생 회관 세미나실에서 진행되는 영어 스터디에 참여하세요.",
                "스터디",
                "비교과 사이트",
                "2025-01-29"
            )
        );
    }
}
