# A0125473Hunused
###### src\main\paddletask\common\data\Pair.java
``` java
package main.paddletask.common.data;

public class Pair<A,B> {
	
	private A first;
	private B second;
	
	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}

	public A getFirst() {
		return first;
	}

	public void setFirst(A first) {
		this.first = first;
	}

	public B getSecond() {
		return second;
	}

	public void setSecond(B second) {
		this.second = second;
	}
}
```
