package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.client.ClientService;
import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    GameService gameService;

    @Autowired
    ClientService clientService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Loan> findFilteredPage(LoanSearchDto dto, Long idGame, Long idClient, LocalDate date) {

        Specification<Loan> spec = Specification.unrestricted();
        if (idGame != null) {
            spec = spec.and(new LoanSpecification(new SearchCriteria("game.id", ":", idGame)));
        }

        if (idClient != null) {
            spec = spec.and(new LoanSpecification(new SearchCriteria("client.id", ":", idClient)));
        }

        if (date != null) {
            spec = spec.and(LoanSpecification.dateBetween(date));
        }

        return this.loanRepository.findAll(spec, dto.getPageable().getPageable());

    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public void save(Long id, LoanDto dto) {
        Loan loan;
        Long dias;
        Specification<Loan> specLoanedGame = Specification.unrestricted();
        Specification<Loan> specClientLoanLimitPerDate = Specification.unrestricted();
        boolean gameAlreadyLoaned;

        if (id == null) {
            loan = new Loan();
        } else {
            loan = this.loanRepository.findById(id).orElse(null);
        }

        // Validar que la fecha de préstamo sea anterior a la fecha de devolución
        if (dto.getLoanDate().isAfter(dto.getReturnDate())) {
            throw new IllegalArgumentException("La fecha de préstamo debe ser anterior a la fecha de devolución.");
        }

        // Validar que el préstamo no dure más de 14 días
        dias = ChronoUnit.DAYS.between(dto.getLoanDate(), dto.getReturnDate()) + 1;

        if (dias > 14) {
            throw new IllegalArgumentException("El préstamo no puede durar más de 14 días.");
        }

        // Verificar que el juego no esté prestado en las fechas indicadas
        specLoanedGame = specLoanedGame.and(LoanSpecification.sameGame(dto.getGame().getId())).and(LoanSpecification.overlapDate(dto.getLoanDate(), dto.getReturnDate()));

        if (id != null) {
            specLoanedGame = specLoanedGame.and(LoanSpecification.excludeId(id));
        }

        gameAlreadyLoaned = this.loanRepository.count(specLoanedGame) > 0;

        if (gameAlreadyLoaned) {
            throw new IllegalArgumentException("El juego ya está prestado en las fechas indicadas.");
        }
        
        // Verificar que el cliente no tenga más de 2 juegos prestados en las fechas indicadas
        specClientLoanLimitPerDate = specClientLoanLimitPerDate.and(LoanSpecification.sameClient(dto.getClient().getId())).and(LoanSpecification.overlapDate(dto.getLoanDate(), dto.getReturnDate()));

        if (id != null) {
            specClientLoanLimitPerDate = specClientLoanLimitPerDate.and(LoanSpecification.excludeId(id));
        }

        List<Loan> loans = this.loanRepository.findAll(specClientLoanLimitPerDate);

        for (LocalDate day = dto.getLoanDate(); !day.isAfter(dto.getReturnDate()); day = day.plusDays(1)) {

            final LocalDate currentDay = day;

            long loansActiveThisDay = loans.stream().filter(p -> !p.getLoanDate().isAfter(currentDay) && !p.getReturnDate().isBefore(currentDay)).count();

            if (loansActiveThisDay + 1 > 2) {
                throw new IllegalArgumentException("El cliente tendría más de 2 juegos prestados el día " + currentDay);
            }
        }

        BeanUtils.copyProperties(dto, loan, "id", "game", "client");

        loan.setGame(gameService.get(dto.getGame().getId()));
        loan.setClient(clientService.get(dto.getClient().getId()));

        this.loanRepository.save(loan);
    }

    /**
     * {@inheritDoc}
     */
    public void delete(Long id) throws Exception {

        if (this.loanRepository.findById(id).orElse(null) == null) {
            throw new Exception("Not exists");
        } else {
            this.loanRepository.deleteById(id);
        }
    }
}
