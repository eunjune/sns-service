package com.github.prgrms.social.push.Service;

import com.github.prgrms.social.api.error.NotFoundException;
import com.github.prgrms.social.api.event.CommentCreatedEvent;
import com.github.prgrms.social.api.model.user.Subscription;
import com.github.prgrms.social.push.repository.PushRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PushService {

    private final PushRepository pushRepository;

    public PushService(PushRepository pushRepository) {
        this.pushRepository = pushRepository;
    }

    // Subscribe 데이터 저장.
    @KafkaListener(containerFactory="kafkaListenerContainerSubscriptionFactory", topics = "${spring.kafka.topic.request}")
    @SendTo
    public Subscription listenSubscription(Subscription subscription){
        return pushRepository.save(subscription);
    }


    // 댓글 작성시 푸쉬 처리
    @KafkaListener(containerFactory="kafkaListenerContainerPushMessageFactory", topics = "${spring.kafka.topic.comment-created}")
    public void listenCommentCreated(CommentCreatedEvent commentCreatedEvent){

        pushRepository.findByUserId(commentCreatedEvent.getPostWriterSeq()).map(subscription -> {

            String message = String.format("%s -> %s가 코멘트를 달았습니다.\n"
                    ,subscription.getEndpoint(), commentCreatedEvent.getCommentWriter());
            System.out.println(message);

            return subscription;
        }).orElseThrow(() -> new NotFoundException(Long.class,String.valueOf(commentCreatedEvent.getPostWriterSeq())));
    }

}
