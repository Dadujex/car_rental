package com.gabor.carrental.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SearchModel {

    private String dateRange;

    @Override
    public String toString() {
        return dateRange;
    }
}
