import java.io.*;

public class Main {

    public static void main(String[] args) {
        NoticeRepository repository = new JsonNoticeRepository();
        NoticeManager manager = new NoticeManager(repository);
        manager.addSource(new PortalNoticeSource());

        User user = loadUserFromJson("user.json");

        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            MainFrame frame = new MainFrame(manager, user);
            frame.setVisible(true);
        });
    }

    public static User loadUserFromJson(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            User defaultUser = new User("기본사용자");
            defaultUser.addKeyword("장학");
            return defaultUser;
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), java.nio.charset.StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null)
                sb.append(line);

            String content = sb.toString();
            String name = JsonUtil.extractValue(content, "name");
            User user = new User(name != null ? name : "Unknown");

            int keyStart = content.indexOf("\"keywords\": [") + 13;
            int keyEnd = content.indexOf("]", keyStart);
            if (keyStart > 12 && keyEnd > keyStart) {
                String rawKeywords = content.substring(keyStart, keyEnd);
                String[] parts = rawKeywords.split(",");
                for (String p : parts) {
                    user.addKeyword(p.replace("\"", "").trim());
                }
            }
            return user;
        } catch (IOException e) {
            return new User("ErrorUser");
        }
    }

    public static void saveUserToJson(User user, String fileName) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), java.nio.charset.StandardCharsets.UTF_8)))) {
            out.println("{");
            out.printf("  \"name\": \"%s\",%n", user.getName());
            out.println("  \"keywords\": [");
            java.util.List<String> keywords = user.getSubscribedKeywords();
            for (int i = 0; i < keywords.size(); i++) {
                out.printf("    \"%s\"%s%n", keywords.get(i), (i < keywords.size() - 1 ? "," : ""));
            }
            out.println("  ]");
            out.println("}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
