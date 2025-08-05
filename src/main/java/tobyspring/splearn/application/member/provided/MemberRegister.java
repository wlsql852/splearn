package tobyspring.splearn.application.member.provided;

/*
* 회원의 등록과 관련된 기능을 제공한다*/

import jakarta.validation.Valid;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberResisterRequest;

public interface MemberRegister {
    Member register(@Valid MemberResisterRequest resisterRequest);

    Member activate(Long memberId);

}
