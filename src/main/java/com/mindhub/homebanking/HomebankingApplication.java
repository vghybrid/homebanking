package com.mindhub.homebanking;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEnconder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(
			ClientRepository clientRepository,
			AccountRepository accountRepository,
			TransactionRepository transactionRepository,
			LoanRepository loanRepository,
			ClientLoansRespository clientLoansRespository,
			CardRepository cardRepository)
	{
		return args -> {
//			LocalDateTime now = LocalDateTime.now();
//			LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
//			LocalDateTime years = LocalDateTime.now().plusYears(5);
//			DateTimeFormatter format =  DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
//			String formatDateTime = now.format(format);
//			String formatDateTimeTomorrow = tomorrow.format(format);
//			String formatDateTimeYears = years.format(format);
//			List<Integer> feeMortgage = List.of(12, 24, 36, 48, 60);
//			List<Integer> feePersonal = List.of(6, 12, 24);
//			List<Integer> feeAutomotive = List.of(6, 12, 24, 36);
//			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEnconder.encode("1234"));
//			Client client2 = new Client("Anderson", "Sarmiento", "anderson@gmail.com", passwordEnconder.encode("1234"));
//			clientRepository.save(client1);
//			clientRepository.save(client2);
//			Account account1 = new Account("VIN001", formatDateTime, 5000.0);
//			Account account2 = new Account("VIN002", formatDateTimeTomorrow, 7500.0);
//			Account account3 = new Account("VIN003", formatDateTime, 12500.0);
//			Account account4 = new Account("VIN004", formatDateTime, 7800.0);
//			client1.addAccount(account1);
//			client1.addAccount(account2);
//			client2.addAccount(account3);
//			client2.addAccount(account4);
//			accountRepository.save(account2);
//			accountRepository.save(account1);
//			accountRepository.save(account3);
//			accountRepository.save(account4);
//			Transaction transaction1 = new Transaction("Twitch Userunk_ Suscription", formatDateTime, 500.0, TransactionType.DEBIT);
//			Transaction transaction2 = new Transaction("Discord Nitro Refund", formatDateTime, 850.0, TransactionType.CREDIT);
//			Transaction transaction3 = new Transaction("Twitch Sub", formatDateTime, 550.0, TransactionType.DEBIT);
//			Transaction transaction4 = new Transaction("Discord Nitro", formatDateTime, 1600.0, TransactionType.DEBIT);
//			Transaction transaction5 = new Transaction("Quesito", formatDateTime, 2500.0, TransactionType.CREDIT);
//			account1.addTransaction(transaction1);
//			account1.addTransaction(transaction2);
//			account3.addTransaction(transaction4);
//			account3.addTransaction(transaction5);
//			account4.addTransaction(transaction3);
//			transactionRepository.save(transaction1);
//			transactionRepository.save(transaction2);
//			transactionRepository.save(transaction3);
//			transactionRepository.save(transaction4);
//			transactionRepository.save(transaction5);
//			Loan loan1 = new Loan("Mortgage", 500000.0, feeMortgage);
//			Loan loan2 = new Loan("Personal", 100000.0, feePersonal);
//			Loan loan3 = new Loan("Automotive", 300000.0, feeAutomotive);
//			loanRepository.save(loan1);
//			loanRepository.save(loan2);
//			loanRepository.save(loan3);
//			ClientLoan clientloan1 = new ClientLoan(60, 400000.0, client1, loan1);
//			ClientLoan clientloan2 = new ClientLoan(12, 50000.0, client1, loan2);
//			ClientLoan clientloan3 = new ClientLoan(24, 100000.0, client2, loan2);
//			ClientLoan clientloan4 = new ClientLoan(36, 200000.0, client2, loan3);
//			clientLoansRespository.save(clientloan1);
//			clientLoansRespository.save(clientloan2);
//			clientLoansRespository.save(clientloan3);
//			clientLoansRespository.save(clientloan4);
//			Card card1 = new Card(client1.getFirstName() + " " +  client1.getLastName(), CardType.DEBIT, CardColor.GOLD, "3325-6745-7876-4445", 990, formatDateTime, formatDateTimeYears);
//			Card card2 = new Card(client1.getFirstName() + " " +  client1.getLastName(), CardType.CREDIT, CardColor.TITANIUM, "2234-6745-5522-7888", 750, formatDateTime, formatDateTimeYears);
//			Card card3 = new Card(client2.getFirstName() + " " +  client2.getLastName(), CardType.DEBIT, CardColor.SILVER, "6456-3342-7645-9865", 990, formatDateTime, formatDateTimeYears);
//			client1.addCard(card1);
//			client1.addCard(card2);
//			client2.addCard(card3);
//			cardRepository.save(card1);
//			cardRepository.save(card2);
//			cardRepository.save(card3);
		};
	}

}
