package com.dcap.service;

import com.dcap.domain.Settings;
import com.dcap.repository.SettingInterface;
import com.dcap.service.serviceInterfaces.SettingServiceInterface;
import com.dcap.domain.Settings;
import com.dcap.repository.SettingInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to manage the Settings in the database
 *
 * @author uli
 */

@Service
public class SettingService implements SettingServiceInterface {

    private final SettingInterface settingRepo;

    @Autowired
    public SettingService(SettingInterface settingRepo) {
        this.settingRepo = settingRepo;
    }

    /**
     * finds all Settings in the database
     * @return list of Settings items in database
     */
    @Override
    public List<Settings> gettAllSettings(){
        return  settingRepo.findAll();
    }

    /**
     *saves or updates  items in the database
     * @param  setting Setting item to be saved or updated
     * @return  Setting item saved in the database. Attention: if you create a new Object, the ID is only created by saving it in the database!
     */
    @Override
    public Settings saveOrUpdate(Settings setting){
        return settingRepo.save(setting);
    }

    /**
     * deletes given Setting in the database
     * @param setting Setting to be deleted
     */
    @Override
    public void delete(Settings setting){
        settingRepo.delete(setting);
    }
}
