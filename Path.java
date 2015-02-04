
import java.util.List;

public class Path {
    public final List<Vertex> vertices;
    public final int cost;
    
    public Path(List<Vertex> vertices, int cost) {
	this.vertices = vertices;
	this.cost = cost;
    }
}
