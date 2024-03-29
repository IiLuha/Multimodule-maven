package unit.com.itdev.database.entity;

import com.itdev.database.entity.Order;
import com.itdev.database.entity.User;
import com.itdev.database.entity.UserAddress;
import com.itdev.database.entity.UserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserTest {

    private User user;
    private UserDetails details;
    private UserAddress address;
    private Order order;

    @BeforeEach
    void prepare() {
        user = new User();
        details = mock(UserDetails.class);
        address = mock(UserAddress.class);
        order = mock(Order.class);
    }

    @Test
    void establishRelationsWithAddressIfItIsNotNull() {
        //given

        //when
        user.addUserAddress(address);

        //then
        assertSame(address, user.getUserAddress());
        verify(address).setUser(user);
    }

    @Test
    void establishRelationsWithDetailsIfItIsNotNull() {
        //given

        //when
        user.addUserDetails(details);

        //then
        assertSame(details, user.getUserDetails());
        verify(details).setUser(user);
    }

    @Test
    void establishRelationsWithOrderIfItIsNotNull() {
        //given
        try {
            Field orders = user.getClass().getDeclaredField("orders");
            orders.setAccessible(true);
            orders.set(user, mock(ArrayList.class));
        } catch (NoSuchFieldException e) {
            System.err.println("Not found field with name \"orders\"");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        //when
        user.addOrder(order);

        //then
        verify(user.getOrders()).add(order);
        verify(order).setUser(user);
    }

    @Test
    void returnFullNameIfDetailsIsNotNull() {
        //given
        String firstname = "Firstname";
        String lastname = "Lastname";
        String expected = firstname + " " + lastname;
        try {
            Field orders = user.getClass().getDeclaredField("userDetails");
            orders.setAccessible(true);
            orders.set(user, details);
            doReturn(firstname).when(details).getFirstname();
            doReturn(lastname).when(details).getLastname();
        } catch (NoSuchFieldException e) {
            System.err.println("Not found field with name \"orders\"");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        //when
        String actual = user.fullName();

        //then
        assertEquals(expected, actual);
        verify(details).getFirstname();
        verify(details).getLastname();
    }

    @Test
    void returnDefaultStringIfDetailsIsNull() {
        //given
        String expected = "no information available";

        //when
        String actual = user.fullName();

        //then
        assertEquals(expected, actual);
    }

    @Test
    void throwNullPointerExceptionIfParameterDetailsIsNull() {
        assertThrows(NullPointerException.class, () -> user.addUserDetails(null));
    }

    @Test
    void throwNullPointerExceptionIfParameterAddressIsNull() {
        assertThrows(NullPointerException.class, () -> user.addUserAddress(null));
    }

    @Test
    void throwNullPointerExceptionIfParameterOrderIsNull() {
        assertThrows(NullPointerException.class, () -> user.addOrder(null));
    }
}
