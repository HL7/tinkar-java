package org.hl7.tinkar.entity.graph;

import io.activej.bytebuf.ByteBuf;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.primitive.ImmutableIntList;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.api.map.primitive.*;
import org.eclipse.collections.impl.factory.primitive.IntIntMaps;
import org.eclipse.collections.impl.factory.primitive.IntLists;
import org.eclipse.collections.impl.factory.primitive.IntObjectMaps;
import org.hl7.tinkar.common.alert.AlertStreams;
import org.hl7.tinkar.component.graph.DiTree;
import org.hl7.tinkar.component.graph.Vertex;
import org.hl7.tinkar.entity.graph.isomorphic.IsomorphicResults;
import org.hl7.tinkar.entity.graph.isomorphic.IsomorphicResultsLeafHash;

public class DiTreeEntity extends DiTreeAbstract<EntityVertex> {


    public DiTreeEntity(EntityVertex root,
                        ImmutableList<EntityVertex> vertexMap,
                        ImmutableIntObjectMap<ImmutableIntList> successorMap,
                        ImmutableIntIntMap predecessorMap) {
        super(root, vertexMap, successorMap, predecessorMap);
    }

    /**
     * Make a correlated tree, where the vertex ids of this tree will be preserved when the ids are
     * correlated by an isomorphic analysis.
     *
     * @param that The tree that is considered secondary with respect to preserving vertex ids.
     * @return a copy of that which is updated with correlated vertex ids based on isomorphic analysis.
     */
    public DiTreeEntity makeCorrelatedTree(DiTreeEntity that, int referencedConceptNid) {

        // A special case for correlation when this and that are equal and vertexes are indexed the same.
        if (this.vertexMap.size() == that.vertexMap.size()) {
            boolean maybeEqual = true;
            for (int index = 0; index < this.vertexMap.size(); index++) {
                if (this.vertexMap.get(index) == null || that.vertexMap.get(index) == null) {
                    if (this.vertexMap.get(index) != that.vertexMap.get(index)) {
                        maybeEqual = false;
                        break;
                    }
                }
                if (!this.vertexMap.get(index).equivalent(that.vertexMap.get(index))) {
                    maybeEqual = false;
                    break;
                }
                if (this.predecessorMap.containsKey(index) != that.predecessorMap.containsKey(index)) {
                    maybeEqual = false;
                    break;
                }
                if (this.predecessorMap.containsKey(index) && that.predecessorMap.containsKey(index)) {
                    if (this.predecessorMap.get(index) != that.predecessorMap.get(index)) {
                        maybeEqual = false;
                        break;
                    }
                }
            }
            if (maybeEqual) {
                // return this as the correlated tree
                return this;
            }
        }
        try {
            IsomorphicResultsLeafHash isomorphicResult = new IsomorphicResultsLeafHash(this, that, referencedConceptNid);
            IsomorphicResults results = isomorphicResult.call();
            return results.getIsomorphicTree();
        } catch (Exception e) {
            AlertStreams.dispatchToRoot(e);
        }
        throw new IllegalStateException("No result");
    }

    public final VertexVisitData breadthFirstProcess() {
        VertexVisitData vertexVisitData = new VertexVisitData(this.vertexMap.size());
        breadthFirstProcess(this.root.vertexIndex, vertexVisitData);
        return vertexVisitData;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DiTreeEntity another) {
            return isomorphic(another);
        }
        return super.equals(obj);
    }

    // TODO Consider using JGraphT Isomorphic package here, and compare performance.
    // TODO: Note implementation assumes no null values in vertex map.
    public boolean isomorphic(DiTreeEntity another) {
        if (this.vertexMap.size() != another.vertexMap.size()) {
            return false;
        }
        //TODO implement
        throw new UnsupportedOperationException();
    }

    public static DiTreeEntity make(DiTree<Vertex> tree) {
        ImmutableList<EntityVertex> vertexMap = getVertexEntities(tree);
        EntityVertex root = vertexMap.get(tree.root().vertexIndex());
        return new DiTreeEntity(root, vertexMap, tree.successorMap(), tree.predecessorMap());
    }

    public static DiTreeEntity make(ByteBuf readBuf, byte entityFormatVersion) {
        if (entityFormatVersion != ENTITY_FORMAT_VERSION) {
            throw new IllegalStateException("Unsupported entity format version: " + entityFormatVersion);
        }

        ImmutableList<EntityVertex> vertexMap = readVertexEntities(readBuf, entityFormatVersion);
        ImmutableIntObjectMap<ImmutableIntList> successorMap = readIntIntListMap(readBuf);

        int predecessorMapSize = readBuf.readInt();
        MutableIntIntMap predecessorMap = IntIntMaps.mutable.ofInitialCapacity(predecessorMapSize);
        for (int i = 0; i < predecessorMapSize; i++) {
            predecessorMap.put(readBuf.readInt(), readBuf.readInt());
        }

        int rootVertexIndex = readBuf.readInt();

        return new DiTreeEntity(vertexMap.get(rootVertexIndex), vertexMap,
                successorMap, predecessorMap.toImmutable());

    }

    public static DiTreeEntity.Builder builder() {
        return new DiTreeEntity.Builder();
    }

    /**
     * Called to generate an isomorphicExpression and a mergedExpression.
     *
     * @param another  the existing DiTreeEntity to add nodes from.
     * @param solution an array mapping from the vertexIndexes in another to the vertexIndexes
     *                 in this expression. If the value of the solution element == -1, that node
     *                 is not added to this tree, otherwise the value of the
     *                 solution element is used for the vertexIndex in this tree.
     */
    public static DiTreeEntity.Builder addVertexesFromSolution(DiTreeEntity another, ImmutableIntList solution) {
        Builder builder = new Builder();
        builder.addVertexesWithMap(another, solution.toArray(), new int[another.vertexCount()], another.root.vertexIndex);
        return builder;
    }

    /**
     * Called to generate an isomorphicExpression and a mergedExpression.
     *
     * @param another                     the logical expression to add nodes from.
     * @param solution                    an array mapping from the nodeId in another to the nodeId
     *                                    in this expression. If the value of the solution element == -1, that node
     *                                    is not added to this logical expression, otherwise the value of the
     *                                    solution element is used for the nodeId in this logical expression.
     * @param anotherToThisVertexIndexMap contains a mapping from nodeId in another
     *                                    to nodeId in this constructed tree.
     */
    public static DiTreeEntity.Builder addVertexesFromSolution(DiTreeEntity another, ImmutableIntList solution, int[] anotherToThisVertexIndexMap) {
        Builder builder = new Builder();
        builder.addVertexesWithMap(another, solution.toArray(), anotherToThisVertexIndexMap, another.root.vertexIndex);
        return builder;
    }

    public static class Builder extends DiTreeAbstract.Builder<EntityVertex> {
        protected Builder() {
        }

        public DiTreeEntity build() {

            MutableIntObjectMap<ImmutableIntList> intermediateSuccessorMap = IntObjectMaps.mutable.ofInitialCapacity(successorMap.size());
            successorMap.forEachKeyValue((vertex, successorList) -> intermediateSuccessorMap.put(vertex, successorList.toImmutable()));

            return new DiTreeEntity(root,
                    vertexMap.toImmutable(),
                    intermediateSuccessorMap.toImmutable(),
                    predecessorMap.toImmutable());
        }

        public int vertexCount() {
            return this.vertexMap.size();
        }

        /**
         * Adds the nodes with map.
         *
         * @param anotherTree                 the tree to add nodes from.
         * @param solution                    an array mapping from the vertexIndex in anotherTree to the vertexIndex
         *                                    in this tree. If the value of the solution element == -1, that vertex
         *                                    is not added to this tree, otherwise the value of the
         *                                    solution element is used for the vertexIndex in this directed tree.
         * @param vertexIndexesToAddFromAnother   the list of vertexIndexes in anotherTree
         *                                    to add to this tree on this invocation. Note that
         *                                    children of the nodes indicated by vertexIndexesToAddFromAnother may be added by recursive calls
         *                                    to this method, if the vertexIndexesToAddFromAnother index in the solution array is >= 0.
         * @return the EntityVertex elements added as a result of this instance of the
         * call, not including any children EntityVertexes added by recursive
         * calls. Those children EntityVertex elements can be retrieved by recursively
         * traversing the children of these returned EntityVertexes.
         */
        public EntityVertex[] addVertexesWithMap(DiTreeEntity anotherTree,
                                                 int[] solution,
                                                 int... vertexIndexesToAddFromAnother) {
            return addVertexesWithMap(anotherTree, solution, null, vertexIndexesToAddFromAnother);
        }

        /**
         * Adds the nodes with map.
         *
         * @param anotherTree                 the tree to add nodes from.
         * @param solution                    an array mapping from the vertexIndex in anotherTree to the vertexIndex
         *                                    in this tree. If the value of the solution element == -1, that vertex
         *                                    is not added to this tree, otherwise the value of the
         *                                    solution element is used for the vertexIndex in this directed tree.
         * @param anotherToThisVertexIndexMap if not null, contains a mapping from vertexIndex in anotherTree
         *                                    to vertexIndex in this constructed expression. The mapping is populated by this routine, it
         *                                    is an output parameter.
         * @param anotherVertexIndexesToAdd   the list of vertexIndexes in anotherTree
         *                                    to add to this tree on this invocation. Note that
         *                                    children of the nodes indicated by anotherVertexIndexesToAdd may be added by recursive calls
         *                                    to this method, if the anotherVertexIndexesToAdd index in the solution array is >= 0.
         * @return the EntityVertex elements added as a result of this instance of the
         * call, not including any children EntityVertexes added by recursive
         * calls. Those children EntityVertex elements can be retrieved by recursively
         * traversing the children of these returned EntityVertexes.
         */
        public EntityVertex[] addVertexesWithMap(DiTreeEntity anotherTree,
                                                 int[] solution,
                                                 int[] anotherToThisVertexIndexMap,
                                                 int... anotherVertexIndexesToAdd) {
            final EntityVertex[] results = new EntityVertex[anotherVertexIndexesToAdd.length];

            for (int i = 0; i < anotherVertexIndexesToAdd.length; i++) {
                final EntityVertex oldVertex = anotherTree.vertex(anotherVertexIndexesToAdd[i]);
                EntityVertex newVertex = oldVertex.copyWithUnassignedIndex();
                this.addVertex(newVertex);
                if (oldVertex.vertexIndex == anotherTree.root.vertexIndex) {
                    this.setRoot(newVertex);
                }
                results[i] = newVertex;
                //  filter successor vertex indexes not in solution, then recursive call to add filtered children...
                int[] filteredSuccessorVertexIndexes = anotherTree.successors(oldVertex.vertexIndex)
                        .primitiveStream().filter(oldVertexIndex -> solution[oldVertexIndex] >= 0).toArray();

                if (filteredSuccessorVertexIndexes.length > 0) {
                    EntityVertex[] successorVertexes = addVertexesWithMap(anotherTree,
                            solution,
                            anotherToThisVertexIndexMap,
                            filteredSuccessorVertexIndexes);

                    // Add filtered successors to builder
                    for (EntityVertex successorVertex: successorVertexes) {
                        addEdge(successorVertex.vertexIndex, newVertex.vertexIndex);
                    }
                }

                // update anotherToThisVertexIndexMap...
                if (anotherToThisVertexIndexMap != null) {
                    anotherToThisVertexIndexMap[oldVertex.vertexIndex] = results[i].vertexIndex;
                }
            }
            return results;
        }
    }

}
