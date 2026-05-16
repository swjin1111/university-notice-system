import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonNoticeRepository implements NoticeRepository {

    private final String filePath = "notices.json";
    private List<Notice> cachedNotices;

    public JsonNoticeRepository() {
        this.cachedNotices = loadFromFile();
    }

    @Override
    public void saveAll(List<Notice> notices) {
        for (Notice notice : notices) {
            if (!exists(notice)) cachedNotices.add(notice);
        }
        commit();
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
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), java.nio.charset.StandardCharsets.UTF_8)))) {
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

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), java.nio.charset.StandardCharsets.UTF_8))) {
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
