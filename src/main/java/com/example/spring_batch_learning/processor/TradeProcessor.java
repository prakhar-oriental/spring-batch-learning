package com.example.spring_batch_learning.processor;

import com.example.spring_batch_learning.model.Trade;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;


public class TradeProcessor implements ItemProcessor<Trade, Trade> {


    @Override
    public @Nullable Trade process(Trade trade) throws Exception {
        System.out.println("Processing trade: " + trade.getTradeId());

        if ("T003".equals(trade.getTradeId())) {
            throw new RuntimeException("Invalid trade: " + trade.getTradeId());
        }
        return trade;
    }
}
