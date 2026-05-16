import java.util.List;

public class NotificationService {

    public void notifyUser(User user, List<Notice> newNotices) {
        if (newNotices.isEmpty()) return;

        System.out.println("============================================================");
        System.out.println("🔔  " + user.getName() + "님의 맞춤 공지 알림");
        System.out.println("   관심 키워드: " + user.getSubscribedKeywords());
        System.out.println("============================================================");
        System.out.println("  총 " + newNotices.size() + "건의 공지가 있습니다.\n");

        for (int i = 0; i < newNotices.size(); i++) {
            Notice n = newNotices.get(i);
            System.out.printf("  [%d] %s\n", i + 1, n.getTitle());
            System.out.printf("      출처: %s | 분류: %s | 날짜: %s\n", n.getSource(), n.getCategory(), n.getDate());
            System.out.printf("      내용: %s\n\n", n.getContent());
        }
        System.out.println("============================================================");
    }
}
