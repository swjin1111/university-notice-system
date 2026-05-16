import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoticeManager {

    private List<NoticeSource> sources;
    private NoticeFilter filter;
    private NotificationService notifier;
    private NoticeRepository repository;

    public NoticeManager(NoticeRepository repository) {
        this.sources = new ArrayList<>();
        this.filter = new NoticeFilter();
        this.notifier = new NotificationService();
        this.repository = repository;
    }

    public void addSource(NoticeSource source) {
        sources.add(source);
    }

    public void run(User user) {
        cleanupNotices(user);

        List<Notice> allNotices = new ArrayList<>();
        for (NoticeSource source : sources) {
            allNotices.addAll(source.fetchNotices());
        }
        System.out.println("\n[시스템] 총 " + allNotices.size() + "건의 공지를 수집했습니다.");

        List<Notice> keywordMatched = filter.filterByKeyword(allNotices, user.getSubscribedKeywords());

        List<Notice> newNotices = new ArrayList<>();
        for (Notice notice : keywordMatched) {
            if (!repository.exists(notice)) {
                newNotices.add(notice);
            }
        }

        if (!newNotices.isEmpty()) {
            repository.saveAll(newNotices);
            notifier.notifyUser(user, newNotices);
            System.out.println("[시스템] " + newNotices.size() + "개의 새로운 맞춤 공지를 저장하고 알림을 보냈습니다.");
        } else {
            System.out.println("[시스템] 새로운 맞춤 공지가 없습니다.");
        }
        System.out.println("[시스템] 현재 누적 저장된 공지 수: " + repository.findAll().size());
    }

    public void cleanupNotices(User user) {
        List<Notice> stored = repository.findAll();
        List<Notice> toKeep = filter.filterByKeyword(stored, user.getSubscribedKeywords());

        if (stored.size() > toKeep.size()) {
            int removedCount = stored.size() - toKeep.size();
            repository.clearAll();
            repository.saveAll(toKeep);
            System.out.println("[시스템] 키워드 변경으로 인해 불필요한 공지 " + removedCount + "건을 정리했습니다.");
        }
    }

    public List<Notice> getStoredNotices() {
        List<Notice> stored = repository.findAll();
        Collections.sort(stored);
        return stored;
    }
}
