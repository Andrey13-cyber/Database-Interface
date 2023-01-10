package core;

import java.util.List;

public class CheckHealth extends DataClass {
	
	private String[] col = { "Номер проверки здоровья", "Дата проверки", "Статус проверки"};
	
	public CheckHealth() {
		super();
		columnNames = col;
		title = "Проверка здоровья";
	}

	public CheckHealth(List<Object> data) { super(data); columnNames = col; title = "Проверка здоровья"; }

}

