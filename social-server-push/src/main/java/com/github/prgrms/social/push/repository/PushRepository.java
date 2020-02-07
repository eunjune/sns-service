package com.github.prgrms.social.push.repository;

import com.github.prgrms.social.api.model.user.Subscription;

import java.util.Optional;

public interface PushRepository {

    Subscription save(Subscription subscription);

    Optional<Subscription> findByUserId(Long postWriterSeq);
}