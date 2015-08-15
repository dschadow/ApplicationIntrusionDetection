package de.dominikschadow.javasecurity.duke.domain;

/**
 * Encounter likelihood enum.
 *
 * @author Dominik Schadow
 */
public enum Likelihood {
    ALL("*"), NOT_CONFIRMED("not confirmed"), PLAUSIBLE("plausible"), CONFIRMED("confirmed");

    private String name;

    Likelihood(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
