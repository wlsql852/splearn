package tobyspring.splearn.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.Objects;


@Getter
@ToString    //enum값 한글로 넣기
public class Member {
    private Email email;

    private String nickname;


    private String passwordHash;

//    @Getter(AccessLevel.NONE)  //getter를 안만듬
    private MemberStatus status;


//    private Member(String email, String nickname, String passwordHash) {   //@NonNull을 위해 @NotNull String email,...을 사용할수도 있음
//        this.email = Objects.requireNonNull(email);   //requireNonNull() : 널값이 들어오면 실행 안함
//        this.nickname = Objects.requireNonNull(nickname);
//        this.passwordHash = Objects.requireNonNull(passwordHash);
//        this.status = MemberStatus.PENDING;
//    }

//    public static Member create(String email, String nickname, String password, PasswordEncoder passwordEncoder) {
//        return new Member(email, nickname, passwordEncoder.encode(password));
//    }

    private Member() {}

    public static Member create(MemberCreateRequest createRequest, PasswordEncoder passwordEncoder) {
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
