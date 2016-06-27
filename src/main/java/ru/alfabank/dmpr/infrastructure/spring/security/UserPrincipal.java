package ru.alfabank.dmpr.infrastructure.spring.security;

import org.springframework.security.core.GrantedAuthority;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;

import java.security.Principal;
import java.util.Set;

public class UserPrincipal implements Principal {
    private String userName;
    private String displayName;
    private Set<GrantedAuthority> authorities;
    private String[] roles;

    public UserPrincipal(String userName, String displayName, Set<GrantedAuthority> authorities) {
        this.userName = userName;
        this.displayName = displayName;
        this.authorities = authorities;
        this.roles = LinqWrapper.from(authorities)
                .select(new Selector<GrantedAuthority, String>() {
                    @Override
                    public String select(GrantedAuthority authority) {
                        return authority.getAuthority();
                    }
                })
                .toArray(String.class);
    }

    public String getDisplayName() {
        return displayName;
    }

    public Set<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String[] getRoles() {
        return roles;
    }

    @Override
    public String getName() {
        return userName;
    }
}
