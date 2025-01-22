package org.gescomlbank.services.bankaccounts;

import org.gescomlbank.dtos.BankAccountDto;
import org.gescomlbank.entities.BankAccount;
import org.gescomlbank.entities.Client;
import org.gescomlbank.entities.CurrentAccount;
import org.gescomlbank.entities.SavingAccount;
import org.gescomlbank.enums.AccountStatus;
import org.gescomlbank.repositories.BankAccountRepository;
import org.gescomlbank.repositories.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BankAccountService implements IBankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final ClientRepository clientRepository;
    BankAccountService(
            final BankAccountRepository bankAccountRepository,
            final ClientRepository clientRepository
            ) {
        this.bankAccountRepository = bankAccountRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public void createBankAccount(BankAccountDto bankAccountDto) {
        Optional<Client> client = this.clientRepository.findById(bankAccountDto.getClientId());
        System.out.println(client);
        if (client.isPresent() && (bankAccountDto.getOverdraft() > 0 && bankAccountDto.getInterestRate() == 0)) {
            // compte courant
            CurrentAccount currentAccount = new CurrentAccount();

            currentAccount.setClient(client.get());
            currentAccount.setStatus(AccountStatus.ACTIVATED);
            currentAccount.setNumAccount(generateNumAccount());
            currentAccount.setBalance(bankAccountDto.getBalance());
            currentAccount.setCurrency(bankAccountDto.getCurrency());
            currentAccount.setOverdraft(bankAccountDto.getOverdraft());

            this.bankAccountRepository.save(currentAccount);
        }

        if (client.isPresent() && (bankAccountDto.getOverdraft() == 0 && bankAccountDto.getInterestRate() > 0)) {
            // compte epargne
            SavingAccount savingAccount = new SavingAccount();

            savingAccount.setClient(client.get());
            savingAccount.setStatus(AccountStatus.ACTIVATED);
            savingAccount.setNumAccount(generateNumAccount());
            savingAccount.setBalance(bankAccountDto.getBalance());
            savingAccount.setCurrency(bankAccountDto.getCurrency());
            savingAccount.setInterestRate(bankAccountDto.getInterestRate());

            this.bankAccountRepository.save(savingAccount);
        }
    }

    @Override
    public List<SavingAccount> findSavingAccounts() {
        List<SavingAccount> list = new ArrayList<>();

        for (BankAccount bankAccount : this.bankAccountRepository.findAll()) {
            if (bankAccount instanceof SavingAccount) {
                list.add((SavingAccount) bankAccount);
            }
        }
        return list;
    }

    @Override
    public List<CurrentAccount> findCurrentAccounts() {
        List<CurrentAccount> list = new ArrayList<>();

        for (BankAccount bankAccount : this.bankAccountRepository.findAll()) {
            if (bankAccount instanceof CurrentAccount) {
                list.add((CurrentAccount) bankAccount);
            }
        }
        return list;
    }

    @Override
    public BankAccount findOne(String numAccount) {
        try {
           return this.bankAccountRepository.findByNumAccount(numAccount).get();
        } catch (Exception e) {
            throw new RuntimeException("Ce compte n'existe pas !");
        }
    }

    @Override
    public boolean activeAccount(String numAccount) {
        try {
            BankAccount bankAccount = findOne(numAccount);
            if (bankAccount != null && bankAccount.getStatus().equals(AccountStatus.SUSPENDED)) {
                bankAccount.setStatus(AccountStatus.ACTIVATED);
                this.bankAccountRepository.save(bankAccount);

                return true;
            }
        } catch (RuntimeException e) {
            System.err.println("Erreur lors de l'activation du compte : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean suspendAccount(String numAccount) {
        try {
            BankAccount bankAccount = findOne(numAccount);
            if (bankAccount != null && bankAccount.getStatus().equals(AccountStatus.ACTIVATED)) {
                bankAccount.setStatus(AccountStatus.SUSPENDED);
                this.bankAccountRepository.save(bankAccount);

                return true;
            }
        } catch (RuntimeException e) {
            System.err.println("Erreur lors de la suspension du compte : " + e.getMessage());
        }

        return false;
    }


    private static String generateNumAccount (){
        Random rand = new Random();

        // les 4 premiers chifres sont 0
        StringBuilder sb = new StringBuilder("0000");

        // les 4 suivants sont 0 ou 1
        for (int i = 0; i < 4; i++) {
            sb.append(rand.nextInt(2));
        }

        //les 10 derniers sont generes aleatoirement
        for (int i = 0; i < 10; i++) {
            sb.append(rand.nextInt(10));
        }

        return sb.toString();
    }
}
