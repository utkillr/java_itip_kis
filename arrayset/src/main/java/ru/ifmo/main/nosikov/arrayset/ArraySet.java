package ru.ifmo.main.nosikov.arrayset;

import java.util.*;

public class ArraySet<E> implements SortedSet<E> {

    // Stupid test
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(-1);
        list.add(100);
        list.add(83);
        list.add(25);
        list.add(26);
        list.add(27);
        ArraySet<Integer> arraySet = new ArraySet<>(list);
        System.out.println(arraySet.first());
        System.out.println(arraySet.last());
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer item : arraySet.subSet(25, 27)) {
            stringBuilder.append(item).append(" ");
        }
        System.out.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        for (Integer item : arraySet.tailSet(80)) {
            stringBuilder.append(item).append(" ");
        }
        System.out.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        for (Integer item : arraySet.headSet(83)) {
            stringBuilder.append(item).append(" ");
        }
        System.out.println(stringBuilder.toString());
        System.out.println(arraySet.size());
        System.out.println(arraySet.isEmpty());
        System.out.println(arraySet.contains(83));
        System.out.println(arraySet.contains(22));
    }

    final E[] array;

    // Just creates sorted array
    public ArraySet(Collection<E> collection) {
        array = (E[]) new Object[collection.size()];
        int i = 0;
        for (E item : collection) {
            array[i++] = item;
        }
        Arrays.sort(array, this.comparator());
    }

    @Override
    public Comparator<? super E> comparator() {
        return (o1, o2) ->  {
            // If comparable, compare
            if (o1 instanceof Comparable) {
                return ((Comparable) o1).compareTo(o2);
            // else stringify and compare strings
            } else {
                return o1.toString().compareTo(o2.toString());
            }
        };
    }

    @Override
    // Iterate array and add each element from interval to list, then instantiate new ArraySet which is SortedSet
    public SortedSet<E> subSet(E fromElement, E toElement) {
        List<E> list = new ArrayList<>();
        for (E item : array) {
            if (this.comparator().compare(item, fromElement) >= 0 && this.comparator().compare(item, toElement) <= 0) {
                list.add(item);
            }
        }
        return new ArraySet<>(list);
    }

    @Override
    // Iterate array and add each element greater then toElement to list,
    // then instantiate new ArraySet which is SortedSet
    public SortedSet<E> headSet(E toElement) {
        List<E> list = new ArrayList<>();
        for (E item : array) {
            if (this.comparator().compare(item, toElement) <= 0) {
                list.add(item);
            }
        }
        return new ArraySet<>(list);
    }

    @Override
    // Iterate array and add each element less than fromElement to list,
    // then instantiate new ArraySet which is SortedSet
    public SortedSet<E> tailSet(E fromElement) {
        List<E> list = new ArrayList<>();
        for (E item : array) {
            if (this.comparator().compare(item, fromElement) >= 0) {
                list.add(item);
            }
        }
        return new ArraySet<>(list);
    }

    @Override
    // First element of array, since it's sorted
    public E first() {
        return array[0];
    }

    @Override
    // Last element of array, since it's sorted
    public E last() {
        return array[array.length - 1];
    }

    @Override
    // Length of array
    public int size() {
        return array.length;
    }

    @Override
    // Emptiness of array
    public boolean isEmpty() {
        return array.length == 0;
    }

    @Override
    // Binary search, since array is sorted
    public boolean contains(Object o) {
        int left = 0;
        int right = array.length - 1;
        int middle = -1;
        while (left < right) {
            if (middle != (left + right) / 2) {
                middle = (left + right) / 2;
            } else {
                return false;
            }
            if (comparator().compare(array[middle], (E)o) == 0) {
                return true;
            } else if (comparator().compare(array[middle], (E)o) < 0) {
                left = middle;
            } else if (comparator().compare(array[middle], (E)o) > 0) {
                right = middle;
            }
        }
        return false;
    }

    @Override
    // Simplest iterator
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int next = 0;

            @Override
            public boolean hasNext() {
                return array.length > next;
            }

            @Override
            public E next() {
                return array[next++];
            }
        };
    }

    @Override
    // Simple convert
    public Object[] toArray() {
        return array.clone();
    }

    @Override
    // Attempt to convert. Error is caused if types are incompatible
    public <T> T[] toArray(T[] a) {
        int i = 0;
        for (E item : array) {
            a[i] = (T)item;
        }
        return a;
    }

    @Override
    // We don't add, since ArraySet is immutable
    public boolean add(E e) {
        return false;
    }

    @Override
    // We don't remove, since ArraySet is immutable
    public boolean remove(Object o) {
        return false;
    }

    @Override
    // Iterate and find all elements
    public boolean containsAll(Collection<?> c) {
        for (Object obj : c) {
            if (! this.contains(obj)) {
                return false;
            }
        }
        return true;
    }

    @Override
    // We don't add, since ArraySet is immutable
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    // We don't retain, since ArraySet is immutable
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    // We don't remove, since ArraySet is immutable
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    // No need for clear, since underlying type is simple array
    public void clear() {}
}
