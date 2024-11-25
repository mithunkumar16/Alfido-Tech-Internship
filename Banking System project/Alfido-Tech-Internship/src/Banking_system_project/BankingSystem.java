package Banking_system_project;

import java.io.*;
import java.util.Scanner;

class BankAccount {
	private String accountHolderName;
	private double balance;
	

	public BankAccount(String accountHolderName, double initialBalance) {
		this.accountHolderName = accountHolderName;
		this.balance = initialBalance;
	}

	public void deposit(double amount) {
		if (amount > 0) {
			balance += amount;
			System.out.println("Deposited: $" + amount);
		} else {
			System.out.println("Deposit amount must be positive.");
		}
	}

	public void withdraw(double amount) {
		if (amount > 0 && amount <= balance) {
			balance -= amount;
			System.out.println("Withdrawn: $" + amount);
		} else if (amount > balance) {
			System.out.println("Insufficient funds for withdrawal.");
		} else {
			System.out.println("Withdrawal amount must be positive.");
		}
	}

	public double getBalance() {

		return balance;

	}

	public String getAccountHolderName() {
		return accountHolderName;
	}

	public void saveAccountInfo() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(accountHolderName + ".txt"))) {
			writer.write("Account Holder: " + accountHolderName);
			writer.newLine();
			writer.write("Balance: $" + balance);
			System.out.println("Account information saved to " + accountHolderName + ".txt");
		} catch (IOException e) {
			System.out.println("Error saving account information: " + e.getMessage());
		}
	}
}

public class BankingSystem {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("********  Welcome to the Banking System  *********");

		// Create a new account
		System.out.print("Enter your name: ");
		String name = scanner.nextLine();
		System.out.print("Enter initial balance: ");
		double initialBalance = scanner.nextDouble();
		BankAccount account = new BankAccount(name, initialBalance);

		boolean exit = false;
		while (!exit) {
			System.out.println("\nChoose an option:");
			System.out.println("**********************************************");
			System.out.print("1. Deposit Money");
			System.out.println("\t2. Withdraw Money");
			System.out.print("3. Check Balance");
			System.out.println("\t4. Save Account Info");
			System.out.println("5. Exit");
			System.out.println("**********************************************");
			int choice = scanner.nextInt();
			switch (choice) {
			case 1:
				System.out.print("Enter amount to deposit: ");
				double depositAmount = scanner.nextDouble();
				System.out.println("Enter the Pin");
			    int pin1 = scanner.nextInt();
				if (pin1 == 123) {
					account.deposit(depositAmount);
				}
				else {
					System.out.println("Invalid pin !");
				}
				
				break;
			case 2:
				System.out.print("Enter amount to withdraw: ");
				double withdrawAmount = scanner.nextDouble();
				System.out.println("Enter the Pin");
			    int pin2 = scanner.nextInt();
				if (pin2 == 123) {
					account.withdraw(withdrawAmount);
				}
				else {
					System.out.println("Invalid pin !");
				}
				
				break;
			case 3:

				System.out.println("Enter the Pin");
				 int pin = scanner.nextInt();
				if (pin == 123) {
					System.out.println("Current Balance: $" + account.getBalance());
				}
				else {
					System.out.println("Invalid pin !");
				}
				break;
			case 4:
				account.saveAccountInfo();
				break;
			case 5:
				exit = true;
				System.out.println("Thank you for using the Banking System!");
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		}

		scanner.close();
	}
}
