package core;

import java.util.List;

public class Client extends DataClass {

	private String[] col = { "Номер клиента", "Название предприятия", "ФИО", "Телефон"};
	
	public Client() {
		super();
		columnNames = col;
		title = "клиент";
	}
	
	public Client(List<Object> data) { super(data); columnNames = col; title = "клиент"; }

}
