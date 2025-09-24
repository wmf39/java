package know.how.computer2;

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
	public HashMap<Integer, Triple<Command, Integer, Integer>> getProgram() {
		HashMap<Integer, Triple<Command, Integer, Integer>> program = new HashMap<>();
		// storage address; command, source, target
		// Load counter, backup Multiplier b
		program.put(1, new Triple(Command.STR, 1, 4));				// Store R1 -> R4 (counter)
		program.put(2, new Triple(Command.STR, 2, 5));				// Store R2 -> R5 (backup)
		// Multiply
		program.put(3, new Triple(Command.DEC, null, 2));			// Dec R2
		program.put(4, new Triple(Command.INC, null, 3));			// Inc R3 (result)
		program.put(5, new Triple(Command.EQZ, null, 2));			// R2 empty?
		program.put(6, new Triple(Command.SPC, null, 3));			// No
		// Restore Multiplier b
		program.put(7, new Triple(Command.STR, 5, 2));				// Store R5 -> R2
		// Multiply loop
		program.put(8, new Triple(Command.DEC, null, 4));			// Dec R4
		program.put(9, new Triple(Command.EQZ, null, 4));			// R4 empty?
		program.put(10, new Triple(Command.SPC, null, 3));			// No
		// End
		program.put(11, new Triple(Command.STP, null, null));		// Stop
		return program;
	}
}
