package com.example.finalapp.mapper.reply;

import com.example.finalapp.dto.board.BoardListDTO;
import com.example.finalapp.dto.board.BoardWriteDTO;
import com.example.finalapp.dto.page.Criteria;
import com.example.finalapp.mapper.board.BoardMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ReplyMapperTest {

    @Autowired
    private BoardMapper boardMapper;

    private final int TEST_DATA_COUNT = 10;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 삽입
        for (int i = 1; i <= TEST_DATA_COUNT; i++) {
            BoardWriteDTO boardWriteDTO = new BoardWriteDTO();
            boardWriteDTO.setTitle("Test Title " + i);
            boardWriteDTO.setContent("Test Content " + i);
            boardWriteDTO.setUserId(1L);
            boardMapper.insertBoard(boardWriteDTO);
        }
    }

    @AfterEach
    void tearDown() {
        // 모든 데이터 삭제
        for (int i = 1; i <= TEST_DATA_COUNT; i++) {
            boardMapper.deleteBoard((long) i);
        }
    }

    @Test
    void selectAllPage() {
        // given: 테스트 상황 설정 단계
        Criteria criteria = new Criteria();
        criteria.setPage(1);
        criteria.setAmount(10);

        // when: 테스트 실행 단계
        List<BoardListDTO> list = boardMapper.selectAllPage(criteria);

        // then: 검증 단계
        assertThat(list).hasSize(TEST_DATA_COUNT); // 예상한 결과: 리스트 크기가 10이어야 함
    }
}