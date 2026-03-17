package fr.snipertvmc.essentialsxgui.infrastructure.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EssentialsXGUICommand {
	String NAME();
}
