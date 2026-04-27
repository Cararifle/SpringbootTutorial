package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface LoanService {

    Page<Loan> findFilteredPage(LoanSearchDto dto, Long idGame, Long idClient, LocalDate date);

    void save(Long id, LoanDto dto);

    void delete(Long id) throws Exception;
}
