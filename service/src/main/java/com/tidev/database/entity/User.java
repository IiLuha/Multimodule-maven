package com.tidev.database.entity;

import com.tidev.database.entity.fields.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "email")
@ToString(exclude = {"orders", "userAddress", "userDetails"})
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
public class User implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Order> orders = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserAddress userAddress;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserDetails userDetails;

    private String image;

    public void addUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
        userAddress.setUser(this);
    }

    public void addUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
        userDetails.setUser(this);
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.setUser(this);
    }

    public String fullName() {
        if(userDetails != null) {
            return this.userDetails.getFirstname() + " " + this.userDetails.getLastname();
        }else {
            return "no information available";
        }
    }
}
