import java.util.List;

/**
 * 공지를 제공하는 소스(출처)가 반드시 구현해야 하는 인터페이스.
 *
 * - 새로운 공지 소스(예: 도서관, 취업센터 등)가 생겨도
 *   이 인터페이스를 구현하기만 하면 시스템에 바로 연결할 수 있다.
 * - 즉, NoticeManager는 구체적인 소스 클래스를 몰라도 된다. (OCP 원칙)
 */
public interface NoticeSource {
    List<Notice> fetchNotices();
}
