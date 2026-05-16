import java.util.ArrayList;
import java.util.List;
/* 
기본적으로 자료형 느낌으로 사용됨. 
사용자의 이름 + 구독키워드 리스트를 필드로 가짐.
*/

public class User {

    private String name;
    private List<String> subscribedKeywords; // 구독 키워드 리스트. arrayList를 사용해 동적으로 관리

    public User(String name) { // user 생성자
        this.name = name;
        this.subscribedKeywords = new ArrayList<>();
    }

    public void addKeyword(String keyword) { // 구독 키워드 리스트에 키워드 추가
        if (!subscribedKeywords.contains(keyword)) {
            subscribedKeywords.add(keyword);
        }
    }

    public void removeKeyword(String keyword) { // 구독 키워드 리스트에서 키워드 삭제
        subscribedKeywords.remove(keyword);
    }

    // 이 아래는 getter 및 setter들.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSubscribedKeywords() {
        return subscribedKeywords;
    }
}
