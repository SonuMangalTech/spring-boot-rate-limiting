package com.sonumangal.tech.service;

import com.sonumangal.tech.entity.ApiEntity;
import com.sonumangal.tech.entity.UserEntity;
import com.sonumangal.tech.model.Constant;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {
    private final ConcurrentHashMap<String, Bucket> bucketMap = new ConcurrentHashMap<>();


    public boolean validateRateLimit(String apiName, UserEntity userEntity) {

        String bucketKey = userEntity.getUserKey() + ":" + apiName;
        Bucket bucket = bucketMap.computeIfAbsent(bucketKey,
                create -> createBucket(userEntity.getApiEntities().stream().filter(x -> x.getApiName().equals(apiName)).map(ApiEntity::getRateLimit).findFirst().get()));

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(Constant.TOKEN_CONSUME);

        System.out.println("Bucket Object: " + System.identityHashCode(bucket));

        System.out.println("Bucket Key: " + bucketKey);

        System.out.println("Consumed: " + probe.isConsumed());

        System.out.println("Remaining Tokens : " + probe.getRemainingTokens());

        return probe.isConsumed();
    }

    // Refill value can also fetch from Api_rate_limit repository, create new repository and call db
    private Bucket createBucket(Integer capacity) {
        Bandwidth bandwidth = Bandwidth.builder()
                .capacity(capacity)
                .refillGreedy(
                        Constant.REFILL_CAPACITY,
                        Duration.ofSeconds(Constant.REFILL_TIME)
                ).build();

        return Bucket.builder()
                .addLimit(bandwidth)
                .build();
    }
}
