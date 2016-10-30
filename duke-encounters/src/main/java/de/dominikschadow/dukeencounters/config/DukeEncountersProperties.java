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
    private int passwordStrengh;

    public int getLatestAmount() {
        return latestAmount;
    }

    public void setLatestAmount(int latestAmount) {
        this.latestAmount = latestAmount;
    }

    public int getPasswordStrengh() {
        return passwordStrengh;
    }

    public void setPasswordStrengh(int passwordStrengh) {
        this.passwordStrengh = passwordStrengh;
    }
}
