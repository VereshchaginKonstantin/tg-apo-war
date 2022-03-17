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
        value = init;
        timestamp = new Date();
    }

    Date timestamp;
    T value;
}
