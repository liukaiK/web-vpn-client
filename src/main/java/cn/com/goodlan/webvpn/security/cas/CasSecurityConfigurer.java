package cn.com.goodlan.webvpn.security.cas;

import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;

import java.util.Collections;

/**
 * CAS单点登录配置类
 *
 * @author liukai
 */
@Configuration
public class CasSecurityConfigurer {

    @Value("${cas.serverUrlPrefix}")
    private String casServerUrlPrefix;

    /**
     * 跳转到CAS的登录地址
     */
    @Value("${cas.casLoginUrl}")
    private String casLoginUrl;

    /**
     * 跳回到系统的回调地址
     */
    @Value("${cas.callBackUrl}")
    private String callBackUrl;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 请求这个地址 security判断没有权限 然后跳转到cas登录
     */
    private final static String EXCEPTION_URL = "/login/sso";

    @Bean
    public SecurityFilterChain casLoginSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .csrf().ignoringAntMatchers("/druid/**")
                .and()
                .addFilterBefore(casAuthenticationFilter(), RequestCacheAwareFilter.class)
                .exceptionHandling().authenticationEntryPoint(casAuthenticationEntryPoint());
        return http.build();
    }

    public CasAuthenticationFilter casAuthenticationFilter() {
        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
        casAuthenticationFilter.setAuthenticationManager(new ProviderManager(Collections.singletonList(casAuthenticationProvider())));
        return casAuthenticationFilter;
    }

    public AuthenticationProvider casAuthenticationProvider() {
        CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
        casAuthenticationProvider.setTicketValidator(ticketValidator());
        casAuthenticationProvider.setKey("an_id_for_this_auth_provider_only");
        casAuthenticationProvider.setAuthenticationUserDetailsService(authenticationUserDetailsService());
        casAuthenticationProvider.setServiceProperties(serviceProperties());
        return casAuthenticationProvider;
    }


    public AuthenticationUserDetailsService<CasAssertionAuthenticationToken> authenticationUserDetailsService() {
        UserDetailsByNameServiceWrapper<CasAssertionAuthenticationToken> userDetailsByNameServiceWrapper = new UserDetailsByNameServiceWrapper<>();
        userDetailsByNameServiceWrapper.setUserDetailsService(userDetailsService);
        return userDetailsByNameServiceWrapper;
    }

    public AuthenticationEntryPoint casAuthenticationEntryPoint() {
        CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
        casAuthenticationEntryPoint.setLoginUrl(casLoginUrl);
        casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
        return casAuthenticationEntryPoint;
    }

    public ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setService(callBackUrl);
        return serviceProperties;
    }


    public TicketValidator ticketValidator() {
        Cas20ServiceTicketValidator ticketValidator = new Cas20ServiceTicketValidator(casServerUrlPrefix);
        return ticketValidator;
    }

}
