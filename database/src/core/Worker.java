package core;

import java.util.List;

public class Worker extends DataClass {

	private String[] col = { "Номер сотрудника", "Должность", "ФИО", "Зарплата", "Номер самолета", "Номер медкомиссии", "Номер проверки"};
	
	public Worker() {
		super();
		columnNames = col;
		title = "сотрудник";
	}
	
	public Worker(List<Object> data) { super(data); columnNames = col; title = "сотрудник"; }

}
