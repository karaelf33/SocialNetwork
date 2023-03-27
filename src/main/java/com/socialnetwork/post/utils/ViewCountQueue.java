package com.socialnetwork.post.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;


public class ViewCountQueue<T extends ViewCountable> {
    private final List<T> items;
    private final int maxSize;

    public ViewCountQueue(int maxSize) {
        this.maxSize = maxSize;
        this.items = new ArrayList<>(maxSize);
    }

    public void add(T item) {
        if (items.size() < maxSize) {
            items.add(item);
        } else {
            T min = getMinItem(item);
            if (item.getViewCount() > min.getViewCount()) {
                items.remove(min);
                items.add(item);
            }
        }
        items.sort(Comparator.comparingLong(ViewCountable::getViewCount).reversed());
    }

    private T getMinItem(T item) {
        return items.stream()
                .min(Comparator.comparingLong(ViewCountable::getViewCount))
                .orElseThrow(NoSuchElementException::new);
    }

    public List<T> getItems() {
        return items;
    }

    public void addAll(List<T> itemList) {
        for (T item : itemList) {
            add(item);
        }
    }

    public void deleteAll() {
        items.clear();
    }

    public void swap(int index1, int index2) {
        T temp = items.get(index1);
        items.set(index1, items.get(index2));
        items.set(index2, temp);
    }
}