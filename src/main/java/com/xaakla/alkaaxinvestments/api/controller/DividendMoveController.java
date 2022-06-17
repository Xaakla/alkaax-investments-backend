package com.xaakla.alkaaxinvestments.api.controller;

import com.xaakla.alkaaxinvestments.api.model.dividendMove.DividendMoveCreateReqModel;
import com.xaakla.alkaaxinvestments.api.model.dividendMove.DividendMoveEditReqModel;
import com.xaakla.alkaaxinvestments.domain.service.DividendMoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/dividend-moves")
public class DividendMoveController {

    @Autowired
    private DividendMoveService dividendMoveService;

    @GetMapping
    public ResponseEntity findAll() { return dividendMoveService.findAll(); }

    @PostMapping
    public ResponseEntity save(@RequestBody @Valid DividendMoveCreateReqModel dividendMoveCreateReqModel) {
        return dividendMoveService.save(dividendMoveCreateReqModel);
    }

    @PatchMapping
    public ResponseEntity edit(@RequestBody @Valid DividendMoveEditReqModel dividendMoveEditReqModel) {
        return dividendMoveService.edit(dividendMoveEditReqModel);
    }

    @DeleteMapping("/{dividendMoveId}")
    public ResponseEntity deleteById(@PathVariable Long dividendMoveId) {
        return dividendMoveService.deleteById(dividendMoveId);
    }
}
