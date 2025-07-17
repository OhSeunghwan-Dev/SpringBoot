package org.mbc.board.service;

import org.mbc.board.dto.BoardDTO;

public interface BoardService {

    Long register(BoardDTO boardDTO);   // 프론트에서 폼을 통해 내용이 DTO로 들어온다. bno가 반환된다.

    BoardDTO readOne(Long bno); // 프론트에서 bno가 넘어오면 객체가 리턴된다.

    void modify(BoardDTO boardDTO); // 프론트에서 DTO가 넘어오면 값을 수정한다.
    
    void remove(Long bno);      // 프론트에서 bno가 넘어오면 삭제 작업 진행

}
