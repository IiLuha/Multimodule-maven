package unit.com.itdev.util;

import com.itdev.database.entity.User;
import com.itdev.database.entity.UserAddress;
import com.itdev.database.entity.UserDetails;
import com.itdev.database.entity.fields.Role;
import com.itdev.dto.*;
import org.jetbrains.annotations.NotNull;

public class UnitTestUtil {

    @NotNull
    public static UserReadDto getUserReadDto(UserDetailsReadDto userDetails, UserAddressReadDto userAddress) {
        return new UserReadDto(
                1,
                "email1",
                "pass1",
                Role.USER,
                null,
                userDetails,
                userAddress);
    }

    @NotNull
    public static UserCreateEditDto getCreateUser(UserDetailsReadDto userDetails, UserAddressReadDto userAddress) {
        return new UserCreateEditDto(
                "email1",
                "pass1",
                Role.USER,
                null);
    }

    @NotNull
    public static UserAddressReadDto getUserAddressReadDto() {
        return new UserAddressReadDto(
                1,
                "region",
                "district",
                "popCent",
                "street",
                "house",
                true,
                null,
                null,
                null
        );
    }

    @NotNull
    public static UserDetailsReadDto getUserDetailsReadDto() {
        return new UserDetailsReadDto(
                1,
                1,
                "Ivan",
                "Ivanov",
                "Ivanovich",
                "+78005553535"
        );
    }

    @NotNull
    public static UserDetailsCreateEditDto getCreateUserDetails() {
        return new UserDetailsCreateEditDto(
                1,
                "Ivan",
                "Ivanov",
                "Ivanovich",
                "+78005553535"
        );
    }

    @NotNull
    public static User getUser() {
        return User.builder()
                .id(1)
                .email("email1")
                .image(null)
                .password("pass1")
                .role(Role.USER)
                .build();
    }

    @NotNull
    public static UserDetails getUserDetails(User user) {
        return UserDetails.builder()
                .id(1)
                .user(user)
                .firstname("Ivan")
                .lastname("Ivanov")
                .patronymic("Ivanovich")
                .phone("+78005553535")
                .build();
    }

    public static UserAddress getUserAddress(User user) {
        return UserAddress.builder()
                .id(1)
                .user(user)
                .region( "region")
                .district("district")
                .populationCenter("popCent")
                .street("street")
                .house("house")
                .isPrivate(true)
                .flat(null)
                .floor(null)
                .frontDoor(null)
                .build();
    }

    public static UserReadDto getEmptyUserReadDto() {
        return new UserReadDto(
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }
}
