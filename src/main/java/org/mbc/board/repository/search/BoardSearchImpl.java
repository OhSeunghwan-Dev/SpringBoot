package org.mbc.board.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.mbc.board.domain.Board;
import org.mbc.board.domain.QBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> search1(Pageable pageable) {
        // 쿼리DSL로 다중검색용 코드 구현
        // 쿼리DSL의 목적은 타입 기반으로 코드를 이용한다 : Q도메인 클래스

        QBoard board = QBoard.board;    // Q도메인 객체

        JPQLQuery<Board> query = from(board);   // SELECT * FROM board

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.or(board.title.contains("11"));
        booleanBuilder.or(board.content.contains("11"));    // ( WHERE title LIKE 11 OR content LIKE 11 )
        // 다중조건일 때, 연산자 공식에 의해 특수기호가 먼저 계산될 때가 있다.
        // ()를 사용하면 괄호 내 연산이 선행되는데 BooleanBuilder가 그 역할을 한다.
        query.where(booleanBuilder);    //
        query.where(board.bno.gt(0L));  // pk를 이용한 빠른 검색
        // WHERE ( title LIKE 11 OR content LIKE0 11) AND bno > 0

        query.where(board.title.contains("1")); // WHERE title LIKE 1
        // SELECT * FROM board WHERE title LIKE 1

        List<Board> list = query.fetch();   // 쿼리문 처리 후 리스트에 담는다.
        long count = query.fetchCount();

        return null;
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;    // Querydsl 객체 생성
        JPQLQuery<Board> query = from(board);   // SELECT * FROM board

        // 프론트에서 검색폼에 검색어 keyword가 비었을 경우도 존재한다.
        if ((types != null && types.length > 0) && keyword != null) {
            // 제목,내용,이름 값이 존재하며 검색어가 null이 아닐 경우
            BooleanBuilder booleanBuilder = new BooleanBuilder();   // 선실행용 괄호()

            for (String type : types) {
                switch (type) {
                    case "t" :  // 제목일 경우
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c" :  // 내용일 경우
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w" :  // 작성자일 경우
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }   // 프론트에서 넘어오는 String[]의 type 을 파악하고 쿼리문 적용
            }
            query.where(booleanBuilder);    // 위에서 만든 조건을 적용 WHERE title OR content OR writer
        }
        query.where(board.bno.gt(0L));  // pk 활용해서 인덱싱 처리용 코드
        // WHERE (title OR content OR writer) AND bno > 0L

        this.getQuerydsl().applyPagination(pageable, query);    // 페이징처리용 코드 + 쿼리문

        // Page<t> 클래스는 3가지의 리턴 타입을 만들어 준다.
        List<Board> list = query.fetch();   // 쿼리문 실행
        long count = query.fetchCount();    // 검색된 게시물 수

        return new PageImpl<>(list, pageable, count);
        // 검색된 boardlist, 페이징처리용 pageable, 검색된 게시물 수 반환
    }
}
