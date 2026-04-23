package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class LoanSpecification {

    public static Specification<Loan> hasClientId(Long clientId) {
        return ((root, query, criteriaBuilder) -> clientId == null ? null : criteriaBuilder.equal(root.get("client").get("id"), clientId));
    }

    public static Specification<Loan> hasGameId(Long gameId) {
        return ((root, query, criteriaBuilder) -> gameId == null ? null : criteriaBuilder.equal(root.get("game").get("id"), gameId));
    }

    public static Specification<Loan> hasDate(LocalDate date) {
        return ((root, query, criteriaBuilder) -> {
            if (date == null)
                return null;

            return criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), date), criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), date));
        });
    }
}
