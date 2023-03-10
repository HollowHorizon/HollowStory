package ru.hollowhorizon.hollowstory.client.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import ru.hollowhorizon.hc.client.screens.widget.HollowWidget;
import ru.hollowhorizon.hc.client.utils.ScissorUtil;

public class FadeInLabelWidget extends HollowWidget {
    public static final int DELAY = 80;
    private int ticker = 0;
    private ITextComponent text = new StringTextComponent("");
    private Runnable onFadeInComplete;
    private boolean paused = true;

    public FadeInLabelWidget(int x, int y, int width, int height) {
        super(x, y, width, height, new StringTextComponent(""));
    }

    public void setText(ITextComponent text) {
        this.text = text;
        this.ticker = 0;
        paused = true;
    }

    public void resetTicker() {
        this.ticker = 0;
        paused = false;
    }

    public int getTicker() {
        return ticker;
    }

    public void onFadeInComplete(Runnable onFadeInComplete) {
        this.onFadeInComplete = onFadeInComplete;
    }

    public void setText(String text) {
        setText(new StringTextComponent(text));
    }

    @Override
    public void renderButton(MatrixStack stack, int mouseX, int mouseY, float ticks) {
        super.renderButton(stack, mouseX, mouseY, ticks);

        ScissorUtil.start(this.x, this.y, this.width * this.ticker / DELAY, this.height);
        stack.pushPose();
        stack.translate(0, 0, 500f);
        this.getFont().drawShadow(stack, this.text, this.x, this.y + this.height / 2F - this.getFont().lineHeight / 2F, 0xFFFFFF);
        stack.popPose();
        ScissorUtil.stop();

        if(paused) return;

        if (this.ticker < DELAY) {
            this.ticker++;
        } else {
            if (this.onFadeInComplete != null) {
                this.onFadeInComplete.run();
            }
            this.paused = true;
        }
    }

    public void setTicker(int ticker) {
        this.ticker = ticker;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    public boolean isComplete() {
        return getFont().width(this.text) <= this.width * this.ticker / DELAY;
    }

    public void complete() {
        this.paused = true;
        this.ticker = DELAY;
    }

    public String getText() {
        return this.text.getString();
    }
}
