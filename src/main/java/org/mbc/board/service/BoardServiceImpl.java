package org.mbc.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mbc.board.domain.Board;
import org.mbc.board.dto.BoardDTO;
import org.mbc.board.repository.BoardRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor    // 필드 값을 보고 생성자를 만든다. final 필드나 @NonNull이 붙은 필드용
@Transactional  // commit 용 ( 여러개의 테이블이 조합될 때 해결하는 역할 )
public class BoardServiceImpl implements BoardService {

    private final ModelMapper modelMapper;  // 엔티티와 DTO를 변환
    private final BoardRepository boardRepository;     // JPA용 클레스( CRUD, 페이징, 정렬, 다중검색 )

    @Override
    public Long register(BoardDTO boardDTO) {
        // 폼에서 넘어온 DTO가 데이터베이스에 기록되어야 한다.
        Board board = modelMapper.map(boardDTO, Board.class);   // 엔티티가 dto로 변환된다.

        Long bno = boardRepository.save(board).getBno();

        return bno;
    }

    @Override
    public BoardDTO readOne(Long bno) {

        Optional<Board> result = boardRepository.findById(bno);
        // SELECT * FROM board WHERE bno = bno
        // Optional : null 값을 받아도 예외처리를 하지 않는다.

        Board board = result.orElseThrow();

        BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);
        // 모델 메퍼를 이용해서 엔티티로 나온 board 를 dto로 변환한다.

        return boardDTO;
    }

    @Override
    public void modify(BoardDTO boardDTO) {

        Optional<Board> result = boardRepository.findById(boardDTO.getBno());
        // SELECT * FROM board WHERE bno = bno -> 엔티티로 나온다.

        Board board = result.orElseThrow(); // result가 null이 아니라면 결과를 엔티티로 저장
        board.change(boardDTO.getTitle(), boardDTO.getContent());   // 제목과 내용이 수정된다.
        boardRepository.save(board);    // DB에 해당 pk가 존재하면 UPDATE, 없다면 INSERT
    }

    @Override
    public void remove(Long bno) {
        boardRepository.deleteById(bno);    // DELETE FROM board WHERE bno = bno
    }
}
