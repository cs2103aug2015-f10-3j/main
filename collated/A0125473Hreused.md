# A0125473Hreused
###### src\main\paddletask\command\api\HelpCommand.java
``` java
	@Override
	public ArrayList<Task> undo() {
		// TODO Auto-generated method stub
		return null;
	}

}
```
###### src\main\paddletask\command\api\MoreCommand.java
``` java
	@Override
	public ArrayList<Task> undo() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
```
###### src\main\paddletask\command\api\ViewTaskCommand.java
``` java
	@Override
	public ArrayList<Task> undo() {
		// TODO Auto-generated method stub
		return null;
	}
}
```
###### src\main\paddletask\common\data\HelpManual.java
``` java
	protected static enum COMMANDS {
		ADD("add"), VIEW("view"), EDIT("edit"), DELETE("delete"), 
		COMPLETE("complete"), SEARCH("search"), UNDO("undo"), REDO("redo"),
		EXIT("exit"), CLEAR("clear"), HELP("help");

		private final String commandText;
		
		private COMMANDS(final String commandText) {
			this.commandText = commandText;
		}

		public String toString() {
			return commandText;
		}
	}

	
	
}
```
