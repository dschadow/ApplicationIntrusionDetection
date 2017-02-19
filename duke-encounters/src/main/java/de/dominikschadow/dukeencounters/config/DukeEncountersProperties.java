package de.dominikschadow.dukeencounters.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties holder for all {@code Duke Encounter} properties.
 *
 * @author Dominik Schadow
 */
@ConfigurationProperties(prefix = "duke.encounters")
@Data
public class DukeEncountersProperties {
    /**
     * The number of encounters to be shown in the latest encounters list.
     */
    private int latestAmount;
    private int passwordStrength;
}
