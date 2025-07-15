package tobyspring.splearn.application.provided;

import tobyspring.splearn.domain.Member;

/*
* 멤버를 조회한다.
* */
public interface MemberFinder {
    Member find(Long memberId);
}
