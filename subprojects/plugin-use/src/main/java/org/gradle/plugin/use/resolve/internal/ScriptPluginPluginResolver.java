package org.gradle.plugin.use.resolve.internal;

import org.gradle.api.initialization.dsl.ScriptHandler;
import org.gradle.api.internal.initialization.ClassLoaderScope;
import org.gradle.configuration.ScriptPlugin;
import org.gradle.configuration.ScriptPluginFactory;
import org.gradle.groovy.scripts.ScriptSource;
import org.gradle.groovy.scripts.UriScriptSource;
import org.gradle.plugin.management.internal.InvalidPluginRequestException;
import org.gradle.plugin.management.internal.PluginRequestInternal;

import java.net.URI;

public class ScriptPluginPluginResolver implements PluginResolver {

    private final ClassLoaderScope coreAndPluginsScope;
    private final ScriptPluginFactory scriptPluginFactory;

    public ScriptPluginPluginResolver(ClassLoaderScope coreAndPluginsScope, ScriptPluginFactory scriptPluginFactory) {
        this.coreAndPluginsScope = coreAndPluginsScope;
        this.scriptPluginFactory = scriptPluginFactory;
    }

    @Override
    public void resolve(PluginRequestInternal pluginRequest, PluginResolutionResult result) throws InvalidPluginRequestException {
        if (pluginRequest.getModule() != null) {
            result.notFound(getDescription(), "explicit artifact coordinates are not supported by this source");
            return;
        }
        if (pluginRequest.getVersion() != null) {
            result.notFound(getDescription(), "explicit version is not supported by this source");
            return;
        }
        URI requestUri = pluginRequest.getUri();
        if (requestUri == null) {
            result.notFound(getDescription(), "only URI plugin requests are supported by this source");
            return;
        }

        ScriptSource scriptSource = new UriScriptSource("script", requestUri);
        if (!scriptSource.getResource().getExists()) {
            result.notFound(getDescription(), requestUri + " does not exists");
        }

        // TODO Move this into ScriptPluginPluginResolution.execute
        ScriptHandler scriptHandler = null; // TODO Hum
        ClassLoaderScope loaderScope = coreAndPluginsScope.createChild("script-plugin-" + requestUri.toASCIIString());
        ScriptPlugin scriptPlugin = scriptPluginFactory.create(scriptSource, scriptHandler, loaderScope, coreAndPluginsScope, false);


        // TODO
        result.found(getDescription(), new ScriptPluginPluginResolution());

        throw new RuntimeException("TODO");
    }

    public static String getDescription() {
        return "Script Plugins";
    }
}
