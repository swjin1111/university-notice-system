public class Notice implements Comparable<Notice> {
    /*
     * 공지사항을 담아서 넘겨주기 위한 Class, 이 객체를 자료형처럼 넘기고 넘겨받음
     * 
     * 필드:
     * title: 공지사항 제목
     * content: 공지사항 내용
     * category: 공지사항 카테고리
     * source: 공지사항 출처
     * date: 공지사항 날짜
     */

    private String title;
    private String content;
    private String category;
    private String source;
    private String date;

    public Notice(String title, String content, String category, String source, String date) { // 생성자
        this.title = title;
        this.content = content;
        this.category = category;
        this.source = source;
        this.date = date;
    }

    // getters

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getCategory() {
        return category;
    }

    public String getSource() {
        return source;
    }

    public String getDate() {
        return date;
    }

    @Override
    public int compareTo(Notice other) { // 최신 날짜가 먼저 오도록 객체 간의 순서를 정렬해주는 method
        return other.date.compareTo(this.date);
    }
}
