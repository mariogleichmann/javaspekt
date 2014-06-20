package streams.spliterator;

public class Pair<A,B> {

	private A a;
	private B b;
	
	public Pair( A a, B b ) {
		this.a = a;
		this.b = b;
	}

	public A fst(){
		return a;
	}
	
	public B snd(){
		return b;
	}
	
	public String toString(){
		return new StringBuffer( "( " ).append( a ).append( ", " ).append( b ).append( " )" ).toString();
	}
	
	public static <X,Y> Pair<X,Y> pair( X x, Y y ){
		return new Pair<X,Y>( x, y );
	}
}
