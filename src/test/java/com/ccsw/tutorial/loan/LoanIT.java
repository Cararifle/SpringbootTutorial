package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.common.pagination.PageableRequest;
import com.ccsw.tutorial.config.ResponsePage;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoanIT {

    private static final String LOCALHOST = "http://localhost:";
    private static final String SERVICE_PATH = "/loan";

    private static final int TOTAL_LOANS = 6;
    private static final int PAGE_SIZE = 5;

    private static final String ID_GAME_PARAM = "idGame";
    private static final String ID_CLIENT_PARAM = "idClient";
    private static final String DATE_PARAM = "date";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private final ParameterizedTypeReference<ResponsePage<LoanDto>> responseTypePage = new ParameterizedTypeReference<ResponsePage<LoanDto>>() {
    };

    private String getUrlWithParams() {
        return UriComponentsBuilder.fromHttpUrl(LOCALHOST + port + SERVICE_PATH).queryParam(ID_GAME_PARAM, "{" + ID_GAME_PARAM + "}").queryParam(ID_CLIENT_PARAM, "{" + ID_CLIENT_PARAM + "}").queryParam(DATE_PARAM, "{" + DATE_PARAM + "}")
                .encode().toUriString();
    }

    @Test
    public void findFirstPageShouldReturnFiveResults() {

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        Map<String, Object> params = new HashMap<>();
        params.put(ID_GAME_PARAM, null);
        params.put(ID_CLIENT_PARAM, null);
        params.put(DATE_PARAM, null);

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(TOTAL_LOANS, response.getBody().getTotalElements());
        assertEquals(PAGE_SIZE, response.getBody().getContent().size());
    }

    @Test
    public void findByGameFilterShouldReturnOneResult() {

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, TOTAL_LOANS));

        Map<String, Object> params = new HashMap<>();
        params.put(ID_GAME_PARAM, 1L);
        params.put(ID_CLIENT_PARAM, null);
        params.put(DATE_PARAM, null);

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    public void saveWithoutIdShouldCreateNewLoan() {

        LoanDto dto = buildLoanDto(1L, 1L, LocalDate.of(2026, 5, 10), LocalDate.of(2026, 5, 11));

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, TOTAL_LOANS + 1));

        Map<String, Object> params = new HashMap<>();
        params.put(ID_GAME_PARAM, null);
        params.put(ID_CLIENT_PARAM, null);
        params.put(DATE_PARAM, null);

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(TOTAL_LOANS + 1L, response.getBody().getTotalElements());
    }

    @Test
    public void saveWithInvalidDateRangeShouldReturnConflict() {

        LoanDto dto = buildLoanDto(1L, 1L, LocalDate.of(2026, 5, 10), LocalDate.of(2026, 5, 9));

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void deleteWithExistsIdShouldDeleteLoan() {

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/1", HttpMethod.DELETE, null, Void.class);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, TOTAL_LOANS));

        Map<String, Object> params = new HashMap<>();
        params.put(ID_GAME_PARAM, null);
        params.put(ID_CLIENT_PARAM, null);
        params.put(DATE_PARAM, null);

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(TOTAL_LOANS - 1L, response.getBody().getTotalElements());
    }

    private LoanDto buildLoanDto(Long gameId, Long clientId, LocalDate loanDate, LocalDate returnDate) {

        GameDto gameDto = new GameDto();
        gameDto.setId(gameId);

        ClientDto clientDto = new ClientDto();
        clientDto.setId(clientId);

        LoanDto dto = new LoanDto();
        dto.setGame(gameDto);
        dto.setClient(clientDto);
        dto.setLoanDate(loanDate);
        dto.setReturnDate(returnDate);

        return dto;
    }
}