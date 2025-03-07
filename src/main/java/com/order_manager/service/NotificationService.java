package com.order_manager.service;

import com.order_manager.entity.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    @Async
    public void sendOrderStatusChangeNotification(String email, Long orderId, OrderStatus status) {
        log.info("Dispatch notification to {} about status of order #{}: {}", email, orderId, status);

        try {
            Thread.sleep(5000);
            log.info("Notification successfully pushed to {}", email);
        } catch (InterruptedException e) {
            log.error("Error while sending notification to {}: {}", email, e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("Unexpected error while sending notification to {}: {}", email, e.getMessage());
        }
    }
}

