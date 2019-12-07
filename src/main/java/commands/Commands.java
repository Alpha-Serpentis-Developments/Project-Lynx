package commands;

public enum Commands {
		
	ABOUT("name", 0),
	HELP("help", 1);
	
	private final String assignmentName = "DEFAULT";
	private final int assignmentNum = -1;
	
	Commands(String n, int i) {
		
	}
	
	public int getCommandAssignmentNum(String n) {
		
		
		return -1;
	}
}