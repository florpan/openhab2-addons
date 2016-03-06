package org.openhab.binding.thermiaonline.models;

import java.util.ArrayList;
import java.util.List;

public class InstallationsResult {

    public List<InstallationInfo> installationsInfo = new ArrayList<InstallationInfo>();
    public Integer numberOfReturnedInstallations;
    public Boolean maxReached;
}
