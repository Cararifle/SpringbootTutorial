package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.client.ClientService;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    GameService gameService;

    @Autowired
    ClientService clientService;

    @Autowired
    ModelMapper modelMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Long id, LoanDto dto) {
        Loan loan;

        if (id == null) {
            loan = new Loan();
        } else {
            loan = this.loanRepository.findById(id).orElse(null);
        }

        BeanUtils.copyProperties(dto, loan, "id", "game", "client");

        loan.setGame(gameService.get(dto.getGame().getId()));
        loan.setClient(clientService.get(dto.getClient().getId()));

        this.loanRepository.save(loan);
    }

    @Override
    public void delete(Long id) throws Exception {

        if (this.loanRepository.findById(id).orElse(null) == null) {
            throw new Exception("Not Exists");
        }

        this.loanRepository.deleteById(id);
    }

    @Override
    public Page<LoanDto> findPage(LoanSearchDto dto) {

        Specification<Loan> spec = (root, query, cb) -> cb.conjunction();

        if (dto.getClientId() != null) {
            spec = spec.and(LoanSpecification.hasClientId(dto.getClientId()));
        }

        if (dto.getGameId() != null) {
            spec = spec.and(LoanSpecification.hasGameId(dto.getGameId()));
        }

        if (dto.getDate() != null) {
            spec = spec.and(LoanSpecification.hasDate(dto.getDate()));
        }

        Page<Loan> page = loanRepository.findAll(spec, dto.getPageable().getPageable());

        return page.map(loan -> modelMapper.map(loan, LoanDto.class));
    }
}
