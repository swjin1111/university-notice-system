// Json 파일에서 데이터를 추출하는 mothod UserPersistence.java & JsonNoticeRepository.java 에서 사용 
//json에 저장된 내용을 객체로 변환하는 모든 순간에 사용
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
