package tobyspring.splearn.application.provided;

/*
* 회원의 등록과 관련된 기능을 제공한다*/

import jakarta.validation.Valid;
import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberResisterRequest;

public interface MemberRegister {
    Member register(@Valid MemberResisterRequest resisterRequest);

    Member activate(Long memberId);

}
