package net.voids.unethicalite.voidaio.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import javax.inject.Singleton;

import net.runelite.api.widgets.Widget;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.game.Vars;
import net.unethicalite.api.game.GameSettings.Audio;
import net.unethicalite.api.packets.DialogPackets;
import net.unethicalite.api.packets.WidgetPackets;
import net.unethicalite.api.widgets.Dialog;
import net.unethicalite.api.widgets.Widgets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class SettingsFix
{
    private static final Logger log = LoggerFactory.getLogger(SettingsFix.class);
    private static final int ALL_SETTINGS_WIDGET_ID = 7602208;
    private static final int MENU_TAB_WIDGET_ID = 8781847;
    private static final int OPTION_WIDGET_ID = 8781843;
    private static final int CLOSE_BUTTON_WIDGET_ID = 8781828;
    private boolean settingsFixed = false;

    public SettingsFix()
    {
    }

    public boolean isSettingsFixed()
    {
        return this.settingsFixed;
    }

    private void queueWidgetAction(int menuTab, List<Integer> options, int widgetId)
    {
        log.debug("Menu tab: " + menuTab);
        GameThread.invoke(() -> {
            WidgetPackets.queueWidgetAction1Packet(7602208, -1, -1);
            WidgetPackets.queueWidgetAction1Packet(8781847, -1, menuTab);
            Iterator var3 = options.iterator();

            while (var3.hasNext())
            {
                int option = (Integer) var3.next();
                log.debug("Option: " + option);
                WidgetPackets.queueWidgetAction1Packet(widgetId, -1, option);
            }

            WidgetPackets.queueWidgetAction1Packet(8781828, -1, -1);
        });
    }

    public int configureSettings()
    {
        log.debug("Configuring");
        if (!Audio.isFullMuted())
        {
            if (Dialog.isOpen())
            {
                DialogPackets.closeInterface();
                return 600;
            }
            else
            {
                log.debug("Muting");
                Widget widget;
                if (Vars.getVarp(168) != 0)
                {
                    widget = Widgets.get(116, 93);
                    widget.interact(((String[]) Objects.requireNonNull(widget.getActions()))[0]);
                }

                if (Vars.getVarp(169) != 0)
                {
                    widget = Widgets.get(116, 107);
                    widget.interact(((String[]) Objects.requireNonNull(widget.getActions()))[0]);
                }

                if (Vars.getVarp(872) != 0)
                {
                    widget = Widgets.get(116, 122);
                    widget.interact(((String[]) Objects.requireNonNull(widget.getActions()))[0]);
                }

                return 1000;
            }
        }
        else
        {
            List<SettingsCategory> categories = Arrays.asList(new SettingsCategory(1, 8781845, new HashMap<Integer, Supplier<Boolean>>() {
                {
                    this.put(21, () -> {
                        return Vars.getVarp(168) != 0;
                    });
                    this.put(42, () -> {
                        return Vars.getVarp(169) != 0;
                    });
                    this.put(63, () -> {
                        return Vars.getVarp(872) != 0;
                    });
                }
            }), new SettingsCategory(2, 8781843, new HashMap<Integer, Supplier<Boolean>>() {
                {
                    this.put(1, () -> {
                        return Vars.getVarp(1074) == 0;
                    });
                }
            }), new SettingsCategory(3, 8781843, new HashMap<Integer, Supplier<Boolean>>() {
                {
                    this.put(7, () -> {
                        return Vars.getBit(5542) == 0;
                    });
                    this.put(39, () -> {
                        return Vars.getBit(4681) == 0;
                    });
                }
            }), new SettingsCategory(4, 8781843, new HashMap<Integer, Supplier<Boolean>>() {
                {
                    this.put(4, () -> {
                        return Vars.getBit(12378) == 0;
                    });
                }
            }), new SettingsCategory(5, 8781843, new HashMap<Integer, Supplier<Boolean>>() {
                {
                    this.put(1, () -> {
                        return Vars.getBit(4180) == 1;
                    });
                    this.put(5, () -> {
                        return Vars.getBit(14819) == 1;
                    });
                    this.put(6, () -> {
                        return Vars.getBit(5697) == 0;
                    });
                    this.put(7, () -> {
                        return Vars.getBit(5698) == 0;
                    });
                    this.put(17, () -> {
                        return Vars.getBit(14197) == 0;
                    });
                    this.put(18, () -> {
                        return Vars.getBit(4814) == 0;
                    });
                    this.put(19, () -> {
                        return Vars.getBit(14198) == 0;
                    });
                }
            }), new SettingsCategory(6, 8781843, new HashMap<Integer, Supplier<Boolean>>() {
                {
                    this.put(14, () -> {
                        return Vars.getBit(13037) == 1;
                    });
                    this.put(15, () -> {
                        return Vars.getBit(10113) == 0;
                    });
                    this.put(16, () -> {
                        return Vars.getBit(5368) == 0;
                    });
                    this.put(19, () -> {
                        return Vars.getBit(13130) == 0;
                    });
                    this.put(21, () -> {
                        return Vars.getBit(9452) == 0;
                    });
                }
            }), new SettingsCategory(7, 8781843, new HashMap<Integer, Supplier<Boolean>>() {
                {
                    this.put(32, () -> {
                        return Vars.getBit(4100) == 0;
                    });
                    this.put(39, () -> {
                        return Vars.getBit(14700) == 0;
                    });
                    this.put(40, () -> {
                        return Vars.getBit(14701) == 0;
                    });
                }
            }));

            SettingsCategory category;
            for (Iterator var2 = categories.iterator(); var2.hasNext(); category.applySettings())
            {
                category = (SettingsCategory) var2.next();
                if (Dialog.isOpen())
                {
                    log.debug("Closing interface");
                    DialogPackets.closeInterface();
                    Time.sleepTick();
                }
            }

            log.debug("Settings fixed");
            this.settingsFixed = true;
            return 1000;
        }
    }

    private class SettingsCategory
    {
        int menuTab;
        int widgetId;
        Map<Integer, Supplier<Boolean>> conditions;

        public SettingsCategory(int menuTab, int widgetId, Map<Integer, Supplier<Boolean>> conditions)
        {
            this.menuTab = menuTab;
            this.widgetId = widgetId;
            this.conditions = conditions;
        }

        public void applySettings()
        {
            List<Integer> optionsToQueue = new ArrayList();
            Iterator var2 = this.conditions.entrySet().iterator();

            while (var2.hasNext())
            {
                Map.Entry<Integer, Supplier<Boolean>> entry = (Map.Entry) var2.next();
                if ((Boolean) ((Supplier) entry.getValue()).get())
                {
                    optionsToQueue.add((Integer) entry.getKey());
                }
            }

            if (!optionsToQueue.isEmpty())
            {
                SettingsFix.this.queueWidgetAction(this.menuTab, optionsToQueue, this.widgetId);
                var2 = optionsToQueue.iterator();

                while (var2.hasNext())
                {
                    Integer option = (Integer) var2.next();
                    SettingsFix.log.debug("Before: " + option);
                    Time.sleepUntil(() -> {
                        return !(Boolean) ((Supplier) this.conditions.get(option)).get();
                    }, 5000);
                    SettingsFix.log.debug("After");
                }
            }

        }
    }
}
