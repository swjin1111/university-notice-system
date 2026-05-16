import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 시스템의 전체 흐름을 조율하는 클래스. (Orchestrator)
 */
public class NoticeManager {

    private List<NoticeSource> sources; // 공지 소스 목록
    private NoticeFilter filter; // 필터링 담당
    private NotificationService notifier; // 알림 담당
    private NoticeRepository repository; // 저장소 담당 (DB/JSON)

    public NoticeManager(NoticeRepository repository) {
        this.sources = new ArrayList<>();
        this.filter = new NoticeFilter();
        this.notifier = new NotificationService();
        this.repository = repository;
    }

    /** 공지 소스를 추가한다. */
    public void addSource(NoticeSource source) {
        sources.add(source);
    }

    /** 공지 소스 리스트를 반환한다. */
    public List<NoticeSource> getSources() {
        return sources;
    }

    /**
     * 전체 프로세스를 실행한다:
     * 수집 -> 키워드 필터링 -> 중복 체크 -> 저장 -> 알림
     */
    public void run(User user) {
        // 0단계: 기존 저장된 공지 중 현재 키워드와 안 맞는 것 정리
        cleanupNotices(user);

        // 1단계: 모든 소스에서 실시간 공지 수집
        List<Notice> allNotices = new ArrayList<>();
        for (NoticeSource source : sources) {
            allNotices.addAll(source.fetchNotices());
        }
        System.out.println("\n[시스템] 총 " + allNotices.size() + "건의 공지를 수집했습니다.");

        // 2단계: 사용자 키워드로 필터링
        List<Notice> keywordMatched = filter.filterByKeyword(allNotices, user.getSubscribedKeywords());

        // 3단계: 저장소에 없는 "새로운" 공지만 선별
        List<Notice> newNotices = new ArrayList<>();
        for (Notice notice : keywordMatched) {
            if (!repository.exists(notice)) {
                newNotices.add(notice);
            }
        }

        // 4단계: 일괄 저장 및 알림
        if (!newNotices.isEmpty()) {
            repository.saveAll(newNotices); // 일괄 저장 (파일 한 번만 쓰기)
            notifier.notifyUser(user, newNotices);
            System.out.println("[시스템] " + newNotices.size() + "개의 새로운 맞춤 공지를 저장하고 알림을 보냈습니다.");
        } else {
            System.out.println("[시스템] 새로운 맞춤 공지가 없습니다.");
        }
        System.out.println("[시스템] 현재 누적 저장된 공지 수: " + repository.findAll().size());
    }

    /**
     * 현재 키워드와 매칭되지 않는 과거 공지들을 저장소에서 삭제한다.
     */
    public void cleanupNotices(User user) {
        List<Notice> stored = repository.findAll();
        List<Notice> toKeep = filter.filterByKeyword(stored, user.getSubscribedKeywords());

        if (stored.size() > toKeep.size()) {
            int removedCount = stored.size() - toKeep.size();
            repository.clearAll(); // 전체 삭제 후
            repository.saveAll(toKeep); // 필요한 것만 일괄 저장
            System.out.println("[시스템] 키워드 변경으로 인해 불필요한 공지 " + removedCount + "건을 정리했습니다.");
        }
    }

    /**
     * 현재 저장소에 보관된 모든 공지사항을 콘솔에 출력한다.
     */
    public void showStoredNotices() {
        List<Notice> stored = repository.findAll();
        if (stored.isEmpty()) {
            System.out.println("\n[안내] 저장된 공지사항이 없습니다.");
            return;
        }

        // 최신순 정렬 적용
        Collections.sort(stored);

        System.out.println("\n===== [저장된 공지사항 리스트 (최신순)] =====");
        System.out.printf("%-3s | %-10s | %s\n", "번호", "날짜", "제목");
        System.out.println("--------------------------------------------------");

        for (int i = 0; i < stored.size(); i++) {
            Notice n = stored.get(i);
            System.out.printf("[%2d] | %-10s | %s\n", i + 1, n.getDate(), n.getTitle());
        }
        System.out.println("==================================================");
    }
}
