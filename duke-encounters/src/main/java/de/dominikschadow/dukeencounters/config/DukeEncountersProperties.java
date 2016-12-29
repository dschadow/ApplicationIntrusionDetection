package de.dominikschadow.dukeencounters.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties holder for all {@code Duke Encounter} properties.
 *
 * @author Dominik Schadow
 */
@ConfigurationProperties(prefix = "duke.encounters")
public class DukeEncountersProperties {
    /**
     * The number of encounters to be shown in the latest encounters list.
     */
    private int latestAmount;
    private int passwordStrength;

    public int getLatestAmount() {
        return latestAmount;
    }

    public void setLatestAmount(int latestAmount) {
        this.latestAmount = latestAmount;
    }

    public int getPasswordStrength() {
        return passwordStrength;
    }

    public void setPasswordStrength(int passwordStrength) {
        this.passwordStrength = passwordStrength;
    }
}
