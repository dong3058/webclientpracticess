package sprginsecuritytest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
@Slf4j
public class JsonFilter extends  AbstractAuthenticationProcessingFilter{



    private static final String DEFAULT_LOGIN_REQUEST_URL = "/login"; // "/login"으로 오는 요청을 처리
    private static final String HTTP_METHOD = "POST"; // 로그인 HTTP 메소드는 POST
    private static final String CONTENT_TYPE = "application/json"; // JSON 타입의 데이터로 오는 로그인 요청만 처리
    private static final String USERNAME_KEY = "username"; // 회원 로그인 시 이메일 요청 JSON Key : "email"
    private static final String PASSWORD_KEY = "password"; // 회원 로그인 시 비밀번호 요청 JSon Key : "password"
    private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD); // "/login" + POST로 온 요청에 매칭된다.
    private final ObjectMapper objectMapper;


    public JsonFilter(ObjectMapper objectMapper) {
        // 위에서 설정한 "login" + POST로 온 요청을 처리하기 위해 설정
        super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER);
        this.objectMapper = objectMapper;

    }






    /**
     * 인증 처리 메소드
     *
     * UsernamePasswordAuthenticationFilter와 동일하게 UsernamePasswordAuthenticationToken 사용
     * StreamUtils를 통해 request에서 messageBody(JSON) 반환
     * 요청 JSON Example
     * {
     *    "email" : "aaa@bbb.com"
     *    "password" : "test123"
     * }
     * 꺼낸 messageBody를 objectMapper.readValue()로 Map으로 변환 (Key : JSON의 키 -> email, password)
     * Map의 Key(email, password)로 해당 이메일, 패스워드 추출 후
     * UsernamePasswordAuthenticationToken의 파라미터 principal, credentials에 대입
     *
     * AbstractAuthenticationProcessingFilter(부모)의 getAuthenticationManager()로 AuthenticationManager 객체를 반환 받은 후
     * authenticate()의 파라미터로 UsernamePasswordAuthenticationToken 객체를 넣고 인증 처리
     * (여기서 AuthenticationManager 객체는 ProviderManager -> SecurityConfig에서 설정)
     */
        @Override
        public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
            log.info("jsonfilter작동");
            if(request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)  ) {
                throw new AuthenticationServiceException("Authentication Content-Type not supported: " + request.getContentType());
            }


            log.info("{}",request.getInputStream());
        String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

        Map<String, String> usernamePasswordMap = objectMapper.readValue(messageBody, Map.class);

        String username = usernamePasswordMap.get(USERNAME_KEY);
        String password = usernamePasswordMap.get(PASSWORD_KEY);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username,password);//principal 과 credentials 전달
        log.info("필터작동끝");
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
