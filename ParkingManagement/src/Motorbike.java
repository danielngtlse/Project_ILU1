

/**
 * Class Motorbike describes the parking motorbike vehicle with base cost = 300,
 * base time = 60 minutes
 * 
 * @author TungDuong
 *
 */
public class Motorbike extends Vehicle {

	@Override
	public double calculateFare() {
		// default cost
		return 300;
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
	public Motorbike(String licensePlate) {
		super(licensePlate);

		// declare some default value
		this.setBaseTime(60);
	}
	
	@Override
	public String toString() {
		return "Motorbike [License Plate=" + getLicensePlate() + ", Base Time=" + getBaseTime()
				+ ", Total Parking Time=" + getTotalParkingTime() + ", Fare Fee=" + calculateFare()
				+ ", Fire Fee=" + calculateFire() + "]";
	}
}
