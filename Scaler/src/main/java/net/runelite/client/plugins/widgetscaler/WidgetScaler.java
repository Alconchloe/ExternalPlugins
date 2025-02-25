/*
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.widgetscaler;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;

@PluginDescriptor(
	name = "Widget Scaler",
	description = "Widget Scaling",
	type = PluginType.EXTERNAL
)

@Slf4j
public class WidgetScaler extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private WidgetScalerConfig config;
	@Inject
	private EventBus eventBus;

	@Provides
	WidgetScalerConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(WidgetScalerConfig.class);
	}

	@Override
	protected void startUp()
	{
		eventBus.subscribe(GameTick.class, this, this::onGameTick);
	}

	@Override
	protected void shutDown()
	{
		eventBus.unregister(this);
	}

	private void onGameTick(GameTick event)
	{
		Widget ring = client.getWidget(WidgetInfo.EQUIPMENT_RING);

		if (ring != null)
		{
			widgetHandler(ring, config.widgetSizeRing(), config.xOffsetRing(), config.yOffsetRing());
		}

		Widget cape = client.getWidget(WidgetInfo.EQUIPMENT_CAPE);

		if (cape != null)
		{
			widgetHandler(cape, config.widgetSizeCape(), config.xOffsetCape(), config.yOffsetCape());
		}
	}

	private void widgetHandler(Widget widget, int size, int offsetX, int offsetY)
	{
		Widget[] ringChildren = widget.getChildren();

		for (Widget widgets : ringChildren)
		{
			widgets.setOriginalWidth(size);
			widgets.setOriginalHeight(size);
			widgets.revalidate();
		}

		widget.setOriginalWidth(size);
		widget.setOriginalHeight(size);
		widget.setOriginalX(offsetX);
		widget.setOriginalY(offsetY);
		widget.revalidate();
	}
}
