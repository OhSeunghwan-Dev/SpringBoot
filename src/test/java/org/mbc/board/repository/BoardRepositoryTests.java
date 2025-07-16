package org.mbc.board.repository;

import lombok.extern.log4j.Log4j2;
import net.bytebuddy.TypeCache;
import org.junit.jupiter.api.Test;
import org.mbc.board.domain.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest // 메서드용 테스트 동작
@Log4j2 // 로그용
public class BoardRepositoryTests {
    // 영속성 계층에 테스트용

    @Autowired // 생성자 자동 주입
    private BoardRepository boardRepository;

    @Test
    public void testInsert(){
        // 데이터베이스에 데이터 주입(c) 테스트 코드
        IntStream.rangeClosed(1,100).forEach(i -> {
            // i 변수에 1~100까지 100개의 정수를 반복해서 생성
            Board board = Board.builder()
                    .title("제목..."+i)  // board.setTitle()
                    .content("내용..."+i) // board.setContent()
                    .writer("user"+(i%10))  // board.setWriter()
                    .build(); // @Builder 용 (세터 대신 좀더 간단하고 가독성 좋게 )
                // log.info((board));
            Board result = boardRepository.save(board) ; // 데이터베이스에 기록하는 코드
            //                            .save 메서드는 용도
            //                                          이미 값이 있으면 updajpa에서 상속한 메서드로 값을 저장하는 te를 진행한다.
            log.info("게시물 번호 출력 : " + result.getBno() + "게시물의 제목 : " + result.getTitle());

                }// forEach문 종료
        );// IntStream. 종료

    } // testInsert 메서드 종료

    @Test
    public void testSelect(){
        Long bno = 100L;    // 게시물 번호가 100인 객체 확인하기.

        Optional<Board> result = boardRepository.findById(bno); // SELECT * FROM BOARD WHERE bno = bno;
        // Null 값이 나올 경우를 대비한 객체 Optional

        Board board = result.orElseThrow();  // result에 값이 넘어왔다면 대입해라
        log.info(bno + "번 게시물이 데이터베이스에 존재합니다.");
        log.info(board);
    }

    @Test
    public void testUpdate() {
        // 특정 게시물( 100번 )을 조회 후 내용을 수정하고 테스트를 종료한다.
        Long bno = 100L;

        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow(); // result 에 값이 넘어왔다면 board에 대입한다.

        board.change("수정테스트 제목", "수정테스트 내용"); // 제목과 내용만 수정할 수 있는 메서드

        boardRepository.save(board);    // .save 메서드는 데이터베이스에 해당 pk값이 없으면 insert, 있으면 update를 한다.
    }

    @Test
    public void testDelete() {
        Long bno = 1L;

        boardRepository.deleteById(bno);
    }

    @Test
    public void testPaging(){
        // .findAll() : 모든 값을 리스트로 출력하는 메서드 SELECT * FROM BOARD;
        // 전체 리스트에 페이징과 정렬 기법을 추가한다.
        
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
        // 시작번호 : 0, 페이지당 게시물 : 10, 번호를 기준으로 내림차순 정렬
        // 192.168.111.105/board/list?page=0

        Page<Board> result = boardRepository.findAll(pageable);

        log.info("전체 페이지 수 : " + result.getTotalElements());
        log.info("총 페이지 수 : " + result.getTotalPages());
        log.info("현재 페이지 번호 : " + result.getNumber());
        log.info("페이지의 크기 : " + result.getSize());
        log.info("다음 페이지 여부 : " + result.hasNext());
        log.info("시작 페이지 여부 : " + result.isFirst());

        // 콘솔에 결과를 출력하기
        List<Board> boardList = result.getContent();    // 페이징 처리가 된 내용을 boardList 에 담는다.
        boardList.forEach(board -> log.info(board));
        // forEach 는 인덱스를 사용하지 않고 앞에서부터 객체를 반환한다.
        // board -> log.info(board)
        // 람다식 1개의 명령어가 있을 때 활용한다.
    }



} // 클래스 종료
