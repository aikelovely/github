package ru.alfabank.dmpr.model;

public class ChildEntityWithInfo extends ChildEntity {
    public String additionalInfo;

    @Override
    public String toString() {
        return "ChildEntity{" +
                "additionalInfo=" + additionalInfo +
                "} " + super.toString();
    }
}
