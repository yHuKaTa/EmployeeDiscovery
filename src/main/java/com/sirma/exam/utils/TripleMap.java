package com.sirma.exam.utils;

import java.util.*;

/**
 * The TripleMap class represents a data structure that associates two keys (Key1 and Key2) with a corresponding value.
 * It allows for efficient retrieval, addition, and removal of values based on the two keys.
 *
 * @param <Key1> The type of the first key.
 * @param <Key2> The type of the second key.
 * @param <Value> The type of the value associated with the keys.
 */
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

    /**
     * Adds a value to the TripleMap, associating it with the specified Key1 and Key2.
     * If the combination of keys already exists, the existing value is replaced.
     *
     * @param key1  The first key.
     * @param key2  The second key.
     * @param value The value to be associated with the keys.
     */
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

    /**
     * Retrieves the value associated with the specified Key1 and Key2.
     *
     * @param key1 The first key.
     * @param key2 The second key.
     * @return The value associated with the keys, or null if not found.
     */
    public Value get(Key1 key1, Key2 key2) {
        Integer index = findIndexByKey(key1, key2);
        if (Objects.nonNull(index)) {
            return values.get(index);
        } else {
            return null;
        }
    }

    /**
     * Retrieves all values associated with the specified Key1.
     *
     * @param key1 The first key.
     * @return A list of values associated with the specified Key1.
     *         Returns an empty list if no values are associated with the key.
     */
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

    /**
     * Removes all entries associated with the specified Key1 from the TripleMap.
     *
     * @param key1 The first key.
     */
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

    /**
     * Removes the entry associated with the specified Key1 and Key2 from the TripleMap.
     *
     * @param key1 The first key.
     * @param key2 The second key.
     */
    public void remove(Key1 key1, Key2 key2) {
        if (contains(key1, key2)) {
            if (Objects.nonNull(matching.get(key1).get(key2))) {
                int index = matching.get(key1).get(key2);
                values.remove(index);
                matching.get(key1).remove(key2);
            }
        }
    }

    /**
     * Checks if the TripleMap contains an entry for the specified Key1 and Key2.
     *
     * @param key1 The first key.
     * @param key2 The second key.
     * @return true if the entry exists, false otherwise.
     */
    public boolean contains(Key1 key1, Key2 key2) {
        if (Objects.nonNull(key1) && Objects.nonNull(key2)) {
            return matching.containsKey(key1) && matching.get(key1).containsKey(key2);
        }
        return false;
    }

    /**
     * Checks if the TripleMap contains any entry associated with the specified Key1.
     *
     * @param key1 The first key.
     * @return true if entries exist for the key, false otherwise.
     */
    public boolean contains(Key1 key1) {
        if (Objects.nonNull(key1)) {
            return matching.containsKey(key1);
        }
        return false;
    }

    /**
     * Checks if the TripleMap is empty, i.e., it contains no entries.
     *
     * @return true if the TripleMap is empty, false otherwise.
     */
    public boolean isEmpty() {
        return matching.isEmpty() && values.isEmpty();
    }
}
