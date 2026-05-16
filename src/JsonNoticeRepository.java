import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonNoticeRepository implements NoticeRepository {

    private final String filePath = "notices.json";
    private List<Notice> cachedNotices; // 프로그램이 켜질 때 json에 저장된 공지사항들을 임시로 담아두는 리스트

    public JsonNoticeRepository() {
        this.cachedNotices = loadFromFile();
    }

    @Override
    public int saveAll(List<Notice> notices) { // 입력받은 공지사항들을 임시 메모리에 저장 (만약 있다면 제외)
        int newCount = 0;
        for (Notice notice : notices) {
            if (!exists(notice)) {
                cachedNotices.add(notice);
                newCount++;
            }
        }
        if (newCount > 0) {
            commit();
        }
        return newCount;
    }

    @Override
    public List<Notice> findAll() { // 임시 메모리에 저장된 모든 공지사항을 반환
        return new ArrayList<>(cachedNotices);
    }

    @Override
    public boolean exists(Notice notice) { // 임시 메모리에 공지사항이 있는지 확인 saveAll과 NoticeManager의 run에서 사용(새로 추가된 공지만 볼 때)
        return cachedNotices.stream()
                .anyMatch(n -> n.getTitle().equals(notice.getTitle()) && n.getDate().equals(notice.getDate()));
    }

    @Override
    public void commit() { // json 파일에 내용을 저장하는 메서드
        try (PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath), java.nio.charset.StandardCharsets.UTF_8)))) {
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
                if (i < cachedNotices.size() - 1)
                    out.println(",");
                else
                    out.println();
            }
            out.println("]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearAll() { // 임시 메모리에 있는 내용과 파일에 있는 내용을 모두 지움
        cachedNotices.clear();
        commit();
    }

    /*
     * loadFromFile
     * json에서 내용을 읽어서 임시로 메모리에 담는 메서드
     * 메모리에 담을 때 Notice 객체로 변환
     * 생성자에서 딱 한번 사용
     */

    private List<Notice> loadFromFile() {
        List<Notice> list = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists())
            return list;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), java.nio.charset.StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null)
                sb.append(line.trim());

            String content = sb.toString();
            if (content.length() < 10)
                return list;

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

    private String escape(String raw) {// JSON 파일의 형식이 망가지지 않도록 특수문자를 안전하게 변형해주는 보호 장치, commit에서만 사용
        if (raw == null)
            return "";
        return raw.replace("\"", "\\\"").replace("\n", " ");
    }
}
