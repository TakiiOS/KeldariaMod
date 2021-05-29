package fr.nathanael2611.keldaria.mod.client.gui;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.container.ContainerCookingFurnace;
import fr.nathanael2611.keldaria.mod.container.slot.SlotJustOneItem;
import fr.nathanael2611.keldaria.mod.features.cookingfurnace.CookingRegistry;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityCookingFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCookingFurnace extends GuiContainer
{
    private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation(Keldaria.MOD_ID.toLowerCase(), "textures/gui/containers/cooking_furnace.png");
    /** The player inventory bound to this GUI. */
    private final InventoryPlayer playerInventory;
    private final TileEntityCookingFurnace tileFurnace;
    private CookingRegistry cookingRegistry = Keldaria.getInstance().getRegistry().getCookingRegistry();

    public GuiCookingFurnace(InventoryPlayer playerInv, TileEntityCookingFurnace tileFurnace)
    {
        super(new ContainerCookingFurnace(playerInv, tileFurnace));
        this.playerInventory = playerInv;
        this.tileFurnace = tileFurnace;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);

        Slot slot = getSlotUnderMouse();
        if(slot instanceof SlotJustOneItem && slot.getStack().isEmpty() && playerInventory.getItemStack().isEmpty())
        {
            drawHoveringText("§2Ce slot ne peut contenir qu'un item à la fois.\n§c - Tenter de mettre un stack de plus d'items\n ne fonctionnera pas.", mouseX, mouseY);
        }
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String s = "Four de cuisine";
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(FURNACE_GUI_TEXTURES);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        if (this.tileFurnace.isBurning())
        {
            int k = this.getBurnLeftScaled(13);
            this.drawTexturedModalRect(i + 11, j + 37 + 12 - k, 176, 12 - k, 14, k /*+ 1*/);
        }

        if(this.tileFurnace.getBurnTime() != 0) {
            int l = this.getCookProgressScaled(100);
            this.drawTexturedModalRect(i + 34, j + 35, 0, 166, l/* + 1*/, 15);
        }
    }

    private int getCookProgressScaled(int pixels)
    {
        int i = this.tileFurnace.getFurnaceCookTime();
        ItemStack stack = this.tileFurnace.getEntryCopy();
        if(this.tileFurnace.getFurnaceCookTime() >= this.tileFurnace.getItemCookTime(this.tileFurnace.getEntryCopy())) stack = this.tileFurnace.getLastEntryCopy();
        int j = this.cookingRegistry.containsCookEntry(stack.getItem()) ? this.tileFurnace.getItemCookTimeWithBurn(stack) : 0;
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    private int getBurnLeftScaled(int pixels)
    {
        int i = this.tileFurnace.totalBurnTime;
        if (i == 0)
        {
            i = 200;
        }

        return this.tileFurnace.getBurnTime() * pixels / i;
    }
}