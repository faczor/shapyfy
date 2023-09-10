package com.sd.shapyfy;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface SystemTime {

    LocalDate today();

    LocalDateTime now();
}
