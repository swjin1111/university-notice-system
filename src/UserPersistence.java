import java.io.*;
import java.nio.charset.StandardCharsets; //문자 Encoding 담당하는 라이브러리
import java.util.List;

/**
 * 사용자의 설정 정보(이름, 키워드)를 JSON 파일로 저장하고 불러오는 역할을 담당.
 * Main 클래스에서 복잡한 I/O 로직을 분리하기 위해 작성.
 */
public class UserPersistence {

    private static final String FILE_NAME = "user.json"; // 파일의 경로를 지정.

    public static User load() { // json 파일의 데이터를 읽어와서 User객체를 만들고 리턴
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            User defaultUser = new User("기본사용자");
            defaultUser.addKeyword("장학");
            return defaultUser;
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            String content = sb.toString();
            String name = JsonUtil.extractValue(content, "name");
            User user = new User(name != null ? name : "Unknown");

            int keyStart = content.indexOf("\"keywords\": [") + 13;
            int keyEnd = content.indexOf("]", keyStart);
            if (keyStart > 12 && keyEnd > keyStart) {
                String rawKeywords = content.substring(keyStart, keyEnd);
                String[] parts = rawKeywords.split(",");
                for (String p : parts) {
                    String clean = p.replace("\"", "").trim();
                    if (!clean.isEmpty()) {
                        user.addKeyword(clean);
                    }
                }
            }
            return user;
        } catch (IOException e) {
            System.err.println("사용자 정보를 불러오는 중 오류가 발생했습니다: " + e.getMessage());
            return new User("ErrorUser");
        }
    }

    public static void save(User user) { // 실행중에 변경된 user 객체의 정보를 다시 json 파일로 저장
        try (PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(FILE_NAME), StandardCharsets.UTF_8)))) {
            out.println("{");
            out.printf("  \"name\": \"%s\",%n", user.getName());
            out.println("  \"keywords\": [");
            List<String> keywords = user.getSubscribedKeywords();
            for (int i = 0; i < keywords.size(); i++) {
                out.printf("    \"%s\"%s%n", keywords.get(i), (i < keywords.size() - 1 ? "," : ""));
            }
            out.println("  ]");
            out.println("}");
        } catch (IOException e) {
            System.err.println("사용자 정보를 저장하는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
