import java.util.ArrayList;
import java.util.List;

/**
 * 알림을 받는 사용자를 표현하는 클래스.
 * 이름과 관심 키워드 목록을 캡슐화한다.
 */
public class User {

    private String name;
    private List<String> subscribedKeywords;

    public User(String name) {
        this.name               = name;
        this.subscribedKeywords = new ArrayList<>();
    }

    /** 관심 키워드 추가 */
    public void addKeyword(String keyword) {
        subscribedKeywords.add(keyword);
    }

    // ── Getters ──────────────────────────────────────────────────────────────
    public String getName()                       { return name; }
    public List<String> getSubscribedKeywords()   { return subscribedKeywords; }
}
