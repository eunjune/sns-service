package com.github.prgrms.social.api.validator;

import com.github.prgrms.social.api.model.api.request.user.CheckNameRequest;
import com.github.prgrms.social.api.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class CheckNameValidator implements Validator {

    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return CheckNameRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CheckNameRequest checkNameRequest = (CheckNameRequest) target;

        if(userRepository.existsByName(checkNameRequest.getName())){
            errors.rejectValue("name", "invalid.name", new Object[]{checkNameRequest.getName()}, "사용중인 이름 입니다.");
        }
    }
}
