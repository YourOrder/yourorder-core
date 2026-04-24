package org.example.yourordercore.order.specification;
import org.example.yourordercore.order.dto.OrderFilter;
import org.example.yourordercore.order.entity.OrderEntity;
import org.example.yourordercore.order.entity.ProductEntity;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {
    public static Specification<OrderEntity> withFilter(OrderFilter filter) {
        return (root, query, cb) -> {
            var predicate = cb.conjunction();

            if (filter.getUserId() != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("userId"), filter.getUserId()));
            }

            if (filter.getStatus() != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("status"), filter.getStatus()));
            }

            return predicate;
        };
    }
}
