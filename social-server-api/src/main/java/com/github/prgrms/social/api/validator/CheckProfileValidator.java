package com.github.prgrms.social.api.validator;

import com.github.prgrms.social.api.model.api.request.user.ProfileRequest;
import com.github.prgrms.social.api.repository.user.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class CheckProfileValidator implements Validator {

    private final JpaUserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return ProfileRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProfileRequest profileRequest = (ProfileRequest) target;

        if(profileRequest.getName() != null && userRepository.existsByName(profileRequest.getName())){
            errors.rejectValue("name", "invalid.name", new Object[]{profileRequest.getName()}, "사용중인 이름 입니다.");
        }
    }

}
