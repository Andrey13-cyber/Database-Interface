package core;

	
import java.util.List;

public class Flight extends DataClass  {
	
	private String[] col = { "номер рейса", "пункт отправления", "пункт назначения", 
			"дата", "время вылета", "номер самолета"};
	
	public Flight() {
		super();
		columnNames = col;
		title = "рейс";
	}
	
	public Flight(List<Object> data) { super(data); columnNames = col; title = "рейс"; }

}
