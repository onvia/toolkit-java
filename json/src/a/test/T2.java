package a.test;

import com.keyroy.util.json.JsonAn;

public class T2 {
	private static int ID;
	@JsonAn(value = "4name")
	protected String name = "T2";
	protected int id = (ID++);
}
