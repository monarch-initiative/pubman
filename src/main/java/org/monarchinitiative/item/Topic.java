package org.monarchinitiative.item;

/**
 * Enumeration of article topics
 */
public enum Topic {
    RESOURCE("resource"), CLINICAL("clinical.use"),PHENOGENO("phenogeno.algorithm"),
    SYSTEMSBIO("systems.bio.algorithm"),COMMONDISEASE("common.disease"),CROSS_SPECIES("cross.species"),
    ENVIRONMENT("environment"),CANCER("cancer"),REVIEW("review"), UNKNOWN("unknown");


    private final String topicname;

    Topic(String name) {
         this.topicname=name;
    }
    @Override
    public String toString(){return topicname;}

    public static Topic fromString(String t) {
        switch (t) {
            case  "resource": return RESOURCE;
            case "clinical.use": return CLINICAL;
            case "phenogeno.algorithm": return PHENOGENO;
            case "systems.bio.algorithm": return SYSTEMSBIO;
            case "common.disease": return  COMMONDISEASE;
            case "cross.species": return CROSS_SPECIES;
            case  "environment": return ENVIRONMENT;
            case "cancer": return CANCER;
            case "review": return REVIEW;
            default: return UNKNOWN; // should never happen
        }

    }
}
