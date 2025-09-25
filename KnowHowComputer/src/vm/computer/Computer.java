package vm.computer;

public class Computer {

	public Computer() {
	}

	public static void main(String[] args) {
		Software software;
		Hardware hardware;
		int selection = 3;
		
		if(selection == 1) {
		software = new Addition();
		hardware = new Hardware(software);
		hardware.run();
		}
		else if(selection == 2) {
		software = new Multiplication();
		hardware = new Hardware(software);
		hardware.run();
		}
		else if(selection == 3) {
		software = new Multiplication2();
		hardware = new Hardware(software);
		hardware.run();
		}
	}
}
