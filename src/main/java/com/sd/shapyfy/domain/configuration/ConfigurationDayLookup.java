package com.sd.shapyfy.domain.configuration;

import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;
import com.sd.shapyfy.domain.plan.model.ConfigurationDayId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConfigurationDayLookup {

    private final ConfigurationDayFetcher configurationDayFetcher;

    public ConfigurationDay lookup(ConfigurationDayId configurationDayId) {
        log.info("Lookup {}", configurationDayId);

        return configurationDayFetcher.fetchForId(configurationDayId);
    }
}
