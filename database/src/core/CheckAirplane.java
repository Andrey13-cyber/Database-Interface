package core;

import java.util.List;

public class CheckAirplane extends DataClass {
	
	private String[] col = { "Номер проверки самолета", "Дата проверки", "Состояние"};
	
	public CheckAirplane() {
		super();
		columnNames = col;
		title = "Проверка самолета";
	}

	public CheckAirplane(List<Object> data) { super(data); columnNames = col; title = "Проверка самолет"; }

}

