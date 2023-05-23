

/**
 * Class Car describes the parking car vehicle with base cost = 500, base time =
 * 60 minutes
 * 
 * @author TungDuong
 *
 */
public class Car extends Vehicle {

	@Override
	public double calculateFare() {
		// default cost
		return 500;
	}

	@Override
	public double calculateFire() {
		// if not overtime
		if (this.getTotalParkingTime() <= this.getBaseTime())
			return 0;

		// overtime fire
		return this.calculateFare() * 0.3;
	}

	/**
	 * @param licensePlate
	 */
	public Car(String licensePlate) {
		super(licensePlate);

		// declare some default value
		this.setBaseTime(60);
	}

	@Override
	public String toString() {
		return "Car [License Plate=" + getLicensePlate() + ", Base Time=" + getBaseTime()
				+ ", Total Parking Time=" + getTotalParkingTime() + ", Fare Fee=" + calculateFare()
				+ ", Fire Fee=" + calculateFire() + "]";
	}
}
