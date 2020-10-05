package Logic;
import java.util.ArrayList;

public class Directions {
	public static final pair<Integer, Integer> N = new pair<Integer, Integer>(0, -1);
	public static final pair<Integer, Integer> NE = new pair<Integer, Integer>(1, -1);
	public static final pair<Integer, Integer> E = new pair<Integer, Integer>(1, 0);
	public static final pair<Integer, Integer> SE = new pair<Integer, Integer>(1, 1);
	public static final pair<Integer, Integer> S = new pair<Integer, Integer>(0, 1);
	public static final pair<Integer, Integer> SW = new pair<Integer, Integer>(-1, 1);
	public static final pair<Integer, Integer> W = new pair<Integer, Integer>(-1, 0);
	public static final pair<Integer, Integer> NW = new pair<Integer, Integer>(-1, -1);
	@SuppressWarnings("serial")
	public static final ArrayList<pair<Integer, Integer> > allDir = new ArrayList<pair<Integer, Integer> >(){{
		add(N);
		add(NE);
		add(E);
		add(SE);
		add(S);
		add(SW);
		add(W);
		add(NW);
	}
	};
}
