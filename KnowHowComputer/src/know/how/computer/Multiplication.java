package know.how.computer;

public class Multiplication implements Software {
	
	private int programLength = 23;
		
	public int[] getRegisterInitValues() {
		int[] registerValues = new int[2];
		registerValues[0] = 3;
		registerValues[1] = 4;
		return registerValues;
	}
	
	public Command[] getCommands() {
		Command[] cmds = new Command[programLength];
		// Load counter, backup Multiplier a
		cmds[0] = Command.SPC;
		cmds[1] = Command.DEC;
		cmds[2] = Command.INC;
		cmds[3] = Command.INC;
		cmds[4] = Command.EQZ;
		cmds[5] = Command.SPC;
		// Multiply
		cmds[6] = Command.DEC;
		cmds[7] = Command.INC;
		cmds[8] = Command.INC;
		cmds[9] = Command.EQZ;
		cmds[10] = Command.SPC;
		// Restore Multiplier b
		cmds[11] = Command.DEC;
		cmds[12] = Command.INC;
		cmds[13] = Command.EQZ;
		cmds[14] = Command.SPC;
		// Multiply loop
		cmds[15] = Command.DEC;
		cmds[16] = Command.EQZ;
		cmds[17] = Command.SPC;
		// Restore Multiplier a
		cmds[18] = Command.DEC;
		cmds[19] = Command.INC;
		cmds[20] = Command.EQZ;
		cmds[21] = Command.SPC;
		// End
		cmds[22] = Command.STP;
		return cmds;
	}
	
	public int[] getTargets() {
		int[] targets = new int[programLength];
		targets[0] = 5;		// Start checking if R1 empty
		targets[1] = 1;		// Dec R1
		targets[2] = 4;		// Inc R4 (counter)
		targets[3] = 6;		// Inc R6 (backup R1)
		targets[4] = 1;		// R1 empty?
		targets[5] = 2;		// No
		
		targets[6] = 2;		// Dec R2
		targets[7] = 3;		// Inc R3 (result)
		targets[8] = 5;		// Inc R5 (backup R2)
		targets[9] = 2;		// R2 empty?
		targets[10] = 7;	// No
		
		targets[11] = 5;	// Dec R5
		targets[12] = 2;	// Inc R2
		targets[13] = 5;	// R5 empty?
		targets[14] = 12;	// No
		
		targets[15] = 4;	// Dec R4
		targets[16] = 4;	// R4 empty?
		targets[17] = 7;	// No
		
		targets[18] = 6;	// Dec R6
		targets[19] = 1;	// Inc R1
		targets[20] = 6;	// R6 empty?
		targets[21] = 19;	// No
		
		targets[22] = 0;	// STOP to null
		return targets;
	}
}
