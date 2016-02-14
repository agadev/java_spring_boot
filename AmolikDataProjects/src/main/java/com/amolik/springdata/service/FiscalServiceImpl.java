package com.amolik.springdata.service;

import com.amolik.springdata.entity.Fiscal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("fiscalService")
@Transactional
public class FiscalServiceImpl implements FiscalService {

    @Autowired
    private FiscalRepository fiscalRepository;

    @Override
    public Fiscal addFiscal(Fiscal fiscal) {
        return fiscalRepository.save(fiscal);
    }

}
