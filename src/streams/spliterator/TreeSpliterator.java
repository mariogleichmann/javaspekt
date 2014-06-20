package streams.spliterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class TreeSpliterator<T> implements Spliterator<T> {

	private List<Tree<T>> trees = new ArrayList<Tree<T>>();

	
	public TreeSpliterator(Tree<T> tree) {
		trees.add( tree );
	}

	
	@Override
	public boolean tryAdvance(Consumer<? super T> action) {

		if (!trees.isEmpty()) {

			Tree<T> tree = trees.remove(0);

			tree.left().ifPresent(t -> trees.add(t));

			tree.right().ifPresent(t -> trees.add(t));

			action.accept(tree.value());

			return true;
		}

		return false;
	}

	
	@Override
	public Spliterator<T> trySplit() {

		if (trees.size() >= 2 && trees.get(0).depth() > 10 ) {

			return new TreeSpliterator<T>(trees.remove(0));
		}
		
		return null;
	}

	
	@Override
	public long estimateSize() {
		return trees.stream().reduce( 0, (acc, t ) -> acc+t.size(), Integer::sum ).longValue();
	}
	
	
	@Override
	public void forEachRemaining(Consumer<? super T> action){
		for( Tree<T> t : trees ){
			t.iterator().forEachRemaining(action);
		}
	}
	

	@Override
	public int characteristics() {
		return CONCURRENT | IMMUTABLE | SIZED | SUBSIZED | NONNULL;
	}

}