package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface LoanService {

    /**
     * Metodo para recuperar una pagina filtrada de {@link Loan}
     *
     * @return {@link Page} of {@link Loan}
     */
    Page<Loan> findFilteredPage(LoanSearchDto dto, Long idGame, Long idClient, LocalDate date);

    /**
     * Metodo para crear o actualizar un {@link Loan}
     *
     * @param id  PK de la entidad
     * @param dto datos de la entidad
     */
    void save(Long id, LoanDto dto);

    /**
     * Metodo par eliminar un {@link Loan}
     *
     * @param id PK de la entidad
     */
    void delete(Long id) throws Exception;
}
