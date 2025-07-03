package tobyspring.splearn.domain;

import lombok.Getter;

import java.util.Objects;


@Getter
public class Member {
    private String email;

    private String nickname;


    private String passwordHash;

    private MemberStatus status;

    public Member(String email, String nickname, String passwordHash) {
        this.email = Objects.requireNonNull(email);   //requireNonNull() : 널값이 들어오면 실행 안함
        this.nickname = Objects.requireNonNull(nickname);
        this.passwordHash = Objects.requireNonNull(passwordHash);
        this.status = MemberStatus.PENDING;
    }

}
