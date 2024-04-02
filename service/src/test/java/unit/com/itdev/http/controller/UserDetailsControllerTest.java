package unit.com.itdev.http.controller;

import com.itdev.dto.*;
import com.itdev.http.controller.UserDetailsController;
import com.itdev.service.UserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;
import unit.com.itdev.util.UnitTestUtil;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class UserDetailsDetailsControllerTest {

    private UserDetailsController controller;
    private UserDetailsService service;
    private Model model;

    @BeforeEach
    void prepare() {
        service = mock(UserDetailsService.class);
        controller = new UserDetailsController(service);
        model = mock(Model.class);
    }

    @Test
    void findByUserIdDoesReturnPathToTemplateUserDetailsIfUserDetailsFound() {
        //given
        UserDetailsReadDto userDetails = UnitTestUtil.getUserDetailsReadDto();
        Integer userId = userDetails.getUserId();
        doReturn(Optional.of(userDetails)).when(service).findByUserId(userId);

        //when
        String actual = controller.findByUserId(userId, model);

        //then
        verify(service).findByUserId(userId);
        verify(model).addAttribute("userDetails", userDetails);
        assertEquals("user/userDetails", actual);
    }

    @Test
    void findByUserIdDoesReturnPathToTemplateUserDetailsCreateIfUserDetailsNotFound() {
        //given
        Integer userId = 1;
        doReturn(Optional.empty()).when(service).findByUserId(userId);

        //when

        //then
        assertDoesNotThrow(() -> controller.findByUserId(userId, model));

        //given
        UserDetailsReadDto UserDetails = UnitTestUtil.getUserDetailsReadDto();
        doReturn(Optional.of(UserDetails)).when(service).findByUserId(userId);

        //when

        //then
        assertDoesNotThrow(() -> controller.findByUserId(userId, model));
    }

    @Test
    void findByUserIdDoesNotThrowIfUserIdIsNull() {
        //given
        Integer userId = null;
        doReturn(Optional.empty()).when(service).findByUserId(userId);

        //when
        String actual = controller.findByUserId(userId, model);

        //then
        verify(service).findByUserId(userId);
        verify(model).addAttribute("userId", userId);
        assertEquals("user/userDetailsCreate", actual);
    }

    @Test
    void findByUserIdThrowsNullPointerExceptionIfModelIsNull() {
        //given
        UserDetailsReadDto userDetails = UnitTestUtil.getUserDetailsReadDto();
        Integer userId = userDetails.getUserId();
        doReturn(Optional.of(userDetails)).when(service).findByUserId(userId);
        model = null;

        //when

        //then
        assertThrows(NullPointerException.class, () -> controller.findByUserId(userId, model));

        //given
        doReturn(Optional.empty()).when(service).findByUserId(userId);

        //when

        //then
        assertThrows(NullPointerException.class, () -> controller.findByUserId(userId, model));
    }

    @Test
    void createDoesReturnStringOfRedirectToPageUser() {
        //given
        UserDetailsReadDto detailsReadDto = UnitTestUtil.getUserDetailsReadDto();
        UserDetailsCreateEditDto userDetails = UnitTestUtil.getCreateUserDetails();
        doReturn(detailsReadDto).when(service).create(userDetails);

        //when
        String actual = controller.create(userDetails);

        //then
        verify(service).create(userDetails);
        assertEquals("redirect:/users/" + detailsReadDto.getUserId(), actual);
    }

    @Test
    void createThrowsIfUserDetailsIsNull() {
        //given
        UserDetailsCreateEditDto userDetails = null;
        doThrow(NoSuchElementException.class).when(service).create(userDetails);

        //when

        //then
        assertThrows(NoSuchElementException.class, () -> controller.create(userDetails));
    }

    @Test
    void updateByUserIdThrowsResponseStatusExceptionIfUserDetailsNotFound() {
        //given
        UserDetailsCreateEditDto cUserDetailsDetails = UnitTestUtil.getCreateUserDetails();
        Integer userId = cUserDetailsDetails.getUserId();
        doReturn(Optional.empty()).when(service).updateByUserId(userId, cUserDetailsDetails);

        //when

        //then
        assertThrows(ResponseStatusException.class, () -> controller.updateByUserId(userId, cUserDetailsDetails));
        verify(service).updateByUserId(userId, cUserDetailsDetails);
    }

    @Test
    void updateByUserIdDoesReturnStringOfRedirectToPageUserIfUserDetailsFound() {
        //given
        Integer userId = 1;
        UserDetailsCreateEditDto cUserDetailsDetails = UnitTestUtil.getCreateUserDetails();
        UserDetailsReadDto UserDetails = UnitTestUtil.getUserDetailsReadDto();
        doReturn(Optional.of(UserDetails)).when(service).updateByUserId(userId, cUserDetailsDetails);

        //when
        String actual = controller.updateByUserId(userId, cUserDetailsDetails);

        //then
        verify(service).updateByUserId(userId, cUserDetailsDetails);
        assertEquals("redirect:/users/{userId}/", actual);
    }

    @Test
    void updateByUserIdThrowsResponseStatusExceptionIfIdIsNull() {
        //given
        Integer userId = null;
        UserDetailsCreateEditDto cUserDetailsDetails = UnitTestUtil.getCreateUserDetails();
        doReturn(Optional.empty()).when(service).updateByUserId(userId, cUserDetailsDetails);

        //when

        //then
        assertThrows(ResponseStatusException.class, () -> controller.updateByUserId(userId, cUserDetailsDetails));
        verify(service).updateByUserId(userId, cUserDetailsDetails);
    }

    @Test
    void updateByUserIdThrowsNullPointerExceptionIfUserDetailsIsNull() {
        //given
        Integer userId = 1;
        UserDetailsCreateEditDto userDetails = UnitTestUtil.getCreateUserDetails();
        doThrow(NullPointerException.class).when(service).updateByUserId(userId, userDetails);

        //when

        //then
        assertThrows(NullPointerException.class, () -> controller.updateByUserId(userId, userDetails));
        verify(service).updateByUserId(userId, userDetails);
    }

    @Test
    void deleteByUserIdDoesReturnPathToTemplateUserIfUserDetailsDeleted() {
        //given
        doReturn(true).when(service).deleteByUserId(anyInt());

        //when
        String actual = controller.deleteByUserId(anyInt());

        //then
        verify(service).deleteByUserId(anyInt());
        assertEquals("redirect:/users/{userId}/", actual);
    }

    @Test
    void deleteByUserIdThrowsResponseStatusExceptionIfUserDetailsNotFound() {
        //given
        doReturn(false).when(service).deleteByUserId(anyInt());

        //when

        //then
        assertThrows(ResponseStatusException.class, () -> controller.deleteByUserId(anyInt()));
        verify(service).deleteByUserId(anyInt());
    }

    @Test
    void deleteByUserIdThrowsResponseStatusExceptionIfIdIsNull() {
        //given
        Integer userId = null;
        doReturn(false).when(service).deleteByUserId(userId);

        //when

        //then
        assertThrows(ResponseStatusException.class, () -> controller.deleteByUserId(userId));
        verify(service).deleteByUserId(userId);
    }
}
