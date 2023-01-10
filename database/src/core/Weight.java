package core;


import java.util.List;

public class Weight extends DataClass {
	
	private String[] col = {  "Номер груза", "Вес", "Состояние перевозки", 
			"Дата доставки"};
	
	
	public Weight() {
		super();
		columnNames = col;
		title = "груз";
	}
	
	public Weight(List<Object> data) { super(data); columnNames = col; title = "груз"; }

}
