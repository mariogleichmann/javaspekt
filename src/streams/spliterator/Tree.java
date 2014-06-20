package streams.spliterator;

import static java.lang.Math.max;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Tree<A> implements Iterable<A> {

	private A value;

	private Optional<Tree<A>> left = Optional.empty();

	private Optional<Tree<A>> right = Optional.empty();
	
	
	private int depth = -1;
	
	private int size = -1;
	
	private List<A> elements = null;
	

	public boolean isLeaf() {
		return !left.isPresent() && !right.isPresent();
	}

	
	public A value() {
		return value;
	}

	
	public Optional<Tree<A>> left() {
		return left;
	}

	
	public Optional<Tree<A>> right() {
		return right;
	}


	public int depth() {

		if( depth == -1 ){
			
			depth = 1 + max( left.map( t -> t.depth() ).orElse(0),
							 right.map( t -> t.depth() ).orElse(0) );
		}

		return depth;
	}

	
	public int size() {
		
		if( size == -1 ){
			
			size = 1 + left.map(t -> t.size()).orElse(0) + right.map(t -> t.size()).orElse(0);
		}
		
		return size;
	}

	
	private List<A> collectTo( List<A> accu ) {

		if( elements == null ){
			
			elements = new LinkedList<>();
			
			elements.add( value );

			left.ifPresent(t -> t.collectTo( elements ) );

			right.ifPresent(t -> t.collectTo( elements ) );
		}
		
		accu.addAll( elements );

		return accu;

	}


	@Override
	public Iterator<A> iterator() {
		return collectTo( new LinkedList<A>() ).iterator();
	}

	
	public Stream<A> stream() {
		return StreamSupport.stream( Spliterators.spliteratorUnknownSize( iterator(), Spliterator.IMMUTABLE), false );
	}

	
	
	public static <T> Tree<T> leaf(T value) {

		Tree<T> leaf = new Tree<T>();

		leaf.value = value;

		return leaf;
	}

	
	public static <T> Tree<T> node(T value, Tree<T> left, Tree<T> right) {

		Tree<T> node = new Tree<T>();

		node.value = value;

		node.left = Optional.ofNullable(left);

		node.right = Optional.ofNullable(right);

		return node;
	}

}