package unit.com.itdev.mapper;

import com.itdev.database.dao.repositories.UserRepository;
import com.itdev.database.entity.User;
import com.itdev.database.entity.UserDetails;
import com.itdev.dto.UserDetailsCreateEditDto;
import com.itdev.mapper.UserDetailsCreateEditMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unit.com.itdev.util.UnitTestUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

class UserDetailsCreateEditMapperTest {

    private UserRepository userRepository;
    private UserDetailsCreateEditMapper mapper;
    private User user;

    @BeforeEach
    void prepare() {
        userRepository = mock(UserRepository.class);
        mapper = new UserDetailsCreateEditMapper(userRepository);
        user = UnitTestUtil.getUser();
    }

    @Test
    void mapWithOneParamDoReturnUserDetailsIfUserDetailsCreateEditDtoIsNotNull() {
        //given
        UserDetails expected = UnitTestUtil.getUserDetails(user);
        expected.setId(null);
        UserDetailsCreateEditDto createUserDetails = UnitTestUtil.getCreateUserDetails();
        doReturn(Optional.of(user)).when(userRepository).findById(createUserDetails.getUserId());

        //when
        UserDetails actual = mapper.map(createUserDetails);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void mapWithOneParamDoReturnEmptyUserDetailsIfUserNotFound() {
        //given
        UserDetails expected = UserDetails.builder().build();
        UserDetailsCreateEditDto createUserDetails = UnitTestUtil.getCreateUserDetails();
        doReturn(Optional.empty()).when(userRepository).findById(createUserDetails.getUserId());

        //when
        UserDetails actual = mapper.map(createUserDetails);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void mapWithOneParamThrowsNullPointerExceptionIfUserDetailsCreateEditDtoIsNull() {
        //given
        UserDetailsCreateEditDto createUserDetails = null;

        //when

        //then
        assertThrows(NullPointerException.class, () -> mapper.map(createUserDetails));
    }

    @Test
    void mapWithTwoParamsDoReturnUserDetailsIfUserDetailsCreateEditDtoAndUserDetailsAreNotNull() {
        //given
        UserDetails empty = UserDetails.builder().build();
        UserDetails expected = UnitTestUtil.getUserDetails(user);
        expected.setId(null);
        UserDetailsCreateEditDto createUserDetails = UnitTestUtil.getCreateUserDetails();
        doReturn(Optional.of(user)).when(userRepository).findById(createUserDetails.getUserId());

        //when
        UserDetails actual = mapper.map(createUserDetails, empty);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void mapWithTwoParamDoReturnEmptyUserDetailsIfUserNotFound() {
        //given
        UserDetails empty = UserDetails.builder().build();
        UserDetails expected = UserDetails.builder().build();
        UserDetailsCreateEditDto createUserDetails = UnitTestUtil.getCreateUserDetails();
        doReturn(Optional.empty()).when(userRepository).findById(createUserDetails.getUserId());

        //when
        UserDetails actual = mapper.map(createUserDetails, empty);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void mapWithTwoParamsThrowsNullPointerExceptionIfUserDetailsCreateEditDtoIsNull() {
        //given
        UserDetailsCreateEditDto createUserDetails = null;
        UserDetails empty = UserDetails.builder().build();

        //when

        //then
        assertThrows(NullPointerException.class, () -> mapper.map(createUserDetails, empty));
    }

    @Test
    void mapWithTwoParamsThrowsNullPointerExceptionIfUserDetailsIsNullAndUserFound() {
        //given
        UserDetailsCreateEditDto createUserDetails = UnitTestUtil.getCreateUserDetails();
        doReturn(Optional.of(user)).when(userRepository).findById(createUserDetails.getUserId());

        //when

        //then
        assertThrows(NullPointerException.class, () -> mapper.map(createUserDetails, null));
    }
}
