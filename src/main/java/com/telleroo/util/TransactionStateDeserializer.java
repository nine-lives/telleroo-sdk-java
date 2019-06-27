package com.telleroo.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.telleroo.TransactionState;

import java.io.IOException;

public class TransactionStateDeserializer extends StdScalarDeserializer<TransactionState> {
    TransactionStateDeserializer() {
        super(TransactionState.class);
    }

    @Override
    public TransactionState deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.getCurrentToken().id() == JsonTokenId.ID_STRING) {
            String text = p.getText().trim();
            if (_isEmptyOrTextualNull(text)) {
                return getNullValue(ctxt);
            }

            try {
                return TransactionState.fromTransmissionValue(text);
            } catch (IllegalArgumentException iae) {
                return (TransactionState) ctxt.handleWeirdStringValue(_valueClass, text, "not a valid transaction state");
            }
        }

        return (TransactionState) ctxt.handleUnexpectedToken(_valueClass, p);
    }
}
