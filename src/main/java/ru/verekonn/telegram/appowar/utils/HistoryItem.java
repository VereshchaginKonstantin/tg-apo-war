package ru.verekonn.telegram.appowar.utils;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HistoryItem<T> implements History {
    public HistoryItem(T init) {
        this.value = init;
        this.timestamp = new Date();
        this.reported = false;
    }

    public HistoryItem(T init, Date timestamp) {
        this.value = init;
        this.timestamp = timestamp;
        this.reported = false;
    }

    Date timestamp;
    T value;
    boolean reported;
}
