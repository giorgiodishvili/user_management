package com.gv.user.management.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class HazelcastConfiguration {

    @Bean
    public HazelcastInstance hazelcastClient(@Value("${hazelcast.network.address}") final String hazelcastAddress) {

        final ClientConfig clientConfig = new ClientConfig();
        clientConfig.getNetworkConfig().addAddress(hazelcastAddress);

        return HazelcastClient.newHazelcastClient(clientConfig);
    }

    @Bean
    public HazelcastCacheManager cacheManager(final HazelcastInstance hazelcastInstance) {
        return new HazelcastCacheManager(hazelcastInstance);
    }
}
