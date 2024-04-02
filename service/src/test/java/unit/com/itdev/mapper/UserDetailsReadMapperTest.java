package unit.com.itdev.mapper;

import com.itdev.database.entity.User;
import com.itdev.database.entity.UserDetails;
import com.itdev.dto.UserDetailsReadDto;
import com.itdev.mapper.UserDetailsReadMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unit.com.itdev.util.UnitTestUtil;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsDetailsReadMapperTest {
    
    private UserDetailsReadMapper userDetailsReadMapper;

    @BeforeEach
    void prepare() {
        userDetailsReadMapper = new UserDetailsReadMapper();
    }

    @Test
    void mapDoReturnUserDetailsReadDto() {
        //given
        User user = UnitTestUtil.getUser();
        UserDetails userDetails = UnitTestUtil.getUserDetails(user);
        UserDetailsReadDto expected = UnitTestUtil.getUserDetailsReadDto();

        //when
        UserDetailsReadDto actual = userDetailsReadMapper.map(userDetails);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void mapThrowsNullPointerExceptionIfUserDetailsIsNull() {
        //given

        //when

        //then
        assertThrows(NullPointerException.class, () -> userDetailsReadMapper.map(null));
    }

    @Test
    void mapThrowsNullPointerExceptionIfUserIsNull() {
        //given
        UserDetails userDetails = UnitTestUtil.getUserDetails(null);

        //when

        //then
        assertThrows(NullPointerException.class, () -> userDetailsReadMapper.map(userDetails));
    }
}
