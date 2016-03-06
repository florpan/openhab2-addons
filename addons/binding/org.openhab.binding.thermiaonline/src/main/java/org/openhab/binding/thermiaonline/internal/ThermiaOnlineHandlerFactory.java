/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.thermiaonline.internal;

import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.openhab.binding.thermiaonline.ThermiaOnlineBinding;
import org.openhab.binding.thermiaonline.handler.ThermiaOnlineHandler;
import org.openhab.binding.thermiaonline.handler.ThermiaOnlinePumpHandler;
import org.openhab.binding.thermiaonline.network.NetworkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link ThermiaOnlineHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Christer Åkerlund - Initial contribution
 */
public class ThermiaOnlineHandlerFactory extends BaseThingHandlerFactory {

    private Logger logger = LoggerFactory.getLogger(ThermiaOnlineHandlerFactory.class);
    private NetworkHandler network = new NetworkHandler();

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return ThermiaOnlineBinding.SupportedThingTypes.contains(thingTypeUID);
        // BINDING_ID.contains(thingTypeUID.getBindingId());
    }

    @Override
    protected ThingHandler createHandler(Thing thing) {

        ThingTypeUID thingTypeUID = thing.getThingTypeUID();
        if (thingTypeUID.equals(ThermiaOnlineBinding.THING_TYPE_ONLINE)) {
            logger.info("Thermia: Thermia Creating bridge.");
            ThermiaOnlineHandler handler = new ThermiaOnlineHandler((Bridge) thing, network);
            return handler;
        }

        logger.debug("Thermia: Thermia Creating handler: " + thingTypeUID);
        return new ThermiaOnlinePumpHandler(thing, network);
    }

}
