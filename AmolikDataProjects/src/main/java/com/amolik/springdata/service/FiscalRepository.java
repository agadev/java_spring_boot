package com.amolik.springdata.service;

import com.amolik.springdata.entity.Fiscal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface FiscalRepository extends Repository<Fiscal, Long> {


    Fiscal findByImageName(String imageName);
    Fiscal save(Fiscal fiscal);

}
