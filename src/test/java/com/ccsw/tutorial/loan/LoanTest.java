package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.client.ClientService;
import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.common.pagination.PageableRequest;
import com.ccsw.tutorial.exception.BusinessConflictException;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanTest {

    private static final Long EXISTS_LOAN_ID = 1L;
    private static final Long GAME_ID = 1L;
    private static final Long CLIENT_ID = 1L;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private GameService gameService;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private LoanServiceImpl loanService;

    @Test
    public void findFilteredPageShouldCallRepositoryAndReturnData() {

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, 5));

        Page<Loan> page = new PageImpl<>(List.of(new Loan()), PageRequest.of(0, 5), 1);
        when(loanRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), any(Pageable.class))).thenReturn(page);

        Page<Loan> result = loanService.findFilteredPage(searchDto, GAME_ID, CLIENT_ID, LocalDate.of(2026, 4, 27));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(loanRepository).findAll(any(org.springframework.data.jpa.domain.Specification.class), any(Pageable.class));
    }

    @Test
    public void saveWithReturnDateBeforeLoanDateShouldThrowBusinessConflict() {

        LoanDto dto = buildLoanDto(LocalDate.of(2026, 5, 10), LocalDate.of(2026, 5, 9), GAME_ID, CLIENT_ID);

        BusinessConflictException exception = assertThrows(BusinessConflictException.class, () -> loanService.save(null, dto));

        assertEquals("RETURN_DATE_BEFORE_LOAN_DATE", exception.getErrorCode());
    }

    @Test
    public void saveWithLoanPeriodExceeding14DaysShouldThrowBusinessConflict() {

        LoanDto dto = buildLoanDto(LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 15), GAME_ID, CLIENT_ID);

        BusinessConflictException exception = assertThrows(BusinessConflictException.class, () -> loanService.save(null, dto));

        assertEquals("LOAN_PERIOD_EXCEEDS_LIMIT", exception.getErrorCode());
    }

    @Test
    public void saveWithGameAlreadyLoanedShouldThrowBusinessConflict() {

        LoanDto dto = buildLoanDto(LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 2), GAME_ID, CLIENT_ID);
        when(loanRepository.count(any(org.springframework.data.jpa.domain.Specification.class))).thenReturn(1L);

        BusinessConflictException exception = assertThrows(BusinessConflictException.class, () -> loanService.save(null, dto));

        assertEquals("GAME_ALREADY_LOANED", exception.getErrorCode());
    }

    @Test
    public void saveValidLoanShouldPersist() {

        LoanDto dto = buildLoanDto(LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 2), GAME_ID, CLIENT_ID);
        when(loanRepository.count(any(org.springframework.data.jpa.domain.Specification.class))).thenReturn(0L);
        when(loanRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class))).thenReturn(List.of());

        Game game = new Game();
        game.setId(GAME_ID);
        Client client = new Client();
        client.setId(CLIENT_ID);

        when(gameService.get(GAME_ID)).thenReturn(game);
        when(clientService.get(CLIENT_ID)).thenReturn(client);

        ArgumentCaptor<Loan> loanCaptor = ArgumentCaptor.forClass(Loan.class);

        loanService.save(null, dto);

        verify(loanRepository).save(loanCaptor.capture());
        Loan persistedLoan = loanCaptor.getValue();

        assertEquals(GAME_ID, persistedLoan.getGame().getId());
        assertEquals(CLIENT_ID, persistedLoan.getClient().getId());
        assertEquals(LocalDate.of(2026, 5, 1), persistedLoan.getLoanDate());
        assertEquals(LocalDate.of(2026, 5, 2), persistedLoan.getReturnDate());
    }

    @Test
    public void deleteExistsLoanIdShouldDeleteLoan() throws Exception {

        when(loanRepository.findById(EXISTS_LOAN_ID)).thenReturn(Optional.of(new Loan()));

        loanService.delete(EXISTS_LOAN_ID);

        verify(loanRepository).deleteById(EXISTS_LOAN_ID);
    }

    private LoanDto buildLoanDto(LocalDate loanDate, LocalDate returnDate, Long gameId, Long clientId) {

        GameDto gameDto = new GameDto();
        gameDto.setId(gameId);

        ClientDto clientDto = new com.ccsw.tutorial.client.model.ClientDto();
        clientDto.setId(clientId);

        LoanDto dto = new LoanDto();
        dto.setGame(gameDto);
        dto.setClient(clientDto);
        dto.setLoanDate(loanDate);
        dto.setReturnDate(returnDate);

        return dto;
    }
}