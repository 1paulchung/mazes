package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;

    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */
    HashMap<T, Integer> vertices;

    public UnionBySizeCompressingDisjointSets() {
        pointers = new ArrayList<>();
        vertices = new HashMap<>();
    }

    @Override
    public void makeSet(T item) {
        vertices.put(item, pointers.size());
        pointers.add(-1);
    }

    @Override
    public int findSet(T item) {
        if (!vertices.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        int index = vertices.get(item);
        Stack<Integer> children = new Stack<>();
        while (pointers.get(index) >= 0) { // added an equal sign
            children.push(index);
            index = pointers.get(index);
        }
        while (!children.isEmpty()) {
            pointers.set(children.pop(), index);
        }
        return index;
    }

    @Override
    public boolean union(T item1, T item2) {
        if (!vertices.containsKey(item1) || !vertices.containsKey(item2)) {
            throw new IllegalArgumentException();
        }
        int indexOfA = findSet(item1);
        int indexOfB = findSet(item2);
        if (indexOfA == indexOfB) {
            return false;
        }
        int elementA = pointers.get(indexOfA);
        int elementB = pointers.get(indexOfB);
        int newWeight = elementA + elementB;
        if (elementA <= elementB) {
            pointers.set(indexOfB, indexOfA);
            pointers.set(indexOfA, newWeight);
        } else {
            pointers.set(indexOfA, indexOfB);
            pointers.set(indexOfB, newWeight);
        }
        return true;
    }
}
