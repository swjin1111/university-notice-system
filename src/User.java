import java.util.ArrayList;
import java.util.List;

public class User {

    private String name;
    private List<String> subscribedKeywords;

    public User(String name) {
        this.name               = name;
        this.subscribedKeywords = new ArrayList<>();
    }

    public void addKeyword(String keyword) {
        if (!subscribedKeywords.contains(keyword)) {
            subscribedKeywords.add(keyword);
        }
    }

    public void removeKeyword(String keyword) {
        subscribedKeywords.remove(keyword);
    }

    public String getName()                       { return name; }
    public void setName(String name)             { this.name = name; }
    public List<String> getSubscribedKeywords()   { return subscribedKeywords; }
}
