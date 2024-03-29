package me.jvlk.dius.store.models;

import java.util.Collection;
import java.util.Objects;

/**
 * Immutable value object representing a
 */
public class MonetaryAmount implements Comparable<MonetaryAmount> {
    private int cents;

    public MonetaryAmount(double value) {
        if (value < 0) throw new IllegalArgumentException(String.format("MonetaryAmount must be >= 0 (%f)", value));
        this.cents = (int) (value * 100);
    }

    public MonetaryAmount vary(double ratio) {
        return new MonetaryAmount((cents * ratio)/100);
    }

    // FUTURE - gst settings
    // FUTURE - support multiple currencies - issues: conversion, validation, time differences?

    public static MonetaryAmount sum(Collection<MonetaryAmount> amounts) {
        return amounts.stream().reduce(FREE, (a, b) -> new MonetaryAmount((a.cents + b.cents)/100D));
    }

    public static final MonetaryAmount FREE = new MonetaryAmount(0);

    @Override
    public String toString() {
        return String.format("$%,.02f", cents / 100D);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonetaryAmount that = (MonetaryAmount) o;
        return cents == that.cents;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cents);
    }


    @Override
    public int compareTo(MonetaryAmount other) {
        return Integer.compare(this.cents, other.cents);
    }

    public double asRatioOf(MonetaryAmount other) {
        return this.cents * 1.0D / other.cents;
    }

    public boolean isFree() {
        return cents <= 0;
    }
}
