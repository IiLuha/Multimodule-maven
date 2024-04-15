package unit.com.itdev.service;

import com.itdev.database.dao.repositories.FilterUserRepositoryImpl;
import com.itdev.database.dao.repositories.UserRepository;
import com.itdev.database.entity.User;
import com.itdev.dto.UserCreateEditDto;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import unit.com.itdev.util.UnitTestUtil;

import java.util.ArrayList;
import java.util.Collections;
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

    @Test
    void createDoesReturnUserReadDto() {
        //given
        UserCreateEditDto cUser = UnitTestUtil.getCreateUser(null, null);
        UserReadDto expected = UnitTestUtil.getUserReadDto(null, null);
        User userBeforeSave = UnitTestUtil.getUser();
        User userAfterSave = UnitTestUtil.getUser();
        userBeforeSave.setId(null);
        doReturn(userBeforeSave).when(userCreateEditMapper).map(cUser);
        doReturn(userAfterSave).when(userRepository).save(userBeforeSave);
        doReturn(expected).when(userReadMapper).map(userAfterSave);

        //when
        UserReadDto actual = service.create(cUser);

        //then
        verify(userCreateEditMapper).map(cUser);
        verify(userRepository).save(userBeforeSave);
        verify(userReadMapper).map(userAfterSave);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void createThrowsNullPointerExceptionIfUserCreateEditDtoIsNull() {
        //given

        //when

        //then
        verifyNoInteractions(userCreateEditMapper);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(userReadMapper);
        assertThrows(NullPointerException.class, () -> service.create(null));
    }

    @Test
    void updateDoesReturnOptionalWithUserReadDtoIfUserFound() {
        //given
        User userBeforeChanges = UnitTestUtil.getUser();
        userBeforeChanges.setEmail("happened-before");
        User userAfterChanges = UnitTestUtil.getUser();
        Integer id = userBeforeChanges.getId();
        UserCreateEditDto cUser = UnitTestUtil.getCreateUser(null, null);
        UserReadDto expected = UnitTestUtil.getUserReadDto(null, null);
        doReturn(Optional.of(userBeforeChanges)).when(userRepository).findById(id);
        doReturn(expected).when(userReadMapper).map(userAfterChanges);
        doReturn(userAfterChanges).when(userCreateEditMapper).map(cUser, userBeforeChanges);
        doReturn(userAfterChanges).when(userRepository).saveAndFlush(userAfterChanges);

        //when
        Optional<UserReadDto> actual = service.update(id, cUser);

        //then
        assertThat(actual).isPresent();
        assertThat(actual.get()).isEqualTo(expected);
    }

    @Test
    void updateDoesReturnOptionalEmptyIfUserNotFound() {
        //given
        UserCreateEditDto cUser = UnitTestUtil.getCreateUser(null, null);
        Integer id = 1;
        doReturn(Optional.empty()).when(userRepository).findById(anyInt());

        //when
        Optional<UserReadDto> actual = service.update(id, cUser);

        //then
        assertThat(actual).isEmpty();
    }

    @Test
    void updateThrowsNullPointerExceptionIfIdIsNull() {
        //given
        UserCreateEditDto cUser = UnitTestUtil.getCreateUser(null, null);
        doThrow(NullPointerException.class).when(userRepository).findById(null);

        //when

        //then
        assertThrows(NullPointerException.class, () -> service.update(null, cUser));
    }

    @Test
    void updateThrowsNullPointerExceptionIfUserCreateEditDtoIsNull() {
        //given
        User user = UnitTestUtil.getUser();
        doReturn(Optional.of(user)).when(userRepository).findById(anyInt());
        doThrow(NullPointerException.class).when(userCreateEditMapper).map(null, user);

        //when

        //then
        assertThrows(NullPointerException.class, () -> service.update(1, null));
    }

    @Test
    void deleteDoesReturnTrueIfUserFound() {
        //given
        User user = UnitTestUtil.getUser();
        Integer id = user.getId();
        doReturn(Optional.of(user)).when(userRepository).findById(id);

        //when
        boolean actual = service.delete(id);

        //then
        verify(userRepository).findById(id);
        verify(userRepository).delete(user);
        verify(userRepository).flush();
        assertTrue(actual);
    }

    @Test
    void deleteDoesReturnFalseIfUserNotFound() {
        //given
        Integer id = 1;
        doReturn(Optional.empty()).when(userRepository).findById(anyInt());

        //when
        boolean actual = service.delete(id);

        //then
        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
        assertFalse(actual);
    }

    @Test
    void deleteThrowsNullPointerExceptionIfIdIsNull() {
        //given
        doThrow(NullPointerException.class).when(userRepository).findById(null);

        //when

        //then
        assertThrows(NullPointerException.class, () -> service.delete(null));
    }

    @Test
    void loadUserByUsernameDoesReturnUserDetailsIfUserFound() {
        //given
        User user = UnitTestUtil.getUser();
        String email = user.getEmail();
        org.springframework.security.core.userdetails.User expected = new org.springframework.security.core.userdetails.User(
                email,
                user.getPassword(),
                Collections.singleton(user.getRole()));
        doReturn(Optional.of(user)).when(userRepository).findByEmail(email);

        //when
        UserDetails actual = service.loadUserByUsername(email);

        //then
        verify(userRepository).findByEmail(email);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void loadUserByUsernameThrowsUsernameNotFoundExceptionIfUserNotFound() {
        //given
        doReturn(Optional.empty()).when(userRepository).findByEmail(anyString());

        //when

        //then
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(null));
    }

    @Test
    void loadUserByUsernameThrowsNullPointerExceptionIfIdIsNull() {
        //given
        doThrow(NullPointerException.class).when(userRepository).findByEmail(null);

        //when

        //then
        assertThrows(NullPointerException.class, () -> service.loadUserByUsername(null));
    }
}
