import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 공지사항을 JSON 파일 형식으로 저장하는 클래스.
 * 라이브러리 없이 구현하기 위해 간단한 문자열 처리를 사용한다.
 */
public class JsonNoticeRepository implements NoticeRepository {

    private final String filePath = "notices.json";
    private List<Notice> cachedNotices;

    public JsonNoticeRepository() {
        this.cachedNotices = loadFromFile();
    }

    @Override
    public void save(Notice notice) {
        if (!exists(notice)) {
            cachedNotices.add(notice);
            // 매번 commit() 하지 않고 메모리에만 유지
        }
    }

    @Override
    public void saveAll(List<Notice> notices) {
        for (Notice notice : notices) {
            if (!exists(notice)) cachedNotices.add(notice);
        }
        commit(); // 일괄 저장 후 딱 한 번만 파일 쓰기
    }

    @Override
    public List<Notice> findAll() {
        return new ArrayList<>(cachedNotices);
    }

    @Override
    public boolean exists(Notice notice) {
        return cachedNotices.stream()
                .anyMatch(n -> n.getTitle().equals(notice.getTitle()) && n.getDate().equals(notice.getDate()));
    }

    @Override
    public void commit() {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath)))) {
            out.println("[");
            for (int i = 0; i < cachedNotices.size(); i++) {
                Notice n = cachedNotices.get(i);
                out.println("  {");
                out.printf("    \"title\": \"%s\",%n", escape(n.getTitle()));
                out.printf("    \"category\": \"%s\",%n", escape(n.getCategory()));
                out.printf("    \"source\": \"%s\",%n", escape(n.getSource()));
                out.printf("    \"date\": \"%s\",%n", escape(n.getDate()));
                out.printf("    \"content\": \"%s\"%n", escape(n.getContent()));
                out.print("  }");
                if (i < cachedNotices.size() - 1) out.println(",");
                else out.println();
            }
            out.println("]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearAll() {
        cachedNotices.clear();
        commit();
    }

    private List<Notice> loadFromFile() {
        List<Notice> list = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line.trim());
            
            String content = sb.toString();
            if (content.length() < 10) return list;

            String[] items = content.split("\\},\\{");
            for (String item : items) {
                String title = JsonUtil.extractValue(item, "title");
                String category = JsonUtil.extractValue(item, "category");
                String source = JsonUtil.extractValue(item, "source");
                String date = JsonUtil.extractValue(item, "date");
                String body = JsonUtil.extractValue(item, "content");
                
                if (title != null) {
                    list.add(new Notice(title, body, category, source, date));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private String escape(String raw) {
        if (raw == null) return "";
        return raw.replace("\"", "\\\"").replace("\n", " ");
    }
}
