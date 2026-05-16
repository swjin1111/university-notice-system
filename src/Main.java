import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // 1. 초기 설정
        NoticeRepository repository = new JsonNoticeRepository();
        NoticeManager manager = new NoticeManager(repository);
        manager.addSource(new PortalNoticeSource());

        // 2. 사용자 설정 로드 (외부 파일에서 읽기)
        User user = loadUserFromJson("user.json");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n===== [대학 공지 알림 시스템] =====");
            System.out.println("사용자: " + user.getName());
            System.out.println("키워드: " + user.getSubscribedKeywords());
            System.out.println("-----------------------------------");
            System.out.println("1. 새로운 공지 수집 및 알림 확인");
            System.out.println("2. 저장된 전체 공지 목록 보기");
            System.out.println("3. 종료");
            System.out.print("선택: ");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                manager.run(user);
            } else if (choice.equals("2")) {
                manager.showStoredNotices();
            } else if (choice.equals("3")) {
                System.out.println("시스템을 종료합니다.");
                break;
            } else {
                System.out.println("잘못된 선택입니다. 다시 입력해주세요.");
            }
        }
        scanner.close();
    }

    /** user.json 파일에서 사용자 정보를 읽어오는 간이 메서드 */
    private static User loadUserFromJson(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            User defaultUser = new User("기본사용자");
            defaultUser.addKeyword("장학");
            return defaultUser;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null)
                sb.append(line);

            String content = sb.toString();
            String name = JsonUtil.extractValue(content, "name");
            User user = new User(name != null ? name : "Unknown");
            
            // 키워드 리스트 추출 (단순 파싱)
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
}
