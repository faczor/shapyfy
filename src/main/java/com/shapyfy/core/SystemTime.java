package com.shapyfy.core;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface SystemTime {
    LocalDate today();

    LocalDateTime now();
}
