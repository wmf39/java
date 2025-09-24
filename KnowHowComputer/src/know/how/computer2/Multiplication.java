package know.how.computer2;

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
	public HashMap<Integer, Triple<Command, Integer, Integer>> getProgram() {
		HashMap<Integer, Triple<Command, Integer, Integer>> program = new HashMap<>();
		// storage address; command, source, target
		// Load counter, backup Multiplier a
		program.put(1, new Triple(Command.SPC, null, 5));			// Start checking if R1 empty
		program.put(2, new Triple(Command.DEC, null, 1));			// Dec R1
		program.put(3, new Triple(Command.INC, null, 4));			// Inc R4 (counter)
		program.put(4, new Triple(Command.INC, null, 6));			// Inc R6 (backup R1)
		program.put(5, new Triple(Command.EQZ, null, 1));			// R1 empty?
		program.put(6, new Triple(Command.SPC, null, 2));			// No
		// Multiply
		program.put(7, new Triple(Command.DEC, null, 2));			// Dec R2
		program.put(8, new Triple(Command.INC, null, 3));			// Inc R3 (result)
		program.put(9, new Triple(Command.INC, null, 5));			// Inc R5 (backup R2)
		program.put(10, new Triple(Command.EQZ, null, 2));			// R2 empty?
		program.put(11, new Triple(Command.SPC, null, 7));			// No
		// Restore Multiplier b
		program.put(12, new Triple(Command.DEC, null, 5));			// Dec R5
		program.put(13, new Triple(Command.INC, null, 2));			// Inc R2
		program.put(14, new Triple(Command.EQZ, null, 5));			// R5 empty?
		program.put(15, new Triple(Command.SPC, null, 12));			// No
		// Multiply loop
		program.put(16, new Triple(Command.DEC, null, 4));			// Dec R4
		program.put(17, new Triple(Command.EQZ, null, 4));			// R4 empty?
		program.put(18, new Triple(Command.SPC, null, 7));			// No
		// Restore Multiplier a
		program.put(19, new Triple(Command.DEC, null, 6));			// Dec R6
		program.put(20, new Triple(Command.INC, null, 1));			// Inc R1
		program.put(21, new Triple(Command.EQZ, null, 6));			// R6 empty?
		program.put(22, new Triple(Command.SPC, null, 19));			// No
		// End
		program.put(23, new Triple(Command.STP, null, null));		// Stop
		return program;
	}
}
