package com.yassir.bank.controller;

import com.yassir.bank.exception.ResourceNotFoundException;
import com.yassir.bank.model.Currency;
import com.yassir.bank.model.User;
import com.yassir.bank.repos.CurrencyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/Currency")
public class CurrencyController {

}
