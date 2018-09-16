package de.dominikschadow.dukeencounters.encounter;

public class EncounterNotFoundException extends RuntimeException {
    public EncounterNotFoundException(String message) {
        super(message);
    }
}
