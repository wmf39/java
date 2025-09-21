package know.how.computer;

public class Computer {

	public Computer() {
	}

	public static void main(String[] args) {
		Software software;
		Hardware hardware;
		software = new Addition();
		hardware = new Hardware(software);
		hardware.run();
		software = new Multiplication();
		hardware = new Hardware(software);
		hardware.run();
	}
}
