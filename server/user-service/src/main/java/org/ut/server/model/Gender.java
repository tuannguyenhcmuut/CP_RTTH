package org.ut.server.model;

public enum Gender {
    MALE, FEMALE, OTHER;
    public String getGender() {
        return this.name();
    }
}
