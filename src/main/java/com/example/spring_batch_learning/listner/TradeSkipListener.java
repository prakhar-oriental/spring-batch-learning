package com.example.spring_batch_learning.listner;

import com.example.spring_batch_learning.model.Trade;
import org.springframework.batch.core.listener.SkipListener;

public class TradeSkipListener implements SkipListener<Trade, Trade> {

    @Override
    public void onSkipInProcess(
            Trade trade,
            Throwable t) {

        System.out.println(
                "Skipped trade: "
                + trade.getTradeId()
                + " Reason: "
                + t.getMessage()
        );
    }
}