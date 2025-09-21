package know.how.computer;

public class Computer {

	public Computer() {
	}

	public static void main(String[] args) {
		Software software = new Software();
		Hardware hardware = new Hardware(software);
		hardware.run();
	}
}
