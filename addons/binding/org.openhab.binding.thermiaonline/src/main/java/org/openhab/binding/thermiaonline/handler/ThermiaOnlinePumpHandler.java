package org.openhab.binding.thermiaonline.handler;

import java.util.List;

import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.thermiaonline.ThermiaOnlineBinding;
import org.openhab.binding.thermiaonline.models.Installation;
import org.openhab.binding.thermiaonline.models.RegisterGroup;
import org.openhab.binding.thermiaonline.network.NetworkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThermiaOnlinePumpHandler extends BaseThingHandler {
    int installationId;
    Installation installation;
    List<RegisterGroup> registerGroups;
    NetworkHandler network;
    private final static Logger logger = LoggerFactory.getLogger(ThermiaOnlinePumpHandler.class);

    public ThermiaOnlinePumpHandler(Thing thing, NetworkHandler network) {
        super(thing);
        this.network = network;
        installationId = Integer.parseInt(thing.getProperties().get(ThermiaOnlineBinding.PROPERTY_PUMPID));
        logger.info("Thermia: Thing created: " + installationId);
    }

    @Override
    public void initialize() {
        logger.info("Thermia: Initializing thing: " + installationId);
        update();
    }

    private void update() {
        installation = network.getInstallation(installationId);
        logger.debug("Thermia: Updated installation: " + installation.id + ": " + installation.profile.thermiaName);
        List<RegisterGroup> registers = network.getRegisters(installationId);
        logger.debug("Thermia: Updated registers");

        updateState(new ChannelUID(getThing().getUID(), "name"), new StringType(installation.profile.thermiaName));
        updateState(new ChannelUID(getThing().getUID(), "retailer"), new StringType(installation.retailer.name));
        updateState(new ChannelUID(getThing().getUID(), "unreadErrors"), new DecimalType(installation.unreadErrors));
        updateState(new ChannelUID(getThing().getUID(), "unreadInfo"), new DecimalType(installation.unreadInfo));
        updateState(new ChannelUID(getThing().getUID(), "unreadWarnings"),
                new DecimalType(installation.unreadWarnings));
        updateState(new ChannelUID(getThing().getUID(), "activeAlarms"), new DecimalType(installation.activeAlarms));
        updateState(new ChannelUID(getThing().getUID(), "heatingEffect"),
                new DecimalType(installation.status.heatingEffect));
        updateState(new ChannelUID(getThing().getUID(), "reducedHeatingEffect"),
                new DecimalType(installation.status.reducedHeatingEffect));
        updateState(new ChannelUID(getThing().getUID(), "isonline"),
                installation.isOnline ? OnOffType.ON : OnOffType.OFF);
        updateState(new ChannelUID(getThing().getUID(), "isOutdoorTempSensorFunctioning"),
                installation.isOutdoorTempSensorFunctioning ? OnOffType.ON : OnOffType.OFF);
        updateState(new ChannelUID(getThing().getUID(), "isHotwaterActive"),
                installation.isHotwaterActive ? OnOffType.ON : OnOffType.OFF);

        for (int i = 0; i < registers.size(); i++) {
            RegisterGroup g = registers.get(i);
            logger.debug(" * Group: " + g.groupName);
            /*
             * ThingTypeUID t = getThingUIDFromDefinition(g.groupDefinition);
             * if (t != null) {
             * for (int j = 0; j < g.registers.size(); j++) {
             * Register r = g.registers.get(j);
             * logger.debug(" * * register: " + r.registerName + " [" + (r.isReadOnly ? " R  " : " RW ")
             * + r.presentation + "]");
             * // State v = new DecimalType(123);
             * // updateState(new ChannelUID(getThing().getUID(), "c"+r.registerId), v);
             * }
             * }
             */
        }

        registerGroups = registers;
    }

    /*
     * ThingTypeUID getThingUIDFromDefinition(String definition) {
     * ThingTypeUID t = new ThingTypeUID(ThermiaOnlineBinding.BINDING_ID, definition);
     * if (ThermiaOnlineBinding.SUPPORTED_DEVICE_THING_TYPES_UIDS.contains(t)) {
     * return t;
     * }
     * logger.warn("Thermia: Failed to load ThingTypeUID from deinition: " + definition);
     * return null;
     * }
     */

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        // TODO Auto-generated method stub
        logger.info("Thermia: Pump Command: " + channelUID.getId() + " - " + command);
    }

}
