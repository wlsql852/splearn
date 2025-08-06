package tobyspring.splearn.application.member;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import tobyspring.splearn.application.member.provided.MemberFinder;
import tobyspring.splearn.application.member.provided.MemberRegister;
import tobyspring.splearn.application.member.required.EmailSender;
import tobyspring.splearn.application.member.required.MemberRepository;
import tobyspring.splearn.domain.member.*;
import tobyspring.splearn.domain.shared.Email;

@Service
@Transactional
@RequiredArgsConstructor
@Validated
public class MemberModifyService implements MemberRegister {
    private final MemberFinder memberFinder;
    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(MemberRegisterRequest resisterRequest) {
        checkDuplicateEmail(resisterRequest);

        Member member = Member.register(resisterRequest, passwordEncoder);

        memberRepository.save(member);

        sendWelcomeEmail(member);

        return member;

    }

    @Override
    public Member activate(Long memberId) {
        Member member = memberFinder.find(memberId);

        member.activate();
          //변경사항 저장
        return memberRepository.save(member);
    }

    @Override
    public Member deactivate(Long memberId) {
        Member member = memberFinder.find(memberId);
        member.deactivate();
        return memberRepository.save(member);
    }

    @Override
    public Member updateInfo(Long memberId, MemberInfoUpdateRequest memberInfoUpdateRequest) {
        Member member = memberFinder.find(memberId);
        checkDuplicateProfile(member, memberInfoUpdateRequest.profileAddress());
        member.updateInfo(memberInfoUpdateRequest);
        return memberRepository.save(member);
    }

    private void checkDuplicateProfile(Member member, String profileAddress) {
        if(profileAddress.isEmpty()) return;
        Profile currentProfile = member.getDetail().getProfile();
        if (currentProfile != null && member.getDetail().getProfile().address().equals(profileAddress)) return;
        if(memberRepository.findByProfile(new Profile(profileAddress)).isPresent()) {
            throw new DuplicateProfileException("이미 존재하는 프로필 주소입니다: " + profileAddress);
        }
    }

    private void sendWelcomeEmail(Member member) {
        emailSender.send(member.getEmail(), "등록을 완료해주세요", "아래 링크를 클릭해서 등록을 완료해주세요");
    }

    private void checkDuplicateEmail(MemberRegisterRequest resisterRequest) {
        if(memberRepository.findByEmail(new Email(resisterRequest.email())).isPresent()){
            throw new DuplicateEmailException("이미 사용중인 이메일입니다: "+ resisterRequest.email());
        }
    }




}
