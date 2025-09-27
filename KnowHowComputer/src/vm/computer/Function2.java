package vm.computer;

import java.util.HashMap;

public class Function2 implements Software{


	
	@Override
	public HashMap<Integer, Integer> getInitialisation() {
		HashMap<Integer, Integer> registers = new HashMap<>();
		// register address, register value
		registers.put(1, 5);
		registers.put(2, 7);
		registers.put(8, 5);
		return registers;
	}

	@Override
	public HashMap<Integer, Instruction<Mnemonic, Integer, Integer, Integer>> getProgram() {
		HashMap<Integer, Instruction<Mnemonic, Integer, Integer, Integer>> program = new HashMap<>();
		program.put(1, new Instruction<>(Mnemonic.PUSH, 8));			// push return address (4)
		program.put(2, new Instruction<>(Mnemonic.PUSH, 2));			// push value 2 (7)
		program.put(3, new Instruction<>(Mnemonic.PUSH, 1));			// push value 1 (5)
		program.put(4, new Instruction<>(Mnemonic.JMP, 14));			// function call
		program.put(5, new Instruction<>(Mnemonic.STP));				// stop
		program.put(14, new Instruction<>(Mnemonic.POP, 3));			// function: restore value 1 to R3
		program.put(15, new Instruction<>(Mnemonic.POP, 4));			// function: restore value 2 to R4
		program.put(16, new Instruction<>(Mnemonic.ADD, 5, 3, 4));		// function: value 1 + value 2 to R5
		program.put(17, new Instruction<>(Mnemonic.RET));				// return to stop (5)
		return program;
	}
}
