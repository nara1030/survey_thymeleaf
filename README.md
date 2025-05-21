[해당 프로젝트](https://github.com/nara1030/survey_jsp)를 JSP에 이어 Thymeleaf로 구축해보았다.
- - -

## 목차
1. [요구사항](#요구사항)
2. [기타](#기타)

## 요구사항
```text
1. 설문조사 프로젝트를 재구축한다.
   TOBE: Spring Boot, Thymeleaf, Thymeleaf-Layout-Dialect, Spring Security
   ASIS: Spring MVC, JSP, Tiles
2. Thymeleaf 관련 연습해보기 위한 목적이 커서 설문 시스템 개발은 진행하지 않고
   로그인 및 화면 연동 작업 위주로 진행한다.
```


##### [목차로 이동](#목차)

## 기록
```text
1. 기존 JSP로 구축했을 때와 마찬가지로 SSR(서버사이드 렌더링)이다.
   즉, @Controller에서 데이터가 아닌 화면을 반환한다는 점은 같다.
2. SSR에서 흔히 사용하는 방식인 세션 기반 로그인을 사용했다.
   다만 기존 JSP 때와는 다르게 Spring Security가 제공하는 기능(formLogin, logout)을 사용했다.
   (만약 컨트롤러를 통해 직접 로그인 프로세스 구현하는 경우 Form Login 비활성해주어야 한다.)
3. 로그인 유지 기능 역시 지속적 쿠키를 직접 구현하지 않고 시큐리티가 제공하는 remember-me 이용했다.
   (form에서 boolean 타입, 즉 Y가 아닌 true로 넘겨야 연동이 된다.)
4. 권한별 화면 제어를 위해 기존 JSP 프로젝트(Spring MVC)에선 인터셉터를 두어 제어했다.
   (https://github.com/nara1030/survey_jsp/blob/master/among/src/main/java/org/among/interceptor/AuthInterceptor.java)
   TOBE에선 필터 체인에 권한을 설정해 좀 더 보기 쉽게 만들었다.
5. 로그인 세션 방식을 사용했지만, 화면(Thymeleaf)에서 직접 세션에 접근하지 않았다.
   대신 Thymeleaf에서는 시큐리티의 Authentication 객체를 통해 로그인한 사용자 정보에 접근한다.
   그 이유는 세션을 통해 사용자 정보에 접근하면 시큐리티와 무관한 상태를 갖기 때문이다.
   (시큐리티도 세션에 자동적으로 저장하긴 하나, 여기서 말하는 세션은 개발자가 직접 생성/삭제해준다는 의미다.)
6. 이때 스프링 시큐리티와 타임리프를 연동하기 위해 다음 의존성(thymeleaf-extras-springsecurity6)을 추가해야 한다.
   시큐리티 필터 체인에서 서버측 권한별 URL 제어했다면, 화면에서는 다음과 같이 권한별 노출을 제어할 수 있다.
   <div th:if="${#authentication != null and #authentication.name != 'anonymousUser'}">
   <li class="nav-item dropdown" sec:authorize="hasRole('ADMIN')">
7. 예외 처리 핸들러를 시큐리티 필터 체인에 추가했다.
   기존 Spring MVC와는 달리 필터에서 발생하는 예외의 경우 컨트롤러까지 도달하지 못하기 때문에
   @ControllerAdvice, @ExceptionHandler로 예외 처리 불가하다.
   따라서 Form Login의 경우 CustomAuthenticationFailureHandler를 추가하여 세팅하였고,
   외 URL 접근 관련 예외의 경우 따로 설정해주었다.
   (exceptionHandling 내부가 너무 길어 추출해야 할 거 같기도 하다.)
8. 위 에러 처리 관련하여 문제되었던 부분은 다음과 같다.
   A) 에러 페이지 URL을 최초에 /error로 했으나 충돌이 나는지 동작하지 않아 /security-error로 변경해주었다.
   B) 에러 페이지에 넘겨주는 파람이 디코딩되지 않아, YML 및 Redirect URL 현재와 같이 세팅해주었다.
9. 접근 권한이 있더라도 기로그인 사용자가 또 로그인 요청하거나, 또 회원가입하는 경우는 막아야 한다.
   이런 경우 막기 위해 LoginRetryFilter로 두 번 이상의 요청 막도록 했다(URL 추가 필요).
   기존 Spring MVC의 경우 이를 개별 컨트롤러에서 세션 검사 방식으로 걸러주었다.
10. 타임리프에서 th:object는 폼 객체 바인딩용 선언이라고 한다.
    <form th:action="@{/signup}" th:object="${signupForm}" method="post">
    회원가입 페이지가 위와 같은 형식이었는데 에러가 나서 보니, 컨트롤러에 해당 이름의 모델을 던져주어야 한다.
    model.addAttribute("signupForm", new SignupReqDto());
    위 객체가 없으면 템플릿 렌더링이 실패하면서 에러가 난다고 한다.
11. 스프링 시큐리티에는 로그인 이후의 세션 정책도 설정할 수 있다.
    여기서 계정당 하나의 세션만 허용한다든지, 중복 로그인시 리다이렉트된다든지 설정이 가능하다.
    (추후 테스트해보는 걸로...)
12. .
```

##### [목차로 이동](#목차)