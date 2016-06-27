package ru.alfabank.dmpr.infrastructure.spring.security;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.ArrayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import ru.alfabank.dmpr.repository.authorization.UIAccessRepository;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.*;

public class CustomAuthenticationProvider implements AuthenticationProvider {
    private String userNameAD = System.getProperty("ors.userNameAD");
    private String passwordAD = System.getProperty("ors.passwordAD");

    @Autowired
    UIAccessRepository repository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        PreAuthenticatedAuthenticationToken token = (PreAuthenticatedAuthenticationToken) authentication;

        String userName = token.getName();

        String[] parts = userName.split("@");

        if (parts.length != 2) {
            throw new AuthenticationServiceException("Wrong format of username:" + userName + ". Correct format is username@domain.");
        }

        String userAD = parts[0];
        String domainAD = parts[1];

        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        String displayName = null;

        String server = "ldap://" + domainAD + ":389";

        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, server);
        env.put(Context.REFERRAL, "follow");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, userNameAD);
        env.put(Context.SECURITY_CREDENTIALS, passwordAD);

        DirContext ctx = null;
        try {
            ctx = new InitialDirContext(env);
            SearchControls ctls = new SearchControls();
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration namingEnumeration = ctx.search(getDomainBase(domainAD), "(sAMAccountName=" + userAD + ")", ctls);

            if (namingEnumeration.hasMore()) {
                SearchResult entry = (SearchResult) namingEnumeration.next();

                if (entry.getAttributes().get("displayName").size() > 0) {
                    displayName = entry.getAttributes().get("displayName").get(0).toString();
                } else {
                    displayName = userName;
                }

                String[] roles = repository.getUserRoles(userAD);
                if(roles != null){
                    for(int i = 0; i < roles.length; i++){
                        authorities.add(new SimpleGrantedAuthority(roles[i]));
                    }
                }
            }
            ctx.close();
            ctx = null;
        } catch (NamingException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Username: ").append(userName).append(" ERROR: ").append(e.getExplanation());

            if (e.getRootCause() != null) {
                sb.append("; ").append(e.getRootCause().getMessage());
            }

            String errorMessage = sb.toString();
            e.printStackTrace();

            throw new AuthenticationServiceException(errorMessage, e);
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                    throw new AuthenticationServiceException("Username:" + userName + " ERROR:" + e.getExplanation(), e);
                }
            }
        }

        UserPrincipal userPrincipal = new UserPrincipal(userName, displayName, authorities);
        return new PreAuthenticatedAuthenticationToken(userPrincipal, userName, authorities);
    }

    private static String getDomainBase(String domain) {
        char[] namePair = domain.toUpperCase().toCharArray();
        String dn = "DC=";
        for (int i = 0; i < namePair.length; i++) {
            if (namePair[i] == '.') {
                dn += ",DC=" + namePair[++i];
            } else {
                dn += namePair[i];
            }
        }
        return dn;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
