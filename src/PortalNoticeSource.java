import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;

public class PortalNoticeSource implements NoticeSource {

    private static final String URL = "https://www.kw.ac.kr/ko/life/notice.jsp";

    @Override
    public List<Notice> fetchNotices() {
        List<Notice> notices = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(URL)
                    .userAgent("Mozilla/5.0")
                    .maxBodySize(0)
                    .get();

            Elements noticeLinks = doc.select(".board-text a");

            for (Element linkElement : noticeLinks) {
                String fullTitle = linkElement.text().trim();
                String linkHref = linkElement.attr("abs:href");

                String category = "일반";
                String refinedTitle = fullTitle;

                if (fullTitle.contains("[") && fullTitle.contains("]")) {
                    int start = fullTitle.indexOf("[");
                    int end = fullTitle.indexOf("]");
                    category = fullTitle.substring(start + 1, end);
                    refinedTitle = fullTitle.substring(end + 1).trim();
                }
                
                refinedTitle = refinedTitle.replace("신규게시글", "").trim();

                String date = "0000-00-00";
                Element row = linkElement.closest("li");
                if (row != null) {
                    Element infoElement = row.selectFirst(".info");
                    if (infoElement != null) {
                        String infoText = infoElement.text();
                        String[] parts = infoText.split("\\|");
                        for (String part : parts) {
                            if (part.contains("수정일") || part.contains("작성일")) {
                                date = part.replaceAll("[^0-9-]", "").trim();
                            }
                        }
                    } else {
                        date = row.text().replaceAll(".*(\\d{4}-\\d{2}-\\d{2}).*", "$1");
                        if (date.length() > 10) date = "0000-00-00";
                    }
                }

                notices.add(new Notice(
                    refinedTitle,
                    linkHref,
                    category,
                    "광운대학교 공지사항",
                    date
                ));
            }

        } catch (Exception e) {
            System.err.println("PortalNoticeSource 크롤링 중 오류 발생: " + e.getMessage());
        }

        return notices;
    }
}
