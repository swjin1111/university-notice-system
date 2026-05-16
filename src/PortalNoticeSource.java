import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;

/**
 * 학교 포털 사이트의 공지사항을 실시간으로 크롤링하여 제공하는 클래스.
 */
public class PortalNoticeSource implements NoticeSource {

    private static final String URL = "https://www.kw.ac.kr/ko/life/notice.jsp";

    @Override
    public List<Notice> fetchNotices() {
        List<Notice> notices = new ArrayList<>();

        try {
            // 1. 페이지 데이터 가져오기
            Document doc = Jsoup.connect(URL)
                    .userAgent("Mozilla/5.0")
                    .maxBodySize(0)
                    .get();

            // 2. 검증된 선택자인 ".board-text a"를 사용하여 공지사항 리스트를 가져옵니다.
            Elements noticeLinks = doc.select(".board-text a");

            for (Element linkElement : noticeLinks) {
                String fullTitle = linkElement.text().trim();
                String linkHref = linkElement.attr("abs:href");

                // [1] 카테고리 추출 및 제목 정제
                String category = "일반";
                String refinedTitle = fullTitle;

                if (fullTitle.contains("[") && fullTitle.contains("]")) {
                    int start = fullTitle.indexOf("[");
                    int end = fullTitle.indexOf("]");
                    category = fullTitle.substring(start + 1, end);
                    refinedTitle = fullTitle.substring(end + 1).trim();
                }
                
                // "신규게시글" 등 노이즈 제거
                refinedTitle = refinedTitle.replace("신규게시글", "").trim();

                // [2] 날짜 추출: 부모 li 태그 내의 .info 클래스 분석
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
                        // fallback: 텍스트에서 날짜 패턴 직접 추출
                        date = row.text().replaceAll(".*(\\d{4}-\\d{2}-\\d{2}).*", "$1");
                        if (date.length() > 10) date = "0000-00-00";
                    }
                }

                // [3] Notice 객체 생성 및 리스트 추가
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
