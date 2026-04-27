package com.ccsw.tutorial.loan.model;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.game.model.Game;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "loan")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "loan_date", nullable = false)
    private LocalDate loanDate;

    @Column(name = "return_date", nullable = false)
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
    public Game getGame() {
        return game;
    }

    /**
     *
     * @param game value of {@link #getGame}
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     *
     * @return client
     */
    public Client getClient() {
        return client;
    }

    /**
     *
     * @param client value of {@link #getClient}
     */
    public void setClient(Client client) {
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
