import java.util.ArrayList;
import java.util.Collections; //  Notice 객체들의 compareTo 메서드를 기반으로 자동으로 정렬해줌
import java.util.List;

/*
NoticeManager는 전체적인 공지사항 수집, 정제, 알림 기능을 관장.
notice.json을 관리하는 핵심 엔진 기능 
*/

public class NoticeManager {

    private PortalNoticeSource source; // 공지사항을 가져올 소스 (단일 소스로 변경됨)
    private NoticeRepository repository; // 공지사항들을 저장할 repository

    public NoticeManager(NoticeRepository repository) { // 생성자.
        this.repository = repository;
    }

    public void setSource(PortalNoticeSource source) { // 단일 소스 설정
        this.source = source;
    }

    /*
     * run정리
     * 공지사항 수집 method. [F5누르거나 새로고침 눌렀을 때, 설정으로 키워드 변경했을 때, 자동 업데이트 등]
     * MainFrame.java에서 int newCount에서 사용
     */
    public int run(User user) {
        cleanupNotices(user);

        List<Notice> allNotices = source.fetchNotices();
        List<Notice> keywordMatched = NoticeFilter.filterByKeyword(allNotices, user.getSubscribedKeywords());

        // 저장소에 저장을 요청하고, 새로 저장된 개수를 반환받음
        return repository.saveAll(keywordMatched);
    }

    /*
     * CleanupNotices 정리.
     * 키워드 변화 등을 토대로 공지사항 정제 [1.run(공지 수집) 2.사용자의 관심 키워드 변경] 이 두가지 경우에 사용
     * run(user) 내부
     * MainFrame.java의 설정 창 (저장 버튼 클릭 시)
     */
    public void cleanupNotices(User user) {
        List<Notice> stored = repository.findAll();
        List<Notice> toKeep = NoticeFilter.filterByKeyword(stored, user.getSubscribedKeywords());

        if (stored.size() > toKeep.size()) {
            int removedCount = stored.size() - toKeep.size();
            repository.clearAll();
            repository.saveAll(toKeep);
        }
    }

    /*
     * getStoredNotices 정리
     * 저장된 공지사항들을 불러와서 리스트로 반환하는 method
     * MainFrame.java에서 공지사항 리스트 테이블에 뿌릴 때 사용
     * 
     * getStoredNotices는 공지사항들을 저장할 repository에서 공지사항들을 불러와서 리스트로 반환하는 method.
     * MainFrame.java에서 공지사항 리스트 테이블에 뿌릴 때 사용.
     */

    public List<Notice> getStoredNotices() {
        List<Notice> stored = repository.findAll();
        Collections.sort(stored);
        return stored;
    }
}
