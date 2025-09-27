package vm.computer;

import java.util.HashMap;

public class Hardware {

	public static final int PROGRAM_COUNTER_MIN = 1;
	public static final int STACKPOINTER_END = 1;
	public static final int STACKPOINTER_START = 128;
	public static final int MIN_STORAGE_ADR = 1;
	public static final int MAX_STORAGE_ADR = 100;
	public static final int MIN_REGISTER_ADR = 1;
	public static final int MAX_REGISTER_ADR = 8;
	public static final int MIN_RAM_ADR = 1;
	public static final int MAX_RAM_ADR = 1024;
	
	HashMap <Integer, Integer> dataRegister = new HashMap<>();
	HashMap <Integer, Integer> ram = new HashMap<>();
	HashMap<Integer, Instruction<Mnemonic, Integer, Integer, Integer>> program;

	public Hardware(Software software) {
		this.program = software.getProgram();
		initRegisters(software.getInitialisation());
	}
	
	private void  initRegisters(HashMap <Integer, Integer> initValues) {
		if(program.size() < MIN_STORAGE_ADR || program.size() > MAX_STORAGE_ADR)
			throw new IllegalArgumentException("storage size missmatch");
		for (int i = 1; i <= 8; i++) {
			dataRegister.put(i, 0);
        }
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
		int pc = PROGRAM_COUNTER_MIN;
		int sp = STACKPOINTER_START;
		boolean run = true;
		while(run) {
			Mnemonic mnemonic = program.get(pc).mnemonic;
			Integer operand1 = 0;
			Integer operand2 = 0;
			Integer operand3 = 0;
			Integer data = 0;
			switch (mnemonic) {
		    case INC:
		    	operand1 = program.get(pc).operand1;
		        data = dataRegister.get(operand1) + 1;
		        dataRegister.put(operand1, data);
		        System.out.println("INC: Incremented register" + operand1 + " to " + data);
		        pc++;
		        break;
		    case DEC:
		    	operand1 = program.get(pc).operand1;
		        data = dataRegister.get(operand1) - 1;
		        dataRegister.put(operand1, data);
		        System.out.println("DEC: Decremented register" + operand1 + " to " + data);
		        pc++;
		        break;
		    case ADD:
		    	operand1 = program.get(pc).operand1;
		    	operand2 = program.get(pc).operand2;
		    	operand3 = program.get(pc).operand3;
		        data = dataRegister.get(operand2) + dataRegister.get(operand3);
		        dataRegister.put(operand1, data);
		        System.out.println("ADD: Added register" + operand2 + " to register" + operand3 + " stored >" + data + "< in register" + operand1);
		        pc++;
		        break;
		    case MULT:
		    	operand1 = program.get(pc).operand1;
		    	operand2 = program.get(pc).operand2;
		    	operand3 = program.get(pc).operand3;
		        data = dataRegister.get(operand2) * dataRegister.get(operand3);
		        dataRegister.put(operand1, data);
		        System.out.println("MULT: Multiply register" + operand2 + " with register" + operand3 + " stored >" + data + "< in register" + operand1);
		        pc++;
		        break;
		    case JMP:
		    	pc = program.get(pc).operand1;
		    	System.out.println("JMP: Set program counter to " + pc);
		        break;
		    case EQZ:
		    	operand1 = program.get(pc).operand1;
		    	if(dataRegister.get(operand1) == 0) {
		    		pc+=2;
		    		System.out.println("EQZ: Equal to zero, set program counter to " + pc);
		    	}
		    	else {
		    		pc++;
		    		System.out.println("EQZ: Not equal to zero, set program counter to " + pc);
		    	}
		        break;
		    case STR:
		    	operand2 = program.get(pc).operand2;
		    	data = dataRegister.get(operand2);
		    	operand1 = program.get(pc).operand1;
		    	dataRegister.put(operand1, data);
		        System.out.println("STR: Store >" + data + "< from register" + operand2 + " to " + "register" + operand1);
		        pc++;
		        break;
		    case POP:
		    	if(sp >= STACKPOINTER_END) {
			    	sp++;
			    	operand1 = program.get(pc).operand1;
			    	data = ram.get(sp);
			    	dataRegister.put(operand1, data);
			        System.out.println("POP: Pop >" + data + "< from ram address " + sp + " to " + "register" + operand1);
		    	}
		        pc++;
		        break;
		    case PUSH:
		    	if(sp <= STACKPOINTER_START) {
		    		operand1 = program.get(pc).operand1;
			    	data = dataRegister.get(operand1);
			    	ram.put(sp, data);
			        System.out.println("PUSH: Push >" + data  + "< from " + "register" + operand1 + "< to ram address " + sp);
			        sp--;
		    	}
		        pc++;
		        break;
		    case CALL:
		    	if(sp >= STACKPOINTER_END) {
		    		operand1 = program.get(pc).operand1;
		    		ram.put(sp, pc + 1);
		    		pc = operand1;
			        System.out.println("CALL: Jump to " + pc);
			        sp--;
		    	}
		        break;
		    case RET:
		    	if(sp <= STACKPOINTER_START) {
		    		sp++;
			    	pc = ram.get(sp);
			        System.out.println("RET: Return to " + pc);
		    	}
		        break;
		    case STP:
		    	run = false;
		    	System.out.println("STP: Stop\n");
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

