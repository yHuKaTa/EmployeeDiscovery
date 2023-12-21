package com.sirma.exam.utils;

import java.util.*;

public class TripleMap<Key1, Key2, Value> {
    private final List<Value> values = new ArrayList<>();

    private final Map<Key1, Map<Key2, Integer>> matching = new HashMap<>();

    public TripleMap() {}

    private Integer findIndexByKey(Key1 key1, Key2 key2) {
        if (Objects.nonNull(key1) && Objects.nonNull(matching.get(key1)) && matching.containsKey(key1)) {
            if (Objects.nonNull(key2) && Objects.nonNull(matching.get(key1)) && matching.get(key1).containsKey(key2)) {
                if (Objects.nonNull(matching.get(key1).get(key2))) {
                    return matching.get(key1).get(key2);
                }
            }
        }
        return null;
    }

    public void addValue(Key1 key1, Key2 key2, Value value) {
        if (Objects.nonNull(key1) && Objects.nonNull(key2)) {
            if (contains(key1, key2)) {
                Integer index = findIndexByKey(key1, key2);
                if (Objects.nonNull(index)) {
                    int i = index;
                    values.remove(i);
                    values.add(value);
                    i = values.size() - 1;
                    matching.computeIfAbsent(key1, k -> new HashMap<>()).put(key2, i);
                } else throw new NoSuchElementException("Unable to edit element from TripleMap");
                return;
            }
            values.add(value);
            int index = values.size() - 1;
            matching.computeIfAbsent(key1, k -> new HashMap<>()).put(key2, index);
        }
    }

    public Value get(Key1 key1, Key2 key2) {
        Integer index = findIndexByKey(key1, key2);
        if (Objects.nonNull(index)) {
            return values.get(index);
        } else {
            return null;
        }
    }

    public List<Value> getAll(Key1 key1) {
        Map<Key2, Integer> key2IndexMap;
        if (Objects.nonNull(key1)) {
            key2IndexMap = matching.get(key1);
        } else {
            return Collections.emptyList();
        }
        List<Value> searched = new ArrayList<>();
        for (Integer index : key2IndexMap.values()) {
            searched.add(values.get(index));
        }
        return searched;
    }

    public void remove(Key1 key1) {
        if (contains(key1)) {
            for (Map.Entry<Key2, Integer> entry : matching.get(key1).entrySet()) {
                if (Objects.nonNull(entry.getValue())) {
                    int index = entry.getValue();
                    values.remove(index);
                }
            }
            matching.remove(key1);
        }
    }

    public void remove(Key1 key1, Key2 key2) {
        if (contains(key1, key2)) {
            if (Objects.nonNull(matching.get(key1).get(key2))) {
                int index = matching.get(key1).get(key2);
                values.remove(index);
                matching.get(key1).remove(key2);
            }
        }
    }

    public boolean contains(Key1 key1, Key2 key2) {
        if (Objects.nonNull(key1) && Objects.nonNull(key2)) {
            return matching.containsKey(key1) && matching.get(key1).containsKey(key2);
        }
        return false;
    }

    ;

    public boolean contains(Key1 key1) {
        if (Objects.nonNull(key1)) {
            return matching.containsKey(key1);
        }
        return false;
    }

    public boolean isEmpty() {
        return matching.isEmpty() && values.isEmpty();
    }
}
