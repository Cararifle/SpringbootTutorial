package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LoanService {

    /**
     * Metodo para recuperar un listado paginado de {@link Loan}
     *
     * @param dto dto de busqueda
     * @return {@link Page} de {@link Loan}
     */
    Page<Loan> findPage(LoanSearchDto dto);

    /**
     * Metodo para crear o actualizar un {@link Loan}
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    void save(Long id, LoanDto dto);

    /**
     * Metodo para eliminar un {@link Loan}
     *
     * @param id PK de la entidad
     */
    void delete(Long id) throws Exception;

    /**
     * Recuperar los prestamos filtrando opcionalmente por titulo y/o categoria
     *
     * @param idGame PK del juego
     * @param idClient PK del cliente
     * @return {@link List} de {@link Loan}
     */
    List<Loan> find(Long idGame, Long idClient);
}
