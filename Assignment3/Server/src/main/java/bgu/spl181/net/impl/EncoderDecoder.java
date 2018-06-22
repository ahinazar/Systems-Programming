package bgu.spl181.net.impl;

import bgu.spl181.net.api.MessageEncoderDecoder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class EncoderDecoder implements MessageEncoderDecoder {

    private byte[] bytes = new byte[1 << 10];
    private int len = 0;

    @Override
    public Object decodeNextByte(byte nextByte) {
        if (nextByte == '\n') {
            return popString();
        }
        pushByte(nextByte);
        return null; //not a line yet
    }

    @Override
    public byte[] encode(Object message) {
        return (message + "\n").getBytes();
    }

    private String popString() { //pop all the input reset the len
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        } else
            bytes[len++] = nextByte;
    }
}
