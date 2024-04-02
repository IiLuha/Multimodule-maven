package unit.com.itdev.http.controller;

import com.itdev.database.entity.fields.Role;
import com.itdev.dto.*;
import com.itdev.http.controller.UserController;
import com.itdev.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import unit.com.itdev.util.UnitTestUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserController controller;
    private UserService service;
    private Model model;

    @BeforeEach
    void prepare() {
        service = mock(UserService.class);
        controller = new UserController(service);
        model = mock(Model.class);
    }

    @Test
    void findAllDoesReturnPathToTemplateUsers() {
        //given
        UserFilter filter = UserFilter.builder().build();
        Pageable pageable = mock(Pageable.class);
        Page<UserReadDto> page = Page.empty();
        doReturn(page).when(service).findAll(filter, pageable);

        //when
        String actual = controller.findAll(model, filter, pageable);

        //then
        verify(service).findAll(filter, pageable);
        verify(model).addAttribute("users", PageResponse.of(page));
        verify(model).addAttribute("roles", Role.values());
        verify(model).addAttribute("filter", filter);
        assertEquals("user/users", actual);
    }

    @Test
    void findAllThrowsNullPointerExceptionIfFilterIsNull() {
        //given
        UserFilter filter = null;
        Pageable pageable = mock(Pageable.class);
        Page<UserReadDto> page = Page.empty();
        doThrow(NullPointerException.class).when(service).findAll(eq(filter), any(Pageable.class));

        //when

        //then
        assertThrows(NullPointerException.class, () -> controller.findAll(model,
                filter, pageable));
    }

    @Test
    void findAllThrowsNullPointerExceptionIfPageableIsNull() {
        //given
        UserFilter filter = UserFilter.builder().build();
        Pageable pageable = null;
        doThrow(NullPointerException.class).when(service).findAll(any(UserFilter.class), eq(pageable));

        //when

        //then
        assertThrows(NullPointerException.class, () -> controller.findAll(model,
                filter, pageable));
    }

    @Test
    void findAllThrowsNullPointerExceptionIfModelIsNull() {
        //given
        UserFilter filter = UserFilter.builder().build();
        Pageable pageable = mock(Pageable.class);
        model = null;
        Page<UserReadDto> page = Page.empty();
        doReturn(page).when(service).findAll(filter, pageable);

        //when

        //then
        assertThrows(NullPointerException.class, () -> controller.findAll(model,
                filter, pageable));
    }

    @Test
    void findByIdDoesReturnPathToTemplateUser() {
        //given
        Integer id = 1;
        UserDetailsReadDto userDetails = UnitTestUtil.getUserDetailsReadDto();
        UserAddressReadDto userAddress = UnitTestUtil.getUserAddressReadDto();
        UserReadDto user = UnitTestUtil.getUserReadDto(userDetails, userAddress);
        doReturn(Optional.of(user)).when(service).findById(id);

        //when
        String actual = controller.findById(id, model);

        //then
        verify(service).findById(id);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("roles", Role.values());
        verify(model).addAttribute("userDetails", userDetails);
        verify(model).addAttribute("userAddress", userAddress);
        assertEquals("user/user", actual);
    }

    @Test
    void findByIdDoesNotThrowIfIdIsNull() {
        //given
        Integer id = null;
        UserReadDto user = UnitTestUtil.getUserReadDto(null, null);
        doReturn(Optional.of(user)).when(service).findById(id);

        //when

        //then
        assertDoesNotThrow(() -> controller.findById(id, model));
    }

    @Test
    void findByIdThrowsNullPointerExceptionIfModelIsNull() {
        //given
        Integer id = 1;
        UserReadDto user = UnitTestUtil.getUserReadDto(null, null);
        doReturn(Optional.of(user)).when(service).findById(id);
        model = null;

        //when

        //then
        assertThrows(NullPointerException.class, () -> controller.findById(id, model));
    }

    @Test
    void findByIdThrowsResponseStatusExceptionIfUserIsNotFound() {
        //given
        Integer id = 1;
        doReturn(Optional.empty()).when(service).findById(id);

        //when

        //then
        assertThrows(ResponseStatusException.class, () -> controller.findById(id, model));
    }

    @Test
    void createDoesReturnStringOfRedirectToPageLoginIfBindingResultHaveNoErrors() {
        //given
        UserCreateEditDto user = UnitTestUtil.getCreateUser(null, null);
        BindingResult bindingResult = mock(BindingResult.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        doReturn(false).when(bindingResult).hasErrors();

        //when
        String actual = controller.create(user, bindingResult, redirectAttributes);

        //then
        verifyNoInteractions(redirectAttributes);
        verify(service).create(user);
        assertEquals("redirect:/login", actual);
    }

    @Test
    void createDoesReturnStringOfRedirectToPageRegistrationIfBindingResultHasErrors() {
        //given
        UserCreateEditDto user = UnitTestUtil.getCreateUser(null, null);
        BindingResult bindingResult = mock(BindingResult.class);
        List<ObjectError> allErrors = new ArrayList<>(0);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        doReturn(true).when(bindingResult).hasErrors();
        doReturn(allErrors).when(bindingResult).getAllErrors();

        //when
        String actual = controller.create(user, bindingResult, redirectAttributes);

        //then
        verify(bindingResult).getAllErrors();
        verify(redirectAttributes).addFlashAttribute("user", user);
        verify(redirectAttributes).addFlashAttribute("errors", allErrors);
        verifyNoInteractions(service);
        assertEquals("redirect:/users/registration", actual);
    }

    @Test
    void createThrowsNullPointerExceptionIfBindingResultIsNull() {
        //given
        UserCreateEditDto user = UnitTestUtil.getCreateUser(null, null);
        BindingResult bindingResult = null;
        List<ObjectError> allErrors = new ArrayList<>(0);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        //when

        //then
        assertThrows(NullPointerException.class, () -> controller.create(user,
                bindingResult, redirectAttributes));
    }

    @Test
    void createThrowsNullPointerExceptionIfRedirectAttributesIsNullAndBindingResultHasErrors() {
        //given
        UserCreateEditDto user = UnitTestUtil.getCreateUser(null, null);
        BindingResult bindingResult = mock(BindingResult.class);
        List<ObjectError> allErrors = new ArrayList<>(0);
        RedirectAttributes redirectAttributes = null;
        doReturn(true).when(bindingResult).hasErrors();
        doReturn(allErrors).when(bindingResult).getAllErrors();

        //when

        //then
        assertThrows(NullPointerException.class, () -> controller.create(user,
                bindingResult, redirectAttributes));
    }

    @Test
    void createDoesNotThrowIfUserIsNull() {
        //given
        UserCreateEditDto user = null;
        BindingResult bindingResult = mock(BindingResult.class);
        List<ObjectError> allErrors = new ArrayList<>(0);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        doReturn(true).when(bindingResult).hasErrors();
        doReturn(allErrors).when(bindingResult).getAllErrors();

        //when

        //then
        assertDoesNotThrow(() -> controller.create(user, bindingResult, redirectAttributes));

        //given
        doReturn(false).when(bindingResult).hasErrors();

        //when

        //then
        assertDoesNotThrow(() -> controller.create(user, bindingResult, redirectAttributes));
    }

    @Test
    void updateThrowsResponseStatusExceptionIfBindingResultHaveNoErrorsAndUserNotFound() {
        //given
        Integer id = 1;
        UserCreateEditDto cUser = UnitTestUtil.getCreateUser(null, null);
        UserReadDto user = UnitTestUtil.getUserReadDto(null, null);
        doReturn(Optional.empty()).when(service).update(id, cUser);
        BindingResult bindingResult = mock(BindingResult.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        doReturn(false).when(bindingResult).hasErrors();

        //when

        //then
        assertThrows(ResponseStatusException.class, () -> controller.update(id,
                cUser, bindingResult, redirectAttributes));
        verifyNoInteractions(redirectAttributes);
        verify(service).update(id, cUser);
    }

    @Test
    void updateDoesReturnStringOfRedirectToPageLoginIfBindingResultHaveNoErrorsAndUserFound() {
        //given
        Integer id = 1;
        UserCreateEditDto cUser = UnitTestUtil.getCreateUser(null, null);
        UserReadDto user = UnitTestUtil.getUserReadDto(null, null);
        doReturn(Optional.of(user)).when(service).update(id, cUser);
        BindingResult bindingResult = mock(BindingResult.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        doReturn(false).when(bindingResult).hasErrors();

        //when
        String actual = controller.update(id, cUser, bindingResult, redirectAttributes);

        //then
        verifyNoInteractions(redirectAttributes);
        verify(service).update(id, cUser);
        assertEquals("redirect:/users/{id}", actual);
    }

    @Test
    void updateDoesReturnStringOfRedirectToPageRegistrationIfBindingResultHasErrors() {
        //given
        Integer id = 1;
        UserCreateEditDto cUser = UnitTestUtil.getCreateUser(null, null);
        UserReadDto user = UnitTestUtil.getUserReadDto(null, null);
        BindingResult bindingResult = mock(BindingResult.class);
        List<ObjectError> allErrors = new ArrayList<>(0);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        doReturn(true).when(bindingResult).hasErrors();
        doReturn(allErrors).when(bindingResult).getAllErrors();

        //when
        String actual = controller.update(id, cUser, bindingResult, redirectAttributes);

        //then
        verify(service).update(id, cUser);
        verify(bindingResult).hasErrors();
        verify(bindingResult).getAllErrors();
        verify(redirectAttributes).addFlashAttribute("user", cUser);
        verify(redirectAttributes).addFlashAttribute("errors", allErrors);
        assertEquals("redirect:/users/{id}/update", actual);
    }

    @Test
    void updateDoesNotThrowIfIdIsNull() {
        //given
        Integer id = null;
        UserCreateEditDto cUser = UnitTestUtil.getCreateUser(null, null);
        UserReadDto user = UnitTestUtil.getUserReadDto(null, null);
        BindingResult bindingResult = mock(BindingResult.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        doReturn(Optional.of(user)).when(service).update(id, cUser);

        //when

        //then
        assertDoesNotThrow(() -> controller.update(id, cUser,
                bindingResult, redirectAttributes));
    }

    @Test
    void updateThrowsNullPointerExceptionIfBindingResultIsNull() {
        //given
        Integer id = 1;
        UserCreateEditDto user = UnitTestUtil.getCreateUser(null, null);
        BindingResult bindingResult = null;
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        //when

        //then
        assertThrows(NullPointerException.class, () -> controller.update(id, user,
                bindingResult, redirectAttributes));
    }

    @Test
    void updateThrowsNullPointerExceptionIfRedirectAttributesIsNullAndBindingResultHasErrors() {
        //given
        Integer id = 1;
        UserCreateEditDto user = UnitTestUtil.getCreateUser(null, null);
        BindingResult bindingResult = mock(BindingResult.class);
        List<ObjectError> allErrors = new ArrayList<>(0);
        RedirectAttributes redirectAttributes = null;
        doReturn(true).when(bindingResult).hasErrors();
        doReturn(allErrors).when(bindingResult).getAllErrors();

        //when

        //then
        assertThrows(NullPointerException.class, () -> controller.update(id, user,
                bindingResult, redirectAttributes));
    }

    @Test
    void updateDoesNotThrowIfUserIsNull() {
        //given
        Integer id = 1;
        UserCreateEditDto cUser = null;
        UserReadDto user = UnitTestUtil.getUserReadDto(null, null);
        BindingResult bindingResult = mock(BindingResult.class);
        List<ObjectError> allErrors = new ArrayList<>(0);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        doReturn(true).when(bindingResult).hasErrors();
        doReturn(allErrors).when(bindingResult).getAllErrors();
        doReturn(Optional.of(user)).when(service).update(id, cUser);

        //when

        //then
        assertDoesNotThrow(() -> controller.update(id, cUser, bindingResult, redirectAttributes));

        //given
        doReturn(false).when(bindingResult).hasErrors();

        //when

        //then
        assertDoesNotThrow(() -> controller.update(id, cUser, bindingResult, redirectAttributes));
    }

    @Test
    void deleteDoesReturnPathToTemplateUsersIfUserDeleted() {
        //given
        doReturn(true).when(service).delete(anyInt());

        //when
        String actual = controller.delete(anyInt());

        //then
        verify(service).delete(anyInt());
        assertEquals("redirect:/users", actual);
    }

    @Test
    void deleteThrowsResponseStatusExceptionIfUserNotFound() {
        //given
        doReturn(false).when(service).delete(anyInt());

        //when

        //then
        assertThrows(ResponseStatusException.class, () -> controller.delete(anyInt()));
        verify(service).delete(anyInt());
    }

    @Test
    void deleteDoesNotThrowsIfIdIsNull() {
        //given
        Integer id = null;
        doReturn(true).when(service).delete(id);

        //when

        //then
        assertDoesNotThrow(() -> controller.delete(id));
        verify(service).delete(id);
    }

    @Test
    void registrationDoesReturnPathToPageRegistration() {
        //given
        UserCreateEditDto user = UnitTestUtil.getCreateUser(null, null);

        //when
        String actual = controller.registration(model, user);

        //then
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("role", Role.USER);
        assertEquals("/user/registration", actual);
    }

    @Test
    void registrationThrowsNullPointerExceptionIfModelIsNull() {
        //given
        UserCreateEditDto user = UnitTestUtil.getCreateUser(null, null);
        model = null;

        //when

        //then
        assertThrows(NullPointerException.class, () -> controller.registration(model,
                user));
    }

    @Test
    void registrationDoesNotThrowIfUserIsNull() {
        //given
        UserCreateEditDto user = null;

        //when

        //then
        assertDoesNotThrow(() -> controller.registration(model, user));
    }
}
