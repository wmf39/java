package vm.computer;

import java.util.HashMap;

public interface Software {
	public HashMap<Integer, Integer> getInitialisation();
	public HashMap<Integer, Triple<Command, Integer, Integer>> getProgram();
}
