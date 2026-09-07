package com.example.batch.writer;



import com.example.spring_batch_learning.model.Trade;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import javax.sql.DataSource;

public class TradeWriter extends JdbcBatchItemWriter<Trade> {

    public TradeWriter(DataSource dataSource) {

        setDataSource(dataSource);

        setSql("""
                INSERT INTO trades
                (trade_id, symbol, quantity, price)
                VALUES
                (:tradeId, :symbol, :quantity, :price)
                """);

        setItemSqlParameterSourceProvider(
                trade -> new MapSqlParameterSource()
                        .addValue("tradeId", trade.getTradeId())
                        .addValue("symbol", trade.getSymbol())
                        .addValue("quantity", trade.getQuantity())
                        .addValue("price", trade.getPrice())
        );

        afterPropertiesSet();
    }
}