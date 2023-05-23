

/**
 * This class defines the base class for vehicle
 * 
 * @author TungDuong
 *
 */
public abstract class Vehicle {
	private String licensePlate;
	private int baseTime;
	private long totalParkingTime;

	/**
	 * This method helps to calculate the total parking fee in the case of allowed
	 * time
	 * 
	 * @return total parking fee
	 */
	public abstract double calculateFare();

	/**
	 * This method helps to calculate the total parking fee in the case of overtime
	 * 
	 * @return total parking fee
	 */
	public abstract double calculateFire();

	/**
	 * @param licensePlate license plate value
	 */
	public Vehicle(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	/**
	 * @return the licensePlate
	 */
	public String getLicensePlate() {
		return licensePlate;
	}

	/**
	 * @param licensePlate the licensePlate to set
	 */
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	/**
	 * @return the baseTime
	 */
	public int getBaseTime() {
		return baseTime;
	}

	/**
	 * @param baseTime the baseTime to set
	 */
	public void setBaseTime(int baseTime) {
		this.baseTime = baseTime;
	}

	/**
	 * @return the totalParkingTime
	 */
	public long getTotalParkingTime() {
		return totalParkingTime;
	}

	/**
	 * @param totalParkingTime the totalParkingTime to set
	 */
	public void setTotalParkingTime(long totalParkingTime) {
		this.totalParkingTime = totalParkingTime;
	}
}
