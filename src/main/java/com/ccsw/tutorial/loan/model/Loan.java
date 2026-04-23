package com.ccsw.tutorial.loan.model;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.game.model.Game;
import jakarta.persistence.*;

import java.util.Date;

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

    @Column(name = "startDate", nullable = false)
    private Date startDate;

    @Column(name = "endDate", nullable = false)
    private Date endDate;

    /**
     *
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id new value of {@link @getId}
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
     * @param game new value of {@link @getGame}
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
     * @param client new value of {@link @getClient}
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     *
     * @return startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     *
     * @param startDate new value of {@link @getStartDate}
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     *
     * @return endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     *
     * @param endDate new value of {@link @getEndDate}
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
