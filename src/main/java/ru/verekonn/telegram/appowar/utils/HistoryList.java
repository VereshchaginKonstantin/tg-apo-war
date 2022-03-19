package ru.verekonn.telegram.appowar.utils;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class HistoryList<T extends Reportable & History> extends ArrayList<T> {

    public HistoryList() {
    }

    public HistoryList(T init) {
        add(init);
    }

    public static long getDateDiff(Date date1, Date date2) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return diffInMillies;
    }

    public boolean hasChanges(Date timestamp) {
        return timestamp.before(getCurrentDate());
    }

    public Date getStart() {
        return  stream()
                .findFirst()
                .get()
                .getTimestamp();
    }

    public Date getCurrentDate() {
        return  getCurrent()
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

    public long timeSpendAfterLastChanges() {
        return getDateDiff(getCurrentDate(), new Date());
    }

    public List<T> getNotReported() {
        return stream()
                .filter(x -> !x.isReported())
                .collect(Collectors.toList());
    }
}
