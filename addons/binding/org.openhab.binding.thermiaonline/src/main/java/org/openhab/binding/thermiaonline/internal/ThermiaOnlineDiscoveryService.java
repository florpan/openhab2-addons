package org.openhab.binding.thermiaonline.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.thermiaonline.ThermiaOnlineBinding;
import org.openhab.binding.thermiaonline.handler.ThermiaOnlineHandler;
import org.openhab.binding.thermiaonline.models.InstallationInfo;
import org.openhab.binding.thermiaonline.network.NetworkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThermiaOnlineDiscoveryService extends AbstractDiscoveryService {
    NetworkHandler network;
    ThermiaOnlineHandler bridge;
    private final static Logger logger = LoggerFactory.getLogger(ThermiaOnlineDiscoveryService.class);

    public ThermiaOnlineDiscoveryService(ThermiaOnlineHandler bridge, NetworkHandler network) {
        super(ThermiaOnlineBinding.SupportedThingTypes, 10, true);
        this.bridge = bridge;
        this.network = new NetworkHandler();
        // ThermiaOnlineHandler bridge, NetworkHandler network
        logger.info("Thermia: Discovery service created.");
        // DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(bridge.getThing().getUID())
        // .withLabel("Thermia online bridge").build();
        // thingDiscovered(discoveryResult);
    }

    @Override
    public Set<ThingTypeUID> getSupportedThingTypes() {
        return ThermiaOnlineBinding.SupportedThingTypes;
    }

    @Override
    protected void startScan() {
        /*
         * if (bridge == null) {
         * logger.info("Thermia: Creating bridge");
         * ThingUID bridgeuid = new ThingUID(ThermiaOnlineBinding.THING_TYPE_ONLINE, "1");
         * DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(bridgeuid)
         * .withLabel("Thermia online bridge").build();
         * thingDiscovered(discoveryResult);
         * return;
         * }
         */
        logger.info("Thermia: Starting scan..");
        // ThermiaOnlineConfiguration configuration = new ThermiaOnlineConfiguration();// bridge.getConfiguration();
        // if (!network.connect(configuration.username, configuration.password)) {
        // logger.error("Thermia: Login to Thermia Online failed for username " + configuration.username);
        // } else {
        // logger.info("Thermia: Thermia online initialized with username " + configuration.username);
        List<InstallationInfo> installs = network.getInstallations();
        for (int i = 0; i < installs.size(); i++) {
            InstallationInfo inst = installs.get(i);
            ThingUID thingUID = getThingUID(inst);
            // ThingUID bridgeUID = bridge.getThing().getUID();
            Map<String, Object> properties = new HashMap<>(1);
            properties.put(ThermiaOnlineBinding.PROPERTY_PUMPID, inst.id.toString());
            DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(thingUID).withProperties(properties)
                    // .withBridge(bridgeUID)
                    .withLabel(inst.profileThermiaName + ":" + inst.profileName).build();
            thingDiscovered(discoveryResult);
            logger.info("Thermia: Discovered: " + thingUID + " - " + inst.id + ": " + inst.profileThermiaName);
        }
        // configuration.installation = installs.get(0).id;
        // logger.info("Thermia: Using installation: " + configuration.installation
        // + ". To change installation, specify it with the installation parameter in the binding.");
        // }
    }

    private ThingUID getThingUID(InstallationInfo inst) {
        // bridge.getThing().getUID(),
        return new ThingUID(ThermiaOnlineBinding.THING_TYPE_PUMP, inst.id.toString());
    }

    @Override
    protected void thingDiscovered(DiscoveryResult discoveryResult) {
        super.thingDiscovered(discoveryResult);
        logger.info("Thermia: Thing discovered!");
    }
}
