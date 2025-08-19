package br.com.dio.model;

import java.util.Collection;
import java.util.List;

import static br.com.dio.model.GameStatusEnum.*;

public class Board {

    private final List<List<Space>> spaces;

    public Board(List<List<Space>> spaces) {
        this.spaces = spaces;
    }

    public List<List<Space>> getSpaces() {
        return spaces;
    }

    public GameStatusEnum getStatus() {
        boolean anyFilled = spaces.stream()
                .flatMap(Collection::stream)
                .anyMatch(s -> s.getActual() != 0);

        if (!anyFilled) return NON_STARTED;

        boolean anyEmpty = spaces.stream()
                .flatMap(Collection::stream)
                .anyMatch(s -> s.getActual() == 0);

        return anyEmpty ? INCOMPLETE : COMPLETE;
    }

    public boolean hasErrors() {
        if (getStatus() == NON_STARTED) return false;

        return spaces.stream()
                .flatMap(Collection::stream)
                .anyMatch(s -> s.getActual() != 0 && s.getActual() != s.getExpected());
    }

    public boolean changeValue(int col, int row, int value) {
        Space space = spaces.get(col).get(row);
        if (space.isFixed()) return false;
        space.setActual(value);
        return true;
    }

    public boolean clearValue(int col, int row) {
        Space space = spaces.get(col).get(row);
        if (space.isFixed()) return false;
        space.clear();
        return true;
    }

    public void reset() {
        spaces.forEach(col -> col.forEach(Space::clear));
    }

    public boolean gameIsFinished() {
        return !hasErrors() && getStatus() == COMPLETE;
    }
}
