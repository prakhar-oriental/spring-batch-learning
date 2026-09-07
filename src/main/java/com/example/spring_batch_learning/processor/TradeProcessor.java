package com.example.spring_batch_learning.processor;

import com.example.spring_batch_learning.model.Trade;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;

import java.sql.SQLOutput;

public class TradeProcessor implements ItemProcessor<Trade, Trade> {


    @Override
    public @Nullable Trade process(Trade item) throws Exception {
        System.out.println("Processing trade: " + item.getTradeId());
        return item;
    }
}
