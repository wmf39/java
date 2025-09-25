package know.how.computer2;

import java.util.HashMap;

public class Hardware {

	public static final int MIN_STORAGE_ADR = 1;
	public static final int MAX_STORAGE_ADR = 100;
	public static final int MIN_REGISTER_ADR = 1;
	public static final int MAX_REGISTER_ADR = 8;
	
	HashMap <Integer, Integer >dataRegister = new HashMap<>();
	HashMap<Integer, Triple<Command, Integer, Integer>> program;

	public Hardware(Software software) {
		this.program = software.getProgram();
		if(program.size() < MIN_STORAGE_ADR || program.size() > MAX_STORAGE_ADR)
			throw new IllegalArgumentException("storage size missmatch");
		for (int i = 1; i <= 8; i++) {
			dataRegister.put(i, 0);
        }
		HashMap <Integer, Integer> initValues = software.getInitialisation();
		if(initValues.size() > MAX_REGISTER_ADR)
			throw new IllegalArgumentException("register size missmatch");
		if(!initValues.isEmpty()) {
			for(Integer regAdr : initValues.keySet()) {
				if(MIN_REGISTER_ADR <= regAdr && regAdr <= MAX_REGISTER_ADR) {
					Integer regVal = initValues.get(regAdr);
					dataRegister.put(regAdr, regVal);
					System.out.println("Set register" + regAdr + " to " + regVal);
				}
				else {
					throw new IllegalArgumentException("wrong register address");
				}		
			}
		}
	}
	
	public void run() {
		int pc = 1;
		boolean run = true;
		while(run) {
			Command cmd = program.get(pc).first;
			Integer target = 0;
			Integer data = 0;
			switch (cmd) {
		    case INC:
		    	target = program.get(pc).third;
		        data = dataRegister.get(target) + 1;
		        dataRegister.put(target, data);
		        System.out.println("Incremented register" + target + " to " + data);
		        pc++;
		        break;
		    case DEC:
		    	target = program.get(pc).third;
		        data = dataRegister.get(target) - 1;
		        dataRegister.put(target, data);
		        System.out.println("Decremented register" + target + " to " + data);
		        pc++;
		        break;
		    case SPC:
		    	pc = program.get(pc).third;
		    	System.out.println("Set program counter to " + pc);
		        break;
		    case EQZ:
		    	target = program.get(pc).third;
		    	if(dataRegister.get(target) == 0) {
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
		for(Integer regAdr : dataRegister.keySet()) {
			System.out.println("Data register " + regAdr + ": " + dataRegister.get(regAdr));
		}
		System.out.println("-------------------------");
	}
}
