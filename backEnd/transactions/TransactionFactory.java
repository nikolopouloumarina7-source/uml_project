package backEnd.transactions;

public class TransactionFactory {

    public static TransactionCommand createCommand(TransactionRequest request) {
        switch (request.getType()) {

            case DEPOSIT:
                return new DepositCommand(request);

            case WITHDRAWAL:
                return new WithdrawalCommand(request);

            case TRANSFER_BETWEEN_ACCOUNTS:
                return new TransferBetweenAccountsCommand(request);

            case TRANSFER_INTERNAL:
                return new InternalTransferCommand(request);

            case TRANSFER_INTERBANK:
                return new InterbankTransferCommand(request);

            case PAYMENT_RF:
                return new RfPaymentCommand(request);

     

            default:
                throw new IllegalArgumentException("Unsupported transaction type:" + request.getType());
        }
    }

	
}
