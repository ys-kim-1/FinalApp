package com.example.finalapp.mapper.board;

import com.example.finalapp.dto.board.BoardListDTO; // BoardListDTO 임포트
import com.example.finalapp.dto.board.BoardUpdateDTO; // BoardUpdateDTO 임포트
import com.example.finalapp.dto.board.BoardViewDTO; // BoardViewDTO 임포트
import com.example.finalapp.dto.board.BoardWriteDTO; // BoardWriteDTO 임포트
import com.example.finalapp.dto.page.Criteria; // Criteria 임포트
import com.example.finalapp.dto.user.UserDTO; // UserDTO 임포트
import com.example.finalapp.mapper.board.BoardMapper; // BoardMapper 인터페이스 임포트
import com.example.finalapp.mapper.user.UserMapper; // UserMapper 인터페이스 임포트
import org.junit.jupiter.api.BeforeEach; // 테스트 실행 전 설정을 위해 사용
import org.junit.jupiter.api.Test; // JUnit 테스트 메서드 어노테이션
import org.springframework.beans.factory.annotation.Autowired; // 스프링 빈 자동 주입을 위해 사용
import org.springframework.boot.test.context.SpringBootTest; // Spring Boot 테스트 어노테이션
import org.springframework.transaction.annotation.Transactional; // 트랜잭션 관리를 위해 사용

import java.util.List; // List 인터페이스 임포트

import static org.assertj.core.api.Assertions.assertThat; // AssertJ Assertions 임포트
import static org.junit.jupiter.api.Assertions.fail; // JUnit fail 메서드 임포트

@SpringBootTest // Spring Boot 테스트 컨텍스트 설정
@Transactional // 테스트 메서드가 트랜잭션으로 실행되어 롤백되게 함
class BoardMapperTest {

    @Autowired
    BoardMapper boardMapper;

    @Autowired
    UserMapper userMapper;

    BoardWriteDTO boardWriteDTO;
    UserDTO userDTO;

    @BeforeEach // 각 테스트 메서드 실행 전 실행
    void setUp() {
        // 사용자 데이터 설정
        userDTO = new UserDTO();
        userDTO.setLoginId("test");
        userDTO.setPassword("1234");
        userDTO.setEmail("test@naver.com");
        userDTO.setGender("M");
        userDTO.setAddress("강남구");
        userDTO.setAddressDetail("123호");
        userDTO.setZipcode("12345");

        // 사용자 데이터 삽입
        userMapper.insertUser(userDTO);

        // 게시글 데이터를 설정
        boardWriteDTO = new BoardWriteDTO();
        boardWriteDTO.setTitle("test title");
        boardWriteDTO.setContent("test content");
        boardWriteDTO.setUserId(userDTO.getUserId());

        // 게시글 데이터 삽입
        boardMapper.insertBoard(boardWriteDTO);

    }

    @Test // 게시글 삭제 테스트
    void deleteBoard() {
        // given: 테스트 상황 설정 단계
        // when: 테스트 실행 단계
        boardMapper.deleteBoard(boardWriteDTO.getBoardId());
        BoardViewDTO boardViewDTO = boardMapper.selectById(boardWriteDTO.getBoardId()).orElse(null);

        // then: 검증 단계
        assertThat(boardViewDTO).isNull();
    }

    @Test // 게시글 삭제 테스트 (다른 방식)
    void deleteBoard2() {
        // given: 테스트 상황 설정 단계
        // when: 테스트 실행 단계
        boardMapper.deleteBoard(boardWriteDTO.getBoardId());

        // then: 검증 단계
        boardMapper.selectById(boardWriteDTO.getBoardId())
                .ifPresentOrElse((dto) -> fail("삭제 실패"), () -> {});
    }

    @Test // 게시글 수정 테스트
    void updateBoard() {
        // given: 테스트 상황 설정 단계
        BoardUpdateDTO boardUpdateDTO = new BoardUpdateDTO();
        boardUpdateDTO.setTitle("update title");
        boardUpdateDTO.setContent("update content");
        boardUpdateDTO.setBoardId(boardWriteDTO.getBoardId());

        // when: 테스트 실행 단계
        boardMapper.updateBoard(boardUpdateDTO);
        BoardViewDTO boardViewDTO = boardMapper.selectById(boardUpdateDTO.getBoardId()).orElse(null);

        // then: 검증 단계
        assertThat(boardViewDTO) // 검증 대상 설정
                .isNotNull() // null이 아닌지 검사
                .extracting("title") // 검증 대상의 특정 필드를 가져옴
                .isEqualTo(boardUpdateDTO.getTitle()); // 수정된 dto에 들어있는 title과 일치하는지 검사
    }

    @Test // 모든 게시글 조회 테스트
    void selectAll() {
        // given: 테스트 상황 설정 단계
        // when: 테스트 실행 단계
        List<BoardListDTO> boardList = boardMapper.selectAll();

        // then: 검증 단계
        assertThat(boardList) // 검증 대상 설정
                .isNotEmpty() // 비어있지 않은지 검사
                .extracting("title") // 리스트에 담긴 dto들의 title 필드만 가져오기
                .contains(boardWriteDTO.getTitle()); // 여러 title 중에 특정 값이 포함되었는지 검사
    }


    @Test // 페이지 기준으로 게시글 조회 테스트
    void selectAllPage() {
        // given: 테스트 상황 설정 단계
        Criteria criteria = new Criteria();
        criteria.setPage(1);
        criteria.setAmount(3);

        // when: 테스트 실행 단계
        List<BoardListDTO> list = boardMapper.selectAllPage(criteria);

        // then: 검증 단계
        assertThat(list).hasSize(3); // 리스트 크기가 3이어야하고 DB상의 데이터 개수보다 작아야 테스트 성공
    }
}