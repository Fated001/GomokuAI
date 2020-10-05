package Logic;

public class pair<T extends Comparable<T>, U extends Comparable<U> > implements Comparable<pair<T, U>> {
	public T first;
	public U second;
	public pair(T first, U second){
		this.first = first;
		this.second = second;
	}
	
	public static <T extends Comparable<T>, U extends Comparable<U> >pair<T, U> new_pair(T f, U s){
		return new pair<T, U>(f, s);
	}
	
	
	@Override
	public int compareTo(pair<T, U> o) {
		int a = this.first.compareTo(o.first);
		int b = this.second.compareTo(o.second);
		if(a != 0) return a;
		else if(b != 0) return b;
		return 0;
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof pair<?, ?>)) return false;
		try {
			if(((Comparable<pair<T, U>>) o).compareTo(this) == 0) return true;
			else return false;
		}catch(Error E) {
			return false;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public pair<T, U> clone(){
		try {
			return (pair<T, U>) super.clone();
		}
		catch(CloneNotSupportedException e) {
			return new pair<T, U>(this.first, this.second);
		}
	}
	
	@Override
	public String toString() {
		return "("+first+" "+second+")";
	}
}
