# Jipdolstargram
각자 본인들만의 추억을 담을 수 있는 추억 저장소입니다.
인스타그램을 참고하였으며 백엔드 개발에 초점을 맞추면서 진행하였습니다.

# 사용 기술 및 환경
- Java 17
- Database : mySql 8.0 , h2 database
- Framwork : Spring Boot 2.7.2
- ORM : JPA
- IDE : Intellij
- Junit 5
- UI : Thymeleaf

# Context
## 기능
- Admin 기능 (로그인/회원가입)
- 기본적인 게시글 CRUD 기능 구현
- 작성한 게시글에 댓글 기능 구현
- junit 5 를 사용하여 api 검증 테스트 진행
- SCryptPasswordEncoder 를 통한 비밀번호 암호화 진행
- JWT 를 이용한 인증/인가 기능 구현
- P6Spy 라이브러리를 활용한 sql 쿼리 로그 기능 확장

## 개발하면서 마주했던 이슈들
- js fetch API 를 사용하여 multipart/form-data 형식 데이터 전송시 수신이 정상적으로 이루어지지 않았던 문제
    - 원인 : Content-Type 이 multipart/form-data 형식일때 boundry 정의 문제
    - 해결 : Content-Type 를 정의하지 않을시 브라우저에서 자동으로 boundry 정의 [링크](https://boomrabbit.tistory.com/245)
- @Value 어노테이션으로 정의했던 필드값이 정상적으로 주입되지 않았던 문제
    - 원인 : interceptor 를 등록할 때 new 연산자를 이용하여 새로운 interceptor 를 등록했던 것이 원인
    - 해결 : 새로운 interceptor 를 등록하는 것이 아닌 주입받은 interceptor 를 등록함으로써 해결[링크](https://boomrabbit.tistory.com/247)
- pk 에 의존적인 test code 작성시 테스트가 깨지는 문제
    - 원인 : test code 작성시 특정 임의의 pk 값을 지정하여 테스트를 실행했던 것이 원인
    - 해결 : 특정 pk 에 의존적인 test code 를 작성하는 것이 아닌 객체를 save 하고 리턴된 값을 사용[링크](https://boomrabbit.tistory.com/246)
- MultipartFile 형태의 request dto 를 @Valid 어노테이션을 사용하여 유효성 체크를 못하는 문제
    - 해결 : 커스텀 어노테이션과 ConstraintValidator 를 상속받은 커스텀 validator 를 이용하여 유효성 체크 진행[링크](https://boomrabbit.tistory.com/256)
- 댓글 조회 api 를 호출했을 시 양방향 연관관계를 맺고 있던 member entity 에 대해 N+1 문제 발생
    - 해결 : fetch join 작성으로 해결
  ```
    /**
     * Fetch Join 으로 최적화
     */
    public List<Comment> findByAllComment(Long postId){
        return em.createQuery("SELECT c FROM Comment c " +
                "INNER JOIN FETCH c.member " +
                "WHERE c.post.id = :id")
                .setParameter("id",postId)
                .getResultList();
    }
  ```
- 게시글을 조회할 시에 어플리케이션 외부 경로에 저장되어 있는 이미지 파일 접근시 정상적인 접근 불가
  - 해결 : spring mvc 기능 중 하나인 ResourceHandler 를 통한 외부 경로 이미지 접근
   ```
    /**
     * 프로젝트 내부의 이미지가 아닌 외부이미지에 접속하기 위해선 리소스 핸들러를 정의해주어야 한다.
     * - ResourceHandlerRegistry 에 대해서 반드시 공부 필요 + WebMvcConfgiruer
     * 참고
     * - https://25gstory.tistory.com/87
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///D:/upload/2023/")
                .setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES));    //캐싱을 10분동안 사용하겠다
    }
   ```

## GIF
[메인화면]
![image](https://github.com/JipDol2/Jipdolstargram/assets/55746374/a3649c35-674a-4b3b-84c9-96d8a5b50a32)
[회원가입]
![image](https://github.com/JipDol2/Jipdolstargram/assets/55746374/b0fb640e-f7ff-4936-8173-b71e30c0d332)
[로그인 후 게시글 목록]
![image](https://github.com/JipDol2/Jipdolstargram/assets/55746374/499d3395-331e-4b6d-9c9d-bad0470799e3)
[게시글 선택]
![image](https://github.com/JipDol2/Jipdolstargram/assets/55746374/4aa42119-f540-4e12-845f-4c7598fa0802)
[타 계정으로 댓글달기]
![image](https://github.com/JipDol2/Jipdolstargram/assets/55746374/a33479c2-9854-48a2-896d-9b5af9e47b5f)

## ERD
![image](https://github.com/JipDol2/Jipdolstargram/assets/55746374/e825d4ad-4f04-4d6e-90fd-a885ea94bab6)

## Package 구조
```
📦src
 ┣ 📂main
 ┃ ┣ 📂generated
 ┃ ┣ 📂java
 ┃ ┃ ┗ 📂jipdol2
 ┃ ┃ ┃ ┗ 📂eunstargram
 ┃ ┃ ┃ ┃ ┣ 📂auth
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂request
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜LoginRequestDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂response
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜LoginCheckDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SessionResponseDTO.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜NoAuth.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RefreshToken.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RefreshTokenRepository.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜AuthController.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜AuthService.java
 ┃ ┃ ┃ ┃ ┣ 📂comment
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂request
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CommentSaveRequestDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂response
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CommentFindResponseDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ResultComments.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Comment.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CommentRepository.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜CommentController.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜CommentService.java
 ┃ ┃ ┃ ┃ ┣ 📂common
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜EmptyJSON.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜BaseTimeEntity.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜IndexController.java
 ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┣ 📂data
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserSession.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜AuthInterceptor.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜AuthResolver.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜P6SpySqlFormatter.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜WebMvcConfig.java
 ┃ ┃ ┃ ┃ ┣ 📂crypto
 ┃ ┃ ┃ ┃ ┃ ┗ 📜MyPasswordEncoder.java
 ┃ ┃ ┃ ┃ ┣ 📂exception
 ┃ ┃ ┃ ┃ ┃ ┣ 📂auth
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AccessTokenRenew.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ExpiredToken.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜InvalidToken.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MissAuthorized.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Unauthorized.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂file
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FileValidator.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ValidFile.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂image
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ImageFileArgumentNotValidation.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ProfileImageNotFound.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂member
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜InvalidSignInInformation.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberNotFound.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ValidationDuplicateMemberEmail.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ValidationDuplicateMemberNickname.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂post
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PostNotFound.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂response
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ErrorResponse.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜BaseException.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜GlobalExceptionController.java
 ┃ ┃ ┃ ┃ ┣ 📂image
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂request
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ImageRequestDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂response
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ImageResponseDTO.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Image.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ImageCode.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ImageJpaRepository.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜ImageService.java
 ┃ ┃ ┃ ┃ ┣ 📂jwt
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜TokenResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserSessionDTO.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜JwtManager.java
 ┃ ┃ ┃ ┃ ┣ 📂member
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂request
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberEmailRequestDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberLoginRequestDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberProfileImageDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberSaveRequestDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜MemberUpdateRequestDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂response
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberFindResponseDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberLoginResponseDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜MemberValidationCheckEmailDTO.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Member.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberJpaRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜MemberRepository.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberController.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜MemberService.java
 ┃ ┃ ┃ ┃ ┣ 📂post
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂request
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PostEditRequestDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PostSaveRequestDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂response
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PostResponseDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ResultPosts.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Post.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PostRepository.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜PostController.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜PostService.java
 ┃ ┃ ┃ ┃ ┣ 📜EunstargramApplication.java
 ┃ ┃ ┃ ┃ ┗ 📜JpaAuditingConfiguration.java
 ┃ ┗ 📂resources
 ┃ ┃ ┣ 📂data
 ┃ ┃ ┣ 📂static
 ┃ ┃ ┃ ┣ 📂css
 ┃ ┃ ┃ ┃ ┣ 📜common.css
 ┃ ┃ ┃ ┃ ┗ 📜posts.css
 ┃ ┃ ┃ ┣ 📂img
 ┃ ┃ ┃ ┃ ┣ 📂img_section
 ┃ ┃ ┃ ┃ ┃ ┣ 📜.DS_Store
 ┃ ┃ ┃ ┃ ┃ ┣ 📜img01.jpg
 ┃ ┃ ┃ ┃ ┃ ┣ 📜img02.jpg
 ┃ ┃ ┃ ┃ ┃ ┣ 📜img03.jpg
 ┃ ┃ ┃ ┃ ┃ ┣ 📜img04.jpg
 ┃ ┃ ┃ ┃ ┃ ┣ 📜img05.jpg
 ┃ ┃ ┃ ┃ ┃ ┣ 📜img06.png
 ┃ ┃ ┃ ┃ ┃ ┗ 📜img07.png
 ┃ ┃ ┃ ┃ ┣ 📜background01.png
 ┃ ┃ ┃ ┃ ┣ 📜background02.png
 ┃ ┃ ┃ ┃ ┣ 📜instagram.png
 ┃ ┃ ┃ ┃ ┣ 📜thumb.jpeg
 ┃ ┃ ┃ ┃ ┗ 📜여우티콘.jpg
 ┃ ┃ ┃ ┗ 📂js
 ┃ ┃ ┃ ┃ ┣ 📜common.js
 ┃ ┃ ┃ ┃ ┣ 📜index.js
 ┃ ┃ ┃ ┃ ┣ 📜login.js
 ┃ ┃ ┃ ┃ ┣ 📜posts.js
 ┃ ┃ ┃ ┃ ┣ 📜signUp-social.js
 ┃ ┃ ┃ ┃ ┗ 📜signUp.js
 ┃ ┃ ┣ 📂templates
 ┃ ┃ ┃ ┣ 📂fragments
 ┃ ┃ ┃ ┃ ┣ 📜footer.html
 ┃ ┃ ┃ ┃ ┗ 📜header.html
 ┃ ┃ ┃ ┣ 📂modal
 ┃ ┃ ┃ ┃ ┣ 📜postModal.html
 ┃ ┃ ┃ ┃ ┣ 📜postSettingModal.html
 ┃ ┃ ┃ ┃ ┣ 📜postUploadModal.html
 ┃ ┃ ┃ ┃ ┗ 📜profileImageUploadModal.html
 ┃ ┃ ┃ ┣ 📜index.html
 ┃ ┃ ┃ ┣ 📜login.html
 ┃ ┃ ┃ ┣ 📜posts.html
 ┃ ┃ ┃ ┣ 📜signUp-social.html
 ┃ ┃ ┃ ┗ 📜signUp.html
 ┃ ┃ ┣ 📜application-local.yml
 ┃ ┃ ┣ 📜application.yml
 ┃ ┃ ┗ 📜data.sql
 ┣ 📂test
 ┃ ┣ 📂java
 ┃ ┃ ┗ 📂jipdol2
 ┃ ┃ ┃ ┗ 📂eunstargram
 ┃ ┃ ┃ ┃ ┣ 📂auth
 ┃ ┃ ┃ ┃ ┃ ┗ 📜AuthControllerTest.java
 ┃ ┃ ┃ ┃ ┣ 📂comment
 ┃ ┃ ┃ ┃ ┃ ┗ 📜CommentControllerTest.java
 ┃ ┃ ┃ ┃ ┣ 📂member
 ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberControllerTest.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜MemberServiceTest.java
 ┃ ┃ ┃ ┃ ┗ 📂post
 ┃ ┃ ┃ ┃ ┃ ┣ 📜PostControllerTest.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜PostServiceTest.java
 ┃ ┗ 📂resources
 ┃ ┃ ┣ 📂img
 ┃ ┃ ┃ ┗ 📜testImage.jpg
 ┃ ┃ ┗ 📜application-test.yml
 ┗ 📜Eunstargram.txt
```
