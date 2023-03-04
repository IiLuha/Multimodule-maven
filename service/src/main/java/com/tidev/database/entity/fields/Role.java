package com.tidev.database.entity.fields;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, ADMIN;

    public static Boolean isAdmin(Role role){
        return ADMIN.equals(role);
    }

    @Override
    public String getAuthority() {
        return name();
    }
}
