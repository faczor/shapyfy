package com.shapyfy.core.boundary.api.dashboard;

import com.shapyfy.core.boundary.api.TokenUtils;
import com.shapyfy.core.boundary.api.dashboard.adapter.DashboardApiAdapter;
import com.shapyfy.core.boundary.api.dashboard.model.DashboardContract;
import com.shapyfy.core.domain.model.UserId;
import com.shapyfy.core.util.DateRange;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboards")
public class DashboardController {

    private final DashboardApiAdapter dashboardApiAdapter;

    @GetMapping
    public ResponseEntity<DashboardContract> getDashboardForUser(
            @RequestParam(name = "from") LocalDate from,
            @RequestParam(name = "to") LocalDate to) {
        UserId userId = TokenUtils.currentUserId();
        log.debug("Fetching dashboard for user: {} from: {} to: {}", userId, from, to);
        DashboardContract dashboardForUser = dashboardApiAdapter.getDashboardForUser(userId, new DateRange(from, to));
        log.debug("Fetched dashboard for user: {} from: {} to: {}", userId, from, to);
        return ResponseEntity.ok(dashboardForUser);
    }
}
