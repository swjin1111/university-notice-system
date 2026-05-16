public class JsonUtil {
    public static String extractValue(String content, String key) {
        try {
            String pattern = "\"" + key + "\": \"";
            int start = content.indexOf(pattern) + pattern.length();
            int end = content.indexOf("\"", start);
            return content.substring(start, end);
        } catch (Exception e) {
            return null;
        }
    }
}
