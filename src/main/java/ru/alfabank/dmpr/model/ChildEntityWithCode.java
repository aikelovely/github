package ru.alfabank.dmpr.model;

public class ChildEntityWithCode extends ChildEntity {
    public String code;

    @Override
    public String toString() {
        return "ChildEntity{" +
                "code=" + code +
                "} " + super.toString();
    }
}
