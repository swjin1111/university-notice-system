/**
 * 대학교 공지사항 통합 알림 시스템 - 진입점
 *
 * 실행 흐름:
 *  1. 사용자(User)를 생성하고 관심 키워드를 등록한다.
 *  2. NoticeManager에 공지 소스(Source)들을 추가한다.
 *  3. NoticeManager.run()을 호출하면 수집 → 필터링 → 알림이 자동으로 수행된다.
 */
public class Main {

    public static void main(String[] args) {

        // ── 1. 사용자 생성 및 관심 키워드 등록 ──────────────────────────────
        User user1 = new User("김창하");
        user1.addKeyword("장학금");
        user1.addKeyword("프로그래밍");
        user1.addKeyword("캡스톤");

        User user2 = new User("이수진");
        user2.addKeyword("취업");
        user2.addKeyword("수강신청");
        user2.addKeyword("스터디");

        // ── 2. NoticeManager 생성 및 소스 등록 ─────────────────────────────
        NoticeManager manager = new NoticeManager();
        manager.addSource(new PortalNoticeSource());
        manager.addSource(new DepartmentNoticeSource());
        manager.addSource(new ActivityNoticeSource());

        // ── 3. 각 사용자에게 맞춤 알림 실행 ───────────────────────────────
        manager.run(user1);
        System.out.println();
        manager.run(user2);
    }
}
