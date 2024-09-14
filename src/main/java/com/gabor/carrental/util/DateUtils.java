package com.gabor.carrental.util;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DateUtils {

    public static List<Date> dateRangeToDates(String dateRange) {

        String[] dates = dateRange.split("->");
        String[] start = dates[0].split("-");
        String[] end = dates[1].split("-");

        Date startDate = new GregorianCalendar(Integer.parseInt(start[0]),
                Integer.parseInt(start[1]) - 1, Integer.parseInt(start[2])).getTime();

        Date endDate = new GregorianCalendar(Integer.parseInt(end[0]),
                Integer.parseInt(end[1]) - 1, Integer.parseInt(end[2])).getTime();

        List<Date> res = new ArrayList<>();
        res.add(startDate);
        res.add(endDate);

        return res;
    }

    public static int dayBetweenDates(String dateRange) {
        String[] dates = dateRange.split("->");
        LocalDate start = LocalDate.parse(dates[0]);
        LocalDate end = LocalDate.parse(dates[1]);

        Period period = Period.between(start, end);
        return Math.abs(period.getDays()) + 1;
    }
}
