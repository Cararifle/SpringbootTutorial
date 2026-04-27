package com.ccsw.tutorial.loan.model;

import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.game.model.GameDto;

import java.time.LocalDate;

public class LoanDto {

    private Long id;
    private GameDto game;
    private ClientDto client;
    private LocalDate loanDate;
    private LocalDate returnDate;

    /**
     *
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id value of {@link #getId}
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return game
     */
    public GameDto getGame() {
        return game;
    }

    /**
     *
     * @param game value of {@link #getGame}
     */
    public void setGame(GameDto game) {
        this.game = game;
    }

    /**
     *
     * @return client
     */
    public ClientDto getClient() {
        return client;
    }

    /**
     *
     * @param client value of {@link #getClient}
     */
    public void setClient(ClientDto client) {
        this.client = client;
    }

    /**
     *
     * @return loanDate
     */
    public LocalDate getLoanDate() {
        return loanDate;
    }

    /**
     *
     * @param loanDate value of {@link #getLoanDate}
     */
    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    /**
     *
     * @return returnDate
     */
    public LocalDate getReturnDate() {
        return returnDate;
    }

    /**
     *
     * @param returnDate value of {@link #getReturnDate}
     */
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
