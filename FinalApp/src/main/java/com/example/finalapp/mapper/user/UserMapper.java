package com.example.finalapp.mapper.user;

import com.example.finalapp.dto.user.UserDTO;
import com.example.finalapp.dto.user.UserSessionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserMapper {
    void insertUser(UserDTO userDTO);

//    Optional<T>
//    옵셔널 클래스는 NPE를 방어하기 위해 사용한다
    // 참조변수에 들어있는 값이 NULL인 경우 접근 연산자(.)를 이용하여 메소드나 필드에 접근하면 NPE가 발생하므로 프로그램이 강제 종료될수있다
    // if문을 이용하여 해당 값이 NULL인지 검사하게 되는데 NULL이 나올 수 있는 경우의 수가 너무 많기 때문에 개발자가 피곤해진다
    // Optional 타입을 사용하면 NULL체크를 보다 간결하고 안전에게 코드를 작성할 수 있도록 도와준다

    Optional<Long> selectId(@Param("loginId") String loginId, @Param("password") String password);

    Optional<UserSessionDTO> selectLoginInfo(@Param("loginId") String loginId, @Param("password") String password);

}
