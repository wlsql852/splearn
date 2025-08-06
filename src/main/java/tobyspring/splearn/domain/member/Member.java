package tobyspring.splearn.domain.member;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.springframework.util.Assert;
import tobyspring.splearn.domain.AbstractEntity;
import tobyspring.splearn.domain.shared.Email;

import java.util.Objects;

@Entity
@Getter
@ToString(callSuper = true, exclude = "detail")   //enum값 한글로 넣기
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NaturalIdCache //데이터를 읽어올때 이메일을 id로 읽어올 수 있음
public class Member extends AbstractEntity {
    @NaturalId  //비즈니스적으로 의미가 있는 자연키
    private Email email;

    private String nickname;

    private String passwordHash;

    private MemberStatus status;


    private MemberDetail detail;

    public static Member register(MemberRegisterRequest createRequest, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        member.email = new Email(createRequest.email());
        member.nickname = Objects.requireNonNull(createRequest.nickname());
        member.passwordHash = Objects.requireNonNull(passwordEncoder.encode(createRequest.password()));
        member.status = MemberStatus.PENDING;

        member.detail =  MemberDetail.create();
        return member;
    }

    public void activate() {
       // if (status != MemberStatus.PENDING) throw new IllegalStateException("Member is not Pending.");
        Assert.state(status == MemberStatus.PENDING, "Member is not Pending");
        this.status = MemberStatus.ACTIVE;
        this.detail.setActivatedAt();
    }

    public void deactivate() {
        Assert.state(status == MemberStatus.ACTIVE, "Member is not Active");
        this.status = MemberStatus.DEACTIVATED;
        this.detail.deactivate();
    }

    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(passwordEncoder.encode(password), this.passwordHash);
    }


    public void updateInfo(MemberInfoUpdateRequest updateRequest) {
        Assert.state(getStatus() == MemberStatus.ACTIVE, "등록 완료 상태가 아니면 정보를 수정할 수 없습니다.");
        this.nickname = updateRequest.nickname();
        this.detail.updateInfo(updateRequest);
    }

    public void changePassword(String password,  PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(Objects.requireNonNull(password));
    }

    public boolean isActive() {
        return this.status == MemberStatus.ACTIVE;
    }
}
