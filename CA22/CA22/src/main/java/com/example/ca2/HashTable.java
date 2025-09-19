package com.example.ca2;

import java.util.function.BiConsumer;

public class HashTable<K, V> {
    private Node<K, V>[] table; // Hash table (array of nodes)
    private int capacity; // Fixed capacity of the table
    private int size; // Number of elements in the table

    // Constructor
    public HashTable(int capacity) {
        this.capacity = capacity;
        this.table = new Node[capacity];
        this.size = 0;
    }

    // Node class for separate chaining
    public static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next; // Pointer for separate chaining

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    // Hash function
    private int hashFunction(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    // Add a key-value pair to the hash table
    public void put(K key, V value) {
        int index = hashFunction(key);
        Node<K, V> newNode = new Node<>(key, value);

        // Add using separate chaining
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> current = table[index];
            while (current != null) {
                if (current.key.equals(key)) {
                    current.value = value; // Update value if key already exists
                    return;
                }
                if (current.next == null) {
                    current.next = newNode; // Add new node at the end of the chain
                    break;
                }
                current = current.next;
            }
        }
        size++;
    }

    // Get a value by key
    public V get(K key) {
        int index = hashFunction(key);
        Node<K, V> current = table[index];

        while (current != null) {
            if (current.key.equals(key)) {
                return current.value; // Return value if key matches
            }
            current = current.next;
        }
        return null; // Key not found
    }

    // Remove a key-value pair
    public boolean remove(K key) {
        int index = hashFunction(key);
        Node<K, V> current = table[index];
        Node<K, V> prev = null;

        while (current != null) {
            if (current.key.equals(key)) {
                if (prev == null) {
                    table[index] = current.next; // Remove head of chain
                } else {
                    prev.next = current.next; // Bypass the node
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false; // Key not found
    }

    // Check if the hash table contains a key
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    // Get the size of the hash table
    public int size() {
        return size;
    }

    // Display the contents of the hash table
    public void display() {
        for (int i = 0; i < capacity; i++) {
            System.out.print("Index " + i + ": ");
            Node<K, V> current = table[i];
            while (current != null) {
                System.out.print("[" + current.key + " : " + current.value + "] -> ");
                current = current.next;
            }
            System.out.println("null");
        }
    }

    // Traverse method for iterating through all elements
    public void traverse(BiConsumer<K, V> action) {
        for (int i = 0; i < capacity; i++) {
            Node<K, V> current = table[i];
            while (current != null) {
                action.accept(current.key, current.value); // Apply the action to each key-value pair
                current = current.next;
            }
        }
    }
}
