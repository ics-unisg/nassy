package com.dcap.service.serviceInterfaces;

import com.dcap.domain.Settings;

import java.util.List;

public interface SettingServiceInterface {
    List<Settings> gettAllSettings();

    Settings saveOrUpdate(Settings setting);

    void delete(Settings setting);
}
