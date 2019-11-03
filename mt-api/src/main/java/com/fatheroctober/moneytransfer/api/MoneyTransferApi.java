package com.fatheroctober.moneytransfer.api;

import com.fatheroctober.moneytransfer.api.service.AccountService;
import com.fatheroctober.moneytransfer.api.service.FundTransferService;

import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MoneyTransferApi extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> set = new HashSet<>();
        set.add(FundTransferService.class);
        set.add(AccountService.class);
        return set;
    }

    @Override
    public Set<Object> getSingletons() {
        return Collections.emptySet();
    }
}
