package core;

import java.util.List;

public class Airplane extends DataClass {
	
	private String[] col = { "Номер самолета", "Модель", "Дальность полета", "Грузоподъемность"};
	
	public Airplane() {
		super();
		columnNames = col;
		title = "Самолет";
	}

	public Airplane(List<Object> data) { super(data); columnNames = col; title = "Самолет"; }

}
