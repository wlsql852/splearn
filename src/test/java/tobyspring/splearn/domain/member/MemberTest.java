package tobyspring.splearn.domain.member;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tobyspring.splearn.domain.member.MemberFixture.createMemberRegisterRequest;
import static tobyspring.splearn.domain.member.MemberFixture.createPasswordEncoder;

class MemberTest {
    Member member;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = createPasswordEncoder();
        member = member.register(createMemberRegisterRequest(), passwordEncoder);
    }



    @Test
    void registerMember() {
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
        assertThat(member.getDetail().getRegisteredAt()).isNotNull();
     }

    @Test
    void constructorNullCheck() {
        assertThatThrownBy(() -> Member.register(createMemberRegisterRequest(null), passwordEncoder))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void activate() {
        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getDetail().getActivatedAt()).isNotNull();
    }

    @Test
    void activateFail() {
        member.activate();

        assertThatThrownBy(() -> member.activate())
                .isInstanceOf(IllegalStateException.class);

    }

    @Test
    void deactivateFail() {

        assertThatThrownBy(() -> member.deactivate())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void verifyPassword() {
        assertThat(member.verifyPassword("verysecret", passwordEncoder)).isTrue();
        assertThat(member.verifyPassword("hello", passwordEncoder)).isFalse();
    }

    @Test
    void changePassword() {
        member.changePassword("verysecret2", passwordEncoder);
        assertThat(member.verifyPassword("verysecret2", passwordEncoder)).isTrue();
    }

    @Test
    void deactivate() {
        member.activate();
        member.deactivate();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
        assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
    }

    @Test
    void shouldBeActive() {
        member.activate();
        assertThat(member.isActive()).isTrue();
    }

    @Test
    void invalidEmail() {
        assertThatThrownBy(()->
                Member.register(createMemberRegisterRequest("invalid email"), passwordEncoder)
        ).isInstanceOf(IllegalArgumentException.class);

        Member.register(createMemberRegisterRequest(), passwordEncoder);
    }
    
    @Test
    void updateInfo() {
        member.activate();

        var request = new MemberInfoUpdateRequest("Leo","toby", "자기소개");
        member.updateInfo(request);

        assertThat(member.getNickname()).isEqualTo(request.nickname());
        assertThat(member.getDetail().getProfile().address()).isEqualTo(request.profileAddress());
        assertThat(member.getDetail().getIntroduction()).isEqualTo(request.introduction());
    }
    @Test
    void updateInfoFail() {
        AssertionsForClassTypes.assertThatThrownBy(()->  {
            var request = new MemberInfoUpdateRequest("Leo","toby100", "자기소개");
            member.updateInfo(request);
        }).isInstanceOf(IllegalStateException.class);
    }
}