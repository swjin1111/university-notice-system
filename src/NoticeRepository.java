import java.util.List;

/**
 * 공지사항 데이터를 저장하고 관리하는 저장소 인터페이스.
 * 나중에 JSON 파일에서 실제 DB(MySQL, SQLite 등)로 바꿔도 
 * 이 인터페이스를 사용하는 코드는 수정할 필요가 없다. (DIP 원칙)
 */
public interface NoticeRepository {
    /**
     * 공지사항 하나를 저장소에 저장한다.
     */
    void save(Notice notice);

    /**
     * 여러 개의 공지사항을 한 번에 저장한다.
     */
    void saveAll(List<Notice> notices);

    /**
     * 저장된 모든 공지사항을 불러온다.
     */
    List<Notice> findAll();

    /**
     * 해당 공지사항이 이미 저장소에 존재하는지 확인한다. (중복 방지)
     * 주로 제목과 날짜를 기준으로 판단한다.
     */
    boolean exists(Notice notice);
    
    /**
     * 변경사항을 실제 파일이나 DB에 반영한다.
     */
    void commit();

    /**
     * 모든 공지사항을 삭제한다.
     */
    void clearAll();
}
