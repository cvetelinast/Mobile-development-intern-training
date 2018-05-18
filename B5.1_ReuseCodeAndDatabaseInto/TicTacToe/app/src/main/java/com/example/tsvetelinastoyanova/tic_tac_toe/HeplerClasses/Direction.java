package com.example.tsvetelinastoyanova.tic_tac_toe.HeplerClasses;

public enum Direction {
    HORIZONTAL(0), VERTICAL(1), UPDOWNDIAGONAL(2), DOWNUPDIAGONAL(3);

    private final int id;

    Direction(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
