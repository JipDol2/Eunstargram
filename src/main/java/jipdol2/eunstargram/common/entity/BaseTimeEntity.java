package jipdol2.eunstargram.common.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter @Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    /**
     * @MpaaerSuperclass : 공통 매핑 정보가 필요할때 사용한다. createDate 와 modifiedDate는 지속적으로 사용되는 변수여서 BaseEntity 로 선언한 뒤 상속을 받게 한다.
     * 조회 및 검색이 불가하고 직접 생성해서 사용할 일이 없기 때문에 추상 클래스(abstract class)로 만드는 것이 좋다.
     */

    /**
     * @EntityListeners(AuditingEntityListener.class) : Spring Data JPA 에서 지원해주는 기능으로 생성일,수정일과 같은 기록들을 편리하게 관리할 수 있도록 지원해준다.
     * Audit 기능 : spring data jpa 에서 시간에 대한 정보들을 자동으로 넣어주는 기능
     * @CreatedDate : Entity가 생성되어 저장될 때 시간이 자동 저장
     * @LastModifiedDate : 조회한 Entity의 값을 변경할 때 시간이 자동 저장
     */

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
