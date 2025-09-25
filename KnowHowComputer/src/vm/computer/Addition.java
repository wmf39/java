package vm.computer;

import java.util.HashMap;

public class Addition implements Software {
		
	@Override
	public HashMap<Integer, Integer> getInitialisation() {
		HashMap<Integer, Integer> registers = new HashMap<>();
		// register address, register value
		registers.put(1, 2);
		registers.put(2, 3);
		return registers;
	}

	@Override
	public HashMap<Integer, Triple<Command, Integer, Integer>> getProgram() {
		HashMap<Integer, Triple<Command, Integer, Integer>> program = new HashMap<>();
		// storage address; command, source, target
		program.put(1, new Triple<>(Command.JMP, null, 4));
		program.put(2, new Triple<>(Command.INC, null, 1));
		program.put(3, new Triple<>(Command.DEC, null, 2));
		program.put(4, new Triple<>(Command.EQZ, null, 2));
		program.put(5, new Triple<>(Command.JMP, null, 2));
		program.put(6, new Triple<>(Command.STP, null, null));
		return program;
	}
}
