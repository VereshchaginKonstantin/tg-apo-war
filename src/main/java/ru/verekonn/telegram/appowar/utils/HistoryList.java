package ru.verekonn.telegram.appowar.utils;


import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HistoryList<T> extends ArrayList<T> {

    public HistoryList(T init) {
        add(init);
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.SECONDS);
    }

    public boolean hasChanges(Date timestamp) {
        return getDateDiff(getStart(),
                timestamp,
                TimeUnit.NANOSECONDS)
                >
                0;
    }

    public Date getStart() {
        return ((HistoryItemi) stream()
                .findFirst()
                .get()
        ).getTimestamp();
    }

    public Date getCurrentDate() {
        return  ((HistoryItemi) getCurrent())
                .getTimestamp();
    }

    public T getCurrent() {
        return get(size() - 1);
    }

    public T getPrev() {
        if (size() > 1) {
            return get(size() - 2);
        } else {
            return null;
        }
    }

    public long timeSpend() {
        return getDateDiff(getStart(), new Date(), TimeUnit.SECONDS);
    }
}
