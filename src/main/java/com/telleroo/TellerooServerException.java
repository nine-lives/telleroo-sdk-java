package com.telleroo;

public class TellerooServerException extends TellerooException {
    private final int statusCode;
    private final String statusMessage;
    private final TellerooError error;

    public TellerooServerException(int statusCode, String statusMessage, TellerooError error) {
        super(buildMessage(statusCode, statusMessage, error));
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.error = error;
    }

    /**
     * Get the HTTP status code
     *
     * @return the status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Get the HTTP status message
     *
     * @return the status message
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * Get the error returned by the server
     *
     * @return the list of errors
     */
    public TellerooError getError() {
        return error;
    }

    private static String buildMessage(int statusCode, String statusMessage, TellerooError error) {
        StringBuilder sb = new StringBuilder();
        sb.append(statusCode).append(": ").append(statusMessage);

        if (error != null) {
            sb.append(" - ").append(error.getMessage());
        }

        return sb.toString();
    }
}
