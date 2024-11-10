package ru.clevertec.util.testdata;

import ru.clevertec.dto.request.Filter;

public class FilterTestData {

    public static Filter getFilter() {
        return Filter.builder()
                .part("hello")
                .build();
    }
}
