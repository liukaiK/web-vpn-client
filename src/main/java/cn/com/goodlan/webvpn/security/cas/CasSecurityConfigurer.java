package cn.com.goodlan.webvpn.security.cas;

import cn.com.goodlan.webvpn.security.cas.authentication.CasAuthenticationFailureHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
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
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
     * 跳转到CAS的退出地址
     */
    @Value("${cas.casLogOutUrl}")
    private String casLogOutUrl;

    /**
     * 跳回到系统的回调地址
     */
    @Value("${cas.callBackUrl}")
    private String callBackUrl;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain casLoginSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/welcome").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .addFilterBefore(requestSingleLogoutFilter(), LogoutFilter.class)
                .addFilterBefore(singleSignOutFilter(), CasAuthenticationFilter.class)
                .addFilterBefore(casAuthenticationFilter(), RequestCacheAwareFilter.class)
                .exceptionHandling().authenticationEntryPoint(casAuthenticationEntryPoint());
        return http.build();
    }

    @Bean
    public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> configContextListener() {
        ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> registrationBean = new ServletListenerRegistrationBean<>();
        registrationBean.setListener(new SingleSignOutHttpSessionListener());
        return registrationBean;
    }

    public AuthenticationFailureHandler casAuthenticationFailureHandler() {
        return new CasAuthenticationFailureHandler(objectMapper);
    }

    public CasAuthenticationFilter casAuthenticationFilter() {
        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
        casAuthenticationFilter.setAuthenticationManager(new ProviderManager(Collections.singletonList(casAuthenticationProvider())));
        casAuthenticationFilter.setAuthenticationFailureHandler(casAuthenticationFailureHandler());
        return casAuthenticationFilter;
    }

    public LogoutFilter requestSingleLogoutFilter() {
        LogoutFilter logoutFilter = new LogoutFilter(casLogOutUrl, new SecurityContextLogoutHandler());
//        logoutFilter.setLogoutRequestMatcher();
        return logoutFilter;
    }

    private SingleSignOutFilter singleSignOutFilter() {
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        return singleSignOutFilter;
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


//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> web.ignoring().antMatchers("/", "/welcome");
//    }


}
