/**
 * 공지사항 하나를 표현하는 클래스.
 * 데이터(속성)만 담당하며, 로직은 포함하지 않는다. (단일 책임 원칙)
 */
public class Notice {

    private String title;
    private String content;
    private String category;
    private String source;
    private String date;

    public Notice(String title, String content, String category, String source, String date) {
        this.title    = title;
        this.content  = content;
        this.category = category;
        this.source   = source;
        this.date     = date;
    }

    // ── Getters ──────────────────────────────────────────────────────────────
    public String getTitle()    { return title; }
    public String getContent()  { return content; }
    public String getCategory() { return category; }
    public String getSource()   { return source; }
    public String getDate()     { return date; }

    @Override
    public String toString() {
        return String.format("[%s] [%s] %s (%s) - %s", source, category, title, date, content);
    }
}
