package model;

import java.util.Objects;

public class ExpiredNodesTime {
    private PairOfNodes expiredNode;
    private Node lastEmptyAtNode;// l'ultimo posto in cui era vuoto

    public ExpiredNodesTime(PairOfNodes expiredNode, Node lastEmptyAtNode) {
        this.expiredNode = expiredNode;
        this.lastEmptyAtNode = lastEmptyAtNode;
    }

    public ExpiredNodesTime() {
    }

    public PairOfNodes getExpiredNode() {
        return expiredNode;
    }

    public void setExpiredNode(PairOfNodes expiredNode) {
        this.expiredNode = expiredNode;
    }

    public Node getLastEmptyAtNode() {
        return lastEmptyAtNode;
    }

    public void setLastEmptyAtNode(Node lastEmptyAtNode) {
        this.lastEmptyAtNode = lastEmptyAtNode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpiredNodesTime)) return false;
        ExpiredNodesTime that = (ExpiredNodesTime) o;
        return getExpiredNode().equals(that.getExpiredNode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getExpiredNode());
    }
}
