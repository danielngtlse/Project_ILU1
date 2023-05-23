#load "unix.cma";;
open Unix;;
open Scanf;;

(* declare method to cast date to string *)
let days = [| "Sun"; "Mon"; "Tue"; "Wed"; "Thu"; "Fri"; "Sat" |]
let months = [| "Jan"; "Feb"; "Mar"; "Apr"; "May"; "Jun";
                "Jul"; "Aug"; "Sep"; "Oct"; "Nov"; "Dec" |]
let string_of_date time =
  let tm = time in
    Printf.sprintf "%s %s %2d %02d:%02d:%02d %04d"
      days.(tm.tm_wday)
      months.(tm.tm_mon)
      tm.tm_mday
      tm.tm_hour
      tm.tm_min
      tm.tm_sec
      (tm.tm_year + 1900)

(* declare class vehicle *)
class virtual vehicle licensePlate baseTime = 
  object (self)
    val mutable licensePlate : string = licensePlate
    val mutable baseTime : int = baseTime
    val mutable totalParkingTime = (0: int)

    method get_licensePlate = licensePlate
    method set_licensePlate _licensePlate = licensePlate <- _licensePlate
    method get_baseTime = baseTime
    method set_baseTime _baseTime = baseTime <- _baseTime
    method get_totalParkingTime = totalParkingTime
    method set_totalParkingTime _totalParkingTime = totalParkingTime <- _totalParkingTime

    method virtual calculateFare : float
    method virtual calculateFire : float 
    method virtual toString : string
    method virtual getName : string
  end;;

(* declare class car inherit from vehicle *)
class car licensePlate baseTime = 
  object (self)
    inherit vehicle licensePlate baseTime

    method getName = "car"
    method calculateFare = 500.0
    method calculateFire = if self#get_totalParkingTime <= self#get_baseTime then 0.0 else self#calculateFare *. 0.3
    method toString = "Car [License Plate=" ^ self#get_licensePlate ^ ", Base Time=" ^ string_of_int self#get_baseTime ^ ", Total Parking Time=" ^ string_of_int self#get_totalParkingTime ^ ", Fare Fee=" ^ string_of_float self#calculateFare ^ ", Fire Fee=" ^ string_of_float self#calculateFire ^ "]"
  end;;

(* declare class wheelchair inherit from vehicle *)
class wheelchair licensePlate baseTime = 
  object (self)
    inherit vehicle licensePlate baseTime
    
    method getName = "wheelchair"
    method calculateFare = 200.0
    method calculateFire = if self#get_totalParkingTime <= self#get_baseTime then 0.0 else self#calculateFare *. 0.2
    method toString = "Wheelchair [License Plate=" ^ self#get_licensePlate ^ ", Base Time=" ^ string_of_int self#get_baseTime ^ ", Total Parking Time=" ^ string_of_int self#get_totalParkingTime ^ ", Fare Fee=" ^ string_of_float self#calculateFare ^ ", Fire Fee=" ^ string_of_float self#calculateFire ^ "]"
  end;;

(* declare class motorbike inherit from vehicle *)
class motorbike licensePlate baseTime = 
  object (self)
    inherit vehicle licensePlate baseTime
    
    method getName = "motorbike"
    method calculateFare = 300.0
    method calculateFire = if self#get_totalParkingTime <= self#get_baseTime then 0.0 else self#calculateFare *. 0.2
    method toString = "Motorbike [License Plate=" ^ self#get_licensePlate ^ ", Base Time=" ^ string_of_int self#get_baseTime ^ ", Total Parking Time=" ^ string_of_int self#get_totalParkingTime ^ ", Fare Fee=" ^ string_of_float self#calculateFare ^ ", Fire Fee=" ^ string_of_float self#calculateFire ^ "]"
  end;;

(* declare class ticket *)
class ticket = 
  object(self)
    val mutable id = (0 : int)
    val mutable startTime = (Unix.localtime (Unix.time ()))
    val mutable endTime = (Unix.localtime (Unix.time ()))
    val mutable vehicle = (new car "" 0: vehicle)

    method get_vehicle = vehicle
    method set_vehicle _vehicle = vehicle <- _vehicle
    method get_id = id
    method get_startTime = startTime
    method get_endTime = endTime

    (* this method helps to add new vehicle to ticket *)
    method addVehicle licensePlate : bool = 
      let isSuccess = ref false in
        (try
          (* get current time to make sure unique id *)
          id <- int_of_float(Unix.time());

          (* ask to what kind of vehicle to input *)
          let kind = ref 0 in 
            let quit_loop = ref false in
              while not !quit_loop do
                print_string "What kind of vehicle: {0-Car, 1-Motobike, 2-Wheelchair, -1-Exit}? ";
                kind := read_int();

                match !kind with
                | 0 -> vehicle <- new car licensePlate 60; quit_loop := true;
                | 1 -> vehicle <- new motorbike licensePlate 60; quit_loop := true;
                | 2 -> vehicle <- new wheelchair licensePlate 60; quit_loop := true;
                | -1 -> quit_loop := true;
                | _ -> print_endline "Invalid vehicle!";
              done;

          (* ask start time *)
          if !kind <> (-1) then 
            let quit_loop = ref false in
              while not !quit_loop do
                print_string "Input start time (format yyyy-MM-dd hh:mm): ";

                let input = read_line() in 
                  try
                    let (year, month, day, hour, minute) = sscanf input "%d-%d-%d %d:%d" (fun x y z t w -> (x, y, z, t, w)) in
                      startTime <- snd (mktime {
                          tm_sec = 0;
                          tm_min = minute;
                          tm_hour = hour;

                          tm_mday = day;
                          tm_mon = month - 1;
                          tm_year = year - 1900;

                          tm_wday = 0;
                          tm_yday = 0;
                          tm_isdst = false;
                        });
                              
                      quit_loop := true;
                      isSuccess := true;
                  with
                  | _ -> print_endline "Invalid start time!";
              done;
        with
        | _ -> print_endline "Something went wrong when adding vehicle");

      match isSuccess with
      | _ -> !isSuccess;

    method toString = "Ticket [ID=" ^ string_of_int id ^ ", Start Time=" ^ string_of_date startTime ^ ", End Time=" ^ string_of_date endTime ^ ", Vehicle=" ^ vehicle#toString
                      ^ ", Total Fee=" ^ string_of_float (vehicle#calculateFare +. vehicle#calculateFire) ^ "]"
    
    (* method helps to remove vehicle *)
    method removeVehicle : bool = 
      let isSuccess = ref false in
        (try
          let quit_loop = ref false in
            while not !quit_loop do
              print_string "Input end time (format yyyy-MM-dd hh:mm): ";

              let input = read_line () in 
                try
                  let (year, month, day, hour, minute) = sscanf input "%d-%d-%d %d:%d" (fun x y z t w -> (x, y, z, t, w)) in
                  endTime <- snd (mktime {
                      tm_sec = 0;
                      tm_min = minute;
                      tm_hour = hour;

                      tm_mday = day;
                      tm_mon = month - 1;
                      tm_year = year - 1900;

                      tm_wday = 0;
                      tm_yday = 0;
                      tm_isdst = false;
                    });
                  
                  if fst(mktime(endTime)) > fst(mktime(startTime)) then 
                    (quit_loop := true;
                    isSuccess := true;)
                  else
                    print_endline "End time must be after start time!"
                with
                | _ -> print_endline "Invalid end time!";  
            done;
        with
        | _ -> print_endline "Something went wrong when removing vehicle";);

      match isSuccess with
      | _ -> !isSuccess;
  end;;

(* declare class parkingmanagement *)
class parkingmanagement =
  object(self)
    val mutable tickets = ([] : ticket list)

    (* print menu *)
    method private menu : int =
      print_endline "-------------------MENU-------------------";
      print_endline "1. Vehicle in/out";
      print_endline "2. Print all parking vehicle";
      print_endline "3. Sort tickets by type vehicle";
      print_endline "4. Sort tickets by parking time";
      print_endline "5. Find a vehicle";
      print_endline "0. Exit!";
      print_string "Please choose an option: ";

      let option = ref 0 in
        let quit_loop = ref false in
          while not !quit_loop do
            try
              option := read_int(); 
              
              match !option with
              | x when x >= 0 && x <= 5 -> quit_loop := true;
              | _ -> ();
            with
            | _ -> ();

            if not !quit_loop then
              print_endline "Invalid option!" ;
              print_string "Please choose an option: ";
          done;
      
      match option with
      | _ -> !option
    
    (* Find the specific ticket of a vehicle *)
    method private checkExists licensePlate : int = 
      let index = ref 0 in
        let quit_loop = ref false in
          while ((not !quit_loop) && (!index < List.length tickets)) do
            let element = List.nth tickets !index in 
              if element#get_vehicle#get_licensePlate = licensePlate then
                quit_loop := true
              else 
                index := !index +1
          done;
      
      if ((List.length tickets) = !index) then -1 else !index;

    (* find a ticket by license plate*)
    method private searchTicket = 
      print_endline "Searching a ticket by license plate.....";
      print_string "Enter license plate to search: ";
      let licensePlate = read_line () in
        let index = self#checkExists licensePlate in match index with
        | -1 -> Printf.printf "Oooops! We can not find the ticket with license plate %s\n" licensePlate
        | _ -> let elem = List.nth tickets index in 
          print_endline "The ticket is: ";
          print_endline elem#toString

    (* print all tickets in list *)
    method private printListTicket =
      print_endline "-------------------LIST TICKET-------------------";
      let f elem =
        print_endline elem#toString
      in
        List.iter f tickets
    
    (* sort tickets by type of vehicle *)
    method private sortTicketByType = 
      print_endline "Sorting tickets by type increasement.....";
      print_endline "List tickets after sorting....";
      let _tickets = List.sort (fun a b -> compare a#get_vehicle#getName b#get_vehicle#getName) tickets in
        let f elem =
          print_endline elem#toString
        in
          List.iter f _tickets

    (* sort tickets by parking time *)
    method private sortTicketByParkingTime = 
      print_endline "Sorting tickets by parking time increasement.....";
      print_endline "List tickets after sorting....";
      let _tickets = List.sort (fun a b -> compare b#get_startTime a#get_startTime) tickets in
        let f elem =
          print_endline elem#toString
        in
          List.iter f _tickets
    
    (* park vehicle *)
    method private parkVehicle =
      try
        print_string "Please enter the license plate: ";

        let licensePlate = read_line() in
          let index = self#checkExists licensePlate in match index with
          | -1 -> 
            (print_endline "Adding new ticket....";
            let ticket = new ticket in 
              let response = ticket#addVehicle licensePlate in match response with
              | true -> tickets <- tickets@[ticket]
              | false -> print_endline "Fail to add new ticket! Try again!")
          | _ -> 
            (print_endline "Removing existing ticket....";
            let ticket = List.nth tickets index in
              let response = ticket#removeVehicle in match response with
              | true -> 
                let diff = int_of_float (fst(mktime(ticket#get_endTime)) -. fst(mktime(ticket#get_startTime))) in
                  let minutes = diff / 60 in
                    ticket#get_vehicle#set_totalParkingTime minutes;
                    print_endline "Payment ticket information....";
                    print_endline ticket#toString;
                    tickets <- List.filter (fun x -> x <> ticket) tickets
              | false -> print_endline "Fail to remove existing ticket! Try again!")
      with
      | _ -> print_endline "An error occurs!"

    (* run method *)
    method run = 
      let quit_loop = ref false in
        while not !quit_loop do
          let option = self#menu in match option with 
          | 1 -> self#parkVehicle
          | 2 -> self#printListTicket
          | 3 -> self#sortTicketByType
          | 4 -> self#sortTicketByParkingTime
          | 5 -> self#searchTicket
          | _ -> quit_loop := true; print_endline "Good bye!"
        done;
        
        print_string "------------------------------------------\n"
  end;;

let p = new parkingmanagement;;
p#run;;
