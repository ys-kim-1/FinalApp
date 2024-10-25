package com.example.finalapp.mapper.user;

import com.example.finalapp.dto.user.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//SpringBootTest : 스프링 컨텍스트를 로드하고 스프링부트 어플리케이션의 모든 설정을 이용해 통합 테스트를 실행할 수 있도록한다
@Transactional
//테스트 메소드에서 수행된 데이터베이스 작업이 테스트가 끝나면 자동으로 롤백되도록 한다
        //테스트에서 삽입한 데이터는 테스트가 끝난 후 실제 데이터베이스에 반영되지 않는다
class UserMapperTest {
    @Autowired
    UserMapper userMapper;
    UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setLoginId("test");
        userDTO.setPassword("1234");
        userDTO.setEmail("test@naver.com");
        userDTO.setGender("M");
        userDTO.setAddress("강남구");
        userDTO.setAddressDetail("123호");
        userDTO.setZipcode("12345");
        userMapper.insertUser(userDTO);
        System.out.println(userDTO);
    }

    @Test
    void selectId() {
        // given
        String loginId = "test";  // 입력 데이터와 일치시킵니다.
        String password = "1234";

        // when
        Long userId = userMapper.selectId(loginId, password).orElseThrow(() -> new AssertionError("User ID not found"));

        // then
        assertThat(userId).isEqualTo(userDTO.getUserId());
    }
}
