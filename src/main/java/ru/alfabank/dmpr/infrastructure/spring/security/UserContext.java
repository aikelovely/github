package ru.alfabank.dmpr.infrastructure.spring.security;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Статический класс-провайдер текущего пользователя
 */
public class UserContext {
    /**
     * static class
     */
    private UserContext() {
    }

    private static final String AdminRole = "ADMIN";
    private static final String SqlViewerRole = "SQLVIEWER";

    private static final String CardUser = "CARDUSER";
    private static final String NomUser = "NOMUSER";
    private static final String CTQUser = "CTQOBUSER";
    private static final String KpiObUser = "KPIOBUSER";
    private static final String UCUser = "UCUSER";
    private static final String PilAndCCUser = "PILCCUSER";
    private static final String CIBUser = "CIBUSER";
    private static final String ZPUser = "ZPUSER";
    private static final String MassUser = "MASSUSER";
    private static final String CapacityUser = "CPUUSER";
    private static final String CTQBankUser = "CTQBNKUSER";

    public static final UserPrincipal NONE_USER = new UserPrincipal("Dev", "Dev",
            new HashSet<GrantedAuthority>(Arrays.asList(
                    new SimpleGrantedAuthority(AdminRole),
                    new SimpleGrantedAuthority(SqlViewerRole))));

    /**
     * Определяет текущего аутентифицированного пользователя для данного потока
     * @return пользователь
     */
    public static UserPrincipal getUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return authentication == null ? NONE_USER : (UserPrincipal) authentication.getPrincipal();
    }

    public static boolean isAdminRole(){
        return ArrayUtils.contains(getUser().getRoles(), AdminRole);
    }

    public static boolean isSqlViewerRole(){
        return ArrayUtils.contains(getUser().getRoles(), SqlViewerRole);
    }

    public static boolean isCardUser(){
        return ArrayUtils.contains(getUser().getRoles(), CardUser);
    }

    public static boolean isNomUser(){
        return ArrayUtils.contains(getUser().getRoles(), NomUser);
    }

    public static boolean isCTQUser(){
        return ArrayUtils.contains(getUser().getRoles(), CTQUser);
    }

    public static boolean isKpiObUser(){
        return ArrayUtils.contains(getUser().getRoles(), KpiObUser);
    }

    public static boolean isUCUser(){
        return ArrayUtils.contains(getUser().getRoles(), UCUser);
    }

    public static boolean isPilAndCCUser(){
        return ArrayUtils.contains(getUser().getRoles(), PilAndCCUser);
    }

    public static boolean isCIBUser(){
        return ArrayUtils.contains(getUser().getRoles(), CIBUser);
    }

    public static boolean isZPUser(){
        return ArrayUtils.contains(getUser().getRoles(), ZPUser);
    }

    public static boolean isMassUser(){
        return ArrayUtils.contains(getUser().getRoles(), MassUser);
    }

    public static boolean isCapacityUser(){
        return ArrayUtils.contains(getUser().getRoles(), CapacityUser);
    }

    public static boolean isCTQBankUser(){
        return ArrayUtils.contains(getUser().getRoles(), CTQBankUser);
    }
}
