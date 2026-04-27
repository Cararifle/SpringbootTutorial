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

@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    GameService gameService;

    @Autowired
    ClientService clientService;

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

    public void save(Long id, LoanDto dto) {
        Loan loan;
        Long dias;

        if (id == null) {
            loan = new Loan();
        } else {
            loan = this.loanRepository.findById(id).orElse(null);
        }

        if (loan.getLoanDate().isAfter(loan.getReturnDate())) {
            throw new IllegalArgumentException("La fecha de préstamo debe ser anterior a la fecha de devolución.");
        }

        dias = ChronoUnit.DAYS.between(loan.getLoanDate(), loan.getReturnDate());

        if (dias > 14) {
            throw new IllegalArgumentException("El préstamo no puede durar más de 14 días.");
        }

        BeanUtils.copyProperties(dto, loan, "id", "game", "client");

        loan.setGame(gameService.get(dto.getGame().getId()));
        loan.setClient(clientService.get(dto.getClient().getId()));

        this.loanRepository.save(loan);
    }

    public void delete(Long id) throws Exception {

        if (this.loanRepository.findById(id).orElse(null) == null) {
            throw new Exception("Not exists");
        } else {
            this.loanRepository.deleteById(id);
        }
    }
}
