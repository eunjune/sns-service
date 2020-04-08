package com.github.prgrms.social.api.validator;


import com.github.prgrms.social.api.model.api.request.user.CheckEmailRequest;
import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class CheckEmailValidator implements Validator {

    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return CheckEmailRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CheckEmailRequest checkEmailRequest = (CheckEmailRequest)target;

        if(userRepository.existsByEmail(new Email(checkEmailRequest.getAddress()))){
            errors.rejectValue("address", "invalid.email", new Object[]{checkEmailRequest.getAddress()}, "사용중인 이메일 입니다.");
        }
    }
}
