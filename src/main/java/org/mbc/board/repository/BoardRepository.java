package org.mbc.board.repository;

import org.mbc.board.domain.Board;
import org.mbc.board.repository.search.BoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {  // BoardSearch는 다중검색용.
    // extends JpaRepository<엔티티클래스, pk타입>
    // JpaRepository는 jpa에서 미리 만들어 놓은 인터페이스로 C R U D와 페이징처리, 정렬등이 존재한다.

    // 테스트코드에서 JpaRepository 에 내장되 CRUD 기능, 페이징과 정렬 기법을 테스트했다.
    // boardRepository.save(board) : 데이터베이스에 INSERT 쿼리가 동작
    // boardRepository.findById(bno) : 데이터베이스에 SELECT WHERE 쿼리가 동작
    // boardRepository.deleteById(bno) : bno 가 존재하면 DELETE 쿼리가 동작

    // 쿼리 메서드 (Query Method) : 사용자가 만드는 메서드 명이 쿼리문이 된다.
    // https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
    // 단점 : 실제로 사용하기 위해서는 상당히 길고 복잡한 메서드 명이 되므로, 자주 사용되는 기법은 아니다.
    Page<Board> findByTitleContainingOrderByBnoDesc(String keyword, Pageable pageable);
    // 인터페이스에 구현메서드이므로 실행문이 없다.
    // findby : SELECT
    // Title : 제목 필드 Containing
    // OrderBy : 정렬 진행
    // OrderBy 정렬진행 BnoDesc 번호 내림차순

    // @Query : 쿼리 메서드와 병합해서 사용한다. JPQL
    @Query("select b  from Board b where b.title like concat('%', :keyword, '%')")
    Page<Board> findKeyword(String keyword, Pageable pageable);
    // findKeyword 메서드가 실행되면, 매개변수로 keyword 를 받는다. ( 제목 검색 단어 : WHERE 문 )
    // 쿼리문에 객체가 넘어가야 하므로, Board가 클레스 명이 되어야 한다.
    // SELECT * FROM Board WHERE title LIKE '%keywordr%'
    // 단, JOIN과 같은 복잡한 쿼리를 구현하는데 한계가 있다.
    // 원하는 속성만 추출해 객체배열 처리하거나 DTO로 처리하는 것이 불가능하다.
    // 네이티브 쿼리 속성값을 true로 지정해서 특정 데이터베이트에서 동작하는 SQL 사용이 불가능하다.

    // 네이티브 쿼리 (Native Query) : 진짜 쿼리문을 사용하는 기법
    @Query(value = "select sysdate() from dual", nativeQuery = true)  // 실제 쿼리문으로 동작하게 만든다. nativeQuery = true
    String getTime();

}
