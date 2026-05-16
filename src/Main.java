import java.io.*;

public class Main {

    public static void main(String[] args) {
        NoticeRepository repository = new JsonNoticeRepository(); // JsonNoticeRepository를 통해 공지사항 데이터 관리.
        NoticeManager manager = new NoticeManager(repository); // NoticeManager를 통해 공지사항 수집.
        manager.setSource(new PortalNoticeSource()); // PortalNoticeSource를 통해 공지사항 수집.

        User user = UserPersistence.load(); // UserPersistence를 통해 사용자 정보 로드.

        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            MainFrame frame = new MainFrame(manager, user);
            frame.setVisible(true);
        });
    }
}
