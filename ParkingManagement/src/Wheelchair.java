

/**
 * Class Wheelchair describes the parking wheelchair vehicle for disable user
 * with base cost = 200, base time = 60 minutes
 * 
 * @author TungDuong
 *
 */
public class Wheelchair extends Vehicle {

	@Override
	public double calculateFare() {
		// default cost
		return 200;
	}

	@Override
	public double calculateFire() {
		// if not overtime
		if (this.getTotalParkingTime() <= this.getBaseTime())
			return 0;

		// overtime fire
		return this.calculateFare() * 0.2;
	}

	/**
	 * @param licensePlate
	 */
	public Wheelchair(String licensePlate) {
		super(licensePlate);

		// declare some default value
		this.setBaseTime(60);
	}
	
	@Override
	public String toString() {
		return "Wheelchair [License Plate=" + getLicensePlate() + ", Base Time=" + getBaseTime()
				+ ", Total Parking Time=" + getTotalParkingTime() + ", Fare Fee=" + calculateFare()
				+ ", Fire Fee=" + calculateFire() + "]";
	}
}
