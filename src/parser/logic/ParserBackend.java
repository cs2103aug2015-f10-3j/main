package parser.logic;

public class ParserBackend implements ParserConstants {

	public ParserBackend() {
		setupCommandEnums();
	}
	
	private void setupCommandEnums() {
		setupAddOption();
		setupViewOption();
		setupEditOption();
		setupDeleteOption();
		setupCompleteOption();
		setupSearchOption();
		setupUndoOption();
		setupRedoOption();
		setupExitOption();
		setupHelpOption();
	}

	private void setupHelpOption() {
		helpOptions.put(OPTIONS.HELP, TYPE.STRING);
	}

	private void setupExitOption() {
		exitOptions.put(OPTIONS.EXIT, TYPE.NONE);
	}

	private void setupRedoOption() {
		redoOptions.put(OPTIONS.REDO, TYPE.NONE);
	}

	private void setupUndoOption() {
		undoOptions.put(OPTIONS.UNDO, TYPE.NONE);
	}

	private void setupSearchOption() {
		searchOptions.put(OPTIONS.SEARCH, TYPE.STRING_ARRAY);
	}

	private void setupCompleteOption() {
		completeOptions.put(OPTIONS.COMPLETE, TYPE.INTEGER_ARRAY);
	}

	private void setupDeleteOption() {
		deleteOptions.put(OPTIONS.DELETE, TYPE.INTEGER_ARRAY);
	}

	private void setupEditOption() {
		editOptions.put(OPTIONS.EDIT, TYPE.INTEGER);
		editOptions.put(OPTIONS.DESC, TYPE.STRING);
		editOptions.put(OPTIONS.BY, TYPE.DATE);
		editOptions.put(OPTIONS.REMIND, TYPE.DATE);
		editOptions.put(OPTIONS.BETWEEN, TYPE.DATE);
		editOptions.put(OPTIONS.AND, TYPE.DATE);
		editOptions.put(OPTIONS.START, TYPE.DATE);
		editOptions.put(OPTIONS.END, TYPE.DATE);
	}

	private void setupViewOption() {
		viewOptions.put(OPTIONS.VIEW, TYPE.NONE);
		viewOptions.put(OPTIONS.COMPLETE, TYPE.NONE);
		viewOptions.put(OPTIONS.ALL, TYPE.NONE);
		viewOptions.put(OPTIONS.FLOATING, TYPE.NONE);
		viewOptions.put(OPTIONS.TODAY, TYPE.NONE);
		viewOptions.put(OPTIONS.TOMORROW, TYPE.NONE);
		viewOptions.put(OPTIONS.WEEK, TYPE.NONE);
		viewOptions.put(OPTIONS.MONTH, TYPE.NONE);
	}

	private void setupAddOption() {
		addOptions.put(OPTIONS.ADD, TYPE.STRING);
		addOptions.put(OPTIONS.DESC, TYPE.STRING);
		addOptions.put(OPTIONS.BY, TYPE.DATE);
		addOptions.put(OPTIONS.REMIND, TYPE.DATE);
		addOptions.put(OPTIONS.BETWEEN, TYPE.DATE);
		addOptions.put(OPTIONS.AND, TYPE.DATE);
		addOptions.put(OPTIONS.START, TYPE.DATE);
		addOptions.put(OPTIONS.END, TYPE.DATE);
	}
}
