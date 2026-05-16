import java.util.List;

public interface NoticeRepository {
    void saveAll(List<Notice> notices);
    List<Notice> findAll();
    boolean exists(Notice notice);
    void commit();
    void clearAll();
}
