package de.dominikschadow.duke.encounters.repositories;

import de.dominikschadow.duke.encounters.domain.Encounter;
import de.dominikschadow.duke.encounters.domain.Likelihood;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author Dominik Schadow
 */
public class EncounterSpecification {
    public static Specification<Encounter> encounterAfterYear(int year) {
        // TODO date contains timestamp
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.<Integer>get("date"), year);
    }

    public static Specification<Encounter> encounterByConfirmations(int confirmations) {
        // TODO count confirmations
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.<Integer>get(""), confirmations);
    }

    public static Specification<Encounter> encounterByEvent(String event) {
        return (root, query, cb) -> cb.equal(root.<String>get("event"), event);
    }

    public static Specification<Encounter> encounterByLocation(String location) {
        return (root, query, cb) -> cb.equal(root.<String>get("location"), location);
    }

    public static Specification<Encounter> encounterByCountry(String country) {
        return (root, query, cb) -> cb.equal(root.<String>get("country"), country);
    }

    public static Specification<Encounter> encounterByLikelihood(Likelihood likelihood) {
        return (root, query, cb) -> cb.equal(root.<String>get("likelihood"), likelihood.getName());
    }
}
