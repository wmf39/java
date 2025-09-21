package know.how.computer;

public class Hardware {

	public static final int MIN_STORAGE_SIZE = 1;
	public static final int MAX_STORAGE_SIZE = 100;
	
	private int[] dataRegister = new int[8];
	private Command [] commands;
	private int [] targets;

	public Hardware(Software software) {
		this.commands = software.getCommands();
		this.targets = software.getTargets();
		if(commands.length != targets.length)
			throw new IllegalArgumentException("program size missmatch");
		if(commands.length < MIN_STORAGE_SIZE || commands.length > MAX_STORAGE_SIZE)
			throw new IllegalArgumentException("storage size missmatch");
		for(int i=0; i<dataRegister.length; i++) {
			dataRegister[i] = 0;
		}
		int [] initRegisters = software.getRegisterInitValues();
		for(int i=0; i<initRegisters.length; i++) {
			dataRegister[i] = initRegisters[i];
			System.out.println("Set register" + (i+1) + " to " + initRegisters[i]);
		}
	}
	
	public void run() {
		int pc = 1;
		boolean run = true;
		while(run) {
			Command cmd = commands[pc-1];
			int target = 0;
			int data = 0;
			switch (cmd) {
		    case INC:
		    	target = targets[pc-1]-1;
		        data = ++dataRegister[target];
		        System.out.println("Incremented register" + (target+1) + " to " + data);
		        pc++;
		        break;
		    case DEC:
		    	target = targets[pc-1]-1;
		        data = --dataRegister[target];
		        System.out.println("Decremented register" + (target+1) + " to " + data);
		        pc++;
		        break;
		    case SPC:
		    	pc = targets[pc-1];
		    	System.out.println("Set program counter to " + pc);
		        break;
		    case EQZ:
		    	target = targets[pc-1]-1;
		    	if(dataRegister[target] == 0) {
		    		pc+=2;
		    		System.out.println("Equal to zero, set program counter to " + pc);
		    	}
		    	else {
		    		pc++;
		    		System.out.println("Not equal to zero, set program counter to " + pc);
		    	}
		        break;
		    case STP:
		    	run = false;
		    	System.out.println("Stop\n");
		        break;	        
		    default:
		    	run = false;
			}
			if(run) {
				print(pc);
			}
		}	
	}
	
	private void print(int pc) {
		System.out.println("Program counter: " + pc);
		for(int i=0; i<dataRegister.length; i++) {
			System.out.println("Data register " + (i+1) + ": " + dataRegister[i]);
		}
		System.out.println("-------------------------");
	}
}
