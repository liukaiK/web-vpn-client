package cn.com.goodlan.webvpn.security.cas;

import org.jasig.cas.client.validation.Assertion;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserDetailsServiceImplByAttrs implements AuthenticationUserDetailsService<CasAssertionAuthenticationToken> {

    @Override
    public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException {
        Assertion assertion = token.getAssertion();
        Map<String, Object> attributes = assertion.getAttributes();

        Set<Map.Entry<String, Object>> entries = attributes.entrySet();

        for (Map.Entry<String, Object> entry : entries) {
            System.out.println("key:" + entry.getKey() + ",value:" + entry.getValue());

        }

        System.out.println(token.toString());

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        return new User(assertion.getPrincipal().getName(), "", true, true, true, true, grantedAuthorities);
    }

}
