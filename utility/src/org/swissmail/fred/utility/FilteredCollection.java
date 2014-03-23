package org.swissmail.fred.utility;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class FilteredCollection<E> implements Collection<E> {
	public interface CollectionFilter<E> {
		boolean pass(E element);
	}
	
	private static CollectionFilter NULL_FILTER= new CollectionFilter() {

		@Override
		public boolean pass(Object element) {
			return true;
		}
	};
	
	private class FilteredCollectionIterator implements Iterator<E>
	{
		private final CollectionFilter<E> originalFilter;
		
		private final Iterator<E> delegateIterator;
		
		private E nextElement;
		
		FilteredCollectionIterator()
		{
			this.originalFilter= filter;
			delegateIterator= delegate.iterator();
			nextElement= getNextElement();
		}

		@Override
		public boolean hasNext() {
			return nextElement != null;
		}

		@Override
		public E next() {
			if (filter != originalFilter) {
				throw new ConcurrentModificationException("filter changed");
			}
			
			E result= nextElement;
			nextElement= getNextElement();
			
			return result;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
		private E getNextElement()
		{
			while (delegateIterator.hasNext()) {
				E candidate= delegateIterator.next();
				if (originalFilter.pass(candidate)) {
					return candidate;
				}
			}
			
			return null;
		}
	}

	private final Collection<E> delegate;
	
	private CollectionFilter<E> filter= NULL_FILTER;
	
	private int filteredSize= 0;
	
	public FilteredCollection(Collection<E> delegate) {
		super();
		this.delegate = delegate;
	}
	
	public void setFilter(CollectionFilter<E> filter)
	{
		this.filter= filter;
		filteredSize= calculateFilteredSize();
	}
	
	public void clearFilter()
	{
		this.filter= null;
	}

	@Override
	public boolean add(E arg0) {
		return delegate.add(arg0);
	}

	@Override
	public boolean addAll(Collection<? extends E> arg0) {
		return delegate.addAll(arg0);
	}

	@Override
	public void clear() {
		delegate.clear();
	}

	@Override
	public boolean contains(Object arg0) {
		if (delegate.contains(arg0)) {
			filter.pass((E)arg0);
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		
		for (Object element : arg0) {
			if (!contains(element)) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public boolean isEmpty() {
		return filteredSize == 0;
	}

	@Override
	public Iterator<E> iterator() {
		return new FilteredCollectionIterator();
	}

	@Override
	public boolean remove(Object arg0) {
		return delegate.remove(arg0);
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		return delegate.removeAll(arg0);
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		return delegate.retainAll(arg0);
	}

	@Override
	public int size() {
		return filteredSize;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private int calculateFilteredSize()
	{
		int passCount= 0;
		
		for (E element : delegate) {
			if (filter.pass(element)) {
				passCount= passCount + 1;
			}
		}
		
		return passCount;
	}

}
