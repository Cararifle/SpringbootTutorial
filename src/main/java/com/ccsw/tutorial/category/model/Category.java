package com.ccsw.tutorial.category.model;

import jakarta.persistence.*;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    /**
     *
     * @return id
     */
    public long getId() {
        return this.id;
    }

    /**
     *
     * @param id value of {@link #getId()}
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name value of {@link #getName()}
     */
    public void setName(String name) {
        this.name = name;
    }
}
