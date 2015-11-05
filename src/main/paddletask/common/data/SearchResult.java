package main.paddletask.common.data;

import main.paddletask.task.entity.Task;

public class SearchResult implements Comparable<SearchResult> {
	
	private Integer matchCount;
	private Task matchedTask;

	public SearchResult(Integer _matchCount, Task _matchedTask) {
		matchCount = _matchCount;
		matchedTask = _matchedTask;
	}
	
	public Integer getMatchCount() {
		return matchCount;
	}
	
	public Task getMatchedTask() {
		return matchedTask;
	}

	@Override
	public int compareTo(SearchResult o) {
		int thisId = this.getMatchedTask().getTaskId();
		int oId = o.getMatchedTask().getTaskId();
		String thisDescription = this.getMatchedTask().getDescription();
		String oDescription = this.getMatchedTask().getDescription();
		
		if (!this.getMatchCount().equals(o.getMatchCount())) {
			return o.getMatchCount() - this.getMatchCount();
		} else if (!thisDescription.equals(oDescription)) {
			return oDescription.compareTo(thisDescription);
		} else {
			return thisId - oId;
		}
	}
}
