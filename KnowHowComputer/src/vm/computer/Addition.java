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
	public HashMap<Integer, Instruction<Mnemonic, Integer, Integer, Integer>> getProgram() {
		HashMap<Integer, Instruction<Mnemonic, Integer, Integer, Integer>> program = new HashMap<>();
		// storage address; mnemonic, target
		program.put(1, new Instruction<>(Mnemonic.JMP, 4));
		program.put(2, new Instruction<>(Mnemonic.INC, 1));
		program.put(3, new Instruction<>(Mnemonic.DEC, 2));
		program.put(4, new Instruction<>(Mnemonic.EQZ, 2));
		program.put(5, new Instruction<>(Mnemonic.JMP, 2));
		program.put(6, new Instruction<>(Mnemonic.STP));
		return program;
	}
}
