package vm.computer;

import java.util.HashMap;

public class Multiplication implements Software {
			
	@Override
	public HashMap<Integer, Integer> getInitialisation() {
		HashMap<Integer, Integer> registers = new HashMap<>();
		// register address, register value
		registers.put(1, 3);
		registers.put(2, 4);
		return registers;
	}

	@Override
	public HashMap<Integer, Instruction<Mnemonic, Integer, Integer, Integer>> getProgram() {
		HashMap<Integer, Instruction<Mnemonic, Integer, Integer, Integer>> program = new HashMap<>();
		// storage address; mnemonic, target
		// Load counter, backup Multiplier a
		program.put(1, new Instruction<>(Mnemonic.JMP, 5));			// Start checking if R1 empty
		program.put(2, new Instruction<>(Mnemonic.DEC, 1));			// Dec R1
		program.put(3, new Instruction<>(Mnemonic.INC, 4));			// Inc R4 (counter)
		program.put(4, new Instruction<>(Mnemonic.INC, 6));			// Inc R6 (backup R1)
		program.put(5, new Instruction<>(Mnemonic.EQZ, 1));			// R1 empty?
		program.put(6, new Instruction<>(Mnemonic.JMP, 2));			// No
		// Multiply
		program.put(7, new Instruction<>(Mnemonic.DEC, 2));			// Dec R2
		program.put(8, new Instruction<>(Mnemonic.INC, 3));			// Inc R3 (result)
		program.put(9, new Instruction<>(Mnemonic.INC, 5));			// Inc R5 (backup R2)
		program.put(10, new Instruction<>(Mnemonic.EQZ, 2));		// R2 empty?
		program.put(11, new Instruction<>(Mnemonic.JMP, 7));		// No
		// Restore Multiplier b
		program.put(12, new Instruction<>(Mnemonic.DEC, 5));		// Dec R5
		program.put(13, new Instruction<>(Mnemonic.INC, 2));		// Inc R2
		program.put(14, new Instruction<>(Mnemonic.EQZ, 5));		// R5 empty?
		program.put(15, new Instruction<>(Mnemonic.JMP, 12));		// No
		// Multiply loop
		program.put(16, new Instruction<>(Mnemonic.DEC, 4));		// Dec R4
		program.put(17, new Instruction<>(Mnemonic.EQZ, 4));		// R4 empty?
		program.put(18, new Instruction<>(Mnemonic.JMP, 7));		// No
		// Restore Multiplier a
		program.put(19, new Instruction<>(Mnemonic.DEC, 6));		// Dec R6
		program.put(20, new Instruction<>(Mnemonic.INC, 1));		// Inc R1
		program.put(21, new Instruction<>(Mnemonic.EQZ, 6));		// R6 empty?
		program.put(22, new Instruction<>(Mnemonic.JMP, 19));		// No
		// End
		program.put(23, new Instruction<>(Mnemonic.STP));			// Stop
		return program;
	}
}

