package org.openhab.binding.thermiaonline.models;

import java.util.ArrayList;
import java.util.List;

public class Installation {
    public Address address;
    public Profile profile;
    public Device device;
    public Retailer retailer;
    public String model;
    public String operationManualUrl;
    public String timeZone;
    public Integer unreadErrors;
    public Integer unreadWarnings;
    public Integer unreadInfo;
    public Integer activeAlarms;
    public Boolean hasLinkUnit;
    public Boolean hasIndoorTempSensor;
    public Boolean isOutdoorTempSensorFunctioning;
    public Boolean isHotwaterActive;
    public Boolean isOnline;
    public List<Equipment> equipment = new ArrayList<Equipment>();
    public Boolean hasServiceAccess;
    public Boolean hasAdminAccess;
    public InstallationStatus status;
    public Boolean isNCP;
    public Integer id;
    public String name;
}
