package org.gradle.plugin.use.resolve.internal;

import org.gradle.api.internal.plugins.DefaultPotentialPluginWithId;
import org.gradle.api.internal.plugins.PotentialPlugin;
import org.gradle.plugin.use.PluginId;

public class ScriptPluginPluginResolution implements PluginResolution {

    @Override
    public PluginId getPluginId() {
        return null;
    }

    @Override
    public void execute(PluginResolveContext context) {
        context.add(DefaultPotentialPluginWithId.of(null, new PotentialPlugin<Object>() {
            @Override
            public Class<?> asClass() {
                return null;
            }

            @Override
            public boolean isImperative() {
                return false;
            }

            @Override
            public boolean isHasRules() {
                return false;
            }

            @Override
            public Type getType() {
                return null;
            }
        }));
    }
}
