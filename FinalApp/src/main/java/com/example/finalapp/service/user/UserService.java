package com.example.finalapp.service.user;

import com.example.finalapp.dto.user.UserDTO;
import com.example.finalapp.dto.user.UserSessionDTO;
import com.example.finalapp.mapper.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//@Service: 이 클래스가 Spring의 서비스 레이어라는 것을 나타냅니다. 서비스 레이어는 비즈니스 로직을 처리하는 역할을 담당합니다.
@Transactional
//@Transactional: 이 클래스의 모든 메서드는 트랜잭션이 적용됩니다.
// 즉, 하나의 메서드 내에서 여러 데이터베이스 작업이 일어날 경우, 오류가 발생하면 모든 작업이 롤백됩니다.
@RequiredArgsConstructor
//이 어노테이션은 final로 선언된 필드인 userMapper에 대한 생성자를 자동으로 생성해줍니다. 이를 통해 의존성 주입이 가능합니다.
public class UserService {
    private final UserMapper userMapper;
        //userMapper: UserMapper는 MyBatis를 이용해 데이터베이스와 상호작용하는 매퍼 객체입니다.

    //registerUser메소드 : 새로운 사용자를 데이터베이스에 등록하는 기능
    public void registerUser(UserDTO userDTO){
        userMapper.insertUser(userDTO);
    }
    //파라미터로 전달받은 UserDTO 객체는 사용자 정보를 담고 있으며, 이를 매퍼의 insertUser 메서드를 통해 데이터베이스에 삽입합니다.

    //findId메소드 : 로그인 ID와 비밀번호를 기준으로 사용자의 ID(Primary Key)를 찾는 기능
    public Long findId(String loginId, String password) {
        //userMapper.selectId(loginId, password)는 데이터베이스에서 해당하는 사용자의 ID를 검색합니다. 반환값은 Optional<Long> 형태입니다.
        return userMapper.selectId(loginId, password)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원 정보"));
        //만약 사용자를 찾지 못하면 orElseThrow()를 통해 IllegalStateException을 던지며, 예외 메시지는 "존재하지 않는 회원 정보"입니다.
    }

    //findLoginInfo메소드 : 로그인에 성공했을 때, 사용자 세션 정보를 반환하는 기능
    public UserSessionDTO findLoginInfo(String loginId, String password) {
        return userMapper.selectLoginInfo(loginId, password)
                //userMapper.selectLoginInfo(loginId, password)는 로그인 ID와 비밀번호를 기준으로 UserSessionDTO 객체를 반환합니다. 이 객체는 세션에서 사용할 사용자 정보를 담고 있습니다.
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원 정보"));
        //사용자를 찾지 못하면 IllegalStateException을 던지며, 예외 메시지는 "존재하지 않는 회원 정보"입니다.
    }
}
