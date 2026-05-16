import java.util.List;

/**
 * 사용자에게 알림을 전송하는 클래스.
 * "어떻게 알릴까?"라는 책임만 담당한다.
 *
 * 콘솔 출력 방식으로 구현했지만,
 * 이 클래스만 수정하면 이메일·문자 등 다른 알림 방식으로 쉽게 교체할 수 있다.
 */
public class NotificationService {

    /**
     * 사용자에게 필터링된 공지 목록을 출력(알림)한다.
     *
     * @param user    알림 대상 사용자
     * @param notices 전달할 공지 목록
     */
    public void notifyUser(User user, List<Notice> notices) {
        System.out.println("=".repeat(60));
        System.out.println("📢  " + user.getName() + "님의 맞춤 공지 알림");
        System.out.println("   관심 키워드: " + user.getSubscribedKeywords());
        System.out.println("=".repeat(60));

        if (notices.isEmpty()) {
            System.out.println("  ✅ 새로운 관심 공지가 없습니다.");
        } else {
            System.out.println("  총 " + notices.size() + "건의 공지가 있습니다.\n");
            for (int i = 0; i < notices.size(); i++) {
                Notice n = notices.get(i);
                System.out.println("  [" + (i + 1) + "] " + n.getTitle());
                System.out.println("      출처: " + n.getSource() + " | 분류: " + n.getCategory() + " | 날짜: " + n.getDate());
                System.out.println("      내용: " + n.getContent());
                System.out.println();
            }
        }
        System.out.println("=".repeat(60));
    }
}
