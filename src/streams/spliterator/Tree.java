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

	private int depth = -1;

	public int depth() {

		if (depth == -1)
			depth = 1 + max(left.map(t -> t.depth()).orElse(0),
					right.map(t -> t.depth()).orElse(0));

		return depth;
	}

	private int size = -1;
	
	public int size() {
		if( size == -1 )
		 size = 1 + left.map(t -> t.size()).orElse(0) + right.map(t -> t.size()).orElse(0);
		return size;
	}

	private List<A> collected = null;
	
	private List<A> collectTo(List<A> accu) {

		if( collected == null ){
			collected = new LinkedList<>();
			
			collected.add(value);

			left.ifPresent(t -> t.collectTo(collected));

			right.ifPresent(t -> t.collectTo(collected));
		}
		
		accu.addAll( collected );

		return accu;

	}

	
	public List<A> allElementsDownTo(int depth) {
		return collectElementsTo(new LinkedList<A>(), depth);
	}

	
	public List<Tree<A>> allSubtreesDownFrom(int depth) {
		return collectSubtreesTo(new LinkedList<Tree<A>>(), depth);
	}

	
	private List<A> collectElementsTo(List<A> accu, int maxdepth) {

		if (maxdepth > 0) {

			accu.add(value);

			left.ifPresent(t -> t.collectElementsTo(accu, maxdepth - 1));

			right.ifPresent(t -> t.collectElementsTo(accu, maxdepth - 1));

		}

		return accu;
	}

	
	private List<Tree<A>> collectSubtreesTo(List<Tree<A>> accu, int maxdepth) {

		if (maxdepth > 0) {

			left.ifPresent(t -> t.collectSubtreesTo(accu, maxdepth - 1));

			right.ifPresent(t -> t.collectSubtreesTo(accu, maxdepth - 1));
		}
		else {

			left.ifPresent(t -> accu.add(t));

			right.ifPresent(t -> accu.add(t));
		}

		return accu;
	}

	@Override
	public Iterator<A> iterator() {
		return collectTo(new LinkedList<A>()).iterator();
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