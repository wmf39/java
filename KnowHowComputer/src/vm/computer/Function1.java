package vm.computer;

import java.util.HashMap;

public class Function1 implements Software {
	
	@Override
	public HashMap<Integer, Integer> getInitialisation() {
		HashMap<Integer, Integer> registers = new HashMap<>();
		// register address, register value
		registers.put(1, 1);
		registers.put(2, 7);
		return registers;
	}

	@Override
	public HashMap<Integer, Instruction<Mnemonic, Integer, Integer, Integer>> getProgram() {
		HashMap<Integer, Instruction<Mnemonic, Integer, Integer, Integer>> program = new HashMap<>();
		program.put(1, new Instruction<>(Mnemonic.PUSH, 1));			// push value 1
		program.put(2, new Instruction<>(Mnemonic.PUSH, 2));			// push value 2
		program.put(3, new Instruction<>(Mnemonic.CALL, 13));			// function call
		program.put(4, new Instruction<>(Mnemonic.STP));				// stop
		program.put(13, new Instruction<>(Mnemonic.POP, 8));			// save return address to R8
		program.put(14, new Instruction<>(Mnemonic.POP, 4));			// function: restore value 2 to R4
		program.put(15, new Instruction<>(Mnemonic.POP, 3));			// function: restore value 1 to R3
		program.put(16, new Instruction<>(Mnemonic.ADD, 5, 3, 4));		// function: value 1 + value 2 to R5
		program.put(17, new Instruction<>(Mnemonic.PUSH, 8));			// restore return address on stack
		program.put(18, new Instruction<>(Mnemonic.RET));				// return to 
		return program;
	}
}
