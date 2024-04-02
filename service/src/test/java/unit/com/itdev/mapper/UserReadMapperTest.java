package unit.com.itdev.mapper;

import com.itdev.database.entity.User;
import com.itdev.database.entity.UserAddress;
import com.itdev.database.entity.UserDetails;
import com.itdev.dto.UserAddressReadDto;
import com.itdev.dto.UserDetailsReadDto;
import com.itdev.dto.UserReadDto;
import com.itdev.mapper.UserAddressReadMapper;
import com.itdev.mapper.UserDetailsReadMapper;
import com.itdev.mapper.UserReadMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unit.com.itdev.util.UnitTestUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserReadMapperTest {

    private UserDetailsReadMapper detailsReadMapper;
    private UserAddressReadMapper addressReadMapper;
    private UserReadMapper userReadMapper;

    @BeforeEach
    void prepare() {
        detailsReadMapper = mock(UserDetailsReadMapper.class);
        addressReadMapper = mock(UserAddressReadMapper.class);
        userReadMapper = new UserReadMapper(detailsReadMapper, addressReadMapper);
    }

    @Test
    void mapDoReturnUserReadDtoWithDetailsAndAddress() {
        //given
        User user = UnitTestUtil.getUser();
        UserDetails userDetails = UnitTestUtil.getUserDetails(user);
        UserDetailsReadDto userDetailsReadDto = UnitTestUtil.getUserDetailsReadDto();
        UserAddress userAddress = UnitTestUtil.getUserAddress(user);
        UserAddressReadDto userAddressReadDto = UnitTestUtil.getUserAddressReadDto();
        user.addUserDetails(userDetails);
        user.addUserAddress(userAddress);
        UserReadDto expected = UnitTestUtil.getUserReadDto(userDetailsReadDto, userAddressReadDto);
        doReturn(userDetailsReadDto).when(detailsReadMapper).map(userDetails);
        doReturn(userAddressReadDto).when(addressReadMapper).map(userAddress);

        //when
        UserReadDto actual = userReadMapper.map(user);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void mapDoReturnUserReadDtoWithoutDetailsAndAddress() {
        //given
        User user = UnitTestUtil.getUser();
        UserReadDto expected = UnitTestUtil.getUserReadDto(null, null);

        //when
        UserReadDto actual = userReadMapper.map(user);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void mapThrowsNullPointerExceptionIfUserIsNull() {
        //given

        //when

        //then
        assertThrows(NullPointerException.class, () -> userReadMapper.map(null));
    }
}
