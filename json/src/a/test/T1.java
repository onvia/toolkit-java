package a.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class T1 implements Serializable {
	private static final long serialVersionUID = 1L;
	public String name = "中文";
	protected int id = 1;
	protected String[] strings = { "abc", "def", "gh" };
	protected HashMap<Integer, String> map = new HashMap<Integer, String>();
	protected T2 t2 = new T2();
	protected List<T2> t2List = new ArrayList<T2>();
	protected Map<Integer, T2> t2Map = new HashMap<Integer, T2>();

	public T1() {
		for (int i = 0; i < 3; i++) {
			if (i % 2 == 0) {
				t2List.add(new T2());
			} else {
				t2List.add(new T3());
			}
		}

		for (int i = 0; i < 3; i++) {
			T2 t2 = new T2();
			t2Map.put(t2.id, t2);
		}
	}
}
