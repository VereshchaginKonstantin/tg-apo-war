package ru.verekonn.telegram.appowar.utils;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class HistoryItem<T> {
    Date timestamp;
    T value;
}
