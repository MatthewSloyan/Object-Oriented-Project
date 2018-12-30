package ie.gmit.sw;
import java.util.List;

public class Result {
	private int shingle;
	private List<Index> list;
	
	public Result(int shingle, List<Index> list) {
		super();
		this.shingle = shingle;
		this.list = list;
	}

	public int getShingle() {
		return shingle;
	}

	public List<Index> getList() {
		return list;
	}
}
