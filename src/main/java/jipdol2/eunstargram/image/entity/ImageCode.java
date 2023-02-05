package jipdol2.eunstargram.image.entity;

public enum ImageCode {

    PROFILE("프로필사진",1L),
    POST("게시글사진",2L);

    private String name;
    private Long code;

    ImageCode(String name, Long code) {
        this.name = name;
        this.code = code;
    }
}
