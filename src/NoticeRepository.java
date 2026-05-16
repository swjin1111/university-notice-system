import java.util.List;
/*
Notice를 파일에 저장/불러오기/유무확인/파일 비우기/저장 반영
JsonNoticeRepository.java에서 이어받아 사용
만약 Sql을 사용한다면 SqlNoticeRepository.java로 만들어서 상속받아 사용할 수 있음
=>이 때 saveAll, commit,findAll, exists,clearAll만 있으면 됨 
그리고 Main에서는 NoticeRepository repository = new SqlNoticeRepository();로 바꾸면 됨
*/

public interface NoticeRepository {
    int saveAll(List<Notice> notices); // 리스트에 있는 공지사항들을 저장하고, 실제 새로 저장된 개수를 반환

    List<Notice> findAll(); // 파일에 저장된 공지사항들을 List<Notice>타입의 리스트에 담아서 반환

    boolean exists(Notice notice); // 파일에 공지사항이 있는지 확인

    void commit(); // 파일에 공지사항 저장

    void clearAll(); // 파일에 공지사항 모두 지우기
}
