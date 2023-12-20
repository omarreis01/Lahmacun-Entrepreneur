import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * The {@code myDict} class represents a simple hash map implementation with key-value pairs.
 * It provides basic operations like putting, getting, removing, and checking for the existence of a key.
 * @param <K> The type of keys.
 * @param <V> The type of values.
 */
public class myDict<K, V> {
    private int size=1000019;
    private LinkedList<KeyValuePair<K, V>>[] map;

    /**
     * Constructs a new instance of {@code myDict} with the default size.
     */
    public myDict() {
        this.map = new LinkedList[size];
    }

    /**
     *
     * @param key
     * @return hashValue
     */
    private int hash(K key) {
        int hashValue = key.hashCode();
        hashValue = hashValue % map.length;
        if(hashValue < 0) {
            hashValue += map.length;
        }
        return hashValue;
    }
    /**
     * Puts a key-value pair into the dictionary.
     *
     * @param key   The key.
     * @param value The value.
     */
    public void put(K key, V value) {
        int index = hash(key);

        if (map[index] == null) {
            map[index] = new LinkedList<>();
        }

        for (KeyValuePair<K, V> pair : map[index]) {
            if (pair.key.equals(key)) {
                pair.value = value;
                return;
            }
        }
        map[index].add(new KeyValuePair<>(key, value));

        if ((double) map.length / size > 1) {
            rehash();
        }
    }

    public V get(K key) throws IOException {
        int index = hash(key);
        if (map[index] != null) {
            for (KeyValuePair<K, V> pair : map[index]) {
                if (pair.key.equals(key)) {
                    return pair.value;
                }
            }
        }
        throw new IOException();
    }

    public void remove(K key) {
        int index = hash(key);

        if (map[index] != null) {
            map[index].removeIf(pair -> pair.key.equals(key));
        } else {
            throw new IllegalArgumentException("Key not found: " + key);
        }
    }
    public boolean contains(K key) {
        int index = hash(key);

        if (map[index] != null) {
            for (KeyValuePair<K, V> pair : map[index]) {
                if (pair.key.equals(key)) {
                    return true;
                }
            }
        }

        return false;
    }
    private void rehash() {
        int newSize = size * 2; // Double the size for simplicity
        LinkedList<KeyValuePair<K, V>>[] newMap = new LinkedList[newSize];

        // Rehash existing elements into the new array
        for (LinkedList<KeyValuePair<K, V>> list : map) {
            if (list != null) {
                for (KeyValuePair<K, V> pair : list) {
                    int newIndex = pair.key.hashCode() % newSize;

                    if (newMap[newIndex] == null) {
                        newMap[newIndex] = new LinkedList<>();
                    }
                    newMap[newIndex].add(pair);
                }
            }
        }

        // Update the dictionary with the new size and array
        size = newSize;
        map = newMap;
    }
    public KeyValuePair<K, V>[] getAllEntries() {
        LinkedList<KeyValuePair<K, V>> allEntries = new LinkedList<>();

        for (LinkedList<KeyValuePair<K, V>> list : map) {
            if (list != null) {
                allEntries.addAll(list);
            }
        }

        return allEntries.toArray(new KeyValuePair[0]);
    }
    public ArrayList<String> getAllKeys() {
        LinkedList<K> allKeys = new LinkedList<>();
        for (LinkedList<KeyValuePair<K, V>> list : map) {
            if (list != null) {
                for (KeyValuePair<K, V> pair : list) {
                    allKeys.add(pair.key);
                }
            }
        }
        // Use toArray(T[] a) with a properly typed array
        ArrayList<String> storeAll = new ArrayList<>();
        storeAll.addAll((Collection<? extends String>) allKeys);
        return storeAll;
    }
    public boolean isEmpty() {
        for (LinkedList<KeyValuePair<K, V>> list : map) {
            if (list != null && !list.isEmpty()) {
                return false;
            }
        }
        return true;
    }

}

