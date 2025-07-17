package org.mbc.board.repository.search;

import org.mbc.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {
    // 게시물의 검색 조건을 다중화하는 기능을 가진다.

    Page<Board> search1(Pageable pageable);
    // 인터페이스의 추상 메서드로 실제 동작은 구현체에서 작동한다.

    // 실제로 구현할 코드 (다중 조건으로 제목, 내용, 작성자를 키워드와 매칭하여 SELECT 처리한다.
    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);
    // 각각 제목.내용.작성자, 검색어, 페이징 처리용(정렬, 검색, 페이지번호)

}
