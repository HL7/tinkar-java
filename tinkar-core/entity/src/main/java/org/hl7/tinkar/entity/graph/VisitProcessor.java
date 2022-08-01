package org.hl7.tinkar.entity.graph;

public interface VisitProcessor<V extends VertexVisitData> {
    void accept(EntityVertex vertex, DiGraphAbstract<EntityVertex> graph, V visitData);
}
