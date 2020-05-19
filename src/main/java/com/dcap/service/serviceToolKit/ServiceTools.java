package com.dcap.service.serviceToolKit;

import com.dcap.domain.TimePhase;
import com.dcap.repository.TimePhaseInterface;

import java.util.List;


/**
 * This class contains methods that are shared by different services
 *
 * @author uli
 *
 */
public class ServiceTools {

    /**
     * Method used to set pointers on timephases in sessenvideos to <code>null</code>
     *
     * @param timePhase timephase that should be deleted
     * @param timePhaseRepo TimephaseRepository used to find the timephases
     */
    static public void deleteTimePhase(TimePhase timePhase, TimePhaseInterface timePhaseRepo){
        timePhaseRepo.delete(timePhase);
    }
}
