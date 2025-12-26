package backEnd.transactions;

public class TransactionResult {

    private final boolean success;
    private final String message;
    private final double sourceBalanceAfter;
    private final double targetBalanceAfter;

    private TransactionResult(Builder builder) {
        this.success = builder.success;
        this.message = builder.message;
        this.sourceBalanceAfter = builder.sourceBalanceAfter;
        this.targetBalanceAfter = builder.targetBalanceAfter;
    }

    public static class Builder {
        private boolean success;
        private String message;
        private double sourceBalanceAfter;
        private double targetBalanceAfter;

        public Builder success(boolean value) { this.success = value; return this; }
        public Builder message(String value) { this.message = value; return this; }
        public Builder sourceBalanceAfter(double value) { this.sourceBalanceAfter = value; return this; }
        public Builder targetBalanceAfter(double value) { this.targetBalanceAfter = value; return this; }

        public TransactionResult build() {
            return new TransactionResult(this);
        }
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public double getSourceBalanceAfter() { return sourceBalanceAfter; }
    public double getTargetBalanceAfter() { return targetBalanceAfter; }

    @Override
    public String toString() {
        return "TransactionResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", sourceBalance=" + sourceBalanceAfter +
                ", targetBalance=" + targetBalanceAfter +
                '}';
    }
}
