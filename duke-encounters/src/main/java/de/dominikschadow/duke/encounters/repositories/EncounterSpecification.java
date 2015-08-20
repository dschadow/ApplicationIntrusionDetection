package de.dominikschadow.duke.encounters.repositories;

import de.dominikschadow.duke.encounters.domain.Encounter;
import de.dominikschadow.duke.encounters.domain.SearchFilter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author Dominik Schadow
 */
public class EncounterSpecification {
    public static Specification<Encounter> findByFilter(SearchFilter filter) {
        return new Specification<Encounter>() {
            public Predicate toPredicate(Root<Encounter> root, CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                return cb.equal(root.<String>get("country"), filter.getCountry());
            }
        };
    }
}
