package vm.computer;

import java.util.HashMap;

public class Multiplication2 implements Software {
			
	@Override
	public HashMap<Integer, Integer> getInitialisation() {
		HashMap<Integer, Integer> registers = new HashMap<>();
		// register address, register value
		registers.put(1, 3);
		registers.put(2, 4);
		return registers;
	}

	@Override
	public HashMap<Integer, Instruction<Mnemonic, Integer, Integer>> getProgram() {
		HashMap<Integer, Instruction<Mnemonic, Integer, Integer>> program = new HashMap<>();
		// storage address; mnemonic, target, source
		// Load counter, backup Multiplier b
		program.put(1, new Instruction<>(Mnemonic.STR, 4, 1));		// Store R1 -> R4 (counter)
		program.put(2, new Instruction<>(Mnemonic.STR, 5, 2));		// Store R2 -> R5 (backup)
		// Multiply
		program.put(3, new Instruction<>(Mnemonic.DEC, 2));			// Dec R2
		program.put(4, new Instruction<>(Mnemonic.INC, 3));			// Inc R3 (result)
		program.put(5, new Instruction<>(Mnemonic.EQZ, 2));			// R2 empty?
		program.put(6, new Instruction<>(Mnemonic.JMP, 3));			// No
		// Restore Multiplier b
		program.put(7, new Instruction<>(Mnemonic.STR, 2, 5));		// Store R5 -> R2
		// Multiply loop
		program.put(8, new Instruction<>(Mnemonic.DEC, 4));			// Dec R4
		program.put(9, new Instruction<>(Mnemonic.EQZ, 4));			// R4 empty?
		program.put(10, new Instruction<>(Mnemonic.JMP, 3));		// No
		// End
		program.put(11, new Instruction<>(Mnemonic.STP, null));		// Stop
		return program;
	}
}
