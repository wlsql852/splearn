package tobyspring.splearn.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.springframework.util.Assert;

import java.util.Objects;

@Entity
@Getter
@ToString    //enum값 한글로 넣기
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NaturalIdCache //데이터를 읽어올때 이메일을 id로 읽어올 수 있음
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded   //Mysql 에서 지정하지 않은 타입이므로 Embedded-Embeddable 설정
    @NaturalId  //비즈니스적으로 의미가 있는 자연키
    private Email email;

    private String nickname;


    private String passwordHash;

//    @Getter(AccessLevel.NONE)  //getter를 안만듬
    @Enumerated(EnumType.STRING)
    private MemberStatus status;


    public static Member register(MemberResisterRequest createRequest, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        member.email = new Email(createRequest.email());
        member.nickname = Objects.requireNonNull(createRequest.nickname());
        member.passwordHash = Objects.requireNonNull(passwordEncoder.encode(createRequest.password()));
        member.status = MemberStatus.PENDING;
        return member;
    }

    public void activate() {
       // if (status != MemberStatus.PENDING) throw new IllegalStateException("Member is not Pending.");
        Assert.state(status == MemberStatus.PENDING, "Member is not Pending");
        this.status = MemberStatus.ACTIVE;
    }

    public void deactivate() {
        Assert.state(status == MemberStatus.ACTIVE, "Member is not Active");
        this.status = MemberStatus.DEACTIVATED;
    }

    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(passwordEncoder.encode(password), this.passwordHash);
    }

    public void changeNickname(String nickname) {
        this.nickname = Objects.requireNonNull(nickname);
    }

    public void changePassword(String password,  PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(Objects.requireNonNull(password));
    }

    public boolean isActive() {
        return this.status == MemberStatus.ACTIVE;
    }
}
