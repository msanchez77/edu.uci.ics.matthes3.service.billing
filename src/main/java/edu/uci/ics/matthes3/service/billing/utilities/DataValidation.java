package edu.uci.ics.matthes3.service.billing.utilities;

public interface DataValidation {
    class DataValidationException extends Exception {
        public DataValidationException(String s) {
            super(s);
        }
    }

    void isDataValid() throws DataValidationException;
}
