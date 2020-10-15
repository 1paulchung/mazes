package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import java.util.Stack;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    //This needs to be found
    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        //return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        Map<V, E> edgeTo = new HashMap<>();
        if (graph == null) {
            return null;
        }
        if (Objects.equals(start, end)) {
            return edgeTo;
        }
        Map<V, Double> distTo = new HashMap<>();
        Set<V> known = new HashSet<>();
        ExtrinsicMinPQ<V> perimeter = createMinPQ();

        distTo.put(start, 0.0);
        perimeter.add(start, 0.0);

        while (!known.contains(end) && !perimeter.isEmpty()) {
            V from = perimeter.removeMin();
            known.add(from);
            for (E edge : graph.outgoingEdgesFrom(from)) {
                V to = edge.to();
                if (!known.contains(to)) {
                    Double oldDist = distTo.get(to);
                    Double newDist = distTo.get(from) + edge.weight();
                    if (!perimeter.contains(to)) {
                        perimeter.add(to, newDist);
                    } else if (oldDist > newDist) {
                        perimeter.changePriority(to, newDist);
                    }
                    if (oldDist == null || newDist < oldDist) {
                        distTo.put(to, newDist);
                        edgeTo.put(to, edge);
                    }
                }
            }
        }
        return edgeTo;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        List<E> edges = new ArrayList<>();
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }
        if (!spt.containsKey(end) || spt == null) {
            return new ShortestPath.Failure<>();
        }
        V curr = end;
        Stack<E> stack = new Stack<>();
        while (!Objects.equals(start, curr)) {
            stack.push(spt.get(curr));
            curr = spt.get(curr).from();
        }
        while (!stack.isEmpty()) {
            edges.add(stack.pop());
        }
        ShortestPath<V, E> shortestPath = new ShortestPath.Success<>(edges);
        return shortestPath;
    }

}
