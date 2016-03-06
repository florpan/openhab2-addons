/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.thermiaonline;

import java.util.Set;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

import com.google.common.collect.ImmutableSet;

/**
 * The {@link ThermiaOnlineBinding} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Christer Åkerlund - Initial contribution
 */
public class ThermiaOnlineBinding {

    public static final String BINDING_ID = "thermiaonline";
    public static final String ONLINE_BRIDGE = "bridge";
    public static final String ONLINE_PUMP = "pump";
    /*
     * public static final String DEVICE_OPERATION = "operation";
     * public static final String DEVICE_STATUS = "status";
     * public static final String DEVICE_TEMPERATURE = "temperature";
     * public static final String DEVICE_TIME = "time";
     * public static final String DEVICE_HEATING = "heating";
     * public static final String DEVICE_HEATING2 = "heating2";
     * public static final String DEVICE_SOFTWARE = "software";
     */

    public static final String PROPERTY_PUMPID = "pumpid";

    public static final String OPERATION_GROUP = "REG_GROUP_OPERATIONAL_OPERATION";
    public static final int OPERATION_CHANNEL_MODE = 6013;
    public static final int OPERATION_CHANNEL_LINK = 6003;

    public static final String STATUS_GROUP = "REG_GROUP_OPERATIONAL_STATUS";
    public static final int STATUS_CHANNEL_EXTRAPOWER = 6001;
    public static final int STATUS_CHANNEL_STATUS = 6002;
    public static final int STATUS_CHANNEL_CURRENT = 6018;
    public static final int STATUS_CHANNEL_INTEGRAL = 6017;

    public static final String TEMPERATURE_GROUP = "REG_GROUP_TEMPERATURES";
    public static final int TEMPERATURE_CHANNEL_OUTDOOR = 6000;
    public static final int TEMPERATURE_CHANNEL_FEED = 6002;
    public static final int TEMPERATURE_CHANNEL_FEEDSHOULD = 6009;
    public static final int TEMPERATURE_CHANNEL_HOTWATER = 6004;
    public static final int TEMPERATURE_CHANNEL_BRINEOUT = 6005;
    public static final int TEMPERATURE_CHANNEL_BRINEIN = 6006;
    public static final int TEMPERATURE_CHANNEL_COOLING = 6007;
    public static final int TEMPERATURE_CHANNEL_SHUNTGROUP = 6008;
    public static final int TEMPERATURE_CHANNEL_SETINNERTEMP = 6011;

    public static final String TIME_GROUP = "REG_GROUP_OPERATIONAL_TIME";
    public static final int TIME_CHANNEL_COMPRESSOR = 6019;
    public static final int TIME_CHANNEL_HOTWATER = 6020;
    public static final int TIME_CHANNEL_PASSIVECOOL = 6021;
    public static final int TIME_CHANNEL_ACTIVECOOL = 6022;
    public static final int TIME_CHANNEL_EXTRA1 = 6023;
    public static final int TIME_CHANNEL_EXTRA2 = 6024;

    public static final String HEATING_GROUP = "REG_GROUP_HEATING_CURVE";
    public static final int HEATING_CHANNEL_CURVE = 60254;
    public static final int HEATING_CHANNEL_MIN = 6026;
    public static final int HEATING_CHANNEL_MAX = 6027;
    public static final int HEATING_CHANNEL_CURVEPOS5 = 6028;
    public static final int HEATING_CHANNEL_CURVE0 = 6029;
    public static final int HEATING_CHANNEL_CURVENEG5 = 6030;
    public static final int HEATING_CHANNEL_STOP = 6031;

    public static final String HEATING2_GROUP = "REG_GROUP_HEATING_CURVE2";
    public static final int HEATING2_CHANNEL_CURVE = 6042;
    public static final int HEATING2_CHANNEL_MIN = 6043;
    public static final int HEATING2_CHANNEL_MAX = 6044;

    public static final String SOFTWARE_GROUP = "REG_GROUP_SOFTWARE_VERSIONS";
    public static final int SOFTWARE_CHANNEL_VERSION = 6055;

    // Bridge channels
    public static final String BRIDGE_CHANNEL_NAME = "name";
    public static final String BRIDGE_CHANNEL_RETAILER = "retailer";
    public static final String BRIDGE_CHANNEL_UNREADERRORS = "unreadErrors";
    public static final String BRIDGE_CHANNEL_UNREADINFO = "unreadInfo";
    public static final String BRIDGE_CHANNEL_UNREADWARNINGS = "unreadWarnings";
    public static final String BRIDGE_CHANNEL_ACTIVEALARMS = "activeAlarms";
    public static final String BRIDGE_CHANNEL_HEATINGEFFECT = "heatingEffect";
    public static final String BRIDGE_CHANNEL_REDUCEDHEATINGEFFECT = "reducedHeatingEffect";
    public static final String BRIDGE_CHANNEL_ISONLINE = "isonline";
    public static final String BRIDGE_CHANNEL_ISOUTTEMPFUNCTIONING = "isOutdoorTempSensorFunctioning";
    public static final String BRIDGE_CHANNEL_ISHOTWATERACTIVE = "isHotwaterActive";

    // List of all Thing Type UIDs
    public final static ThingTypeUID THING_TYPE_ONLINE = new ThingTypeUID(BINDING_ID, ONLINE_BRIDGE);
    public final static ThingTypeUID THING_TYPE_PUMP = new ThingTypeUID(BINDING_ID, ONLINE_PUMP);

    /*
     * public final static ThingTypeUID THING_TYPE_OPERATION = new ThingTypeUID(BINDING_ID, DEVICE_OPERATION);
     * public final static ThingTypeUID THING_TYPE_STATUS = new ThingTypeUID(BINDING_ID, DEVICE_STATUS);
     * public final static ThingTypeUID THING_TYPE_TEMPERATURE = new ThingTypeUID(BINDING_ID, DEVICE_TEMPERATURE);
     * public final static ThingTypeUID THING_TYPE_TIME = new ThingTypeUID(BINDING_ID, DEVICE_TIME);
     * public final static ThingTypeUID THING_TYPE_HEATING = new ThingTypeUID(BINDING_ID, DEVICE_HEATING);
     * public final static ThingTypeUID THING_TYPE_HEATING2 = new ThingTypeUID(BINDING_ID, DEVICE_HEATING2);
     * public final static ThingTypeUID THING_TYPE_SOFTWARE = new ThingTypeUID(BINDING_ID, DEVICE_SOFTWARE);
     */
    // public final static Collection<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Lists.newArrayList(THING_TYPE_ONLINE,
    // THING_TYPE_PUMP);
    /*
     * THING_TYPE_OPERATION, THING_TYPE_STATUS, THING_TYPE_TEMPERATURE, THING_TYPE_TIME, THING_TYPE_HEATING,
     * THING_TYPE_HEATING2, THING_TYPE_SOFTWARE);
     */
    // public final static Set<ThingTypeUID> SUPPORTED_DEVICE_THING_TYPES_UIDS = ImmutableSet
    // .of(THING_TYPE_PUMP);
    /*
     * ImmutableSet.of(THING_TYPE_OPERATION,
     * THING_TYPE_STATUS, THING_TYPE_TEMPERATURE, THING_TYPE_TIME, THING_TYPE_HEATING,
     * THING_TYPE_HEATING2,
     * THING_TYPE_SOFTWARE);
     */

    // public final static Set<ThingTypeUID> SUPPORTED_BRIDGE_THING_TYPES_UIDS = ImmutableSet.of(THING_TYPE_ONLINE);

    public final static Set<ThingTypeUID> SupportedThingTypes = ImmutableSet.of(THING_TYPE_ONLINE, THING_TYPE_PUMP);
    // List of all Channel ids
    // public final static String CHANNEL_1 = "channel1";

}
