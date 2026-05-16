public class Notice implements Comparable<Notice> {

    private String title;
    private String content;
    private String category;
    private String source;
    private String date;

    public Notice(String title, String content, String category, String source, String date) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.source = source;
        this.date = date;
    }

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getCategory() { return category; }
    public String getSource() { return source; }
    public String getDate() { return date; }

    @Override
    public int compareTo(Notice other) {
        return other.date.compareTo(this.date);
    }
}
