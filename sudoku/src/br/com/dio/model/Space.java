package br.com.dio.model;

public class Space {

    private int actual;
    private final int expected;
    private final boolean fixed;

    public Space(int expected, boolean fixed) {
        this.expected = expected;
        this.fixed = fixed;
        this.actual = fixed ? expected : 0;
    }

    public int getActual() {
        return actual;
    }

    public void setActual(int actual) {
        if (!fixed) {
            this.actual = actual;
        }
    }

    public void clear() {
        if (!fixed) {
            this.actual = 0;
        }
    }

    public int getExpected() {
        return expected;
    }

    public boolean isFixed() {
        return fixed;
    }

    @Override
    public String toString() {
        return actual != 0 ? String.valueOf(actual) : "_";
    }
}
