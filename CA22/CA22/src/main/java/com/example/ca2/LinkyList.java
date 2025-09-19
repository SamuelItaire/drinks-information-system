package com.example.ca2;

import java.util.Comparator;

public class LinkyList<T> {
    private Node<T> head;
    private int size;

    public LinkyList() {
        this.head = null;
        this.size = 0;
    }

    // Inner Node class
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    // Add an element to the end of the linked list
    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    // Get an element by index
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds.");
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    // Remove an element
    public boolean remove(T data) {
        if (head == null) {
            return false; // Empty list
        }
        if (head.data.equals(data)) {
            head = head.next;
            size--;
            return true;
        }
        Node<T> current = head;
        while (current.next != null) {
            if (current.next.data.equals(data)) {
                current.next = current.next.next;
                size--;
                return true;
            }
            current = current.next;
        }
        return false; // Element not found
    }

    // Insert an element at a specific index
    public void insert(int index, T data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of bounds.");
        }
        Node<T> newNode = new Node<>(data);
        if (index == 0) { // Insert at the head
            newNode.next = head;
            head = newNode;
        } else {
            Node<T> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            newNode.next = current.next;
            current.next = newNode;
        }
        size++;
    }

    // Sort the list using selection sort
    public void selectionSort(Comparator<T> comparator) {
        if (head == null || head.next == null) {
            return; // List is already sorted
        }

        Node<T> current = head;
        while (current != null) {
            Node<T> minNode = current;
            Node<T> nextNode = current.next;

            while (nextNode != null) {
                if (comparator.compare(nextNode.data, minNode.data) < 0) {
                    minNode = nextNode;
                }
                nextNode = nextNode.next;
            }

            // Swap data
            if (minNode != current) {
                T temp = current.data;
                current.data = minNode.data;
                minNode.data = temp;
            }

            current = current.next;
        }
    }

    // Display all elements in the list
    public void display() {
        Node<T> current = head;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }

    // Get the size of the linked list
    public int size() {
        return size;
    }

    // Check if the list contains a specific element
    public boolean contains(T data) {
        Node<T> current = head;
        while (current != null) {
            if (current.data.equals(data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // Clear the linked list
    public void clear() {
        head = null; // Reset the head to null
        size = 0; // Reset the size to 0
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size(); i++) {
            sb.append(get(i).toString());
            if (i < size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

}