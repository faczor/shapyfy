package com.sd.shapyfy.domain.configuration;

import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;
import com.sd.shapyfy.domain.plan.model.ConfigurationDayId;

public interface ConfigurationDayFetcher {
    ConfigurationDay fetchForId(ConfigurationDayId configurationDayId);
}
