package me.ilovelean.mobcraft.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class ComponentBuilder {
    private TextComponent.Builder builder = Component.text();

    public static ComponentBuilder of() {
        return new ComponentBuilder();
    }

    public ComponentBuilder text(String text) {
        this.builder.content(text);
        return this;
    }

    public ComponentBuilder color(TextColor color) {
        this.builder.color(color);
        return this;
    }

    public ComponentBuilder color(int rgb) {
        this.builder.color(TextColor.color(rgb));
        return this;
    }

    public ComponentBuilder decoration(TextDecoration decoration, boolean state) {
        this.builder.decoration(decoration, state);
        return this;
    }

    public ComponentBuilder bold() {
        return this.decoration(TextDecoration.BOLD, true);
    }

    public ComponentBuilder italic() {
        return this.decoration(TextDecoration.ITALIC, true);
    }

    public ComponentBuilder underlined() {
        return this.decoration(TextDecoration.UNDERLINED, true);
    }

    public ComponentBuilder strikethrough() {
        return this.decoration(TextDecoration.STRIKETHROUGH, true);
    }

    public ComponentBuilder obfuscated() {
        return this.decoration(TextDecoration.OBFUSCATED, true);
    }

    public ComponentBuilder clickEvent(ClickEvent.Action action, String value) {
        this.builder.clickEvent(ClickEvent.clickEvent(action, value));
        return this;
    }

    public ComponentBuilder hoverEvent(Component hoverText) {
        this.builder.hoverEvent(HoverEvent.showText(hoverText));
        return this;
    }

    public ComponentBuilder append(Component component) {
        this.builder.append(component);
        return this;
    }

    public ComponentBuilder reset() {
        this.builder = Component.text();
        return this;
    }

    public Component build() {
        return this.builder.build();
    }
}
