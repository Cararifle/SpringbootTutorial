package com.ccsw.tutorial.game;

import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.game.model.GameDto;

import java.util.List;

public interface GameService {

    /**
     * Recuperar los juegos filtrando opcionalmente por titulo y/o categoria
     *
     * @param title titulo del juego
     * @param idCategory PK de la categoria
     * @return {@link List} de {@link Game}
     */
    List<Game> find(String title, Long idCategory);

    /**
     * Guarda o modifica un juego, dependiendo de si el identificador esta o no informado
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    void save(Long id, GameDto dto);

    /**
     * Recupera un {@link Game} a través de su ID
     *
     * @param id PK de la entidad
     * @return {@link Game}
     */
    Game get(Long id);
}
