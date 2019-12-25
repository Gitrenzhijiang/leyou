
public class MinHeap<T> {

	

	public MinHeap( int capacity) {
		this.capacity = capacity;
		arr = (T[]) new Object[capacity];
		count = 0;
	}
	/**
	 * 插入一个数
	 * @param e
	 */
	public void insert(T e) {
		arr[count] = e;
		shiftUp(count);
		count++;
	}
	public T extraction() {
		T ret = arr[0];
		swap(arr, 0, count-1);
		count--;
		shiftDown(0);
		return ret;
	}

	protected void swap(T[] arr, int i, int j) {
		T t = arr[i];
		arr [i] = arr [j];
		arr [j] = t;
	}
	protected static<T> int compare(T t1, T t2) {
		Comparable c1 = (Comparable) t1;
		Comparable c2 = (Comparable) t2;
		return c1.compareTo(c2);
	}
	//尾部插入一个数时,使这个数进入最小堆
	private void shiftUp(int k) {
		int p = 0;
		T e = arr[k];
		while( (p = (k%2==0?k/2-1:k/2)) >= 0 && compare(arr[p], e) > 0) {
			arr[k] = arr[p];
			k = p;
		}
		arr[k] = e;
	}
	private void shiftDown(int k) {
		T e = arr[k];
		int j = 2*k+1;
		while(j < count) {
			if(j + 1 < count && compare(arr[j+1], arr[j]) < 0)
				j+=1;
			if(compare(e, arr[j]) > 0) {
				arr[k] = arr[j];
				k = j;
				j = 2*k+1;
			}else {
				break;
			}
		}
		arr[k] = e;
	}
	
	public int size() {
		return count;
	}
	public boolean isEmpty() {
		return count==0;
	}
	//取出堆顶元素
	public T getTop() {
		if(isEmpty())
			throw new RuntimeException("空堆");
		return arr[0];
	}
	
	protected T []arr;//存储数据的数组
	protected int count;//当前堆的大小
	protected int capacity;//堆的容量
}
