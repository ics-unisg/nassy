package com.dcap.service.threads;

import com.dcap.domain.User;

import java.util.List;
import java.util.concurrent.Callable;

public interface CallableForWorkerInterface extends Callable {
    @Override
    Object call() throws Exception;

    User getUser();

    String getId();
}
