package mazes.logic.carvers;

import graphs.EdgeWithData;
import graphs.minspantrees.MinimumSpanningTree;
import graphs.minspantrees.MinimumSpanningTreeFinder;
import mazes.entities.Room;
import mazes.entities.Wall;
import mazes.logic.MazeGraph;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Carves out a maze based on Kruskal's algorithm.
 */
public class KruskalMazeCarver extends MazeCarver {
    MinimumSpanningTreeFinder<MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder;
    private final Random rand;

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random();
    }

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder,
                             long seed) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random(seed);
    }

    @Override
    protected Set<Wall> chooseWallsToRemove(Set<Wall> walls) {
        Set<EdgeWithData<Room, Wall>> edgesWithWeights = new HashSet<>();
        for (Wall edge : walls) {
            Double ran = rand.nextDouble();
            edgesWithWeights.add(new EdgeWithData<Room, Wall>(edge.getRoom1(), edge.getRoom2(), ran, edge));
        }

        MinimumSpanningTree<Room, EdgeWithData<Room, Wall>> mst =
            minimumSpanningTreeFinder.findMinimumSpanningTree(new MazeGraph(edgesWithWeights));
        Set<Wall> ret = new HashSet<>();
        for (EdgeWithData<Room, Wall> edge : mst.edges()) {
            ret.add(edge.data());
        }
        return ret;
    }
}
