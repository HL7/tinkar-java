package org.hl7.tinkar.common.id.impl;

import org.hl7.tinkar.common.id.IntIdSet;
import org.hl7.tinkar.common.service.PrimitiveData;

import java.util.Arrays;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

/**
 * https://dirtyhandscoding.wordpress.com/2017/08/25/performance-comparison-linear-search-vs-binary-search/
 * <p>
 * The cost of setting up a sort, or a branching structure for a binary search, or a set structure for small sets
 * is greater than just iterating through an array. So I chose to use direct iteration for lookup for lists < 32 elements
 * in size. I don’t think there will ever be a case when the public id has > 32 UUIDs inside.
 */
public class IntIdSetArray
        implements IntIdSet {
    private final int[] elements;

    private IntIdSetArray(int... newElements) {
        this.elements = newElements;
    }

    public static IntIdSetArray newIntIdSet(int... newElements) {
        Arrays.sort(newElements);
        return new IntIdSetArray(newElements);
    }

    public static IntIdSetArray newIntIdSetAlreadySorted(int... newElements) {
        return new IntIdSetArray(newElements);
    }


    @Override
    public int size() {
        return elements.length;
    }

    @Override
    public IntStream intStream() {
        return IntStream.of(elements);
    }

    @Override
    public boolean contains(int value) {
        // for small lists, iteration is faster search than binary search because of less branching.
        if (elements.length < 32) {
            for (int element : elements) {
                if (value == element) {
                    return true;
                }
            }
            return false;
        }

        return Arrays.binarySearch(elements, value) >= 0;
    }

    @Override
    public boolean isEmpty() {
        return elements.length == 0;
    }

    @Override
    public int[] toArray() {
        return elements;
    }

    @Override
    public void forEach(IntConsumer consumer) {
        for (int element : elements) {
            consumer.accept(element);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof IntIdSet intIdSet) {
            if (elements.length != intIdSet.size()) {
                return false;
            }
            int[] elements2;
            if (intIdSet instanceof IntIdSetArray intIdSetArray) {
                elements2 = intIdSetArray.elements;
            } else {
                elements2 = intIdSet.toArray();
                Arrays.sort(elements2);
            }

            if (intIdSet.size() == elements.length && Arrays.equals(elements, elements2)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("IntIdSet[");
        for (int i = 0; i < elements.length && i <= TO_STRING_LIMIT; i++) {
            sb.append(PrimitiveData.textWithNid(elements[i])).append(", ");
            if (i == TO_STRING_LIMIT) {
                sb.append("..., ");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.setCharAt(sb.length() - 1, ']');
        return sb.toString();
    }
}
