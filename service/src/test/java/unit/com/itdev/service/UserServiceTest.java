package unit.com.itdev.service;

import com.itdev.database.dao.repositories.FilterUserRepositoryImpl;
import com.itdev.database.dao.repositories.UserRepository;
import com.itdev.database.entity.User;
import com.itdev.dto.UserFilter;
import com.itdev.dto.UserReadDto;
import com.itdev.mapper.UserCreateEditMapper;
import com.itdev.mapper.UserReadMapper;
import com.itdev.service.ImageService;
import com.itdev.service.UserService;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import unit.com.itdev.util.UnitTestUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserReadMapper userReadMapper;
    private UserCreateEditMapper userCreateEditMapper;
    private UserRepository userRepository;
    private ImageService imageService;
    private UserService service;

    @BeforeEach
    void prepare() {
        userReadMapper = mock(UserReadMapper.class);
        userCreateEditMapper = mock(UserCreateEditMapper.class);
        userRepository = mock(UserRepository.class);
        imageService = mock(ImageService.class);
        service = new UserService(userReadMapper, userCreateEditMapper, userRepository, imageService);
    }

    @Test
    void findAllWithTwoParamDoesReturnRage() {
        //given
        User user = UnitTestUtil.getUser();
        UserReadDto userReadDto = UnitTestUtil.getUserReadDto(null, null);
        UserFilter filter = UserFilter.builder().build();
        Pageable pageable = mock(Pageable.class);
        Page<User> page = new PageImpl<>(List.of(user));
        Page<UserReadDto> expected = new PageImpl<>(List.of(userReadDto));
        Predicate predicate = FilterUserRepositoryImpl.buildPredicate(filter);
        doReturn(page).when(userRepository).findAll(predicate, pageable);
        doReturn(userReadDto).when(userReadMapper).map(user);

        //when
        Page<UserReadDto> actual = service.findAll(filter, pageable);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAllWithTwoParamThrowIfFilterIsNull() {
        //given
        UserFilter filter = null;
        Pageable pageable = mock(Pageable.class);

        //when

        //then
        assertThrows(NullPointerException.class, () -> service.findAll(filter, pageable));
    }

    @Test
    void findAllWithTwoParamDoesNotThrowIfPageableIsNull() {
        //given
        UserFilter filter = UserFilter.builder().build();
        Pageable pageable = null;
        doThrow(NullPointerException.class).when(userRepository).findAll(any(Predicate.class), eq(pageable));

        //when

        //then
        assertThrows(NullPointerException.class, () -> service.findAll(filter, pageable));
    }

    @Test
    void findAllWithoutParamDoesReturnNotEmptyListIfUsersFound() {
        //given
        User user = UnitTestUtil.getUser();
        UserReadDto userReadDto = UnitTestUtil.getUserReadDto(null, null);
        doReturn(List.of(user, new User())).when(userRepository).findAll();
        UserReadDto empty = UnitTestUtil.getEmptyUserReadDto();
        doReturn(empty).when(userReadMapper).map(any(User.class));
        doReturn(userReadDto).when(userReadMapper).map(user);

        //when
        List<UserReadDto> actual = service.findAll();

        //then
        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual).contains(userReadDto)
        );
    }

    @Test
    void findAllWithoutParamDoesReturnEmptyListIfUsersNotFound() {
        //given
        doReturn(new ArrayList<>(0)).when(userRepository).findAll();

        //when
        List<UserReadDto> actual = service.findAll();

        //then
        assertThat(actual).isEmpty();
    }

    @Test
    void findByIdDoesReturnOptionalWithUserReadDtoIfUserFound() {
        //given
        User user = UnitTestUtil.getUser();
        UserReadDto expected = UnitTestUtil.getUserReadDto(null, null);
        doReturn(Optional.of(user)).when(userRepository).findById(user.getId());
        doReturn(expected).when(userReadMapper).map(user);

        //when
        Optional<UserReadDto> actual = service.findById(user.getId());

        //then
        assertThat(actual).isPresent();
        assertThat(actual.get()).isEqualTo(expected);
    }

    @Test
    void findByIdDoesReturnOptionalEmptyIfUserNotFound() {
        //given
        User user = UnitTestUtil.getUser();
        doReturn(Optional.empty()).when(userRepository).findById(user.getId());

        //when
        Optional<UserReadDto> actual = service.findById(user.getId());

        //then
        assertThat(actual).isEmpty();
    }

    @Test
    void findByIdThrowsNullPointerExceptionIfIdIsNull() {
        //given
        doThrow(NullPointerException.class).when(userRepository).findById(null);

        //when

        //then
        assertThrows(NullPointerException.class, () -> service.findById(null));
    }

    @Test
    void findImageDoesReturnOptionalWithImageDataIfUserFoundAndImageExist() {
        //given
        User user = mock(User.class);
        String path = "path";
        doReturn(path).when(user).getImage();
        doReturn(Optional.of(user)).when(userRepository).findById(anyInt());
        byte[] expected = {4, 2};
        doReturn(Optional.of(expected)).when(imageService).download(path);

        //when
        Optional<byte[]> actual = service.findImage(anyInt());

        //then
        assertThat(actual).isPresent();
        assertThat(actual.get()).isEqualTo(expected);
    }

    @Test
    void findImageDoesReturnOptionalEmptyIfImageNotExist() {
        //given
        User user = UnitTestUtil.getUser();
        doReturn(Optional.of(user)).when(userRepository).findById(anyInt());
        doReturn(Optional.empty()).when(imageService).download(null);

        //when
        Optional<UserReadDto> actual = service.findById(user.getId());

        //then
        assertThat(actual).isEmpty();
    }

    @Test
    void findImageDoesReturnOptionalEmptyIfUserNotFound() {
        //given
        User user = UnitTestUtil.getUser();
        doReturn(Optional.empty()).when(userRepository).findById(user.getId());

        //when
        Optional<UserReadDto> actual = service.findById(user.getId());

        //then
        assertThat(actual).isEmpty();
    }

    @Test
    void findImageThrowsNullPointerExceptionIfIdIsNull() {
        //given
        doThrow(NullPointerException.class).when(userRepository).findById(null);

        //when

        //then
        assertThrows(NullPointerException.class, () -> service.findImage(null));
    }
}
