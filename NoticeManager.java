import java.util.ArrayList;
import java.util.List;

/**
 * 시스템의 전체 흐름을 조율하는 클래스.
 *
 * ① 여러 NoticeSource에서 공지 수집
 * ② NoticeFilter로 사용자 관심사 필터링
 * ③ NotificationService로 알림 전송
 *
 * 각 역할을 전담 클래스에 위임하고, 이 클래스는 순서만 관리한다. (조율자 역할)
 */
public class NoticeManager {

    private List<NoticeSource>      sources;     // 공지 소스 목록
    private NoticeFilter            filter;      // 필터링 담당
    private NotificationService     notifier;    // 알림 담당

    public NoticeManager() {
        this.sources  = new ArrayList<>();
        this.filter   = new NoticeFilter();
        this.notifier = new NotificationService();
    }

    /** 공지 소스를 추가한다. */
    public void addSource(NoticeSource source) {
        sources.add(source);
    }

    /**
     * 모든 소스에서 공지를 수집한 뒤,
     * 사용자 키워드로 필터링하고 알림을 전송한다.
     */
    public void run(User user) {
        // 1단계: 모든 소스에서 공지 수집
        List<Notice> allNotices = new ArrayList<>();
        for (NoticeSource source : sources) {
            allNotices.addAll(source.fetchNotices());
        }

        System.out.println("\n[시스템] 전체 " + allNotices.size() + "건의 공지를 수집했습니다.");

        // 2단계: 사용자 키워드로 필터링
        List<Notice> filtered = filter.filterByKeyword(allNotices, user.getSubscribedKeywords());

        // 3단계: 알림 전송
        notifier.notifyUser(user, filtered);
    }
}
