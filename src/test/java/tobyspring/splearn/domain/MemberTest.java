package tobyspring.splearn.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {
    @Test
    void createMember() {
        Member member = new Member("toby@splearn.app", "Toby","secret");

        Assertions.assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

}