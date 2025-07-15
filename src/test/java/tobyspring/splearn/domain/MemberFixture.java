package tobyspring.splearn.domain;

public class MemberFixture {
    public static MemberResisterRequest createMemberRegisterRequest() {
        return new MemberResisterRequest("toby@splearn.app", "Charlie", "verysecret");
    }

    public static MemberResisterRequest createMemberRegisterRequest(String email) {
        return new MemberResisterRequest(email, "Charlie", "verysecret");
    }

    public static PasswordEncoder createPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };
    }
}
