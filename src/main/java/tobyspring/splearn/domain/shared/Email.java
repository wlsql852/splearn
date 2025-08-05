package tobyspring.splearn.domain.shared;

import java.util.regex.Pattern;

public record Email(String address) {
    public Email {
        Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
        if(!EMAIL_PATTERN.matcher(address).matches()) {
            throw new IllegalArgumentException("이메일 형식이 바르지 않습니다 : "+address);
        }
    }
}
