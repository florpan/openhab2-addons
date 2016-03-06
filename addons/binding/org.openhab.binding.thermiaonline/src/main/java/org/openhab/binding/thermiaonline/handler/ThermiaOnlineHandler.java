/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.thermiaonline.handler;

import java.util.Hashtable;

import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.openhab.binding.thermiaonline.config.ThermiaOnlineConfiguration;
import org.openhab.binding.thermiaonline.internal.ThermiaOnlineDiscoveryService;
import org.openhab.binding.thermiaonline.network.NetworkHandler;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link ThermiaOnlineHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Christer Åkerlund - Initial contribution
 */
public class ThermiaOnlineHandler extends BaseBridgeHandler {

    private Logger logger = LoggerFactory.getLogger(ThermiaOnlineHandler.class);
    private ServiceRegistration discoveryRegistration;
    private ThermiaOnlineDiscoveryService discoveryService;
    private NetworkHandler network;
    // private Installation install;
    // private List<RegisterGroup> registers;

    public ThermiaOnlineHandler(Bridge bridge, NetworkHandler networkHandler) {
        super(bridge);
        this.network = networkHandler;
        logger.info("Thermia: Bridge created.");
    }

    public ThermiaOnlineConfiguration getConfiguration() {
        return getConfigAs(ThermiaOnlineConfiguration.class);
    }

    @Override
    public void initialize() {
        logger.info("Thermia: Initializing bridge.");
        ThermiaOnlineConfiguration configuration = getConfiguration();
        logger.info("username " + configuration.username);
        logger.info("password " + configuration.password);
        logger.info("refresh " + configuration.refresh);

        if (!network.connect(configuration.username, configuration.password)) {
            updateStatus(ThingStatus.OFFLINE);
            logger.error("Login to Thermia Online failed for username " + configuration.username);
        } else {
            discoveryService = new ThermiaOnlineDiscoveryService(this, network);

            // And register it as an OSGi service
            discoveryRegistration = bundleContext.registerService(DiscoveryService.class.getName(), discoveryService,
                    new Hashtable<String, Object>());

            updateStatus(ThingStatus.ONLINE);
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        // if(channelUID.getId().equals(CHANNEL_1)) {
        // TODO: handle command
        // }

        logger.info("Thermia: Bridge Command: " + channelUID.getId() + " - " + command);

        if (command instanceof RefreshType) {
            // bridgeHandler.handleCommand(channelUID, command);
            return;
        }
    }
}
