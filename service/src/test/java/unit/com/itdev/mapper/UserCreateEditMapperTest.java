package unit.com.itdev.mapper;

import com.itdev.database.entity.User;
import com.itdev.dto.UserCreateEditDto;
import com.itdev.mapper.UserCreateEditMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import unit.com.itdev.util.UnitTestUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserCreateEditMapperTest {

    private PasswordEncoder passwordEncoder;
    private UserCreateEditMapper mapper;

    @BeforeEach
    void prepare() {
        passwordEncoder = mock(PasswordEncoder.class);
        mapper = new UserCreateEditMapper(passwordEncoder);
    }

    @Test
    void mapWithOneParamDoReturnUserIfUserCreateEditDtoIsNotNull() {
        //given
        User expected = UnitTestUtil.getUser();
        UserCreateEditDto createUser = UnitTestUtil.getCreateUser(null, null);
        doReturn(expected.getPassword()).when(passwordEncoder).encode(createUser.getRawPassword());

        //when
        User actual = mapper.map(createUser);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void mapWithOneParamThrowsNullPointerExceptionIfUserCreateEditDtoIsNull() {
        //given
        UserCreateEditDto createUser = null;

        //when

        //then
        assertThrows(NullPointerException.class, () -> mapper.map(createUser));
    }

    @Test
    void mapWithTwoParamsDoReturnUserIfUserCreateEditDtoAndUserAreNotNull() {
        //given
        User expected = UnitTestUtil.getUser();
        User empty = User.builder().build();
        UserCreateEditDto createUser = UnitTestUtil.getCreateUser(null, null);
        doReturn(expected.getPassword()).when(passwordEncoder).encode(createUser.getRawPassword());

        //when
        User actual = mapper.map(createUser, empty);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void mapWithTwoParamsThrowsNullPointerExceptionIfUserCreateEditDtoIsNull() {
        //given
        UserCreateEditDto createUser = null;
        User empty = User.builder().build();

        //when

        //then
        assertThrows(NullPointerException.class, () -> mapper.map(createUser, empty));
    }

    @Test
    void mapWithTwoParamsThrowsNullPointerExceptionIfUserIsNull() {
        //given
        UserCreateEditDto createUser = UnitTestUtil.getCreateUser(null, null);
        User empty = null;

        //when

        //then
        assertThrows(NullPointerException.class, () -> mapper.map(createUser));
    }
}
