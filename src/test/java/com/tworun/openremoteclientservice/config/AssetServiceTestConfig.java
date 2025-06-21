package com.tworun.openremoteclientservice.config;

import com.tworun.openremoteclientservice.service.AssetService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AssetServiceTestConfig {

    @Bean
    public AssetService assetService() {
        return Mockito.mock(AssetService.class);
    }
}
