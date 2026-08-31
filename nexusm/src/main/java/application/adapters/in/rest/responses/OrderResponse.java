package application.adapters.in.rest.responses;

import java.util.List;
import java.time.LocalDateTime;

/**
 * DTO de orden para respuestas REST.
 */
public record OrderResponse(String id, String buyerId, String status,
                            String totalAmount, String totalCurrency,
                            LocalDateTime createdAt, LocalDateTime updatedAt,
                            List<OrderItemResponse> items) {
}