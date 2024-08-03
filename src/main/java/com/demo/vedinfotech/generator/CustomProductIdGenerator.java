package com.demo.vedinfotech.generator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Random;

public class CustomProductIdGenerator implements IdentifierGenerator {

    private static final int FOUR_DIGIT_START = 1000;
    private static final int FOUR_DIGIT_LIMIT = 9999;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) {
        Random random = new Random();
        Long id;

        // Query to get the maximum existing ID in the database
        String query = "select max(p.id) from ProductEntity p";
        Long maxId = (Long) session.createQuery(query).uniqueResult();

        if (maxId == null || maxId < FOUR_DIGIT_LIMIT) {
            id = (long) (random.nextInt(FOUR_DIGIT_LIMIT - FOUR_DIGIT_START + 1) + FOUR_DIGIT_START);
        } else {
            int digitCount = String.valueOf(maxId).length();
            long lowerBound = (long) Math.pow(10, digitCount);
            long upperBound = lowerBound * 10 - 1;
            id = (long) (random.nextDouble() * (upperBound - lowerBound)) + lowerBound;
        }

        return id;
    }
}
