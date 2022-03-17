package ru.verekonn.telegram.appowar.utils;


import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HistoryList<T> extends ArrayList<HistoryItem<T>> {

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.SECONDS);
    }

    public boolean addValue(T end) {
        return add(new HistoryItem<>(new Date(), end));
    }

    public Date getStart() {
        return stream()
                .findFirst()
                .get()
                .getTimestamp();
    }

    public long timeSpend() {
        return getDateDiff(getStart(), new Date(), TimeUnit.SECONDS);
    }
}
