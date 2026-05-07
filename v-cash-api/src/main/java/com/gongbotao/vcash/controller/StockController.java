package com.gongbotao.vcash.controller;

import com.gongbotao.vcash.application.stock.service.StockApplicationService;
import com.gongbotao.vcash.domain.stock.entity.StockBasic;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockApplicationService stockApplicationService;

    @GetMapping("/list")
    public ResponseEntity<List<StockBasic>> list(@RequestParam(required = false) String market) {
        return ResponseEntity.ok(stockApplicationService.listFromDb(market));
    }

    @GetMapping("/{code}")
    public ResponseEntity<StockBasic> detail(@PathVariable String code) {
        StockBasic stock = stockApplicationService.getByCode(code);
        if (stock == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(stock);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@RequestParam(required = false) String market) {
        stockApplicationService.refreshStockList(market);
        return ResponseEntity.ok("ok");
    }
}
