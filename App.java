import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

class App {

    static final int MAX_MEMBERS = 200;
    static final int MAX_BOOKS = 300;
    static final int MAX_TRANSACTIONS = 500;
    static final int MAX_RESERVATIONS = 100;
    static final int BORROW_LIMIT = 3;
    static final int BORROW_DAYS = 14;
    static final int RESERVE_HOLD_DAYS = 7;
    static final double FINE_RATE = 1.0;

    static int[] memberIds = new int[MAX_MEMBERS];
    static String[] memberNames = new String[MAX_MEMBERS];
    static String[] memberAddresses = new String[MAX_MEMBERS];
    static String[] memberPhones = new String[MAX_MEMBERS];
    static String[] memberTypes = new String[MAX_MEMBERS];
    static int memberCount = 0;

    static String[] bookIds = new String[MAX_BOOKS];
    static String[] bookTitles = new String[MAX_BOOKS];
    static String[] bookAuthors = new String[MAX_BOOKS];
    static String[] bookGenres = new String[MAX_BOOKS];
    static int[] bookYears = new int[MAX_BOOKS];
    static int[] totalCopies = new int[MAX_BOOKS];
    static int[] availableCopies = new int[MAX_BOOKS];
    static int bookCount = 0;

    static int[] transactionIds = new int[MAX_TRANSACTIONS];
    static int[] transactionMemberIds = new int[MAX_TRANSACTIONS];
    static String[] transactionBookIds = new String[MAX_TRANSACTIONS];
    static String[] borrowDates = new String[MAX_TRANSACTIONS];
    static String[] dueDates = new String[MAX_TRANSACTIONS];
    static String[] returnDates = new String[MAX_TRANSACTIONS];
    static double[] fines = new double[MAX_TRANSACTIONS];
    static int transactionCount = 0;

    static int[] reservationIds = new int[MAX_RESERVATIONS];
    static int[] reservationMemberIds = new int[MAX_RESERVATIONS];
    static String[] reservationBookIds = new String[MAX_RESERVATIONS];
    static String[] reservationDates = new String[MAX_RESERVATIONS];
    static String[] reservationStatus = new String[MAX_RESERVATIONS];
    static int reservationCount = 0;

    static int nextTransactionId = 1001;
    static int nextReservationId = 5001;
    static double totalFinesCollected = 0.0;

    static Scanner scanner = new Scanner(System.in);
    static DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        initializeSampleData();
        System.out.println("Welcome to the Library Management System!");
        while (true) {
            displayMainMenu();
            int choice = readInt();
            switch (choice) {
                case 1: manageMembers(); 
			break;
                case 2: manageBooks();
			 break;
                case 3: borrowingOperations(); 
			break;
                case 4: reservationSystem(); 
			break;
                case 5: generateReports(); 
			break;
                case 6: System.out.println("Goodbye"); 
			return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    static void displayMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Manage Members");
        System.out.println("2. Manage Books");
        System.out.println("3. Borrowing Operations");
        System.out.println("4. Reservations");
        System.out.println("5. Generate Reports");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    static void manageMembers() {
        while (true) {
            System.out.println("\n=== MEMBER MANAGEMENT ===");
            System.out.println("1. Add Member");
            System.out.println("2. View All Members");
            System.out.println("3. Search Member");
            System.out.println("4. Update Member");
            System.out.println("5. Back");
            System.out.print("Enter your choice: ");
            int choice = readInt();
            switch (choice) {
                case 1: addMember();
		   break;
                case 2: viewAllMembers(); 
			break;
                case 3: searchMember(); 
		  	break;
                case 4: updateMember(); 
			break;
                case 5:
			 return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    static void addMember() {
        if (memberCount >= MAX_MEMBERS) { 
	System.out.println("Member limit reached."); 
		return;
 }
        System.out.print("Enter Member ID: ");
        int id = readInt();
        if (findMemberIndexById(id) != -1) { 
	System.out.println("Member ID already exists."); 
		return; 

}

        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Address: ");
        String addr = scanner.nextLine();
        System.out.print("Enter Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Enter Membership Type (Regular/Premium/Student): ");
        String type = scanner.nextLine();

        memberIds[memberCount] = id;
        memberNames[memberCount] = name;
        memberAddresses[memberCount] = addr;
        memberPhones[memberCount] = phone;
        memberTypes[memberCount] = type;
        memberCount++;
        System.out.println("Member added successfully.");
    }

    static void viewAllMembers() {
        if (memberCount == 0) { 
	System.out.println("No members."); 
		return; 
}
        System.out.printf("%-6s %-22s %-20s %-14s %-10s%n","ID","Name","Address","Phone","Type");
        System.out.println("-".repeat(80));
        for (int i = 0; i < memberCount; i++) {
            System.out.printf("%-6d %-22s %-20s %-14s %-10s%n",
                    memberIds[i], memberNames[i], memberAddresses[i], memberPhones[i], memberTypes[i]);
        }
    }

    static void searchMember() {
        System.out.print("Search by 1) ID or 2) Name: ");
        int opt = readInt();
        if (opt == 1) {
            System.out.print("Enter Member ID: ");
            int id = readInt();
            int idx = findMemberIndexById(id);
            if (idx == -1) System.out.println("Member not found.");
            else printMember(idx);
        } else if (opt == 2) {
            System.out.print("Enter Name: ");
            String name = scanner.nextLine().toLowerCase();
            boolean found = false;
            for (int i = 0; i < memberCount; i++) {
                if (memberNames[i].toLowerCase().contains(name)) { printMember(i); found = true; }
            }
            if (!found) System.out.println("Member not found.");
        } else System.out.println("Invalid option.");
    }

    static void updateMember() {
        System.out.print("Enter Member ID: ");
        int id = readInt();
        int idx = findMemberIndexById(id);
        if (idx == -1) { 
	System.out.println("Member not found."); 
		return;

 }
        System.out.println("Leave blank to keep current.");
        System.out.print("New Name ("+memberNames[idx]+"): ");
        String n = scanner.nextLine();
        if (!n.trim().isEmpty()) memberNames[idx] = n;
        System.out.print("New Address ("+memberAddresses[idx]+"): ");
        String a = scanner.nextLine();
        if (!a.trim().isEmpty()) memberAddresses[idx] = a;
        System.out.print("New Phone ("+memberPhones[idx]+"): ");
        String p = scanner.nextLine();
        if (!p.trim().isEmpty()) memberPhones[idx] = p;
        System.out.print("New Type ("+memberTypes[idx]+"): ");
        String t = scanner.nextLine();
        if (!t.trim().isEmpty()) memberTypes[idx] = t;
        System.out.println("Member updated.");
    }

    static void manageBooks() {
        while (true) {
            System.out.println("\n=== BOOK MANAGEMENT ===");
            System.out.println("1. Add Book");
            System.out.println("2. View All Books");
            System.out.println("3. Search Book");
            System.out.println("4. Update Book");
            System.out.println("5. Manage Availability");
            System.out.println("6. Back");
            System.out.print("Enter your choice: ");
            int choice = readInt();
            switch (choice) {
                case 1: addBook(); 
			break;
                case 2: viewAllBooks(); 
			break;
                case 3: searchBook(); 
			break;
                case 4: updateBook(); 
			break;
                case 5: manageAvailability();
			 break;
                case 6: 
			return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    static void addBook() {
        if (bookCount >= MAX_BOOKS) {
	 System.out.println("Book limit reached."); 
		return; 
}
        System.out.print("Enter Book ID: ");
        String id = scanner.nextLine();
        if (findBookIndexById(id) != -1) { 
	System.out.println("Book ID exists.");
	 return; 
}
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();

        System.out.print("Enter Author: ");
        String author = scanner.nextLine();

        System.out.print("Enter Genre: ");
        String genre = scanner.nextLine();

        System.out.print("Enter Publication Year: ");
        int year = readInt();

        System.out.print("Enter Total Copies: ");
        int copies = readInt();

        if (copies < 0) { 
System.out.println("Invalid copies."); 
	return; 
}
        bookIds[bookCount] = id;
        bookTitles[bookCount] = title;
        bookAuthors[bookCount] = author;
        bookGenres[bookCount] = genre;
        bookYears[bookCount] = year;
        totalCopies[bookCount] = copies;
        availableCopies[bookCount] = copies;
        bookCount++;
        System.out.println("Book added.");
    }

    static void viewAllBooks() {
        if (bookCount == 0) { 
	System.out.println("No books."); 
		return; 
}
        System.out.printf("%-8s %-26s %-18s %-12s %-6s %-6s %-6s%n","ID","Title","Author","Genre","Year","Total","Avail");
        System.out.println("-".repeat(100));
        for (int i = 0; i < bookCount; i++) {
            System.out.printf("%-8s %-26s %-18s %-12s %-6d %-6d %-6d%n",
                    bookIds[i], shorten(bookTitles[i],26), shorten(bookAuthors[i],18), shorten(bookGenres[i],12),
                    bookYears[i], totalCopies[i], availableCopies[i]);
        }
    }

    static void searchBook() {
        System.out.print("Search by 1) ID 2) Title 3) Author: ");
        int opt = readInt();
        if (opt == 1) {
            System.out.print("Enter Book ID: ");
            String id = scanner.nextLine();
            int idx = findBookIndexById(id);
            if (idx == -1) System.out.println("Book not found.");
            else printBook(idx);
        } else if (opt == 2) {
            System.out.print("Enter Title: ");
            String t = scanner.nextLine().toLowerCase();
            boolean found = false;
            for (int i = 0; i < bookCount; i++) if (bookTitles[i].toLowerCase().contains(t)) { printBook(i); found=true; }
            if (!found) System.out.println("Book not found.");
        } else if (opt == 3) {
            System.out.print("Enter Author: ");
            String a = scanner.nextLine().toLowerCase();
            boolean found = false;
            for (int i = 0; i < bookCount; i++) if (bookAuthors[i].toLowerCase().contains(a)) { printBook(i); found=true; }
            if (!found) System.out.println("Book not found.");
        } else System.out.println("Invalid option.");
    }

    static void updateBook() {
        System.out.print("Enter Book ID: ");
        String id = scanner.nextLine();
        int idx = findBookIndexById(id);
        if (idx == -1) { 
System.out.println("Book not found.");
	 return; 
}
        System.out.println("Leave blank to keep current, -1 to keep numeric.");
        System.out.print("New Title ("+bookTitles[idx]+"): ");

        String t = scanner.nextLine();
        if (!t.trim().isEmpty()) bookTitles[idx] = t;

        System.out.print("New Author ("+bookAuthors[idx]+"): ");

        String a = scanner.nextLine();
        if (!a.trim().isEmpty()) bookAuthors[idx] = a;
        System.out.print("New Genre ("+bookGenres[idx]+"): ");
        String g = scanner.nextLine();

        if (!g.trim().isEmpty()) bookGenres[idx] = g;
        System.out.print("New Year ("+bookYears[idx]+"): ");
        int y = readInt();
        if (y != -1) bookYears[idx] = y;
        System.out.print("New Total Copies ("+totalCopies[idx]+"): ");
        int tc = readInt();
        if (tc != -1) {
            int used = totalCopies[idx] - availableCopies[idx];
            if (tc < used) { System.out.println("Cannot set below borrowed count."); 
}
            else {
                int delta = tc - totalCopies[idx];
                totalCopies[idx] = tc;
                availableCopies[idx] += delta;
            }
        }
        System.out.println("Book updated.");
    }

    static void manageAvailability() {
        System.out.print("Enter Book ID: ");
        String id = scanner.nextLine();
        int idx = findBookIndexById(id);
        if (idx == -1) { 
	System.out.println("Book not found."); 
	return; 
}
        System.out.print("Enter copies to add (negative to remove): ");
        int change = readInt();
        if (availableCopies[idx] + change < 0 || totalCopies[idx] + change < 0) { System.out.println("Invalid change."); return; }
        availableCopies[idx] += change;
        totalCopies[idx] += change;
        System.out.println("Availability updated. Total: "+totalCopies[idx]+", Available: "+availableCopies[idx]);
    }

    static void borrowingOperations() {
        while (true) {
            System.out.println("\n=== BORROWING OPERATIONS ===");
            System.out.println("1. Borrow Book");
            System.out.println("2. Return Book");
            System.out.println("3. Back");
            System.out.print("Enter your choice: ");
            int choice = readInt();
            switch (choice) {
                case 1: borrowBook(); break;
                case 2: returnBook(); break;
                case 3: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    static void borrowBook() {
        if (memberCount==0 || bookCount==0) { System.out.println("No members or books."); return; }
        System.out.print("Enter Member ID: ");
        int mid = readInt();
        int mIdx = findMemberIndexById(mid);
        if (mIdx == -1) { 
	System.out.println("Member not found."); 
	return; 
}
        int active = countActiveBorrowingsForMember(mid);
        if (active >= BORROW_LIMIT) { 
	System.out.println("Borrow limit reached."); 
		return; 
}
        System.out.print("Enter Book ID: ");
        String bid = scanner.nextLine();
        int bIdx = findBookIndexById(bid);
        if (bIdx == -1) { 
System.out.println("Book not found."); 
	return; 
}
        if (availableCopies[bIdx] <= 0) {
            System.out.println("Book not available. You may create a reservation in Reservations menu.");
            return;
        }
        if (transactionCount >= MAX_TRANSACTIONS) { 
	System.out.println("Transaction limit reached."); 
	return; 
}
        int tid = nextTransactionId++;
        LocalDate today = LocalDate.now();
        LocalDate due = today.plusDays(BORROW_DAYS);
        transactionIds[transactionCount] = tid;
        transactionMemberIds[transactionCount] = mid;
        transactionBookIds[transactionCount] = bid;
        borrowDates[transactionCount] = today.format(DF);
        dueDates[transactionCount] = due.format(DF);
        returnDates[transactionCount] = null;
        fines[transactionCount] = 0.0;
        transactionCount++;
        availableCopies[bIdx]--;
        System.out.println("Borrowed. Transaction ID: "+tid+" Due: "+due.format(DF));
    }

    static void returnBook() {
        System.out.print("Enter Transaction ID: ");
        int tid = readInt();
        int tIdx = findTransactionIndexById(tid);
        if (tIdx == -1) { 
	System.out.println("Transaction not found."); 
		return; 
}
        if (returnDates[tIdx] != null) { 
System.out.println("Already returned."); 
	return; 
}
        int bIdx = findBookIndexById(transactionBookIds[tIdx]);
        LocalDate today = LocalDate.now();
        LocalDate due = LocalDate.parse(dueDates[tIdx], DF);
        long lateDays = Math.max(0, java.time.temporal.ChronoUnit.DAYS.between(due, today));
        double fine = lateDays * FINE_RATE;
        fines[tIdx] = fine;
        totalFinesCollected += fine;
        returnDates[tIdx] = today.format(DF);
        availableCopies[bIdx]++;
        int rIdx = earliestActiveReservationForBook(transactionBookIds[tIdx]);
        if (rIdx != -1 && availableCopies[bIdx] > 0) {
            reservationStatus[rIdx] = "Fulfilled";
            availableCopies[bIdx]--;
            System.out.println("Reservation fulfilled for Member ID: "+reservationMemberIds[rIdx]);
        }
        if (fine > 0) System.out.println("Late by "+lateDays+" day(s). Fine: $"+String.format("%.2f",fine));
        else System.out.println("Returned on time. No fine.");
    }

    static void reservationSystem() {
        while (true) {
            System.out.println("\n=== RESERVATIONS ===");
            System.out.println("1. Reserve Book");
            System.out.println("2. View Reservations");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. Back");
            System.out.print("Enter your choice: ");
            int choice = readInt();
            switch (choice) {
                case 1: reserveBook(); break;
                case 2: viewReservations(); break;
                case 3: cancelReservation(); break;
                case 4: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    static void reserveBook() {
        if (reservationCount >= MAX_RESERVATIONS) { 
	System.out.println("Reservation limit reached."); 
	return; 
}
        System.out.print("Enter Member ID: ");
        int mid = readInt();
        int mIdx = findMemberIndexById(mid);
        if (mIdx == -1) { 
	System.out.println("Member not found."); 
	return; 
}
        System.out.print("Enter Book ID: ");
        String bid = scanner.nextLine();
        int bIdx = findBookIndexById(bid);
        if (bIdx == -1) { 
System.out.println("Book not found."); 
	return; 
}
        if (availableCopies[bIdx] > 0) { 
	System.out.println("Book is available; no need to reserve."); 
	return; 
}
        int rid = nextReservationId++;
        reservationIds[reservationCount] = rid;
        reservationMemberIds[reservationCount] = mid;
        reservationBookIds[reservationCount] = bid;
        reservationDates[reservationCount] = LocalDate.now().format(DF);
        reservationStatus[reservationCount] = "Active";
        reservationCount++;
        System.out.println("Reservation created. ID: "+rid);
    }

    static void viewReservations() {
        if (reservationCount == 0) { 
	System.out.println("No reservations."); 
	return; 
}
        System.out.print("Filter by 1) All 2) Active 3) Fulfilled 4) Cancelled: ");
        int f = readInt();
        System.out.printf("%-6s %-10s %-8s %-12s %-10s%n","ResID","BookID","MemID","Date","Status");
        System.out.println("-".repeat(60));
        for (int i = 0; i < reservationCount; i++) {
            if (f==1 || (f==2 && reservationStatus[i].equals("Active")) || (f==3 && reservationStatus[i].equals("Fulfilled")) || (f==4 && reservationStatus[i].equals("Cancelled"))) {
                System.out.printf("%-6d %-10s %-8d %-12s %-10s%n",
                        reservationIds[i], reservationBookIds[i], reservationMemberIds[i], reservationDates[i], reservationStatus[i]);
            }
        }
    }

    static void cancelReservation() {
        System.out.print("Enter Reservation ID: ");
        int rid = readInt();
        int idx = findReservationIndexById(rid);
        if (idx == -1) { System.out.println("Reservation not found."); return; }
        if (reservationStatus[idx].equals("Cancelled")) { System.out.println("Already cancelled."); return; }
        if (reservationStatus[idx].equals("Fulfilled")) {
            int bIdx = findBookIndexById(reservationBookIds[idx]);
            availableCopies[bIdx]++;
        }
        reservationStatus[idx] = "Cancelled";
        System.out.println("Reservation cancelled.");
    }

    static void generateReports() {
        while (true) {
            System.out.println("\n=== REPORTS ===");
            System.out.println("1. Library Statistics");
            System.out.println("2. Member Activity Report");
            System.out.println("3. Popular Books Report");
            System.out.println("4. Overdue Books Report");
            System.out.println("5. Back");
            System.out.print("Enter your choice: ");
            int choice = readInt();
            switch (choice) {
                case 1: libraryStatistics(); break;
                case 2: memberActivityReport(); break;
                case 3: popularBooksReport(); break;
                case 4: overdueBooksReport(); break;
                case 5: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    static void libraryStatistics() {
        int totalBooks = 0;
        int available = 0;
        for (int i = 0; i < bookCount; i++) { 
totalBooks += totalCopies[i]; available += availableCopies[i]; 
	}
        int borrowedNow = 0;
        for (int i = 0; i < transactionCount; i++) if (returnDates[i] == null) borrowedNow++;
        System.out.println("Total Members: " + memberCount);
        System.out.println("Total Books (copies): " + totalBooks);
        System.out.println("Available Copies: " + available);
        System.out.println("Currently Borrowed Count: " + borrowedNow);
        System.out.println("Total Fines Collected: $" + String.format("%.2f", totalFinesCollected));
    }

    static void memberActivityReport() {
        if (memberCount == 0) { System.out.println("No members."); return; }
        for (int m = 0; m < memberCount; m++) {
            int mid = memberIds[m];
            int current = 0;
            double memberFines = 0;
            for (int i = 0; i < transactionCount; i++) if (transactionMemberIds[i] == mid) memberFines += fines[i];
            for (int i = 0; i < transactionCount; i++) if (transactionMemberIds[i] == mid && returnDates[i]==null) current++;
            if (current>0 || memberFines>0) {
                System.out.println("\nMember ID: "+mid+" | Name: "+memberNames[m]);
                System.out.println("Current Borrowings: "+current);
                System.out.println("Total Fines: $"+String.format("%.2f",memberFines));
                System.out.printf("%-6s %-10s %-12s %-12s %-12s %-6s%n","TID","BookID","Borrow","Due","Return","Fine");
                for (int i = 0; i < transactionCount; i++) {
                    if (transactionMemberIds[i]==mid) {
                        System.out.printf("%-6d %-10s %-12s %-12s %-12s %-6.2f%n",
                                transactionIds[i], transactionBookIds[i], borrowDates[i], dueDates[i],
                                returnDates[i]==null?"-":returnDates[i], fines[i]);
                    }
                }
            }
        }
    }

    static void popularBooksReport() {
        if (bookCount == 0) { System.out.println("No books."); return; }
        int[] borrowCounts = new int[bookCount];
        for (int i = 0; i < transactionCount; i++) {
            int idx = findBookIndexById(transactionBookIds[i]);
            if (idx != -1) borrowCounts[idx]++;
        }
        System.out.printf("%-8s %-26s %-18s %-10s%n","BookID","Title","Author","Borrows");
        System.out.println("-".repeat(70));
        for (int i = 0; i < bookCount; i++) {
            System.out.printf("%-8s %-26s %-18s %-10d%n", bookIds[i], shorten(bookTitles[i],26), shorten(bookAuthors[i],18), borrowCounts[i]);
        }
        String[] uniqueGenres = new String[bookCount];
        int[] genreCounts = new int[bookCount];
        int ugc = 0;
        for (int i = 0; i < transactionCount; i++) {
            int b = findBookIndexById(transactionBookIds[i]);
            if (b != -1) {
                String g = bookGenres[b];
                int gi = indexOf(uniqueGenres, ugc, g);
                if (gi == -1) { uniqueGenres[ugc] = g; genreCounts[ugc]=1; ugc++; }
                else genreCounts[gi]++;
            }
        }
        if (ugc == 0) { System.out.println("No genre popularity data yet."); return; }
        int maxI = 0, minI = 0;
        for (int i = 1; i < ugc; i++) {
            if (genreCounts[i] > genreCounts[maxI]) maxI = i;
            if (genreCounts[i] < genreCounts[minI]) minI = i;
        }
        System.out.println("Most Popular Genre: " + uniqueGenres[maxI] + " ("+genreCounts[maxI]+")");
        System.out.println("Least Popular Genre: " + uniqueGenres[minI] + " ("+genreCounts[minI]+")");
    }

    static void overdueBooksReport() {
        LocalDate today = LocalDate.now();
        boolean any = false;
        System.out.printf("%-6s %-10s %-8s %-12s %-12s %-6s %-14s%n","TID","BookID","MemID","Borrow","Due","Days","Member Phone");
        System.out.println("-".repeat(80));
        for (int i = 0; i < transactionCount; i++) {
            if (returnDates[i]==null) {
                LocalDate due = LocalDate.parse(dueDates[i], DF);
                long late = java.time.temporal.ChronoUnit.DAYS.between(due, today);
                if (late > 0) {
                    int mIdx = findMemberIndexById(transactionMemberIds[i]);
                    System.out.printf("%-6d %-10s %-8d %-12s %-12s %-6d %-14s%n",
                            transactionIds[i], transactionBookIds[i], transactionMemberIds[i],
                            borrowDates[i], dueDates[i], (int)late, memberPhones[mIdx]);
                    any = true;
                }
            }
        }
        if (!any) System.out.println("No overdue books.");
    }

    static void printMember(int idx) {
        System.out.println("ID: "+memberIds[idx]);
        System.out.println("Name: "+memberNames[idx]);
        System.out.println("Address: "+memberAddresses[idx]);
        System.out.println("Phone: "+memberPhones[idx]);
        System.out.println("Type: "+memberTypes[idx]);
    }

    static void printBook(int idx) {
        System.out.println("ID: "+bookIds[idx]);
        System.out.println("Title: "+bookTitles[idx]);
        System.out.println("Author: "+bookAuthors[idx]);
        System.out.println("Genre: "+bookGenres[idx]);
        System.out.println("Year: "+bookYears[idx]);
        System.out.println("Total Copies: "+totalCopies[idx]);
        System.out.println("Available Copies: "+availableCopies[idx]);
    }

    static int readInt() {
        while (true) {
            try {
                String s = scanner.nextLine();
                return Integer.parseInt(s.trim());
            } catch (Exception e) { System.out.print("Enter a valid number: "); }
        }
    }

    static int findMemberIndexById(int id) {
        for (int i = 0; i < memberCount; i++) if (memberIds[i]==id) return i;
        return -1;
    }

    static int findBookIndexById(String id) {
        for (int i = 0; i < bookCount; i++) if (bookIds[i].equals(id)) return i;
        return -1;
    }

    static int findTransactionIndexById(int tid) {
        for (int i = 0; i < transactionCount; i++) if (transactionIds[i]==tid) return i;
        return -1;
    }

    static int findReservationIndexById(int rid) {
        for (int i = 0; i < reservationCount; i++) if (reservationIds[i]==rid) return i;
        return -1;
    }

    static int countActiveBorrowingsForMember(int mid) {
        int c=0; for (int i=0;i<transactionCount;i++) if (transactionMemberIds[i]==mid && returnDates[i]==null) c++; return c;
    }

    static int earliestActiveReservationForBook(String bid) {
        int idx = -1; String earliest = null;
        for (int i = 0; i < reservationCount; i++) {
            if (reservationBookIds[i].equals(bid) && reservationStatus[i].equals("Active")) {
                if (earliest == null || reservationDates[i].compareTo(earliest) < 0) { earliest = reservationDates[i]; idx = i; }
            }
        }
        return idx;
    }

    static int indexOf(String[] arr, int len, String v) {
        for (int i=0;i<len;i++) if (arr[i].equals(v)) return i; return -1;
    }

    static String shorten(String s, int n) {
        if (s==null) return "";
        return s.length()<=n?s:s.substring(0,n-1);
    }

    public static void initializeSampleData() {
        memberIds[0]=101; memberNames[0]="Kamal Silva"; memberAddresses[0]="Colombo"; memberPhones[0]="0771234567"; memberTypes[0]="Regular";
        memberIds[1]=102; memberNames[1]="Nimali Perera"; memberAddresses[1]="Kandy"; memberPhones[1]="0772345678"; memberTypes[1]="Premium";
        memberIds[2]=103; memberNames[2]="Sunil Jay"; memberAddresses[2]="Galle"; memberPhones[2]="0711111111"; memberTypes[2]="Student";
        memberIds[3]=104; memberNames[3]="Harshi Dias"; memberAddresses[3]="Matara"; memberPhones[3]="0702222222"; memberTypes[3]="Regular";
        memberIds[4]=105; memberNames[4]="Ruwan D"; memberAddresses[4]="Kurunegala"; memberPhones[4]="0723333333"; memberTypes[4]="Student";
        memberCount=5;

        String[] ids = {"B001","B002","B003","B004","B005","B006","B007","B008","B009","B010"};
        String[] titles = {"Clean Code","Algorithms","Data Structures","Operating Systems","Networks","Database Systems","AI Basics","Java Programming","Web Dev","Cloud 101"};
        String[] authors = {"Martin","Cormen","Goodrich","Silberschatz","Tanenbaum","Elmasri","Russell","Schildt","Duckett","Armbrust"};
        String[] genres = {"Tech","Tech","Tech","Tech","Tech","Tech","AI","Programming","Web","Cloud"};
        int[] years = {2008,2009,2014,2018,2010,2015,2020,2021,2016,2019};
        int[] copies = {3,2,4,3,2,3,2,5,3,2};

        for (int i=0;i<10;i++){
            bookIds[i]=ids[i]; bookTitles[i]=titles[i]; bookAuthors[i]=authors[i]; bookGenres[i]=genres[i]; bookYears[i]=years[i];
            totalCopies[i]=copies[i]; availableCopies[i]=copies[i];
        }
        bookCount=10;

        int t1 = nextTransactionId++;
        transactionIds[0]=t1; transactionMemberIds[0]=101; transactionBookIds[0]="B001";
        borrowDates[0]=LocalDate.now().minusDays(10).format(DF); dueDates[0]=LocalDate.now().plusDays(4).format(DF); returnDates[0]=null; fines[0]=0.0;

        int t2 = nextTransactionId++;
        transactionIds[1]=t2; transactionMemberIds[1]=102; transactionBookIds[1]="B003";
        borrowDates[1]=LocalDate.now().minusDays(20).format(DF); dueDates[1]=LocalDate.now().minusDays(6).format(DF); returnDates[1]=null; fines[1]=0.0;

        int t3 = nextTransactionId++;
        transactionIds[2]=t3; transactionMemberIds[2]=103; transactionBookIds[2]="B008";
        borrowDates[2]=LocalDate.now().minusDays(5).format(DF); dueDates[2]=LocalDate.now().plusDays(9).format(DF); returnDates[2]=null; fines[2]=0.0;

        transactionCount=3;
        availableCopies[findBookIndexById("B001")]--;
        availableCopies[findBookIndexById("B003")]--;
        availableCopies[findBookIndexById("B008")]--;

        System.out.println("Sample data initialized.");
    }
}
