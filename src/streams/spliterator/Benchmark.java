package streams.spliterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import static java.util.Spliterator.CONCURRENT;
import static java.util.Spliterator.IMMUTABLE;
import static java.util.Spliterator.NONNULL;
import static java.util.Spliterator.SIZED;
import static java.util.Spliterator.SUBSIZED;
import static java.util.Spliterators.spliterator;
import static java.util.stream.Collectors.toCollection;


public class Benchmark {

	public static Tree<String> makeTree( int depth ) {

		if (depth == 0) return Tree.leaf("a");

		return Tree.node("a", makeTree(depth - 1), makeTree(depth - 1));
	}

	public static void main(String[] args) {

		Tree<String> tree = makeTree( 15 );

		long start;

		System.out.println("warm up parallel IteratorSplitertor ...");

		for (int i = 0; i <= 50; i++) {

			Iterator<String> iterator = tree.iterator();

			int size = tree.size();

			Stream<String> stream = StreamSupport.stream(
					spliterator( iterator, size, CONCURRENT & IMMUTABLE & SIZED & SUBSIZED & NONNULL ), true);

			stream.map(s -> s.length()).collect( toCollection( ArrayList::new ) );
		}

		System.out.println("parallel IteratorSplitertor ...");

		start = System.currentTimeMillis();

		for (int i = 0; i <= 50; i++) {

			Stream<String> stream = StreamSupport.stream(
					spliterator(tree.iterator(), tree.size(), CONCURRENT & IMMUTABLE & SIZED & SUBSIZED & NONNULL), true);

			 stream.map(s -> s.length()).collect( toCollection( ArrayList::new ) );
		}

		System.out.println(" ... parallel IteratorSplitertor : " + ( System.currentTimeMillis() - start ) );

		
		System.out.println("warm up (parallel) TreeSpliterator ...");

		for (int i = 0; i <= 50; i++) {

			Stream<String> stream = StreamSupport.stream( new TreeSpliterator<String>( tree ), true );

			stream.map(s -> s.length()).collect( toCollection( ArrayList::new ) );

		}

		System.out.println("parallel TreeSpliterator ...");

		start = System.currentTimeMillis();

		for (int i = 0; i <= 50; i++) {

			Stream<String> stream = StreamSupport.stream( new TreeSpliterator<String>( tree ), true );

			stream.map(s -> s.length()).collect( toCollection( ArrayList::new ) );
		}

		System.out.println(" ... parallel TreeSpliterator : " + (System.currentTimeMillis() - start));

	}

}
