package a.test;

import com.keyroy.util.json.Json;

public class Test {
	public static void main(String[] args) {
		toJson();
	}

	private static final void toJson() {

		try {
			T1 t1 = new T1();
			t1.map.put(1, "1");
			t1.map.put(2, "2");
			t1.map.put(3, "3");

			System.out.println("---------------make json----------------");
			Json json = new Json(t1);
			String jsonString = json.toString();
			System.out.println(jsonString);
			
			System.out.println("----------------make object---------------");
			Json newJson = new Json(jsonString);
			
			t1 = (T1) newJson.toObject(T1.class);
			System.out.println(new Json(t1).toString());
			//
			// json = new Json(t1);
			// System.out.println(json.toFormateString(Charset.forName("utf-8")));
			// System.out.println("********************************");

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("over");

	}
}
