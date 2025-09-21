package know.how.computer;

public class Software {
	
	private int programLength = 6;
		
	public int[] getRegisterInitValues() {
		int[] registerValues = new int[2];
		registerValues[0] = 2;
		registerValues[1] = 3;
		return registerValues;
	}
	
	public Command[] getCommands() {
		Command[] cmds = new Command[programLength];
		cmds[0] = Command.SPC;
		cmds[1] = Command.INC;
		cmds[2] = Command.DEC;
		cmds[3] = Command.EQZ;
		cmds[4] = Command.SPC;
		cmds[5] = Command.STP;
		return cmds;
	}
	
	public int[] getTargets() {
		int[] targets = new int[programLength];
		targets[0] = 4;
		targets[1] = 1;
		targets[2] = 2;
		targets[3] = 2;
		targets[4] = 2;
		targets[5] = 0; // STOP to null
		return targets;
	}
}
