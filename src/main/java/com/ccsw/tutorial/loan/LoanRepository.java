package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface LoanRepository extends CrudRepository<Loan, Long>, JpaSpecificationExecutor<Loan> {

    /**
     * Metodo para recuperar un listado paginado de {@link Loan}
     *
     * @param pageable pageable
     * @return {@link Page} de {@link Loan}
     */
    Page<LoanDto> findAll(Pageable pageable);
}
