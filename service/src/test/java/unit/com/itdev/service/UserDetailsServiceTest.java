package unit.com.itdev.service;

import com.itdev.database.dao.repositories.UserDetailsRepository;
import com.itdev.database.entity.User;
import com.itdev.database.entity.UserDetails;
import com.itdev.dto.UserDetailsCreateEditDto;
import com.itdev.dto.UserDetailsReadDto;
import com.itdev.mapper.UserDetailsCreateEditMapper;
import com.itdev.mapper.UserDetailsReadMapper;
import com.itdev.service.UserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unit.com.itdev.util.UnitTestUtil;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class UserDetailsServiceTest {

    private UserDetailsReadMapper readMapper;
    private UserDetailsCreateEditMapper createEditMapper;
    private UserDetailsRepository repository;
    private UserDetailsService service;

    @BeforeEach
    void prepare() {
        readMapper = mock(UserDetailsReadMapper.class);
        createEditMapper = mock(UserDetailsCreateEditMapper.class);
        repository = mock(UserDetailsRepository.class);
        service = new UserDetailsService(readMapper, createEditMapper, repository);
    }

    @Test
    void findByUserIdDoesReturnOptionalWithUserDetailsReadDtoIfUserDetailsFound() {
        //given
        UserDetails userDetails = UnitTestUtil.getUserDetails(UnitTestUtil.getUser());
        UserDetailsReadDto expected = UnitTestUtil.getUserDetailsReadDto();
        doReturn(Optional.of(userDetails)).when(repository).findByUserId(userDetails.getUser().getId());
        doReturn(expected).when(readMapper).map(userDetails);

        //when
        Optional<UserDetailsReadDto> actual = service.findByUserId(userDetails.getUser().getId());

        //then
        assertThat(actual).isPresent();
        assertThat(actual.get()).isEqualTo(expected);
    }

    @Test
    void findByUserIdDoesReturnOptionalEmptyIfUserDetailsNotFound() {
        //given
        Integer id = 1;
        doReturn(Optional.empty()).when(repository).findByUserId(anyInt());

        //when
        Optional<UserDetailsReadDto> actual = service.findByUserId(id);

        //then
        assertThat(actual).isEmpty();
    }

    @Test
    void findByUserIdThrowsNullPointerExceptionIfIdIsNull() {
        //given
        doThrow(NullPointerException.class).when(repository).findByUserId(null);

        //when

        //then
        assertThrows(NullPointerException.class, () -> service.findByUserId(null));
    }

    @Test
    void createDoesReturnUserDetailsReadDto() {
        //given
        User user = UnitTestUtil.getUser();
        UserDetailsCreateEditDto cUserDetails = UnitTestUtil.getCreateUserDetails();
        UserDetailsReadDto expected = UnitTestUtil.getUserDetailsReadDto();
        UserDetails userDetailsBeforeSave = UnitTestUtil.getUserDetails(user);
        UserDetails userDetailsAfterSave = UnitTestUtil.getUserDetails(user);
        userDetailsBeforeSave.setId(null);
        doReturn(userDetailsBeforeSave).when(createEditMapper).map(cUserDetails);
        doReturn(userDetailsAfterSave).when(repository).save(userDetailsBeforeSave);
        doReturn(expected).when(readMapper).map(userDetailsAfterSave);

        //when
        UserDetailsReadDto actual = service.create(cUserDetails);

        //then
        verify(createEditMapper).map(cUserDetails);
        verify(repository).save(userDetailsBeforeSave);
        verify(readMapper).map(userDetailsAfterSave);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void createThrowsNullPointerExceptionIfUserDetailsCreateEditDtoIsNull() {
        //given

        //when

        //then
        verifyNoInteractions(createEditMapper);
        verifyNoInteractions(repository);
        verifyNoInteractions(readMapper);
        assertThrows(NullPointerException.class, () -> service.create(null));
    }

    @Test
    void updateByUserIdDoesReturnOptionalWithUserDetailsReadDtoIfUserDetailsFound() {
        //given
        User user = UnitTestUtil.getUser();
        UserDetails userDetailsBeforeChanges = UnitTestUtil.getUserDetails(user);
        userDetailsBeforeChanges.setFirstname("happened-before");
        UserDetails userDetailsAfterChanges = UnitTestUtil.getUserDetails(user);
        Integer id = userDetailsBeforeChanges.getId();
        UserDetailsCreateEditDto cUserDetails = UnitTestUtil.getCreateUserDetails();
        UserDetailsReadDto expected = UnitTestUtil.getUserDetailsReadDto();
        doReturn(Optional.of(userDetailsBeforeChanges)).when(repository).findByUserId(id);
        doReturn(expected).when(readMapper).map(userDetailsAfterChanges);
        doReturn(userDetailsAfterChanges).when(createEditMapper).map(cUserDetails, userDetailsBeforeChanges);
        doReturn(userDetailsAfterChanges).when(repository).saveAndFlush(userDetailsAfterChanges);

        //when
        Optional<UserDetailsReadDto> actual = service.updateByUserId(id, cUserDetails);

        //then
        assertThat(actual).isPresent();
        assertThat(actual.get()).isEqualTo(expected);
    }

    @Test
    void updateByUserIdDoesReturnOptionalEmptyIfUserDetailsNotFound() {
        //given
        UserDetailsCreateEditDto cUserDetails = UnitTestUtil.getCreateUserDetails();
        Integer id = 1;
        doReturn(Optional.empty()).when(repository).findByUserId(anyInt());

        //when
        Optional<UserDetailsReadDto> actual = service.updateByUserId(id, cUserDetails);

        //then
        assertThat(actual).isEmpty();
    }

    @Test
    void updateByUserIdThrowsNullPointerExceptionIfIdIsNull() {
        //given
        UserDetailsCreateEditDto cUserDetails = UnitTestUtil.getCreateUserDetails();
        doThrow(NullPointerException.class).when(repository).findByUserId(null);

        //when

        //then
        assertThrows(NullPointerException.class, () -> service.updateByUserId(null, cUserDetails));
    }

    @Test
    void updateByUserIdThrowsNullPointerExceptionIfUserDetailsCreateEditDtoIsNull() {
        //given
        User user = UnitTestUtil.getUser();
        UserDetails userDetails = UnitTestUtil.getUserDetails(user);
        doReturn(Optional.of(userDetails)).when(repository).findByUserId(anyInt());
        doThrow(NullPointerException.class).when(createEditMapper).map(null, userDetails);

        //when

        //then
        assertThrows(NullPointerException.class, () -> service.updateByUserId(1, null));
    }

    @Test
    void deleteByUserIdDoesReturnTrueIfUserDetailsFound() {
        //given
        User user = UnitTestUtil.getUser();
        UserDetails userDetails = UnitTestUtil.getUserDetails(user);
        Integer id = userDetails.getId();
        doReturn(Optional.of(userDetails)).when(repository).findByUserId(id);

        //when
        boolean actual = service.deleteByUserId(id);

        //then
        verify(repository).findByUserId(id);
        verify(repository).delete(userDetails);
        verify(repository).flush();
        assertTrue(actual);
    }

    @Test
    void deleteByUserIdDoesReturnFalseIfUserDetailsNotFound() {
        //given
        Integer id = 1;
        doReturn(Optional.empty()).when(repository).findByUserId(anyInt());

        //when
        boolean actual = service.deleteByUserId(id);

        //then
        verify(repository).findByUserId(id);
        verifyNoMoreInteractions(repository);
        assertFalse(actual);
    }

    @Test
    void deleteByUserIdThrowsNullPointerExceptionIfIdIsNull() {
        //given
        doThrow(NullPointerException.class).when(repository).findByUserId(null);

        //when

        //then
        assertThrows(NullPointerException.class, () -> service.deleteByUserId(null));
    }
}
