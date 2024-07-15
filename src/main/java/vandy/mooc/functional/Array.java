package vandy.mooc.functional;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * A generic unsynchronized array class implemented via a single
 * contiguous buffer.
 */
@SuppressWarnings({"unchecked", "JavadocBlankLines", "JavadocDeclaration"})
public class Array<E>
        implements Iterable<E> {
    /**
     * Default initial capacity.
     */
    static final int DEFAULT_CAPACITY = 10;

    /**
     * Shared empty array instance used for empty instances.
     */
    static final Object[] EMPTY_ELEMENTDATA = {};

    /**
     * The array buffer that stores all the array elements.  The
     * capacity is the length of this array buffer.
     */
    Object[] mElementData;

    /**
     * The size of the {@link Array} (the number of elements it
     * contains).  This field also indicates the next "open" slot in
     * the array, i.e., where a call to add() will place the new
     * element: mElementData[mSize] = element.
     */
    int mSize;

    /**
     * Sets element data to an immutable static zero-sized array,
     * which is used later by the {@code ensureCapacityInternal()}
     * method to construct an empty array with an initial capacity of
     * {@code DEFAULT_CAPACITY} "on demand", i.e., when it's actually
     * necessary.
     */
    public Array() {
        mElementData = EMPTY_ELEMENTDATA;
    }

    /**
     * Constructs an empty {@link Array} with the specified initial
     * capacity.
     *
     * @param initialCapacity The initial capacity of the {@link
     *                        Array}
     * @throws IllegalArgumentException If the specified initial
     *                                  capacity is negative
     */
    public Array(int initialCapacity) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        mElementData = new Object[initialCapacity];
    }

    /**
     * Constructs an {@link Array} containing the elements of the
     * specified collection in the order they are returned by the
     * {@link Collection}'s iterator.
     *
     * @param c The {@link Collection} whose elements will be placed
     *          into this array
     * @throws NullPointerException If the specified {@link
     *                              Collection} is null
     */
    public Array(Collection<? extends E> c) {
        mElementData = c.toArray();
        mSize = mElementData.length;
        if (mElementData.getClass() != Object[].class)
            mElementData = Arrays.copyOf(mElementData, mSize, Object[].class);
    }

    /**
     * @return <tt>true</tt> If this {@link Array} contains no elements
     */
    public boolean isEmpty() {
        return mSize == 0;
    }

    /**
     * @return The number of elements in this {@link Array}
     */
    public int size() {
        return mSize;
    }

    /**
     * Returns the index of the first occurrence of the specified
     * element in this {@link Array}, or -1 if this {@link Array} does
     * not contain the element.
     *
     * @param o Element to search for
     * @return The index of the first occurrence of the specified
     *         element in this {@link Array}, or -1 if this {@link
     *         Array} does not contain the element
     */
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < mSize; i++)
                if (mElementData[i] == null)
                    return i;
        } else {
            for (int i = 0; i < mSize; i++)
                if (o.equals(mElementData[i]))
                    return i;
        }
        return -1;
    }

    /**
     * Appends all the elements in the specified {@link Collection}
     * to the end of this {@link Array}, in the order that they are
     * returned by the specified {@link Collection}'s {@link
     * Iterator}.  The behavior of this operation is undefined if the
     * specified {@link Collection} is modified while the operation is
     * in progress.  This implies that the behavior of this call is
     * undefined if the specified {@link Collection} is this {@link
     * Array}, and this {@link Array} is nonempty.
     *
     * @param c {@link Collection} containing elements to be added to
     *          this {@link Array}
     * @return <tt>true</tt> If this {@link Array} changed as a result
     *                       of the call
     * @throws {@link NullPointerException} If the specified {@link
     *                                      Collection} is null
     */
    public boolean addAll(Collection<? extends E> c) {
        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityInternal(mSize + numNew);
        System.arraycopy(a, 0, mElementData, mSize, numNew);
        mSize += numNew;
        return numNew != 0;
    }

    /**
     * Appends all the elements in the specified {@link Array} to the
     * end of this {@link Array}, in the order they are returned by
     * the specified {@link Collection}'s {@link Iterator}.  The
     * behavior of this operation is undefined if the specified {@link
     * Collection} is modified while the operation is in progress.
     * This implies that the behavior of this call is undefined if the
     * specified {@link Collection} is this {@link Array}, and this
     * {@link Array} is nonempty.
     *
     * @param array An {@link Array} containing elements to added to
     *              this {@link Array}
     * @return <tt>true</tt> if this {@link Array} changed as a result
     *                       of the call
     * @throws {@link NullPointerException} if the specified {@link
     *                                      Array} is null
     */
    public boolean addAll(Array<E> array) {
        int numNew = array.size();
        ensureCapacityInternal(mSize + numNew);
        System.arraycopy(array.mElementData, 0, mElementData, mSize, numNew);
        mSize += numNew;
        return numNew != 0;
    }

    /**
     * Removes the element at the specified position in this {@link
     * Array}.  Shifts any subsequent elements to the left (subtracts
     * one from their indices).
     *
     * @param index The index of the element to be removed
     * @return The element that was removed from the {@link Array}
     */
    public E remove(int index) {
        rangeCheck(index);
        E oldValue = (E) mElementData[index];
        int numMoved = mSize - index - 1;
        if (numMoved > 0)
            System.arraycopy(mElementData, index + 1, mElementData, index, numMoved);
        mElementData[--mSize] = null;
        return oldValue;
    }

    /**
     * Checks if the given index is in range (i.e., index is
     * non-negative and is not equal to or larger than the size of the
     * {@link Array}) and throws the {@link IndexOutOfBoundsException}
     * if it's not.
     *
     * Normally should be declared as 'private', but for unit test
     * access, has been declared 'public'.
     *
     * @param index The index of the element to check
     * @throws {@link IndexOutOfBoundsException} If {@code index} is
     *                                           out of bounds
     */
    public void rangeCheck(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= mSize)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + mSize);
    }

    /**
     * Returns the element at the specified position in this array.
     * The {@link IndexOutOfBoundsException} is thrown if {@code
     * index} is out of range.
     *
     * @param index The index of the element to return
     * @return The element at the specified position in this {@link
     *         Array}
     */
    public E get(int index) {
        rangeCheck(index);
        return (E) mElementData[index];
    }

    /**
     * Replaces the element at the specified position in this list
     * with the specified element.  {@link IndexOutOfBoundsException}
     * is thrown if {@code index} is out of range.
     *
     * @param index The index of the element to replace
     * @param element The element to be stored at the specified
     *                position
     * @return The element previously at the specified position
     */
    public E set(int index, E element) {
        rangeCheck(index);
        E oldValue = (E) mElementData[index];
        mElementData[index] = element;
        return oldValue;
    }

    /**
     * Appends the specified element to the end of this {@link Array}.
     *
     * @param element The element to append to this {@link Array}
     * @return {@code true}
     */
    public boolean add(E element) {
        ensureCapacityInternal(mSize + 1);
        mElementData[mSize++] = element;
        return true;
    }

    /**
     * Ensure this {@link Array} is large enough to hold {@code
     * minCapacity} elements.  The {@link Array} will be expanded if
     * necessary.
     *
     * Normally should be declared as 'private', but for unit test
     * access, it has been declared 'protected'.
     *
     * @param minCapacity The minimum capacity needed for this {@link
     *                    Array}
     */
    protected void ensureCapacityInternal(int minCapacity) {
        if (mElementData == EMPTY_ELEMENTDATA) {
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }

        if (minCapacity > mElementData.length) {
            int newCapacity = mElementData.length * 2;
            if (newCapacity < minCapacity)
                newCapacity = minCapacity;
            mElementData = Arrays.copyOf(mElementData, newCapacity);
        }
    }

    /**
     * @return An {@link Iterator} over the elements in this {@link
     *         Array} in proper sequence
     */
    public Iterator<E> iterator() {
        return new ArrayIterator();
    }

    /**
     * This class implements an {@link Iterator} that traverses over
     * the elements in an {@link Array} in proper sequence.
     */
    @SuppressWarnings("JavadocDeclaration")
    public class ArrayIterator implements Iterator<E> {
        /**
         * Current position in the {@link Array} (defaults to 0).
         */
        private int mCurrentPosition = 0;

        /**
         * Index of last element returned; -1 if no such element.
         */
        private int mLastReturned = -1;

        /**
         * @return True if the iteration has more elements that
         *         haven't been iterated through yet, else false
         */
        @Override
        public boolean hasNext() {
            return mCurrentPosition < mSize;
        }

        /**
         * @return The next element in the iteration
         * @throws {@link NoSuchElementException} if there's no next
         *         element
         */
        @Override
        public E next() {
            if (mCurrentPosition >= mSize)
                throw new NoSuchElementException();
            mLastReturned = mCurrentPosition;
            return (E) mElementData[mCurrentPosition++];
        }

        /**
         * Removes from the underlying {@link Array} the last element
         * returned by this {@link Iterator}. This method can be
         * called only once per call to {@code next()}.
         *
         * @throws IllegalStateException if no last element was
         *                               returned by the iterator
         */
        @Override
        public void remove() {
            if (mLastReturned < 0)
                throw new IllegalStateException();
            Array.this.remove(mLastReturned);
            mCurrentPosition = mLastReturned;
            mLastReturned = -1;
        }
    }

    /**
     * Replaces each element of this {@link Array} with the result of
     * applying the {@code operator} to that element.  Errors or
     * runtime exceptions thrown by the {@code operator} are relayed
     * to the caller.
     *
     * @param operator The {@link UnaryOperator} to apply to transform
     *                 each element
     */
    public void replaceAll(UnaryOperator<E> operator) {
        for (int i = 0; i < mSize; i++) {
            mElementData[i] = operator.apply((E) mElementData[i]);
        }
    }

    /**
     * Performs the given {@code action} for each element of the
     * {@link Array} until all elements have been processed or the
     * {@code action} throws an exception.  Unless otherwise specified
     * by the implementing class, the {@code action} is performed in
     * the order of iteration (if an iteration order is specified).
     * Exceptions thrown by the {@code action} are relayed to the
     * caller.
     *
     * @param action The {@link Consumer} action to perform for each
     *               element
     */
    public void forEach(Consumer<? super E> action) {
        for (int i = 0; i < mSize; i++) {
            action.accept((E) mElementData[i]);
        }
    }

    /**
     * @return A {@link List} view of the elements in this {@link Array}
     */
    public List<E> asList() {
        List<E> list = new ArrayList<>(mSize);
        for (int i = 0; i < mSize; i++) {
            list.add((E) mElementData[i]);
        }
        return list;
    }
}