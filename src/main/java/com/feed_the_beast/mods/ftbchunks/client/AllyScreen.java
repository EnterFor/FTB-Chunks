package com.feed_the_beast.mods.ftbchunks.client;

import com.feed_the_beast.mods.ftbchunks.net.FTBChunksNet;
import com.feed_the_beast.mods.ftbchunks.net.RequestAllyStatusChangePacket;
import com.feed_the_beast.mods.ftbchunks.net.SendPlayerListPacket;
import com.feed_the_beast.mods.ftbguilibrary.icon.Icon;
import com.feed_the_beast.mods.ftbguilibrary.misc.GuiButtonListBase;
import com.feed_the_beast.mods.ftbguilibrary.utils.MouseButton;
import com.feed_the_beast.mods.ftbguilibrary.utils.TooltipList;
import com.feed_the_beast.mods.ftbguilibrary.widget.GuiIcons;
import com.feed_the_beast.mods.ftbguilibrary.widget.Panel;
import com.feed_the_beast.mods.ftbguilibrary.widget.SimpleTextButton;
import com.feed_the_beast.mods.ftbguilibrary.widget.WidgetType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

/**
 * @author LatvianModder
 */
public class AllyScreen extends GuiButtonListBase
{
	public final List<SendPlayerListPacket.NetPlayer> players;
	public final int allyMode;

	public AllyScreen(List<SendPlayerListPacket.NetPlayer> p, int a)
	{
		setHasSearchBox(true);

		players = p;
		allyMode = a;

		setTitle(new StringTextComponent("Allies"));
	}

	@Override
	public void addButtons(Panel panel)
	{
		for (SendPlayerListPacket.NetPlayer p : players)
		{
			Icon icon;

			if (p.isAlly() && p.isAllyBack())
			{
				icon = GuiIcons.CHECK;
			}
			else if (p.isAlly())
			{
				icon = GuiIcons.RIGHT;
			}
			else if (p.isAllyBack())
			{
				icon = GuiIcons.LEFT;
			}
			else
			{
				icon = GuiIcons.ADD_GRAY;
			}

			panel.add(new SimpleTextButton(panel, new StringTextComponent(p.name).withStyle(p.isFake() ? TextFormatting.YELLOW : TextFormatting.WHITE), icon)
			{
				@Override
				public void addMouseOverText(TooltipList list)
				{
					if (p.isFake())
					{
						list.string("Fake player");
						list.string("UUID: " + p.uuid);
					}
					else if (p.isAlly() != p.isAllyBack())
					{
						list.string("Pending invite...");

						if (p.isAllyBack())
						{
							list.styledString("Click to accept", TextFormatting.GRAY);
							list.styledString("Right-click to deny", TextFormatting.DARK_GRAY);
						}
						else
						{
							list.styledString("Click to cancel", TextFormatting.GRAY);
						}
					}
				}

				@Override
				public WidgetType getWidgetType()
				{
					if (p.isBanned())
					{
						return WidgetType.DISABLED;
					}

					return super.getWidgetType();
				}

				@Override
				public void onClicked(MouseButton mouseButton)
				{
					playClickSound();

					boolean cancelPending = !mouseButton.isLeft() && !p.isAlly() && p.isAllyBack();

					if (cancelPending)
					{
						p.flags &= ~SendPlayerListPacket.NetPlayer.ALLY_BACK;
					}
					else
					{
						if (p.isAlly())
						{
							p.flags &= ~SendPlayerListPacket.NetPlayer.ALLY;
						}
						else
						{
							p.flags |= SendPlayerListPacket.NetPlayer.ALLY;
						}
					}

					if (p.isAlly() && p.isAllyBack())
					{
						icon = GuiIcons.CHECK;
					}
					else if (p.isAlly())
					{
						icon = GuiIcons.RIGHT;
					}
					else if (p.isAllyBack())
					{
						icon = GuiIcons.LEFT;
					}
					else
					{
						icon = GuiIcons.ADD_GRAY;
					}

					FTBChunksNet.MAIN.sendToServer(new RequestAllyStatusChangePacket(p.uuid, !cancelPending));
				}
			});
		}
	}
}