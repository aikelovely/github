package ru.alfabank.dmpr.model;

import java.util.List;

public class Select2QueryResult {
    public boolean more;
    public List<Select2Option> results;

    public Select2QueryResult(boolean more, List<Select2Option> results) {
        this.more = more;
        this.results = results;
    }
}

