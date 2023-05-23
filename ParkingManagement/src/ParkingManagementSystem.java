
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class helps to manage all activity of parking management system
 * 
 * @author TungDuong
 *
 */
public class ParkingManagementSystem {
	private List<Ticket> tickets;
	private static final String fileName = "data.csv";

	public ParkingManagementSystem() {
		this.tickets = new ArrayList<>();
	}

	/**
	 * Show all menu options
	 * 
	 * @return chosen option
	 */
	@SuppressWarnings("resource")
	private int menu() {
		System.out.println("-------------------MENU-------------------");
		System.out.println("1. Vehicle in/out");
		System.out.println("2. Print all parking vehicle");
		System.out.println("3. Sort tickets by type vehicle");
		System.out.println("4. Sort tickets by parking time");
		System.out.println("5. Find a vehicle");
		System.out.println("6. Save data to file " + fileName);
		System.out.println("7. Read data from file " + fileName);
		System.out.println("0. Exit!");
		System.out.print("Please choose an option: ");

		while (true) {
			try {
				Scanner scanner = new Scanner(System.in);
				int option = Integer.parseInt(scanner.nextLine());

				if (option >= 0 && option <= 7) {
					return option;
				}
			} catch (Exception e) {
			}

			System.err.println("Invalid option!");
			System.out.print("Please choose an option: ");
		}
	}

	/**
	 * Find the specific ticket of a vehicle
	 * 
	 * @param licensePlate
	 * @return index of ticket, -1 for not found
	 */
	private int checkExists(String licensePlate) {
		for (int i = 0; i < this.tickets.size(); ++i) {
			if (this.tickets.get(i).getVehicle().getLicensePlate().equalsIgnoreCase(licensePlate)) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * 
	 */
	@SuppressWarnings("resource")
	private void parkVehicle() {
		try {
			Scanner scanner = new Scanner(System.in);
			System.out.print("Please enter the license plate: ");
			String licensePlate = scanner.nextLine();

			int index = this.checkExists(licensePlate);
			if (index == -1) {// insert new
				System.out.println("Adding new ticket....");
				Ticket ticket = new Ticket();
				if (ticket.addVehicle(licensePlate)) {
					this.tickets.add(ticket);
				} else {
					System.err.println("Fail to add new ticket! Try again!");
				}
			} else { // remove existing one
				System.out.println("Removing existing ticket....");
				Ticket ticket = this.tickets.remove(index);
				if (ticket.removeVehicle()) {
					long diff = ticket.getEndTime().getTime() - ticket.getStartTime().getTime();
					long minutes = diff / 60 / 1000;
					ticket.getVehicle().setTotalParkingTime(minutes);
					System.out.println("Payment ticket information....");
					System.out.println(ticket);
				} else {
					System.err.println("Fail to remove existing ticket! Try again!");
					this.tickets.add(ticket);
				}
			}
		} catch (Exception e) {
			System.err.println("An error occurs!");
		}
	}

	/**
	 * 
	 */
	private void printListTicket() {
		System.out.println("-------------------LIST TICKET-------------------");
		for (Ticket ticket : tickets) {
			System.out.println(ticket);
		}
	}

	/**
	 * 
	 */
	private void sortTicketByType() {
		System.out.println("Sorting tickets by type increasement.....");
		for (int i = 0; i < this.tickets.size() - 1; i++) {
			for (int j = i + 1; j < this.tickets.size(); j++) {
				if (this.tickets.get(i).getVehicle().getClass().getName()
						.compareTo(this.tickets.get(j).getVehicle().getClass().getName()) > 0) {
					Ticket ticket = this.tickets.get(i);
					this.tickets.set(i, this.tickets.get(j));
					this.tickets.set(j, ticket);
				}
			}
		}

		System.out.println("List tickets after sorting....");
		this.printListTicket();
	}

	/**
	 * 
	 */
	private void sortTicketByParkingTime() {
		System.out.println("Sorting tickets by parking time increasement.....");
		for (int i = 0; i < this.tickets.size() - 1; i++) {
			for (int j = i + 1; j < this.tickets.size(); j++) {
				if (this.tickets.get(i).getStartTime().after(this.tickets.get(j).getStartTime())) {
					Ticket ticket = this.tickets.get(i);
					this.tickets.set(i, this.tickets.get(j));
					this.tickets.set(j, ticket);
				}
			}
		}

		System.out.println("List tickets after sorting....");
		this.printListTicket();
	}

	/**
	 * 
	 */
	@SuppressWarnings("resource")
	private void searchTicket() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Searching a ticket by license plate.....");
		System.out.print("Enter license plate to search: ");
		String licensePlate = scanner.nextLine();
		int index = -1;
		if ((index = this.checkExists(licensePlate)) == -1) {
			System.out.println("Oooops! We can not find the ticket with license plate " + licensePlate);
		} else {
			System.out.println("The ticket is: ");
			System.out.println(this.tickets.get(index));
		}
	}

	/**
	 * 
	 */
	private void writeCSV() {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(fileName);

			// Write the CSV file header
			fileWriter.append("id,startTime,type,licensePlate,baseTime");

			// Add a new line separator after the header
			fileWriter.append("\n");

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Ticket ticket : this.tickets) {
				fileWriter.append(BigDecimal.valueOf(ticket.getId()).toPlainString());
				fileWriter.append(",");
				fileWriter.append(dateFormat.format(ticket.getStartTime()));
				fileWriter.append(",");
				fileWriter.append(String.valueOf(
						ticket.getVehicle() instanceof Car ? 0 : ticket.getVehicle() instanceof Motorbike ? 1 : 2));
				fileWriter.append(",");
				fileWriter.append(ticket.getVehicle().getLicensePlate());
				fileWriter.append(",");
				fileWriter.append(String.valueOf(ticket.getVehicle().getBaseTime()));
				fileWriter.append("\n");
			}

			// finish
			System.out.println("Write data to CSV file successfully!");
		} catch (Exception e) {
			System.err.println("An error occurs when writing data to file!");
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (Exception e2) {
				System.err.println("Error while flushing/closing fileWriter!!!");
			}
		}
	}

	/**
	 * 
	 */
	private void readCSV() {
		BufferedReader br = null;
		try {
			this.tickets = new ArrayList<>();
			String line;
			br = new BufferedReader(new FileReader(fileName));
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			// read header
			line = br.readLine();

			// read data
			while ((line = br.readLine()) != null) {
				String[] fields = line.split(",");
				Ticket ticket = new Ticket();
				ticket.setId(Long.parseLong(fields[0]));
				ticket.setStartTime(dateFormat.parse(fields[1]));
				int type = Integer.parseInt(fields[2]);
				Vehicle vehicle = type == 0 ? new Car(fields[3])
						: type == 1 ? new Motorbike(fields[3]) : new Wheelchair(fields[3]);
				vehicle.setBaseTime(Integer.parseInt(fields[4]));
				ticket.setVehicle(vehicle);
				
				this.tickets.add(ticket);
			}

			// finish
			System.out.println("Read data from CSV file successfully!");
		} catch (Exception e) {
			System.err.println("An error occurs when reading data from file!");
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception e2) {
				System.err.println("Error while closing BufferedReader!!!");
			}
		}
	}

	/**
	 * 
	 */
	public void run() {
		int option;

		do {
			switch (option = this.menu()) {
			case 1:
				this.parkVehicle();
				break;
			case 2:
				this.printListTicket();
				break;
			case 3:
				this.sortTicketByType();
				break;
			case 4:
				this.sortTicketByParkingTime();
				break;
			case 5:
				this.searchTicket();
				break;
			case 6:
				this.writeCSV();
				break;
			case 7:
				this.readCSV();
				break;
			default:
				System.out.println("Good bye!");
				break;
			}

			System.out.println("------------------------------------------\n");
		} while (option != 0);
	}

	/**
	 * main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new ParkingManagementSystem().run();
	}
}
