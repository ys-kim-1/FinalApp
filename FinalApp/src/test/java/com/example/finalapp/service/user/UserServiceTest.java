package com.example.finalapp.service.user;

import static org.junit.jupiter.api.Assertions.*;

import com.example.finalapp.dto.user.UserDTO;
import com.example.finalapp.mapper.user.UserMapper;
import com.example.finalapp.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//Mockito가 확장되어 JUnit5에서 동작할 수 있게 설정하는 것입니다. 이를 통해 Mockito가 @Mock, @InjectMocks 등의 어노테이션을 사용하여 자동으로 모의 객체를 생성하고 주입할 수 있게 합니다.
class UserServiceTest {
    @Mock
    UserMapper userMapper;
    //UserMapper 인터페이스의 모의(Mock) 객체를 생성합니다. 이 모의 객체는 실제 구현 대신에 테스트에서 사용할 수 있습니다.

    @InjectMocks
//    // @InjectMocks: Mockito를 사용하여 의존성 주입을 통해 userMapper 모의 객체를 UserService에 주입
    UserService userService;
    //UserService 객체를 생성하며, 이 객체에 @Mock으로 선언된 userMapper를 주입합니다. 이를 통해 userService의 UserMapper 의존성을 주입한 상태에서 테스트를 수행할 수 있습니다.

    @DisplayName("유저 등록")
    @Test
    //registerUser 메서드를 테스트하는 메서드
    void registerUser() {
        // given
        doNothing().when(userMapper).insertUser(any());
//userMapper의 insertUser 메서드가 호출되었을 때 아무 동작도 하지 않도록 설정합니다. any()는 이 메서드가 어떤 인자를 받든 상관없다는 의미입니다. 즉, insertUser 메서드가 실제 동작하는 대신 Mockito에 의해 모의 처리됩니다.

        // when
        userService.registerUser(new UserDTO());
//userService의 registerUser 메서드를 호출합니다. registerUser는 내부적으로 userMapper.insertUser()를 호출할 것입니다.

        // then
        verify(userMapper, times(1)).insertUser(any());
//userMapper의 insertUser 메서드가 정확히 한 번 호출되었는지 확인하는 검증 코드입니다. times(1)은 메서드가 1번 호출되어야 테스트가 성공한다는 것을 의미합니다. any()는 메서드가 어떤 인자를 받았는지 상관없이 호출 여부만 확인합니다.
    }

    @DisplayName("유저 단건 조회")
    @Test
//@Test: JUnit의 어노테이션으로, 이 메서드가 테스트 메서드임을 나타냅니다. JUnit 프레임워크가 이 메서드를 실행하여 UserService의 findId 메서드가 제대로 동작하는지 검증합니다.
    void findId() {
// Mockito를 사용하여 userMapper의 selectId 메서드의 동작을 모의(Mock)한 후, userService.findId 메서드가 정상적으로 사용자 ID를 반환하는지 검증하는 것입니다.
//userMapper.selectId가 호출되면 Optional.of(1L)을 반환하도록 설정되어 있으며,
//userService.findId("asdf", "asdf")를 호출한 결과가 1L이어야 테스트가 성공합니다.

        // given
        doReturn(Optional.of(1L)).when(userMapper).selectId(any(), any());
       //doReturn(Optional.of(1L)): userMapper의 selectId 메서드가 호출되었을 때, 항상 Optional.of(1L) 값을 반환하도록 지정합니다. 이는 실제 데이터베이스와 상호작용하는 대신 모의 동작을 정의한 것입니다.
//        when(userMapper).selectId(any(), any()): userMapper의 selectId 메서드가 어떤 인자(로그인 ID, 패스워드)를 사용하든 호출되었을 때 doReturn에서 정의한 값 Optional.of(1L)을 반환하게 합니다. any()는 어떤 값이 오더라도 상관없다는 의미로, 인자 값을 구체적으로 지정하지 않습니다.

        // when
        Long userId = userService.findId("asdf", "asdf");
//userService.findId("asdf", "asdf"): 실제 테스트하려는 메서드입니다. "asdf"라는 로그인 ID와 비밀번호를 전달하여 userService의 findId 메서드를 호출합니다.
//이 메서드는 내부적으로 userMapper.selectId를 호출하며, 이미 모의(Mock)된 값으로 인해 Optional.of(1L)을 반환하게 됩니다.
//결과적으로 findId 메서드는 1L을 반환합니다.

        // then
        assertThat(userId).isEqualTo(1L);
//assertThat(userId).isEqualTo(1L): AssertJ 라이브러리를 사용하여 findId 메서드의 반환값인 userId가 기대한 값인 1L과 동일한지 확인합니다.
//isEqualTo(1L): userId가 1L이어야 테스트가 성공합니다. 만약 그렇지 않다면 테스트가 실패합니다.
    }
}