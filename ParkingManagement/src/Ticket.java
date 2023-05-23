

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Class Ticket supports to create a parking ticket for a vehicle
 * 
 * @author TungDuong
 *
 */
public class Ticket {
	private long id;
	private Date startTime;
	private Date endTime;
	private Vehicle vehicle;

	/**
	 * @param id the id to set
	 */
	protected void setId(long id) {
		this.id = id;
	}

	/**
	 * @param startTime the startTime to set
	 */
	protected void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @param vehicle the vehicle to set
	 */
	protected void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	/**
	 * This method helps to add new ticket
	 * 
	 * @param licensePlate
	 * @author TungDuong
	 * @return false in case failure
	 */
	@SuppressWarnings("resource")
	public boolean addVehicle(String licensePlate) {
		try {
			Scanner scanner = new Scanner(System.in);

			// get current time to make sure unique id
			this.id = System.currentTimeMillis();

			// ask to what kind of vehicle to input
			int kind = -1;
			while (true) {
				System.out.print("What kind of vehicle: {0-Car, 1-Motobike, 2-Wheelchair, -1-Exit}? ");
				kind = Integer.parseInt(scanner.nextLine());

				if (kind < -1 || kind > 2) {
					System.err.println("Invalid vehicle!");
				} else {
					break;
				}
			}

			switch (kind) {
			case 0:
				this.vehicle = new Car(licensePlate);
				break;
			case 1:
				this.vehicle = new Motorbike(licensePlate);
				break;
			case 2:
				this.vehicle = new Wheelchair(licensePlate);
				break;
			default:
				return false;
			}

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
			while (true) {
				try {
					System.out.print("Input start time (format yyyy-MM-dd hh:mm): ");
					this.startTime = simpleDateFormat.parse(scanner.nextLine());
					break;
				} catch (Exception e) {
					System.err.println("Invalid start time! " + e.getMessage());
				}
			}

			return true;
		} catch (Exception e) {
			System.err.println("An error occurs! " + e.getMessage());
			return false;
		}
	}

	/**
	 * This method helps to input end time to remove vehicle
	 * 
	 * @return false in case failure
	 * @author TungDuong
	 */
	@SuppressWarnings("resource")
	public boolean removeVehicle() {
		try {
			Scanner scanner = new Scanner(System.in);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
			while (true) {
				try {
					System.out.print("Input end time (format yyyy-MM-dd hh:mm): ");
					this.endTime = simpleDateFormat.parse(scanner.nextLine());

					if (this.endTime.before(this.startTime)) {
						System.err.println("End time must be after start time!");
					} else {
						break;
					}
				} catch (Exception e) {
					System.err.println("Invalid end time! " + e.getMessage());
				}
			}

			return true;
		} catch (Exception e) {
			System.err.println("An error occurs! " + e.getMessage());
			return false;
		}
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	@Override
	public String toString() {
		return "Ticket [ID=" + id + ", Start Time=" + startTime + ", End Time=" + endTime + ", Vehicle=" + vehicle
				+ ", Total Fee=" + (vehicle.calculateFare() + vehicle.calculateFire()) + "]";
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}
}
